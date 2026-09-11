package com.ferreteriaoccidente.ferreteria.controller;

import com.ferreteriaoccidente.ferreteria.dto.ProductoDTO;
import com.ferreteriaoccidente.ferreteria.entity.Producto;
import com.ferreteriaoccidente.ferreteria.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
    }

    @PostMapping
    public Producto guardar(@RequestBody ProductoDTO productoDTO) {
        return productoService.guardar(productoDTO);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Integer id, @RequestBody ProductoDTO productoDTO) {
        productoDTO.setIdProducto(id);
        return productoService.guardar(productoDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
    }
}