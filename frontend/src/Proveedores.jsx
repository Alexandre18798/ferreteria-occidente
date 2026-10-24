import { useEffect, useState } from "react";
import axios from "axios";
import {
  Truck,
  Plus,
  Pencil,
  Trash2,
  X,
  Save,
  Phone,
  MapPin
} from "lucide-react";

function Proveedores({ actualizarDashboard }) {
  const [proveedores, setProveedores] = useState([]);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [editando, setEditando] = useState(false);
  const [guardando, setGuardando] = useState(false);

  const [proveedorForm, setProveedorForm] = useState({
    idProveedor: null,
    nombre: "",
    telefono: "",
    direccion: "",
    estado: true
  });

  useEffect(() => {
    cargarProveedores();
  }, []);

  const cargarProveedores = async () => {
    try {
      const response = await axios.get(
        "https://ferreteria-occidente-backend.onrender.com/proveedores"
      );

      setProveedores(response.data);
    } catch (error) {
      console.error("Error al cargar los proveedores:", error);
    }
  };

  const abrirNuevoProveedor = () => {
    setProveedorForm({
      idProveedor: null,
      nombre: "",
      telefono: "",
      direccion: "",
      estado: true
    });

    setEditando(false);
    setMostrarFormulario(true);
  };

  const abrirEditarProveedor = (proveedor) => {
    setProveedorForm({
      idProveedor: proveedor.idProveedor,
      nombre: proveedor.nombre || "",
      telefono: proveedor.telefono || "",
      direccion: proveedor.direccion || "",
      estado: proveedor.estado ?? true
    });

    setEditando(true);
    setMostrarFormulario(true);
  };

  const cerrarFormulario = () => {
    setMostrarFormulario(false);
    setEditando(false);

    setProveedorForm({
      idProveedor: null,
      nombre: "",
      telefono: "",
      direccion: "",
      estado: true
    });
  };

  const cambiarCampo = (event) => {
    const { name, value } = event.target;

    setProveedorForm({
      ...proveedorForm,
      [name]: value
    });
  };

  const cambiarEstado = (event) => {
    setProveedorForm({
      ...proveedorForm,
      estado: event.target.value === "true"
    });
  };

  const guardarProveedor = async (event) => {
    event.preventDefault();

    if (
      !proveedorForm.nombre.trim() ||
      !proveedorForm.telefono.trim() ||
      !proveedorForm.direccion.trim()
    ) {
      alert("Complete todos los campos del proveedor.");
      return;
    }

    const datos = {
      nombre: proveedorForm.nombre.trim(),
      telefono: proveedorForm.telefono.trim(),
      direccion: proveedorForm.direccion.trim(),
      estado: proveedorForm.estado
    };

    try {
      setGuardando(true);

      if (editando) {
        await axios.put(
          `https://ferreteria-occidente-backend.onrender.com/proveedores/${proveedorForm.idProveedor}`,
          datos
        );
      } else {
        await axios.post(
          "https://ferreteria-occidente-backend.onrender.com/proveedores",
          datos
        );
      }

      await cargarProveedores();

      if (actualizarDashboard) {
        await actualizarDashboard();
      }

      cerrarFormulario();

      alert(
        editando
          ? "Proveedor actualizado correctamente."
          : "Proveedor registrado correctamente."
      );
    } catch (error) {
      console.error("Error al guardar el proveedor:", error);
      alert("No se pudo guardar el proveedor.");
    } finally {
      setGuardando(false);
    }
  };

  const eliminarProveedor = async (id) => {
    const confirmar = window.confirm(
      "¿Está seguro de eliminar este proveedor?"
    );

    if (!confirmar) {
      return;
    }

    try {
      await axios.delete(
        `https://ferreteria-occidente-backend.onrender.com/proveedores/${id}`
      );

      await cargarProveedores();

      if (actualizarDashboard) {
        await actualizarDashboard();
      }

      alert("Proveedor eliminado correctamente.");
    } catch (error) {
      console.error("Error al eliminar el proveedor:", error);

      alert(
        "No se pudo eliminar el proveedor. Puede estar relacionado con una compra."
      );
    }
  };

  return (
    <>
      <header className="topbar">
        <div>
          <p className="section-label">
            Gestión de proveedores
          </p>

          <h1>Proveedores</h1>
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
            <span>Abastecimiento</span>

            <h2>Proveedores registrados</h2>

            <p>
              Administra los proveedores que abastecen a Ferretería Occidente.
            </p>
          </div>
        </div>

        <button
          className="btn-nuevo"
          onClick={abrirNuevoProveedor}
        >
          <Plus size={19} />
          Nuevo proveedor
        </button>
      </section>

      <section className="productos-panel">
        <div className="productos-panel-header">
          <div>
            <span>Listado general</span>
            <h3>Directorio de proveedores</h3>
          </div>

          <div className="contador-productos">
            {proveedores.length} proveedores
          </div>
        </div>

        <div className="tabla-contenedor">
          <table className="tabla-productos">
            <thead>
              <tr>
                <th>ID</th>
                <th>Proveedor</th>
                <th>Teléfono</th>
                <th>Dirección</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>

            <tbody>
              {proveedores.length > 0 ? (
                proveedores.map((proveedor) => (
                  <tr key={proveedor.idProveedor}>
                    <td>#{proveedor.idProveedor}</td>

                    <td>
                      <div className="producto-nombre">
                        <div className="producto-icono">
                          <Truck size={18} />
                        </div>

                        <strong>{proveedor.nombre}</strong>
                      </div>
                    </td>

                    <td>
                      <div className="dato-proveedor">
                        <Phone size={15} />
                        <span>{proveedor.telefono}</span>
                      </div>
                    </td>

                    <td>
                      <div className="dato-proveedor">
                        <MapPin size={15} />
                        <span>{proveedor.direccion}</span>
                      </div>
                    </td>

                    <td>
                      <span
                        className={
                          proveedor.estado
                            ? "estado estado-activo"
                            : "estado estado-inactivo"
                        }
                      >
                        {proveedor.estado ? "Activo" : "Inactivo"}
                      </span>
                    </td>

                    <td>
                      <div className="acciones">
                        <button
                          className="btn-accion btn-editar"
                          title="Editar proveedor"
                          onClick={() =>
                            abrirEditarProveedor(proveedor)
                          }
                        >
                          <Pencil size={17} />
                        </button>

                        <button
                          className="btn-accion btn-eliminar"
                          title="Eliminar proveedor"
                          onClick={() =>
                            eliminarProveedor(proveedor.idProveedor)
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
                    colSpan="6"
                    className="sin-registros"
                  >
                    No hay proveedores registrados.
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
                    ? "Editar proveedor"
                    : "Nuevo proveedor"}
                </h2>

                <p>
                  {editando
                    ? "Modifica la información del proveedor seleccionado."
                    : "Ingresa la información del nuevo proveedor."}
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
              onSubmit={guardarProveedor}
            >
              <div className="campo-formulario campo-completo">
                <label>Nombre del proveedor</label>

                <input
                  type="text"
                  name="nombre"
                  value={proveedorForm.nombre}
                  onChange={cambiarCampo}
                  placeholder="Ej. Distribuidora Occidente"
                  required
                />
              </div>

              <div className="campo-formulario campo-completo">
                <label>Teléfono</label>

                <input
                  type="text"
                  name="telefono"
                  value={proveedorForm.telefono}
                  onChange={cambiarCampo}
                  placeholder="Ej. 5555-1234"
                  required
                />
              </div>

              <div className="campo-formulario campo-completo">
                <label>Dirección</label>

                <input
                  type="text"
                  name="direccion"
                  value={proveedorForm.direccion}
                  onChange={cambiarCampo}
                  placeholder="Ej. Huehuetenango, Guatemala"
                  required
                />
              </div>

              <div className="campo-formulario campo-completo">
                <label>Estado</label>

                <select
                  value={String(proveedorForm.estado)}
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
                      : "Guardar proveedor"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
}

export default Proveedores;