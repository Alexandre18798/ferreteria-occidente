package com.ferreteriaoccidente.ferreteria.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class CompraDTO {
 private Integer idCompra;
 private Integer idProveedor;
 private Integer idUsuario;
 private LocalDateTime fecha;
 private BigDecimal total;
 private Boolean estado;
 public CompraDTO() {
 }
 public Integer getIdCompra() { return idCompra; }
 public void setIdCompra(Integer idCompra) { this.idCompra = idCompra; }
 public Integer getIdProveedor() { return idProveedor; }
 public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
 public Integer getIdUsuario() { return idUsuario; }
 public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
 public LocalDateTime getFecha() { return fecha; }
 public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
 public BigDecimal getTotal() { return total; }
 public void setTotal(BigDecimal total) { this.total = total; }
 public Boolean getEstado() { return estado; }
 public void setEstado(Boolean estado) { this.estado = estado; }
}
