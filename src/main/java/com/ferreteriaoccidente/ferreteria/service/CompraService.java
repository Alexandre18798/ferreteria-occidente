package com.ferreteriaoccidente.ferreteria.service;
import com.ferreteriaoccidente.ferreteria.dto.CompraDTO;
import com.ferreteriaoccidente.ferreteria.entity.Compra;
import com.ferreteriaoccidente.ferreteria.entity.Proveedor;
import com.ferreteriaoccidente.ferreteria.entity.Usuario;
import com.ferreteriaoccidente.ferreteria.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CompraService {
 @Autowired
 private CompraRepository compraRepository;
 public List<Compra> listar() {
 return compraRepository.findAll();
 }
 public Compra buscarPorId(Integer id) {
 return compraRepository.findById(id).orElse(null);
 }
 public Compra guardar(CompraDTO compraDTO) {
 Compra compra = new Compra();
 Proveedor proveedor = new Proveedor();
 Usuario usuario = new Usuario();
 proveedor.setIdProveedor(compraDTO.getIdProveedor());
 usuario.setIdUsuario(compraDTO.getIdUsuario());
 compra.setIdCompra(compraDTO.getIdCompra());
 compra.setProveedor(proveedor);
 compra.setUsuario(usuario);
 compra.setFecha(compraDTO.getFecha());
 compra.setTotal(compraDTO.getTotal());
 compra.setEstado(compraDTO.getEstado());
 return compraRepository.save(compra);
 }
 public void eliminar(Integer id) {
 compraRepository.deleteById(id);
 }
}
