package com.ferreteriaoccidente.ferreteria.controller;
import com.ferreteriaoccidente.ferreteria.dto.CompraDTO;
import com.ferreteriaoccidente.ferreteria.entity.Compra;
import com.ferreteriaoccidente.ferreteria.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/compras")
public class CompraController {
 @Autowired
 private CompraService compraService;
 @GetMapping
 public List<Compra> listar() {
 return compraService.listar();
 }
 @GetMapping("/{id}")
 public Compra buscarPorId(@PathVariable Integer id) {
 return compraService.buscarPorId(id);
 }
 @PostMapping
 public Compra guardar(@RequestBody CompraDTO compraDTO) {
 return compraService.guardar(compraDTO);
 }
 @PutMapping("/{id}")
 public Compra actualizar(@PathVariable Integer id, @RequestBody CompraDTO compraDTO) {
 compraDTO.setIdCompra(id);
 return compraService.guardar(compraDTO);
 }
 @DeleteMapping("/{id}")
 public void eliminar(@PathVariable Integer id) {
 compraService.eliminar(id);
 }
}
