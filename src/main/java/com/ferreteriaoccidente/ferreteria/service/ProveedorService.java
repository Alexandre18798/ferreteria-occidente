package com.ferreteriaoccidente.ferreteria.service;

import com.ferreteriaoccidente.ferreteria.dto.ProveedorDTO;
import com.ferreteriaoccidente.ferreteria.entity.Proveedor;
import com.ferreteriaoccidente.ferreteria.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public Proveedor buscarPorId(Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    public Proveedor guardar(ProveedorDTO proveedorDTO) {

        Proveedor proveedor = new Proveedor();

        proveedor.setIdProveedor(proveedorDTO.getIdProveedor());
        proveedor.setNombre(proveedorDTO.getNombre());
        proveedor.setTelefono(proveedorDTO.getTelefono());
        proveedor.setDireccion(proveedorDTO.getDireccion());
        proveedor.setEstado(proveedorDTO.getEstado());

        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }
}