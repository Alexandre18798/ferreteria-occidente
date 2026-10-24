package com.ferreteriaoccidente.ferreteria.service;

import com.ferreteriaoccidente.ferreteria.dto.DetalleCompraDTO;
import com.ferreteriaoccidente.ferreteria.entity.Compra;
import com.ferreteriaoccidente.ferreteria.entity.DetalleCompra;
import com.ferreteriaoccidente.ferreteria.entity.Producto;
import com.ferreteriaoccidente.ferreteria.repository.CompraRepository;
import com.ferreteriaoccidente.ferreteria.repository.DetalleCompraRepository;
import com.ferreteriaoccidente.ferreteria.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetalleCompraService {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CompraRepository compraRepository;

    public List<DetalleCompra> listar() {
        return detalleCompraRepository.findAll();
    }

    public DetalleCompra buscarPorId(Integer id) {
        return detalleCompraRepository.findById(id).orElse(null);
    }

    public DetalleCompra guardar(DetalleCompraDTO detalleCompraDTO) {

        Producto producto = productoRepository
                .findById(detalleCompraDTO.getIdProducto())
                .orElse(null);

        Compra compra = compraRepository
                .findById(detalleCompraDTO.getIdCompra())
                .orElse(null);

        if (producto == null || compra == null) {
            return null;
        }

        DetalleCompra detalle = new DetalleCompra();

        detalle.setCompra(compra);
        detalle.setProducto(producto);
        detalle.setCantidad(detalleCompraDTO.getCantidad());
        detalle.setPrecioUnitario(detalleCompraDTO.getPrecioUnitario());

        BigDecimal subtotal = detalleCompraDTO.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalleCompraDTO.getCantidad()));

        detalle.setSubtotal(subtotal);

        Integer stockActual = producto.getStock();

        if (stockActual == null) {
            stockActual = 0;
        }

        producto.setStock(stockActual + detalleCompraDTO.getCantidad());
        productoRepository.save(producto);

        BigDecimal totalActual = compra.getTotal();

        if (totalActual == null) {
            totalActual = BigDecimal.ZERO;
        }

        compra.setTotal(totalActual.add(subtotal));
        compraRepository.save(compra);

        return detalleCompraRepository.save(detalle);
    }

    public DetalleCompra actualizar(Integer id, DetalleCompraDTO detalleCompraDTO) {

        DetalleCompra detalle = detalleCompraRepository.findById(id).orElse(null);

        if (detalle == null) {
            return null;
        }

        Producto productoAnterior = detalle.getProducto();
        Compra compraAnterior = detalle.getCompra();

        Integer stockAnterior = productoAnterior.getStock();

        if (stockAnterior == null) {
            stockAnterior = 0;
        }

        productoAnterior.setStock(stockAnterior - detalle.getCantidad());
        productoRepository.save(productoAnterior);

        BigDecimal totalAnterior = compraAnterior.getTotal();

        if (totalAnterior == null) {
            totalAnterior = BigDecimal.ZERO;
        }

        compraAnterior.setTotal(totalAnterior.subtract(detalle.getSubtotal()));
        compraRepository.save(compraAnterior);

        Producto productoNuevo = productoRepository
                .findById(detalleCompraDTO.getIdProducto())
                .orElse(null);

        Compra compraNueva = compraRepository
                .findById(detalleCompraDTO.getIdCompra())
                .orElse(null);

        if (productoNuevo == null || compraNueva == null) {
            return null;
        }

        BigDecimal subtotalNuevo = detalleCompraDTO.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalleCompraDTO.getCantidad()));

        detalle.setCompra(compraNueva);
        detalle.setProducto(productoNuevo);
        detalle.setCantidad(detalleCompraDTO.getCantidad());
        detalle.setPrecioUnitario(detalleCompraDTO.getPrecioUnitario());
        detalle.setSubtotal(subtotalNuevo);

        Integer stockNuevo = productoNuevo.getStock();

        if (stockNuevo == null) {
            stockNuevo = 0;
        }

        productoNuevo.setStock(stockNuevo + detalleCompraDTO.getCantidad());
        productoRepository.save(productoNuevo);

        BigDecimal totalNuevo = compraNueva.getTotal();

        if (totalNuevo == null) {
            totalNuevo = BigDecimal.ZERO;
        }

        compraNueva.setTotal(totalNuevo.add(subtotalNuevo));
        compraRepository.save(compraNueva);

        return detalleCompraRepository.save(detalle);
    }

    public void eliminar(Integer id) {

        DetalleCompra detalle = detalleCompraRepository.findById(id).orElse(null);

        if (detalle == null) {
            return;
        }

        Producto producto = detalle.getProducto();
        Compra compra = detalle.getCompra();

        Integer stockActual = producto.getStock();

        if (stockActual == null) {
            stockActual = 0;
        }

        producto.setStock(stockActual - detalle.getCantidad());
        productoRepository.save(producto);

        BigDecimal totalActual = compra.getTotal();

        if (totalActual == null) {
            totalActual = BigDecimal.ZERO;
        }

        compra.setTotal(totalActual.subtract(detalle.getSubtotal()));
        compraRepository.save(compra);

        detalleCompraRepository.deleteById(id);
    }
}