package com.ferreteriaoccidente.ferreteria.controller;

import com.ferreteriaoccidente.ferreteria.dto.ProveedorDTO;
import com.ferreteriaoccidente.ferreteria.entity.Proveedor;
import com.ferreteriaoccidente.ferreteria.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorService.listar();
    }

    @GetMapping("/{id}")
    public Proveedor buscarPorId(@PathVariable Integer id) {
        return proveedorService.buscarPorId(id);
    }

    @PostMapping
    public Proveedor guardar(@RequestBody ProveedorDTO proveedorDTO) {
        return proveedorService.guardar(proveedorDTO);
    }

    @PutMapping("/{id}")
    public Proveedor actualizar(@PathVariable Integer id, @RequestBody ProveedorDTO proveedorDTO) {
        proveedorDTO.setIdProveedor(id);
        return proveedorService.guardar(proveedorDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        proveedorService.eliminar(id);
    }
}