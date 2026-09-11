package com.ferreteriaoccidente.ferreteria.controller;
import com.ferreteriaoccidente.ferreteria.dto.PerfilDTO;
import com.ferreteriaoccidente.ferreteria.entity.Perfil;
import com.ferreteriaoccidente.ferreteria.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/perfiles")
public class PerfilController {
 @Autowired
 private PerfilService perfilService;
 @GetMapping
 public List<Perfil> listar() {
 return perfilService.listar();
 }
 @GetMapping("/{id}")
 public Perfil buscarPorId(@PathVariable Integer id) {
 return perfilService.buscarPorId(id);
 }
 @PostMapping
 public Perfil guardar(@RequestBody PerfilDTO perfilDTO) {
 return perfilService.guardar(perfilDTO);
 }
 @PutMapping("/{id}")
 public Perfil actualizar(@PathVariable Integer id, @RequestBody PerfilDTO perfilDTO) {
 perfilDTO.setIdPerfil(id);
 return perfilService.guardar(perfilDTO);
 }
 @DeleteMapping("/{id}")
 public void eliminar(@PathVariable Integer id) {
 perfilService.eliminar(id);
 }
}