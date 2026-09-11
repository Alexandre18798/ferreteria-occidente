package com.ferreteriaoccidente.ferreteria.controller;
import com.ferreteriaoccidente.ferreteria.dto.UsuarioDTO;
import com.ferreteriaoccidente.ferreteria.entity.Usuario;
import com.ferreteriaoccidente.ferreteria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
 @Autowired
 private UsuarioService usuarioService;
 @GetMapping
 public List<Usuario> listar() {
 return usuarioService.listar();
 }
 @GetMapping("/{id}")
 public Usuario buscarPorId(@PathVariable Integer id) {
 return usuarioService.buscarPorId(id);
 }
 @PostMapping
 public Usuario guardar(@RequestBody UsuarioDTO usuarioDTO) {
 return usuarioService.guardar(usuarioDTO);
 }
 @PutMapping("/{id}")
 public Usuario actualizar(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDTO) {
 usuarioDTO.setIdUsuario(id);
 return usuarioService.guardar(usuarioDTO);
 }
 @DeleteMapping("/{id}")
 public void eliminar(@PathVariable Integer id) {
 usuarioService.eliminar(id);
 }
}