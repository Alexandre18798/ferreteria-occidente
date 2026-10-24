package com.ferreteriaoccidente.ferreteria.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.ferreteriaoccidente.ferreteria.entity.Usuario;
import com.ferreteriaoccidente.ferreteria.repository.UsuarioRepository;
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
public class UsuarioExportacionService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioExportacionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public byte[] exportarUsuariosPdf() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();

            ByteArrayOutputStream salida = new ByteArrayOutputStream();

            Document documento = new Document(
                    PageSize.A4.rotate(),
                    30,
                    30,
                    30,
                    30
            );

            PdfWriter.getInstance(documento, salida);

            documento.open();

            agregarLogoPdf(documento);
            agregarTituloPdf(documento, "Reporte de Usuarios");

            PdfPTable tabla = new PdfPTable(6);

            tabla.setWidthPercentage(100);

            tabla.setWidths(
                    new float[]{
                            1f,
                            2.5f,
                            2f,
                            2f,
                            3f,
                            1.5f
                    }
            );

            String[] columnas = {
                    "ID",
                    "Nombre",
                    "Usuario",
                    "Perfil",
                    "Correo",
                    "Estado"
            };

            for (String columna : columnas) {
                agregarEncabezadoPdf(tabla, columna);
            }

            for (Usuario usuario : usuarios) {

                agregarCeldaPdf(
                        tabla,
                        String.valueOf(usuario.getIdUsuario())
                );

                agregarCeldaPdf(
                        tabla,
                        usuario.getNombre()
                );

                agregarCeldaPdf(
                        tabla,
                        usuario.getUsuario()
                );

                agregarCeldaPdf(
                        tabla,
                        obtenerPerfil(usuario)
                );

                agregarCeldaPdf(
                        tabla,
                        usuario.getCorreo()
                );

                agregarCeldaPdf(
                        tabla,
                        Boolean.TRUE.equals(usuario.getEstado())
                                ? "Activo"
                                : "Inactivo"
                );
            }

            documento.add(tabla);

            Paragraph total = new Paragraph(
                    "Total de usuarios registrados: " + usuarios.size(),
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            12,
                            Color.DARK_GRAY
                    )
            );

            total.setSpacingBefore(15);

            documento.add(total);

            documento.close();

            return salida.toByteArray();

        } catch (Exception error) {

            throw new RuntimeException(
                    "Error al generar el PDF de usuarios",
                    error
            );
        }
    }

    public byte[] exportarUsuariosExcel() {

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream salida = new ByteArrayOutputStream()
        ) {

            List<Usuario> usuarios = usuarioRepository.findAll();

            Sheet hoja = workbook.createSheet("Usuarios");

            agregarLogoExcel(workbook, hoja);

            crearTituloExcel(
                    workbook,
                    hoja,
                    "Reporte de Usuarios"
            );

            String[] columnas = {
                    "ID",
                    "Nombre",
                    "Usuario",
                    "Perfil",
                    "Correo",
                    "Estado"
            };

            Row encabezado = hoja.createRow(8);

            CellStyle estilo = crearEstiloEncabezado(workbook);

            for (int i = 0; i < columnas.length; i++) {

                Cell celda = encabezado.createCell(i);

                celda.setCellValue(columnas[i]);

                celda.setCellStyle(estilo);
            }

            int filaActual = 9;

            for (Usuario usuario : usuarios) {

                Row fila = hoja.createRow(filaActual++);

                fila.createCell(0)
                        .setCellValue(usuario.getIdUsuario());

                fila.createCell(1)
                        .setCellValue(
                                valorSeguro(usuario.getNombre())
                        );

                fila.createCell(2)
                        .setCellValue(
                                valorSeguro(usuario.getUsuario())
                        );

                fila.createCell(3)
                        .setCellValue(
                                obtenerPerfil(usuario)
                        );

                fila.createCell(4)
                        .setCellValue(
                                valorSeguro(usuario.getCorreo())
                        );

                fila.createCell(5)
                        .setCellValue(
                                Boolean.TRUE.equals(usuario.getEstado())
                                        ? "Activo"
                                        : "Inactivo"
                        );
            }

            Row total = hoja.createRow(filaActual + 1);

            total.createCell(0)
                    .setCellValue("Total de usuarios:");

            total.createCell(1)
                    .setCellValue(usuarios.size());

            for (int i = 0; i < columnas.length; i++) {
                hoja.autoSizeColumn(i);
            }

            workbook.write(salida);

            return salida.toByteArray();

        } catch (Exception error) {

            throw new RuntimeException(
                    "Error al generar el Excel de usuarios",
                    error
            );
        }
    }

    private String obtenerPerfil(Usuario usuario) {

        if (usuario.getPerfil() == null) {
            return "";
        }

        if (usuario.getPerfil().getNombre() == null) {
            return "";
        }

        return usuario.getPerfil().getNombre();
    }

    private void agregarTituloPdf(
            Document documento,
            String reporte
    ) throws Exception {

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

        empresa.setAlignment(Element.ALIGN_CENTER);

        documento.add(empresa);

        Paragraph nombre =
                new Paragraph(
                        reporte,
                        subtitulo
                );

        nombre.setAlignment(Element.ALIGN_CENTER);

        nombre.setSpacingAfter(20);

        documento.add(nombre);
    }

    private void crearTituloExcel(
            Workbook workbook,
            Sheet hoja,
            String reporte
    ) {

        Row fila = hoja.createRow(5);

        Cell celda = fila.createCell(0);

        celda.setCellValue(
                "FERRETERIA OCCIDENTE"
        );

        Font fuente = workbook.createFont();

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
                .setCellValue(reporte);
    }

    private CellStyle crearEstiloEncabezado(
            Workbook workbook
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

        estilo.setAlignment(
                HorizontalAlignment.CENTER
        );

        return estilo;
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
                InputStream stream = obtenerLogo()
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
                InputStream stream = obtenerLogo()
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
                new Color(25, 70, 45)
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
                                valorSeguro(texto),
                                FontFactory.getFont(
                                        FontFactory.HELVETICA,
                                        8,
                                        Color.DARK_GRAY
                                )
                        )
                );

        celda.setPadding(6);

        tabla.addCell(celda);
    }

    private String valorSeguro(
            String texto
    ) {

        if (texto == null) {
            return "";
        }

        return texto;
    }
}