package com.ferreteriaoccidente.ferreteria.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

      @Entity
      @Table(name = "PROVEEDORES")
public class Proveedor {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "id_proveedor")
    private Integer idProveedor;

      @Column(name = "nombre", length = 100)
    private String nombre;

      @Column(name = "telefono", length = 20)
    private String telefono;

      @Column(name = "direccion", length = 150)
    private String direccion;

      @Column(name = "estado")
    private Boolean estado;

    public Proveedor() {
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}