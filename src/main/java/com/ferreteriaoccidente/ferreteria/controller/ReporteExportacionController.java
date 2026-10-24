package com.ferreteriaoccidente.ferreteria.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.ferreteriaoccidente.ferreteria.service.*;

@RestController
@RequestMapping("/exportaciones")
public class ReporteExportacionController {
    private final ReporteExportacionService productos;
    private final ProveedorExportacionService proveedores;
    private final CompraExportacionService compras;
    private final DetalleCompraExportacionService detalles;
    private final UsuarioExportacionService usuarios;
    private final PerfilExportacionService perfiles;
    private final GeneralExportacionService general;

    public ReporteExportacionController(
            ReporteExportacionService productos,
            ProveedorExportacionService proveedores,
            CompraExportacionService compras,
            DetalleCompraExportacionService detalles,
            UsuarioExportacionService usuarios,
            PerfilExportacionService perfiles,
            GeneralExportacionService general) {
        this.productos=productos;
        this.proveedores=proveedores;
        this.compras=compras;
        this.detalles=detalles;
        this.usuarios=usuarios;
        this.perfiles=perfiles;
        this.general=general;
    }

    @GetMapping("/productos/pdf")
    public ResponseEntity<byte[]> productosPdf(){return pdf(productos.exportarProductosPdf(),"productos-ferreteria-occidente.pdf");}
    @GetMapping("/productos/excel")
    public ResponseEntity<byte[]> productosExcel(){return excel(productos.exportarProductosExcel(),"productos-ferreteria-occidente.xlsx");}
    @GetMapping("/proveedores/pdf")
    public ResponseEntity<byte[]> proveedoresPdf(){return pdf(proveedores.exportarProveedoresPdf(),"proveedores-ferreteria-occidente.pdf");}
    @GetMapping("/proveedores/excel")
    public ResponseEntity<byte[]> proveedoresExcel(){return excel(proveedores.exportarProveedoresExcel(),"proveedores-ferreteria-occidente.xlsx");}
    @GetMapping("/compras/pdf")
    public ResponseEntity<byte[]> comprasPdf(){return pdf(compras.exportarComprasPdf(),"compras-ferreteria-occidente.pdf");}
    @GetMapping("/compras/excel")
    public ResponseEntity<byte[]> comprasExcel(){return excel(compras.exportarComprasExcel(),"compras-ferreteria-occidente.xlsx");}
    @GetMapping("/detalles/pdf")
    public ResponseEntity<byte[]> detallesPdf(){return pdf(detalles.exportarDetallesPdf(),"detalle-compras-ferreteria-occidente.pdf");}
    @GetMapping("/detalles/excel")
    public ResponseEntity<byte[]> detallesExcel(){return excel(detalles.exportarDetallesExcel(),"detalle-compras-ferreteria-occidente.xlsx");}
    @GetMapping("/usuarios/pdf")
    public ResponseEntity<byte[]> usuariosPdf(){return pdf(usuarios.exportarUsuariosPdf(),"usuarios-ferreteria-occidente.pdf");}
    @GetMapping("/usuarios/excel")
    public ResponseEntity<byte[]> usuariosExcel(){return excel(usuarios.exportarUsuariosExcel(),"usuarios-ferreteria-occidente.xlsx");}
    @GetMapping("/perfiles/pdf")
    public ResponseEntity<byte[]> perfilesPdf(){return pdf(perfiles.exportarPerfilesPdf(),"perfiles-ferreteria-occidente.pdf");}
    @GetMapping("/perfiles/excel")
    public ResponseEntity<byte[]> perfilesExcel(){return excel(perfiles.exportarPerfilesExcel(),"perfiles-ferreteria-occidente.xlsx");}
    @GetMapping("/general/pdf")
    public ResponseEntity<byte[]> generalPdf(){return pdf(general.exportarGeneralPdf(),"reporte-general-ferreteria-occidente.pdf");}
    @GetMapping("/general/excel")
    public ResponseEntity<byte[]> generalExcel(){return excel(general.exportarGeneralExcel(),"reporte-general-ferreteria-occidente.xlsx");}

    private ResponseEntity<byte[]> pdf(byte[] archivo,String nombre){
        HttpHeaders h=new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_PDF);
        h.setContentDisposition(ContentDisposition.attachment().filename(nombre).build());
        h.setContentLength(archivo.length);
        return new ResponseEntity<>(archivo,h,HttpStatus.OK);
    }

    private ResponseEntity<byte[]> excel(byte[] archivo,String nombre){
        HttpHeaders h=new HttpHeaders();
        h.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        h.setContentDisposition(ContentDisposition.attachment().filename(nombre).build());
        h.setContentLength(archivo.length);
        return new ResponseEntity<>(archivo,h,HttpStatus.OK);
    }
}
