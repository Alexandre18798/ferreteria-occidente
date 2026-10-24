package com.ferreteriaoccidente.ferreteria.controller;

import com.ferreteriaoccidente.ferreteria.service.ReporteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumen() {
        return reporteService.obtenerResumen();
    }

    @GetMapping("/compras-por-mes")
    public Map<String, BigDecimal> obtenerComprasPorMes() {
        return reporteService.obtenerComprasPorMes();
    }

    @GetMapping("/inventario-por-categoria")
    public Map<String, Integer> obtenerInventarioPorCategoria() {
    return reporteService.obtenerInventarioPorCategoria();
    }

    @GetMapping("/compras-por-proveedor")
    public Map<String, Integer> obtenerComprasPorProveedor() {
    return reporteService.obtenerComprasPorProveedor();
    }

}