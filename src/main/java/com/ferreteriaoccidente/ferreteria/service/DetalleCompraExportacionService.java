package com.ferreteriaoccidente.ferreteria.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.ferreteriaoccidente.ferreteria.entity.DetalleCompra;
import com.ferreteriaoccidente.ferreteria.repository.DetalleCompraRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class DetalleCompraExportacionService {

    private final DetalleCompraRepository detalleCompraRepository;

    public DetalleCompraExportacionService(
            DetalleCompraRepository detalleCompraRepository) {

        this.detalleCompraRepository = detalleCompraRepository;
    }

    public byte[] exportarDetallesPdf() {

        try {

            List<DetalleCompra> detalles =
                    detalleCompraRepository.findAll();

            ByteArrayOutputStream salida =
                    new ByteArrayOutputStream();

            Document documento =
                    new Document(
                            PageSize.A4.rotate(),
                            30,
                            30,
                            30,
                            30
                    );

            PdfWriter.getInstance(
                    documento,
                    salida
            );

            documento.open();

            agregarLogoPdf(documento);

            com.lowagie.text.Font tituloFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            20,
                            new Color(25, 70, 45)
                    );

            com.lowagie.text.Font subtituloFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            13,
                            Color.DARK_GRAY
                    );

            Paragraph empresa =
                    new Paragraph(
                            "FERRETERIA OCCIDENTE",
                            tituloFont
                    );

            empresa.setAlignment(
                    Element.ALIGN_CENTER
            );

            empresa.setSpacingBefore(5);

            documento.add(empresa);

            Paragraph subtitulo =
                    new Paragraph(
                            "Reporte de Detalle de Compras",
                            subtituloFont
                    );

            subtitulo.setAlignment(
                    Element.ALIGN_CENTER
            );

            subtitulo.setSpacingBefore(5);
            subtitulo.setSpacingAfter(20);

            documento.add(subtitulo);

            PdfPTable tabla =
                    new PdfPTable(6);

            tabla.setWidthPercentage(100);

            tabla.setWidths(
                    new float[]{
                            1.3f,
                            1.3f,
                            3.5f,
                            1.5f,
                            2.2f,
                            2.2f
                    }
            );

            agregarEncabezadoPdf(
                    tabla,
                    "ID Detalle"
            );

            agregarEncabezadoPdf(
                    tabla,
                    "ID Compra"
            );

            agregarEncabezadoPdf(
                    tabla,
                    "Producto"
            );

            agregarEncabezadoPdf(
                    tabla,
                    "Cantidad"
            );

            agregarEncabezadoPdf(
                    tabla,
                    "Precio Unitario"
            );

            agregarEncabezadoPdf(
                    tabla,
                    "Subtotal"
            );

            BigDecimal sumaSubtotales =
                    BigDecimal.ZERO;

            for (DetalleCompra detalle : detalles) {

                agregarCeldaPdf(
                        tabla,
                        String.valueOf(
                                detalle.getIdDetalleCompra()
                        )
                );

                agregarCeldaPdf(
                        tabla,
                        obtenerIdCompra(detalle)
                );

                agregarCeldaPdf(
                        tabla,
                        obtenerProducto(detalle)
                );

                agregarCeldaPdf(
                        tabla,
                        detalle.getCantidad() != null
                                ? String.valueOf(
                                        detalle.getCantidad()
                                )
                                : "0"
                );

                agregarCeldaPdf(
                        tabla,
                        detalle.getPrecioUnitario() != null
                                ? "Q "
                                + detalle.getPrecioUnitario()
                                        .toPlainString()
                                : "Q 0.00"
                );

                agregarCeldaPdf(
                        tabla,
                        detalle.getSubtotal() != null
                                ? "Q "
                                + detalle.getSubtotal()
                                        .toPlainString()
                                : "Q 0.00"
                );

                if (detalle.getSubtotal() != null) {

                    sumaSubtotales =
                            sumaSubtotales.add(
                                    detalle.getSubtotal()
                            );
                }
            }

            documento.add(tabla);

            Paragraph totalRegistros =
                    new Paragraph(
                            "Total de detalles registrados: "
                                    + detalles.size(),
                            subtituloFont
                    );

            totalRegistros.setSpacingBefore(15);

            documento.add(totalRegistros);

            Paragraph totalCompras =
                    new Paragraph(
                            "Suma total de subtotales: Q "
                                    + sumaSubtotales.toPlainString(),
                            subtituloFont
                    );

            totalCompras.setSpacingBefore(5);

            documento.add(totalCompras);

            documento.close();

            return salida.toByteArray();

        } catch (Exception error) {

            throw new RuntimeException(
                    "Error al generar el PDF de detalle de compras",
                    error
            );
        }
    }

    public byte[] exportarDetallesExcel() {

        try (
                Workbook workbook =
                        new XSSFWorkbook();

                ByteArrayOutputStream salida =
                        new ByteArrayOutputStream()
        ) {

            List<DetalleCompra> detalles =
                    detalleCompraRepository.findAll();

            Sheet hoja =
                    workbook.createSheet(
                            "Detalle de Compras"
                    );

            agregarLogoExcel(
                    workbook,
                    hoja
            );

            Row titulo =
                    hoja.createRow(5);

            Cell tituloCelda =
                    titulo.createCell(0);

            tituloCelda.setCellValue(
                    "FERRETERIA OCCIDENTE"
            );

            Font tituloFuente =
                    workbook.createFont();

            tituloFuente.setBold(true);

            tituloFuente.setFontHeightInPoints(
                    (short) 16
            );

            CellStyle tituloEstilo =
                    workbook.createCellStyle();

            tituloEstilo.setFont(
                    tituloFuente
            );

            tituloCelda.setCellStyle(
                    tituloEstilo
            );

            Row subtitulo =
                    hoja.createRow(6);

            subtitulo.createCell(0)
                    .setCellValue(
                            "Reporte de Detalle de Compras"
                    );

            Row encabezado =
                    hoja.createRow(8);

            String[] columnas = {
                    "ID Detalle",
                    "ID Compra",
                    "Producto",
                    "Cantidad",
                    "Precio Unitario",
                    "Subtotal"
            };

            CellStyle encabezadoEstilo =
                    workbook.createCellStyle();

            Font encabezadoFuente =
                    workbook.createFont();

            encabezadoFuente.setBold(true);

            encabezadoEstilo.setFont(
                    encabezadoFuente
            );

            encabezadoEstilo
                    .setFillForegroundColor(
                            (short) 42
                    );

            encabezadoEstilo.setFillPattern(
                    FillPatternType.SOLID_FOREGROUND
            );

            encabezadoEstilo.setAlignment(
                    HorizontalAlignment.CENTER
            );

            for (
                    int i = 0;
                    i < columnas.length;
                    i++
            ) {

                Cell celda =
                        encabezado.createCell(i);

                celda.setCellValue(
                        columnas[i]
                );

                celda.setCellStyle(
                        encabezadoEstilo
                );
            }

            int numeroFila = 9;

            BigDecimal sumaSubtotales =
                    BigDecimal.ZERO;

            for (DetalleCompra detalle : detalles) {

                Row fila =
                        hoja.createRow(
                                numeroFila++
                        );

                fila.createCell(0)
                        .setCellValue(
                                detalle.getIdDetalleCompra()
                        );

                fila.createCell(1)
                        .setCellValue(
                                obtenerIdCompraNumero(
                                        detalle
                                )
                        );

                fila.createCell(2)
                        .setCellValue(
                                obtenerProducto(
                                        detalle
                                )
                        );

                fila.createCell(3)
                        .setCellValue(
                                detalle.getCantidad() != null
                                        ? detalle.getCantidad()
                                        : 0
                        );

                fila.createCell(4)
                        .setCellValue(
                                detalle.getPrecioUnitario() != null
                                        ? detalle.getPrecioUnitario()
                                                .doubleValue()
                                        : 0
                        );

                fila.createCell(5)
                        .setCellValue(
                                detalle.getSubtotal() != null
                                        ? detalle.getSubtotal()
                                                .doubleValue()
                                        : 0
                        );

                if (detalle.getSubtotal() != null) {

                    sumaSubtotales =
                            sumaSubtotales.add(
                                    detalle.getSubtotal()
                            );
                }
            }

            Row totalRegistros =
                    hoja.createRow(
                            numeroFila + 1
                    );

            totalRegistros.createCell(0)
                    .setCellValue(
                            "Total de detalles:"
                    );

            totalRegistros.createCell(1)
                    .setCellValue(
                            detalles.size()
                    );

            Row totalSubtotales =
                    hoja.createRow(
                            numeroFila + 2
                    );

            totalSubtotales.createCell(0)
                    .setCellValue(
                            "Suma total de subtotales:"
                    );

            totalSubtotales.createCell(1)
                    .setCellValue(
                            sumaSubtotales.doubleValue()
                    );

            for (
                    int i = 0;
                    i < columnas.length;
                    i++
            ) {

                hoja.autoSizeColumn(i);
            }

            workbook.write(salida);

            return salida.toByteArray();

        } catch (Exception error) {

            throw new RuntimeException(
                    "Error al generar el Excel de detalle de compras",
                    error
            );
        }
    }

    private String obtenerIdCompra(
            DetalleCompra detalle) {

        if (
                detalle.getCompra() == null
                || detalle.getCompra()
                        .getIdCompra() == null
        ) {

            return "";
        }

        return String.valueOf(
                detalle.getCompra()
                        .getIdCompra()
        );
    }

    private int obtenerIdCompraNumero(
            DetalleCompra detalle) {

        if (
                detalle.getCompra() == null
                || detalle.getCompra()
                        .getIdCompra() == null
        ) {

            return 0;
        }

        return detalle.getCompra()
                .getIdCompra();
    }

    private String obtenerProducto(
            DetalleCompra detalle) {

        if (
                detalle.getProducto() == null
                || detalle.getProducto()
                        .getNombre() == null
        ) {

            return "";
        }

        return detalle.getProducto()
                .getNombre();
    }

    private InputStream obtenerLogo() {

        return getClass()
                .getClassLoader()
                .getResourceAsStream(
                        "static/images/logo-ferreteria.png"
                );
    }

    private void agregarLogoPdf(
            Document documento) {

        try (
                InputStream logoStream =
                        obtenerLogo()
        ) {

            if (logoStream == null) {
                return;
            }

            Image logo =
                    Image.getInstance(
                            logoStream.readAllBytes()
                    );

            logo.scaleToFit(
                    120,
                    90
            );

            logo.setAlignment(
                    Element.ALIGN_CENTER
            );

            documento.add(logo);

        } catch (Exception error) {

            throw new RuntimeException(
                    "Error al cargar el logo",
                    error
            );
        }
    }

    private void agregarLogoExcel(
            Workbook workbook,
            Sheet hoja) {

        try (
                InputStream logoStream =
                        obtenerLogo()
        ) {

            if (logoStream == null) {
                return;
            }

            byte[] logoBytes =
                    logoStream.readAllBytes();

            int indiceImagen =
                    workbook.addPicture(
                            logoBytes,
                            Workbook.PICTURE_TYPE_PNG
                    );

            CreationHelper helper =
                    workbook.getCreationHelper();

            Drawing<?> dibujo =
                    hoja.createDrawingPatriarch();

            ClientAnchor ancla =
                    helper.createClientAnchor();

            ancla.setCol1(0);
            ancla.setRow1(0);
            ancla.setCol2(2);
            ancla.setRow2(5);

            Picture imagen =
                    dibujo.createPicture(
                            ancla,
                            indiceImagen
                    );

            imagen.resize(0.75);

        } catch (Exception error) {

            throw new RuntimeException(
                    "Error al agregar el logo al Excel",
                    error
            );
        }
    }

    private void agregarEncabezadoPdf(
            PdfPTable tabla,
            String texto) {

        com.lowagie.text.Font fuente =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        9,
                        Color.WHITE
                );

        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                texto,
                                fuente
                        )
                );

        celda.setBackgroundColor(
                new Color(
                        25,
                        70,
                        45
                )
        );

        celda.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        celda.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        celda.setPadding(7);

        tabla.addCell(celda);
    }

    private void agregarCeldaPdf(
            PdfPTable tabla,
            String texto) {

        com.lowagie.text.Font fuente =
                FontFactory.getFont(
                        FontFactory.HELVETICA,
                        8,
                        Color.DARK_GRAY
                );

        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                texto != null
                                        ? texto
                                        : "",
                                fuente
                        )
                );

        celda.setPadding(6);

        celda.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        tabla.addCell(celda);
    }
}