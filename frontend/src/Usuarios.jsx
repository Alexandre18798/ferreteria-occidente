import { useEffect, useState } from "react";
import axios from "axios";
import {
  Plus,
  Pencil,
  Trash2,
  X,
  Save,
  User,
  ShieldCheck
} from "lucide-react";

function Usuarios({ actualizarDashboard }) {
  const [usuarios, setUsuarios] = useState([]);
  const [perfiles, setPerfiles] = useState([]);

  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [editando, setEditando] = useState(false);
  const [guardando, setGuardando] = useState(false);

  const [usuarioForm, setUsuarioForm] = useState({
    idUsuario: null,
    idPerfil: "",
    nombre: "",
    usuario: "",
    contrasena: "",
    correo: "",
    estado: true
  });

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      const [respuestaUsuarios, respuestaPerfiles] = await Promise.all([
        axios.get("http://localhost:8080/usuarios"),
        axios.get("http://localhost:8080/perfiles")
      ]);

      setUsuarios(respuestaUsuarios.data);
      setPerfiles(respuestaPerfiles.data);
    } catch (error) {
      console.error("Error al cargar usuarios:", error);
    }
  };

  const abrirNuevoUsuario = () => {
    setUsuarioForm({
      idUsuario: null,
      idPerfil: "",
      nombre: "",
      usuario: "",
      contrasena: "",
      correo: "",
      estado: true
    });

    setEditando(false);
    setMostrarFormulario(true);
  };

  const abrirEditarUsuario = (usuario) => {
    setUsuarioForm({
      idUsuario: usuario.idUsuario,
      idPerfil: usuario.perfil?.idPerfil || "",
      nombre: usuario.nombre || "",
      usuario: usuario.usuario || "",
      contrasena: "",
      correo: usuario.correo || "",
      estado: usuario.estado ?? true
    });

    setEditando(true);
    setMostrarFormulario(true);
  };

  const cerrarFormulario = () => {
    setMostrarFormulario(false);
    setEditando(false);

    setUsuarioForm({
      idUsuario: null,
      idPerfil: "",
      nombre: "",
      usuario: "",
      contrasena: "",
      correo: "",
      estado: true
    });
  };

  const cambiarCampo = (event) => {
    const { name, value } = event.target;

    setUsuarioForm({
      ...usuarioForm,
      [name]: value
    });
  };

  const cambiarEstado = (event) => {
    setUsuarioForm({
      ...usuarioForm,
      estado: event.target.value === "true"
    });
  };

  const guardarUsuario = async (event) => {
    event.preventDefault();

    if (
      !usuarioForm.idPerfil ||
      !usuarioForm.nombre.trim() ||
      !usuarioForm.usuario.trim() ||
      !usuarioForm.correo.trim()
    ) {
      alert("Complete todos los campos obligatorios.");
      return;
    }

    if (!editando && !usuarioForm.contrasena.trim()) {
      alert("Ingrese una contraseña para el nuevo usuario.");
      return;
    }

    const datos = {
      idPerfil: Number(usuarioForm.idPerfil),
      nombre: usuarioForm.nombre.trim(),
      usuario: usuarioForm.usuario.trim(),
      contrasena: usuarioForm.contrasena,
      correo: usuarioForm.correo.trim(),
      estado: usuarioForm.estado
    };

    try {
      setGuardando(true);

      if (editando) {
        await axios.put(
          `http://localhost:8080/usuarios/${usuarioForm.idUsuario}`,
          datos
        );
      } else {
        await axios.post(
          "http://localhost:8080/usuarios",
          datos
        );
      }

      await cargarDatos();

      if (actualizarDashboard) {
        await actualizarDashboard();
      }

      cerrarFormulario();

      alert(
        editando
          ? "Usuario actualizado correctamente."
          : "Usuario registrado correctamente."
      );
    } catch (error) {
      console.error("Error al guardar usuario:", error);

      if (error.response) {
        console.error("Estado:", error.response.status);
        console.error("Respuesta:", error.response.data);
      }

      alert(
        "No se pudo guardar el usuario. Verifique que el nombre de usuario no esté repetido."
      );
    } finally {
      setGuardando(false);
    }
  };

  const eliminarUsuario = async (idUsuario) => {
    const confirmar = window.confirm(
      "¿Está seguro de eliminar este usuario?"
    );

    if (!confirmar) {
      return;
    }

    try {
      await axios.delete(
        `http://localhost:8080/usuarios/${idUsuario}`
      );

      await cargarDatos();

      if (actualizarDashboard) {
        await actualizarDashboard();
      }

      alert("Usuario eliminado correctamente.");
    } catch (error) {
      console.error("Error al eliminar usuario:", error);

      alert(
        "No se pudo eliminar el usuario. Puede estar relacionado con una compra."
      );
    }
  };

  return (
    <>
      <header className="topbar">
        <div>
          <p className="section-label">Administración del sistema</p>
          <h1>Usuarios</h1>
        </div>

        <div className="user-box">
          <div className="user-avatar">OC</div>

          <div>
            <strong>Administrador</strong>
            <span>Ferretería Occidente</span>
          </div>
        </div>
      </header>

      <section className="productos-header">
        <div className="section-brand-content">
          <img
            src="/images/logo-ferreteria.png"
            alt="Ferretería Occidente"
            className="section-brand-logo"
          />

          <div>
          <span>Administración</span>
          <h2>Usuarios del sistema</h2>

          <p>
            Administra las personas que tienen acceso al sistema de
            Ferretería Occidente.
          </p>
          </div>
        </div>

        <button
          className="btn-nuevo"
          onClick={abrirNuevoUsuario}
        >
          <Plus size={19} />
          Nuevo usuario
        </button>
      </section>

      <section className="productos-panel">
        <div className="productos-panel-header">
          <div>
            <span>Listado general</span>
            <h3>Usuarios registrados</h3>
          </div>

          <div className="contador-productos">
            {usuarios.length} usuarios
          </div>
        </div>

        <div className="tabla-contenedor">
          <table className="tabla-productos">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Usuario</th>
                <th>Correo</th>
                <th>Perfil</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>

            <tbody>
              {usuarios.length > 0 ? (
                usuarios.map((usuario) => (
                  <tr key={usuario.idUsuario}>
                    <td>#{usuario.idUsuario}</td>

                    <td>
                      <div className="producto-nombre">
                        <div className="producto-icono">
                          <User size={18} />
                        </div>

                        <strong>{usuario.nombre}</strong>
                      </div>
                    </td>

                    <td>{usuario.usuario}</td>

                    <td>{usuario.correo}</td>

                    <td>
                      <div className="producto-nombre">
                        <ShieldCheck size={17} />
                        <span>
                          {usuario.perfil?.nombre || "Sin perfil"}
                        </span>
                      </div>
                    </td>

                    <td>
                      <span
                        className={
                          usuario.estado
                            ? "estado estado-activo"
                            : "estado estado-inactivo"
                        }
                      >
                        {usuario.estado ? "Activo" : "Inactivo"}
                      </span>
                    </td>

                    <td>
                      <div className="acciones">
                        <button
                          className="btn-accion btn-editar"
                          title="Editar usuario"
                          onClick={() =>
                            abrirEditarUsuario(usuario)
                          }
                        >
                          <Pencil size={17} />
                        </button>

                        <button
                          className="btn-accion btn-eliminar"
                          title="Eliminar usuario"
                          onClick={() =>
                            eliminarUsuario(usuario.idUsuario)
                          }
                        >
                          <Trash2 size={17} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colSpan="7"
                    className="sin-registros"
                  >
                    No hay usuarios registrados.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {mostrarFormulario && (
        <div className="modal-fondo">
          <div className="modal-producto">
            <div className="modal-brand">
              <img
                src="/images/logo-ferreteria.png"
                alt="Ferretería Occidente"
                className="modal-brand-logo"
              />
            </div>

            <div className="modal-header">
              <div>
                <span>
                  {editando
                    ? "Editar registro"
                    : "Nuevo registro"}
                </span>

                <h2>
                  {editando
                    ? "Editar usuario"
                    : "Nuevo usuario"}
                </h2>

                <p>
                  {editando
                    ? "Modifica la información del usuario seleccionado."
                    : "Ingresa la información del nuevo usuario."}
                </p>
              </div>

              <button
                type="button"
                className="btn-cerrar-modal"
                onClick={cerrarFormulario}
              >
                <X size={21} />
              </button>
            </div>

            <form
              className="form-producto"
              onSubmit={guardarUsuario}
            >
              <div className="campo-formulario campo-completo">
                <label>Nombre completo</label>

                <input
                  type="text"
                  name="nombre"
                  value={usuarioForm.nombre}
                  onChange={cambiarCampo}
                  placeholder="Ej. Juan Pérez"
                  required
                />
              </div>

              <div className="campo-formulario">
                <label>Usuario</label>

                <input
                  type="text"
                  name="usuario"
                  value={usuarioForm.usuario}
                  onChange={cambiarCampo}
                  placeholder="Ej. jperez"
                  required
                />
              </div>

              <div className="campo-formulario">
                <label>Correo electrónico</label>

                <input
                  type="email"
                  name="correo"
                  value={usuarioForm.correo}
                  onChange={cambiarCampo}
                  placeholder="usuario@correo.com"
                  required
                />
              </div>

              <div className="campo-formulario">
                <label>
                  {editando
                    ? "Nueva contraseña"
                    : "Contraseña"}
                </label>

                <input
                  type="password"
                  name="contrasena"
                  value={usuarioForm.contrasena}
                  onChange={cambiarCampo}
                  placeholder={
                    editando
                      ? "Dejar vacío para conservar"
                      : "Ingrese una contraseña"
                  }
                  required={!editando}
                />
              </div>

              <div className="campo-formulario">
                <label>Perfil</label>

                <select
                  name="idPerfil"
                  value={usuarioForm.idPerfil}
                  onChange={cambiarCampo}
                  required
                >
                  <option value="">
                    Seleccione un perfil
                  </option>

                  {perfiles
                    .filter((perfil) => perfil.estado)
                    .map((perfil) => (
                      <option
                        key={perfil.idPerfil}
                        value={perfil.idPerfil}
                      >
                        {perfil.nombre}
                      </option>
                    ))}
                </select>
              </div>

              <div className="campo-formulario campo-completo">
                <label>Estado</label>

                <select
                  value={String(usuarioForm.estado)}
                  onChange={cambiarEstado}
                >
                  <option value="true">Activo</option>
                  <option value="false">Inactivo</option>
                </select>
              </div>

              {editando && (
                <div className="campo-completo">
                  <p
                    style={{
                      margin: 0,
                      fontSize: "12px",
                      color: "#7b8780"
                    }}
                  >
                    Si no deseas cambiar la contraseña, deja el campo de
                    contraseña vacío.
                  </p>
                </div>
              )}

              <div className="modal-acciones">
                <button
                  type="button"
                  className="btn-cancelar"
                  onClick={cerrarFormulario}
                >
                  Cancelar
                </button>

                <button
                  type="submit"
                  className="btn-guardar"
                  disabled={guardando}
                >
                  <Save size={18} />

                  {guardando
                    ? "Guardando..."
                    : editando
                      ? "Guardar cambios"
                      : "Guardar usuario"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
}

export default Usuarios;