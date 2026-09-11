package com.ferreteriaoccidente.ferreteria.dto;
public class UsuarioDTO {
 private Integer idUsuario;
 private Integer idPerfil;
 private String nombre;
 private String usuario;
 private String contrasena;
 private String correo;
 private Boolean estado;
 public UsuarioDTO() {
 }
 public Integer getIdUsuario() {
 return idUsuario;
 }
 public void setIdUsuario(Integer idUsuario) {
 this.idUsuario = idUsuario;
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
 public String getUsuario() {
 return usuario;
 }
 public void setUsuario(String usuario) {
 this.usuario = usuario;
 }
 public String getContrasena() {
 return contrasena;
 }
 public void setContrasena(String contrasena) {
 this.contrasena = contrasena;
 }
 public String getCorreo() {
 return correo;
 }
 public void setCorreo(String correo) {
 this.correo = correo;
 }
 public Boolean getEstado() {
 return estado;
 }
 public void setEstado(Boolean estado) {
 this.estado = estado;
 }
}
