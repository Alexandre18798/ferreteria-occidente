package com.ferreteriaoccidente.ferreteria.service;

import com.ferreteriaoccidente.ferreteria.dto.ProductoDTO;
import com.ferreteriaoccidente.ferreteria.entity.Producto;
import com.ferreteriaoccidente.ferreteria.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto guardar(ProductoDTO productoDTO) {

        Producto producto = new Producto();

        producto.setIdProducto(productoDTO.getIdProducto());
        producto.setNombre(productoDTO.getNombre());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecioVenta(productoDTO.getPrecioVenta());
        producto.setStock(productoDTO.getStock());
        producto.setEstado(productoDTO.getEstado());

        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
}