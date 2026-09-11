package com.ferreteriaoccidente.ferreteria.controller;
import com.ferreteriaoccidente.ferreteria.dto.DetalleCompraDTO;
import com.ferreteriaoccidente.ferreteria.entity.DetalleCompra;
import com.ferreteriaoccidente.ferreteria.service.DetalleCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/detalle-compras")
public class DetalleCompraController {
 @Autowired
 private DetalleCompraService detalleCompraService;
 @GetMapping
 public List<DetalleCompra> listar() {
 return detalleCompraService.listar();
 }
 @GetMapping("/{id}")
 public DetalleCompra buscarPorId(@PathVariable Integer id) {
 return detalleCompraService.buscarPorId(id);
 }
 @PostMapping
 public DetalleCompra guardar(@RequestBody DetalleCompraDTO detalleCompraDTO) {
 return detalleCompraService.guardar(detalleCompraDTO);
 }
 @PutMapping("/{id}")
 public DetalleCompra actualizar(@PathVariable Integer id,
 @RequestBody DetalleCompraDTO detalleCompraDTO) {
 detalleCompraDTO.setIdDetalleCompra(id);
 return detalleCompraService.guardar(detalleCompraDTO);
 }
 @DeleteMapping("/{id}")
 public void eliminar(@PathVariable Integer id) {
 detalleCompraService.eliminar(id);
 }
}