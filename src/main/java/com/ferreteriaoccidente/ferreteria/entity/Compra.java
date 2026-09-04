package com.ferreteriaoccidente.ferreteria.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

      @Entity
      @Table(name = "COMPRAS")
public class Compra {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "id_compra")
    private Integer idCompra;

      @ManyToOne
      @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

      @ManyToOne
      @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

      @Column(name = "fecha")
    private LocalDateTime fecha;

      @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

      @Column(name = "estado")
    private Boolean estado;

    public Compra() {
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}