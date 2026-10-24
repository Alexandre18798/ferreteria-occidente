import { useEffect, useState } from "react";
import axios from "axios";
import {
  Plus,
  Pencil,
  Trash2,
  X,
  Save,
  ShieldCheck
} from "lucide-react";

function Perfiles() {
  const [perfiles, setPerfiles] = useState([]);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [editando, setEditando] = useState(false);
  const [guardando, setGuardando] = useState(false);

  const [perfilForm, setPerfilForm] = useState({
    idPerfil: null,
    nombre: "",
    estado: true
  });

  useEffect(() => {
    cargarPerfiles();
  }, []);

  const cargarPerfiles = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/perfiles"
      );

      setPerfiles(response.data);
    } catch (error) {
      console.error("Error al cargar perfiles:", error);
    }
  };

  const abrirNuevoPerfil = () => {
    setPerfilForm({
      idPerfil: null,
      nombre: "",
      estado: true
    });

    setEditando(false);
    setMostrarFormulario(true);
  };

  const abrirEditarPerfil = (perfil) => {
    setPerfilForm({
      idPerfil: perfil.idPerfil,
      nombre: perfil.nombre || "",
      estado: perfil.estado ?? true
    });

    setEditando(true);
    setMostrarFormulario(true);
  };

  const cerrarFormulario = () => {
    setMostrarFormulario(false);
    setEditando(false);

    setPerfilForm({
      idPerfil: null,
      nombre: "",
      estado: true
    });
  };

  const cambiarCampo = (event) => {
    const { name, value } = event.target;

    setPerfilForm({
      ...perfilForm,
      [name]: value
    });
  };

  const cambiarEstado = (event) => {
    setPerfilForm({
      ...perfilForm,
      estado: event.target.value === "true"
    });
  };

  const guardarPerfil = async (event) => {
    event.preventDefault();

    if (!perfilForm.nombre.trim()) {
      alert("Ingrese el nombre del perfil.");
      return;
    }

    const datos = {
      nombre: perfilForm.nombre.trim(),
      estado: perfilForm.estado
    };

    try {
      setGuardando(true);

      if (editando) {
        await axios.put(
          `http://localhost:8080/perfiles/${perfilForm.idPerfil}`,
          datos
        );
      } else {
        await axios.post(
          "http://localhost:8080/perfiles",
          datos
        );
      }

      await cargarPerfiles();

      cerrarFormulario();

      alert(
        editando
          ? "Perfil actualizado correctamente."
          : "Perfil registrado correctamente."
      );
    } catch (error) {
      console.error("Error al guardar perfil:", error);

      alert("No se pudo guardar el perfil.");
    } finally {
      setGuardando(false);
    }
  };

  const eliminarPerfil = async (idPerfil) => {
    const confirmar = window.confirm(
      "¿Está seguro de eliminar este perfil?"
    );

    if (!confirmar) {
      return;
    }

    try {
      await axios.delete(
        `http://localhost:8080/perfiles/${idPerfil}`
      );

      await cargarPerfiles();

      alert("Perfil eliminado correctamente.");
    } catch (error) {
      console.error("Error al eliminar perfil:", error);

      alert(
        "No se pudo eliminar el perfil. Puede estar asignado a uno o más usuarios."
      );
    }
  };

  return (
    <>
      <header className="topbar">
        <div>
          <p className="section-label">Administración del sistema</p>
          <h1>Perfiles</h1>
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

          <h2>Perfiles del sistema</h2>

          <p>
            Administra los perfiles utilizados para clasificar a los usuarios
            de Ferretería Occidente.
          </p>
          </div>
        </div>

        <button
          className="btn-nuevo"
          onClick={abrirNuevoPerfil}
        >
          <Plus size={19} />
          Nuevo perfil
        </button>
      </section>

      <section className="productos-panel">
        <div className="productos-panel-header">
          <div>
            <span>Listado general</span>
            <h3>Perfiles registrados</h3>
          </div>

          <div className="contador-productos">
            {perfiles.length} perfiles
          </div>
        </div>

        <div className="tabla-contenedor">
          <table className="tabla-productos">
            <thead>
              <tr>
                <th>ID</th>
                <th>Perfil</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>

            <tbody>
              {perfiles.length > 0 ? (
                perfiles.map((perfil) => (
                  <tr key={perfil.idPerfil}>
                    <td>#{perfil.idPerfil}</td>

                    <td>
                      <div className="producto-nombre">
                        <div className="producto-icono">
                          <ShieldCheck size={18} />
                        </div>

                        <strong>{perfil.nombre}</strong>
                      </div>
                    </td>

                    <td>
                      <span
                        className={
                          perfil.estado
                            ? "estado estado-activo"
                            : "estado estado-inactivo"
                        }
                      >
                        {perfil.estado ? "Activo" : "Inactivo"}
                      </span>
                    </td>

                    <td>
                      <div className="acciones">
                        <button
                          className="btn-accion btn-editar"
                          title="Editar perfil"
                          onClick={() =>
                            abrirEditarPerfil(perfil)
                          }
                        >
                          <Pencil size={17} />
                        </button>

                        <button
                          className="btn-accion btn-eliminar"
                          title="Eliminar perfil"
                          onClick={() =>
                            eliminarPerfil(perfil.idPerfil)
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
                    colSpan="4"
                    className="sin-registros"
                  >
                    No hay perfiles registrados.
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
                    ? "Editar perfil"
                    : "Nuevo perfil"}
                </h2>

                <p>
                  {editando
                    ? "Modifica la información del perfil seleccionado."
                    : "Ingresa la información del nuevo perfil."}
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
              onSubmit={guardarPerfil}
            >
              <div className="campo-formulario campo-completo">
                <label>Nombre del perfil</label>

                <input
                  type="text"
                  name="nombre"
                  value={perfilForm.nombre}
                  onChange={cambiarCampo}
                  placeholder="Ej. Vendedor"
                  maxLength="80"
                  required
                />
              </div>

              <div className="campo-formulario campo-completo">
                <label>Estado</label>

                <select
                  value={String(perfilForm.estado)}
                  onChange={cambiarEstado}
                >
                  <option value="true">Activo</option>
                  <option value="false">Inactivo</option>
                </select>
              </div>

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
                      : "Guardar perfil"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
}

export default Perfiles;