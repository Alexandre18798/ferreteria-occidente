package com.ferreteriaoccidente.ferreteria.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.ferreteriaoccidente.ferreteria.entity.Proveedor;
import com.ferreteriaoccidente.ferreteria.repository.ProveedorRepository;
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
public class ProveedorExportacionService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorExportacionService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public byte[] exportarProveedoresPdf() {
        try {
            List<Proveedor> proveedores = proveedorRepository.findAll();
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            Document documento = new Document(PageSize.A4, 30, 30, 30, 30);
            PdfWriter.getInstance(documento, salida);
            documento.open();
            agregarLogoPdf(documento);

            com.lowagie.text.Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(25, 70, 45));
            com.lowagie.text.Font subtituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.DARK_GRAY);
            com.lowagie.text.Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);

            Paragraph empresa = new Paragraph("FERRETERIA OCCIDENTE", tituloFont);
            empresa.setAlignment(Element.ALIGN_CENTER);
            empresa.setSpacingBefore(5);
            documento.add(empresa);

            Paragraph subtitulo = new Paragraph("Reporte de Proveedores", subtituloFont);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingBefore(5);
            documento.add(subtitulo);

            Paragraph fecha = new Paragraph("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), textoFont);
            fecha.setAlignment(Element.ALIGN_CENTER);
            fecha.setSpacingBefore(5);
            fecha.setSpacingAfter(20);
            documento.add(fecha);

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1f, 2.5f, 2f, 3.5f, 1.5f});

            agregarEncabezadoPdf(tabla, "ID");
            agregarEncabezadoPdf(tabla, "Proveedor");
            agregarEncabezadoPdf(tabla, "Telefono");
            agregarEncabezadoPdf(tabla, "Direccion");
            agregarEncabezadoPdf(tabla, "Estado");

            for (Proveedor proveedor : proveedores) {
                agregarCeldaPdf(tabla, String.valueOf(proveedor.getIdProveedor()));
                agregarCeldaPdf(tabla, proveedor.getNombre());
                agregarCeldaPdf(tabla, proveedor.getTelefono());
                agregarCeldaPdf(tabla, proveedor.getDireccion());
                agregarCeldaPdf(tabla, Boolean.TRUE.equals(proveedor.getEstado()) ? "Activo" : "Inactivo");
            }

            documento.add(tabla);

            Paragraph total = new Paragraph("Total de proveedores registrados: " + proveedores.size(), subtituloFont);
            total.setSpacingBefore(15);
            documento.add(total);

            documento.close();
            return salida.toByteArray();
        } catch (Exception error) {
            throw new RuntimeException("Error al generar el PDF de proveedores", error);
        }
    }

    public byte[] exportarProveedoresExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            List<Proveedor> proveedores = proveedorRepository.findAll();
            Sheet hoja = workbook.createSheet("Proveedores");
            agregarLogoExcel(workbook, hoja);

            Row titulo = hoja.createRow(5);
            Cell tituloCelda = titulo.createCell(0);
            tituloCelda.setCellValue("FERRETERIA OCCIDENTE");

            Font tituloFuente = workbook.createFont();
            tituloFuente.setBold(true);
            tituloFuente.setFontHeightInPoints((short) 16);
            CellStyle tituloEstilo = workbook.createCellStyle();
            tituloEstilo.setFont(tituloFuente);
            tituloCelda.setCellStyle(tituloEstilo);

            hoja.createRow(6).createCell(0).setCellValue("Reporte de Proveedores");
            hoja.createRow(7).createCell(0).setCellValue("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            Row encabezado = hoja.createRow(9);
            String[] columnas = {"ID", "Proveedor", "Telefono", "Direccion", "Estado"};

            CellStyle encabezadoEstilo = workbook.createCellStyle();
            Font encabezadoFuente = workbook.createFont();
            encabezadoFuente.setBold(true);
            encabezadoEstilo.setFont(encabezadoFuente);
            encabezadoEstilo.setFillForegroundColor((short) 42);
            encabezadoEstilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            encabezadoEstilo.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < columnas.length; i++) {
                Cell celda = encabezado.createCell(i);
                celda.setCellValue(columnas[i]);
                celda.setCellStyle(encabezadoEstilo);
            }

            int numeroFila = 10;
            for (Proveedor proveedor : proveedores) {
                Row fila = hoja.createRow(numeroFila++);
                fila.createCell(0).setCellValue(proveedor.getIdProveedor());
                fila.createCell(1).setCellValue(valorSeguro(proveedor.getNombre()));
                fila.createCell(2).setCellValue(valorSeguro(proveedor.getTelefono()));
                fila.createCell(3).setCellValue(valorSeguro(proveedor.getDireccion()));
                fila.createCell(4).setCellValue(Boolean.TRUE.equals(proveedor.getEstado()) ? "Activo" : "Inactivo");
            }

            Row totalFila = hoja.createRow(numeroFila + 1);
            totalFila.createCell(0).setCellValue("Total de proveedores:");
            totalFila.createCell(1).setCellValue(proveedores.size());

            for (int i = 0; i < columnas.length; i++) {
                hoja.autoSizeColumn(i);
            }

            workbook.write(salida);
            return salida.toByteArray();
        } catch (Exception error) {
            throw new RuntimeException("Error al generar el Excel de proveedores", error);
        }
    }

    private InputStream obtenerLogo() {
        return getClass().getClassLoader().getResourceAsStream("static/images/logo-ferreteria.png");
    }

    private void agregarLogoPdf(Document documento) {
        try (InputStream logoStream = obtenerLogo()) {
            if (logoStream == null) return;
            Image logo = Image.getInstance(logoStream.readAllBytes());
            logo.scaleToFit(120, 90);
            logo.setAlignment(Element.ALIGN_CENTER);
            documento.add(logo);
        } catch (Exception error) {
            throw new RuntimeException("Error al cargar el logo", error);
        }
    }

    private void agregarLogoExcel(Workbook workbook, Sheet hoja) {
        try (InputStream logoStream = obtenerLogo()) {
            if (logoStream == null) return;

            int indiceImagen = workbook.addPicture(logoStream.readAllBytes(), Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> dibujo = hoja.createDrawingPatriarch();
            ClientAnchor ancla = helper.createClientAnchor();
            ancla.setCol1(0);
            ancla.setRow1(0);
            Picture imagen = dibujo.createPicture(ancla, indiceImagen);
            imagen.resize(0.75);
        } catch (Exception error) {
            throw new RuntimeException("Error al agregar el logo al Excel", error);
        }
    }

    private void agregarEncabezadoPdf(PdfPTable tabla, String texto) {
        com.lowagie.text.Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(new Color(25, 70, 45));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(7);
        tabla.addCell(celda);
    }

    private void agregarCeldaPdf(PdfPTable tabla, String texto) {
        com.lowagie.text.Font fuente = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.DARK_GRAY);
        PdfPCell celda = new PdfPCell(new Phrase(valorSeguro(texto), fuente));
        celda.setPadding(6);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tabla.addCell(celda);
    }

    private String valorSeguro(String texto) {
        return texto != null ? texto : "";
    }
}
