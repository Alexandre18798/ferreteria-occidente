package com.ferreteriaoccidente.ferreteria.service;

import com.ferreteriaoccidente.ferreteria.dto.CompraDTO;
import com.ferreteriaoccidente.ferreteria.entity.Compra;
import com.ferreteriaoccidente.ferreteria.entity.Proveedor;
import com.ferreteriaoccidente.ferreteria.entity.Usuario;
import com.ferreteriaoccidente.ferreteria.repository.CompraRepository;
import com.ferreteriaoccidente.ferreteria.repository.ProveedorRepository;
import com.ferreteriaoccidente.ferreteria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Compra> listar() {
        return compraRepository.findAll();
    }

    public Compra buscarPorId(Integer id) {
        return compraRepository.findById(id).orElse(null);
    }

    public Compra guardar(CompraDTO compraDTO) {

        Proveedor proveedor = proveedorRepository
                .findById(compraDTO.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Usuario usuario = usuarioRepository
                .findById(compraDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra compra;

        if (compraDTO.getIdCompra() == null) {
            compra = new Compra();
            compra.setTotal(BigDecimal.ZERO);
        } else {
            compra = compraRepository
                    .findById(compraDTO.getIdCompra())
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            compra.setTotal(compraDTO.getTotal());
        }

        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setFecha(compraDTO.getFecha());
        compra.setEstado(compraDTO.getEstado());

        return compraRepository.save(compra);
    }

    public void eliminar(Integer id) {
        compraRepository.deleteById(id);
    }
}