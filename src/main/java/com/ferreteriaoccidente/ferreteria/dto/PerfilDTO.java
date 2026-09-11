package com.ferreteriaoccidente.ferreteria.dto;
public class PerfilDTO {
 private Integer idPerfil;
 private String nombre;
 private Boolean estado;
 public PerfilDTO() {
 }
 public Integer getIdPerfil() {
 return idPerfil;
 }
 public void setIdPerfil(Integer idPerfil) {
 this.idPerfil = idPerfil;
 }
 public String getNombre() {
 return nombre;
 }
 public void setNombre(String nombre) {
 this.nombre = nombre;
 }
 public Boolean getEstado() {
 return estado;
 }
 public void setEstado(Boolean estado) {
 this.estado = estado;
 }
}