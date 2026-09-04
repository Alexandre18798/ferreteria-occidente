package com.ferreteriaoccidente.ferreteria.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

      @Entity
      @Table(name = "DETALLE_COMPRA")
public class DetalleCompra {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "id_detalle_compra")
    private Integer idDetalleCompra;

      @ManyToOne
      @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

      @ManyToOne
      @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

      @Column(name = "cantidad")
    private Integer cantidad;

      @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

      @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    public DetalleCompra() {
    }

    public Integer getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Integer idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}