package com.ferreteriaoccidente.ferreteria.service;
import com.ferreteriaoccidente.ferreteria.dto.DetalleCompraDTO;
import com.ferreteriaoccidente.ferreteria.entity.Compra;
import com.ferreteriaoccidente.ferreteria.entity.DetalleCompra;
import com.ferreteriaoccidente.ferreteria.entity.Producto;
import com.ferreteriaoccidente.ferreteria.repository.DetalleCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class DetalleCompraService {
 @Autowired
 private DetalleCompraRepository detalleCompraRepository;
 public List<DetalleCompra> listar() {
    return detalleCompraRepository.findAll();
 }
 public DetalleCompra buscarPorId(Integer id) {
 return detalleCompraRepository.findById(id).orElse(null);
 }
 public DetalleCompra guardar(DetalleCompraDTO detalleCompraDTO) {
 DetalleCompra detalle = new DetalleCompra();
 Compra compra = new Compra();
 Producto producto = new Producto();
 compra.setIdCompra(detalleCompraDTO.getIdCompra());
 producto.setIdProducto(detalleCompraDTO.getIdProducto());
 detalle.setIdDetalleCompra(detalleCompraDTO.getIdDetalleCompra());
 detalle.setCompra(compra);
 detalle.setProducto(producto);
 detalle.setCantidad(detalleCompraDTO.getCantidad());
 detalle.setPrecioUnitario(detalleCompraDTO.getPrecioUnitario());
 detalle.setSubtotal(detalleCompraDTO.getSubtotal());
 return detalleCompraRepository.save(detalle);
 }
 public void eliminar(Integer id) {
 detalleCompraRepository.deleteById(id);
 }
}