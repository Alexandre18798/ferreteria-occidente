package com.ferreteriaoccidente.ferreteria.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

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
public class GeneralExportacionService {

    private final ReporteService reporteService;

    public GeneralExportacionService(
            ReporteService reporteService
    ) {

        this.reporteService =
                reporteService;
    }

    public byte[] exportarGeneralPdf() {

        try {

            Map<String, Object> resumen =
                    reporteService.obtenerResumen();

            Map<String, BigDecimal> comprasMes =
                    reporteService.obtenerComprasPorMes();

            Map<String, Integer> inventario =
                    reporteService.obtenerInventarioPorCategoria();

            Map<String, Integer> proveedores =
                    reporteService.obtenerComprasPorProveedor();

            ByteArrayOutputStream salida =
                    new ByteArrayOutputStream();

            Document documento =
                    new Document(
                            PageSize.A4,
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

            com.lowagie.text.Font titulo =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            20,
                            new Color(25, 70, 45)
                    );

            com.lowagie.text.Font subtitulo =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            13,
                            Color.DARK_GRAY
                    );

            Paragraph empresa =
                    new Paragraph(
                            "FERRETERIA OCCIDENTE",
                            titulo
                    );

            empresa.setAlignment(
                    Element.ALIGN_CENTER
            );

            documento.add(empresa);

            Paragraph reporte =
                    new Paragraph(
                            "Reporte General del Sistema",
                            subtitulo
                    );

            reporte.setAlignment(
                    Element.ALIGN_CENTER
            );

            reporte.setSpacingAfter(20);

            documento.add(reporte);

            Paragraph tituloResumen =
                    new Paragraph(
                            "Resumen General",
                            subtitulo
                    );

            tituloResumen.setSpacingAfter(8);

            documento.add(tituloResumen);

            PdfPTable resumenTabla =
                    new PdfPTable(2);

            resumenTabla.setWidthPercentage(100);

            agregarFila(
                    resumenTabla,
                    "Total de Productos",
                    resumen.get(
                            "totalProductos"
                    )
            );

            agregarFila(
                    resumenTabla,
                    "Total de Proveedores",
                    resumen.get(
                            "totalProveedores"
                    )
            );

            agregarFila(
                    resumenTabla,
                    "Total de Compras",
                    resumen.get(
                            "totalCompras"
                    )
            );

            agregarFila(
                    resumenTabla,
                    "Total de Usuarios",
                    resumen.get(
                            "totalUsuarios"
                    )
            );

            agregarFila(
                    resumenTabla,
                    "Monto Total de Compras",
                    "Q "
                            + resumen.get(
                                    "montoTotalCompras"
                            )
            );

            documento.add(
                    resumenTabla
            );

            agregarMapaPdf(
                    documento,
                    "Compras por Mes",
                    "Mes",
                    "Monto",
                    comprasMes,
                    true
            );

            agregarMapaPdf(
                    documento,
                    "Inventario por Categoria",
                    "Categoria",
                    "Stock",
                    inventario,
                    false
            );

            agregarMapaPdf(
                    documento,
                    "Compras por Proveedor",
                    "Proveedor",
                    "Compras",
                    proveedores,
                    false
            );

            documento.close();

            return salida.toByteArray();

        } catch (Exception error) {

            throw new RuntimeException(
                    "Error al generar el PDF general",
                    error
            );
        }
    }

    public byte[] exportarGeneralExcel() {

        try (
                Workbook workbook =
                        new XSSFWorkbook();

                ByteArrayOutputStream salida =
                        new ByteArrayOutputStream()
        ) {

            crearResumen(
                    workbook,
                    reporteService.obtenerResumen()
            );

            crearMapa(
                    workbook,
                    "Compras por Mes",
                    "Mes",
                    "Monto",
                    reporteService.obtenerComprasPorMes()
            );

            crearMapa(
                    workbook,
                    "Inventario Categoria",
                    "Categoria",
                    "Stock",
                    reporteService.obtenerInventarioPorCategoria()
            );

            crearMapa(
                    workbook,
                    "Compras Proveedor",
                    "Proveedor",
                    "Compras",
                    reporteService.obtenerComprasPorProveedor()
            );

            workbook.write(salida);

            return salida.toByteArray();

        } catch (Exception error) {

            throw new RuntimeException(
                    "Error al generar el Excel general",
                    error
            );
        }
    }

    private void crearResumen(
            Workbook workbook,
            Map<String, Object> resumen
    ) {

        Sheet hoja =
                workbook.createSheet(
                        "Resumen General"
                );

        agregarLogoExcel(
                workbook,
                hoja
        );

        crearTitulo(
                workbook,
                hoja,
                "Reporte General del Sistema"
        );

        Row encabezado =
                hoja.createRow(8);

        encabezado.createCell(0)
                .setCellValue(
                        "Indicador"
                );

        encabezado.createCell(1)
                .setCellValue(
                        "Valor"
                );

        aplicarEncabezado(
                workbook,
                encabezado,
                2
        );

        Object[][] datos = {

                {
                        "Total de Productos",
                        resumen.get(
                                "totalProductos"
                        )
                },

                {
                        "Total de Proveedores",
                        resumen.get(
                                "totalProveedores"
                        )
                },

                {
                        "Total de Compras",
                        resumen.get(
                                "totalCompras"
                        )
                },

                {
                        "Total de Usuarios",
                        resumen.get(
                                "totalUsuarios"
                        )
                },

                {
                        "Monto Total de Compras",
                        resumen.get(
                                "montoTotalCompras"
                        )
                }
        };

        int numeroFila = 9;

        for (Object[] dato : datos) {

            Row fila =
                    hoja.createRow(
                            numeroFila++
                    );

            fila.createCell(0)
                    .setCellValue(
                            String.valueOf(
                                    dato[0]
                            )
                    );

            fila.createCell(1)
                    .setCellValue(
                            dato[1] != null
                                    ? String.valueOf(
                                            dato[1]
                                    )
                                    : ""
                    );
        }

        hoja.autoSizeColumn(0);

        hoja.autoSizeColumn(1);
    }

    private void crearMapa(
            Workbook workbook,
            String nombre,
            String columna1,
            String columna2,
            Map<?, ?> datos
    ) {

        Sheet hoja =
                workbook.createSheet(
                        nombre
                );

        agregarLogoExcel(
                workbook,
                hoja
        );

        crearTitulo(
                workbook,
                hoja,
                nombre
        );

        Row encabezado =
                hoja.createRow(8);

        encabezado.createCell(0)
                .setCellValue(
                        columna1
                );

        encabezado.createCell(1)
                .setCellValue(
                        columna2
                );

        aplicarEncabezado(
                workbook,
                encabezado,
                2
        );

        int numeroFila = 9;

        for (
                Map.Entry<?, ?> entrada
                        : datos.entrySet()
        ) {

            Row fila =
                    hoja.createRow(
                            numeroFila++
                    );

            fila.createCell(0)
                    .setCellValue(
                            String.valueOf(
                                    entrada.getKey()
                            )
                    );

            fila.createCell(1)
                    .setCellValue(
                            String.valueOf(
                                    entrada.getValue()
                            )
                    );
        }

        hoja.autoSizeColumn(0);

        hoja.autoSizeColumn(1);
    }

    private void crearTitulo(
            Workbook workbook,
            Sheet hoja,
            String reporte
    ) {

        Row fila =
                hoja.createRow(5);

        Cell celda =
                fila.createCell(0);

        celda.setCellValue(
                "FERRETERIA OCCIDENTE"
        );

        Font fuente =
                workbook.createFont();

        fuente.setBold(true);

        fuente.setFontHeightInPoints(
                (short) 16
        );

        CellStyle estilo =
                workbook.createCellStyle();

        estilo.setFont(fuente);

        celda.setCellStyle(estilo);

        hoja.createRow(6)
                .createCell(0)
                .setCellValue(
                        reporte
                );
    }

    private void aplicarEncabezado(
            Workbook workbook,
            Row fila,
            int cantidad
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fuente =
                workbook.createFont();

        fuente.setBold(true);

        estilo.setFont(fuente);

        estilo.setFillForegroundColor(
                (short) 42
        );

        estilo.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        for (
                int i = 0;
                i < cantidad;
                i++
        ) {

            fila.getCell(i)
                    .setCellStyle(
                            estilo
                    );
        }
    }

    private void agregarMapaPdf(
            Document documento,
            String titulo,
            String columna1,
            String columna2,
            Map<?, ?> datos,
            boolean moneda
    ) throws Exception {

        Paragraph encabezado =
                new Paragraph(
                        titulo,
                        FontFactory.getFont(
                                FontFactory.HELVETICA_BOLD,
                                13,
                                Color.DARK_GRAY
                        )
                );

        encabezado.setSpacingBefore(18);

        encabezado.setSpacingAfter(8);

        documento.add(encabezado);

        PdfPTable tabla =
                new PdfPTable(2);

        tabla.setWidthPercentage(100);

        agregarEncabezadoPdf(
                tabla,
                columna1
        );

        agregarEncabezadoPdf(
                tabla,
                columna2
        );

        for (
                Map.Entry<?, ?> entrada
                        : datos.entrySet()
        ) {

            agregarCeldaPdf(
                    tabla,
                    String.valueOf(
                            entrada.getKey()
                    )
            );

            if (moneda) {

                agregarCeldaPdf(
                        tabla,
                        "Q "
                                + entrada.getValue()
                );

            } else {

                agregarCeldaPdf(
                        tabla,
                        String.valueOf(
                                entrada.getValue()
                        )
                );
            }
        }

        documento.add(tabla);
    }

    private void agregarFila(
            PdfPTable tabla,
            String nombre,
            Object valor
    ) {

        agregarEncabezadoPdf(
                tabla,
                nombre
        );

        agregarCeldaPdf(
                tabla,
                valor != null
                        ? String.valueOf(valor)
                        : ""
        );
    }

    private InputStream obtenerLogo() {

        return getClass()
                .getClassLoader()
                .getResourceAsStream(
                        "static/images/logo-ferreteria.png"
                );
    }

    private void agregarLogoPdf(
            Document documento
    ) {

        try (
                InputStream stream =
                        obtenerLogo()
        ) {

            if (stream == null) {
                return;
            }

            Image logo =
                    Image.getInstance(
                            stream.readAllBytes()
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
            Sheet hoja
    ) {

        try (
                InputStream stream =
                        obtenerLogo()
        ) {

            if (stream == null) {
                return;
            }

            int indice =
                    workbook.addPicture(
                            stream.readAllBytes(),
                            Workbook.PICTURE_TYPE_PNG
                    );

            ClientAnchor ancla =
                    workbook
                            .getCreationHelper()
                            .createClientAnchor();

            ancla.setCol1(0);

            ancla.setRow1(0);

            ancla.setCol2(2);

            ancla.setRow2(5);

            Picture imagen =
                    hoja.createDrawingPatriarch()
                            .createPicture(
                                    ancla,
                                    indice
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
            String texto
    ) {

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

        celda.setPadding(7);

        tabla.addCell(celda);
    }

    private void agregarCeldaPdf(
            PdfPTable tabla,
            String texto
    ) {

        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                texto != null
                                        ? texto
                                        : "",
                                FontFactory.getFont(
                                        FontFactory.HELVETICA,
                                        9,
                                        Color.DARK_GRAY
                                )
                        )
                );

        celda.setPadding(7);

        tabla.addCell(celda);
    }
}