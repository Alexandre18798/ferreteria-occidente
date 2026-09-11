package com.ferreteriaoccidente.ferreteria.service;
import com.ferreteriaoccidente.ferreteria.dto.PerfilDTO;
import com.ferreteriaoccidente.ferreteria.entity.Perfil;
import com.ferreteriaoccidente.ferreteria.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PerfilService {
 @Autowired
 private PerfilRepository perfilRepository;
 public List<Perfil> listar() {
 return perfilRepository.findAll();
 }
 public Perfil buscarPorId(Integer id) {
 return perfilRepository.findById(id).orElse(null);
 }
 public Perfil guardar(PerfilDTO perfilDTO) {
 Perfil perfil = new Perfil();
 perfil.setIdPerfil(perfilDTO.getIdPerfil());
 perfil.setNombre(perfilDTO.getNombre());
 perfil.setEstado(perfilDTO.getEstado());
 return perfilRepository.save(perfil);
 }
 public void eliminar(Integer id) {
 perfilRepository.deleteById(id);
 }
}