package com.ferreteriaoccidente.ferreteria.service;
import com.ferreteriaoccidente.ferreteria.dto.UsuarioDTO;
import com.ferreteriaoccidente.ferreteria.entity.Perfil;
import com.ferreteriaoccidente.ferreteria.entity.Usuario;
import com.ferreteriaoccidente.ferreteria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UsuarioService {
 @Autowired
 private UsuarioRepository usuarioRepository;
 public List<Usuario> listar() {
 return usuarioRepository.findAll();
 }
 public Usuario buscarPorId(Integer id) {
 return usuarioRepository.findById(id).orElse(null);
 }
 public Usuario guardar(UsuarioDTO usuarioDTO) {
 Usuario usuario = new Usuario();
 Perfil perfil = new Perfil();
 perfil.setIdPerfil(usuarioDTO.getIdPerfil());
 usuario.setIdUsuario(usuarioDTO.getIdUsuario());
 usuario.setPerfil(perfil);
 usuario.setNombre(usuarioDTO.getNombre());
 usuario.setUsuario(usuarioDTO.getUsuario());
 usuario.setContrasena(usuarioDTO.getContrasena());
 usuario.setCorreo(usuarioDTO.getCorreo());
 usuario.setEstado(usuarioDTO.getEstado());
 return usuarioRepository.save(usuario);
 }
 public void eliminar(Integer id) {
 usuarioRepository.deleteById(id);
 }
}