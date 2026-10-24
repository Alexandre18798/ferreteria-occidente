package com.ferreteriaoccidente.ferreteria.service;

import com.ferreteriaoccidente.ferreteria.dto.UsuarioDTO;
import com.ferreteriaoccidente.ferreteria.entity.Perfil;
import com.ferreteriaoccidente.ferreteria.entity.Usuario;
import com.ferreteriaoccidente.ferreteria.repository.PerfilRepository;
import com.ferreteriaoccidente.ferreteria.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario guardar(UsuarioDTO usuarioDTO) {

        Perfil perfil = perfilRepository
                .findById(usuarioDTO.getIdPerfil())
                .orElse(null);

        if (perfil == null) {
            return null;
        }

        Usuario usuario;

        if (usuarioDTO.getIdUsuario() == null) {
            usuario = new Usuario();
        } else {
            usuario = usuarioRepository
                    .findById(usuarioDTO.getIdUsuario())
                    .orElse(null);

            if (usuario == null) {
                return null;
            }
        }

        usuario.setPerfil(perfil);
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setUsuario(usuarioDTO.getUsuario());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setEstado(usuarioDTO.getEstado());

        if (
                usuarioDTO.getContrasena() != null &&
                !usuarioDTO.getContrasena().isBlank()
        ) {
            usuario.setContrasena(usuarioDTO.getContrasena());
        }

        return usuarioRepository.save(usuario);
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}