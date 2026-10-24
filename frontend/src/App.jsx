import { useEffect, useState } from "react";
import axios from "axios";
import {
  LayoutDashboard,
  Package,
  Truck,
  ShoppingCart,
  Users,
  ShieldCheck,
  BarChart3,
  Download,
  Settings,
  Plus,
  Pencil,
  Trash2,
  X,
  Save
} from "lucide-react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer
} from "recharts";
import Proveedores from "./Proveedores";
import Compras from "./Compras";
import Usuarios from "./Usuarios";
import Perfiles from "./Perfiles";
import Reportes from "./Reportes";
import Exportaciones from "./Exportaciones";
import "./App.css";

function App() {
  const [seccion, setSeccion] = useState("dashboard");

  const [resumen, setResumen] = useState({
    totalProductos: 0,
    totalProveedores: 0,
    totalCompras: 0,
    totalUsuarios: 0,
    montoTotalCompras: 0
  });

  const [comprasPorMes, setComprasPorMes] = useState([]);
  const [productos, setProductos] = useState([]);

  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [editando, setEditando] = useState(false);
  const [guardando, setGuardando] = useState(false);

  const [productoForm, setProductoForm] = useState({
    idProducto: null,
    nombre: "",
    categoria: "",
    precioVenta: "",
    stock: "",
    estado: true
  });

  useEffect(() => {
    cargarDashboard();
    cargarProductos();
  }, []);

  const cargarDashboard = async () => {
    try {
      const respuestaResumen = await axios.get(
        "http://localhost:8080/reportes/resumen"
      );

      setResumen(respuestaResumen.data);

      const respuestaCompras = await axios.get(
        "http://localhost:8080/reportes/compras-por-mes"
      );

      const datosGrafica = Object.entries(
        respuestaCompras.data
      ).map(([mes, total]) => ({
        mes,
        total: Number(total)
      }));

      setComprasPorMes(datosGrafica);
    } catch (error) {
      console.error("Error al cargar el dashboard:", error);
    }
  };

  const cargarProductos = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/productos"
      );

      setProductos(response.data);
    } catch (error) {
      console.error("Error al cargar productos:", error);
    }
  };

  const formatearDinero = (valor) => {
    return new Intl.NumberFormat("es-GT", {
      style: "currency",
      currency: "GTQ"
    }).format(Number(valor || 0));
  };

  const abrirNuevoProducto = () => {
    setProductoForm({
      idProducto: null,
      nombre: "",
      categoria: "",
      precioVenta: "",
      stock: "",
      estado: true
    });

    setEditando(false);
    setMostrarFormulario(true);
  };

  const abrirEditarProducto = (producto) => {
    setProductoForm({
      idProducto: producto.idProducto,
      nombre: producto.nombre || "",
      categoria: producto.categoria || "",
      precioVenta: producto.precioVenta ?? "",
      stock: producto.stock ?? "",
      estado: producto.estado ?? true
    });

    setEditando(true);
    setMostrarFormulario(true);
  };

  const cerrarFormulario = () => {
    setMostrarFormulario(false);
    setEditando(false);

    setProductoForm({
      idProducto: null,
      nombre: "",
      categoria: "",
      precioVenta: "",
      stock: "",
      estado: true
    });
  };

  const cambiarCampoProducto = (event) => {
    const { name, value } = event.target;

    setProductoForm({
      ...productoForm,
      [name]: value
    });
  };

  const cambiarEstadoProducto = (event) => {
    setProductoForm({
      ...productoForm,
      estado: event.target.value === "true"
    });
  };

  const guardarProducto = async (event) => {
    event.preventDefault();

    if (
      !productoForm.nombre.trim() ||
      !productoForm.categoria.trim() ||
      productoForm.precioVenta === "" ||
      productoForm.stock === ""
    ) {
      alert("Complete todos los campos del producto.");
      return;
    }

    const datos = {
      nombre: productoForm.nombre.trim(),
      categoria: productoForm.categoria.trim(),
      precioVenta: Number(productoForm.precioVenta),
      stock: Number(productoForm.stock),
      estado: productoForm.estado
    };

    try {
      setGuardando(true);

      if (editando) {
        await axios.put(
          `http://localhost:8080/productos/${productoForm.idProducto}`,
          datos
        );
      } else {
        await axios.post(
          "http://localhost:8080/productos",
          datos
        );
      }

      await cargarProductos();
      await cargarDashboard();

      cerrarFormulario();

      alert(
        editando
          ? "Producto actualizado correctamente."
          : "Producto registrado correctamente."
      );
    } catch (error) {
      console.error("Error al guardar el producto:", error);
      alert("No se pudo guardar el producto.");
    } finally {
      setGuardando(false);
    }
  };

  const eliminarProducto = async (id) => {
    const confirmar = window.confirm(
      "¿Está seguro de eliminar este producto?"
    );

    if (!confirmar) {
      return;
    }

    try {
      await axios.delete(
        `http://localhost:8080/productos/${id}`
      );

      await cargarProductos();
      await cargarDashboard();

      alert("Producto eliminado correctamente.");
    } catch (error) {
      console.error("Error al eliminar el producto:", error);

      alert(
        "No se pudo eliminar el producto. Puede estar relacionado con una compra."
      );
    }
  };

  const renderDashboard = () => {
    return (
      <>
        <header className="topbar">
          <div>
            <p className="section-label">Panel principal</p>
            <h1>Dashboard</h1>
          </div>

          <div className="user-box">
            <div className="user-avatar">OC</div>

            <div>
              <strong>Administrador</strong>
              <span>Ferretería Occidente</span>
            </div>
          </div>
        </header>

        <section className="welcome">
          <div className="welcome-content">
            <img
              src="/images/logo-ferreteria.png"
              alt="Ferretería Occidente"
              className="welcome-logo"
            />

            <div>
              <span>Sistema de gestión</span>

              <h2>Bienvenido a Ferretería Occidente</h2>

              <p>
                Controla el inventario, proveedores y compras
                desde un solo lugar.
              </p>
            </div>
          </div>

          <Package size={82} strokeWidth={1.3} />
        </section>

        <section className="cards">
          <div className="stat-card">
            <div className="stat-icon">
              <Package size={22} />
            </div>

            <div>
              <span>Productos</span>
              <h3>{resumen.totalProductos || 0}</h3>
              <p>Registrados en inventario</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">
              <Truck size={22} />
            </div>

            <div>
              <span>Proveedores</span>
              <h3>{resumen.totalProveedores || 0}</h3>
              <p>Proveedores registrados</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">
              <ShoppingCart size={22} />
            </div>

            <div>
              <span>Compras</span>
              <h3>{resumen.totalCompras || 0}</h3>
              <p>Compras registradas</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">
              <Users size={22} />
            </div>

            <div>
              <span>Usuarios</span>
              <h3>{resumen.totalUsuarios || 0}</h3>
              <p>Usuarios del sistema</p>
            </div>
          </div>
        </section>

        <section className="dashboard-grid">
          <div className="panel">
            <div className="panel-header">
              <span>Actividad</span>
              <h3>Compras por mes</h3>
            </div>

            {comprasPorMes.length > 0 ? (
              <div
                style={{
                  width: "100%",
                  height: "230px",
                  marginTop: "25px"
                }}
              >
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={comprasPorMes}>
                    <CartesianGrid
                      strokeDasharray="3 3"
                      vertical={false}
                    />

                    <XAxis
                      dataKey="mes"
                      tick={{ fontSize: 11 }}
                    />

                    <YAxis
                      tick={{ fontSize: 11 }}
                    />

                    <Tooltip
                      formatter={(value) => [
                        formatearDinero(value),
                        "Total"
                      ]}
                    />

                    <Bar
                      dataKey="total"
                      fill="#1b5c3c"
                      radius={[6, 6, 0, 0]}
                    />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            ) : (
              <div className="empty-chart">
                No hay información de compras para mostrar.
              </div>
            )}
          </div>

          <div className="panel">
            <div className="panel-header">
              <span>Resumen financiero</span>
              <h3>Total en compras</h3>
            </div>

            <div className="inventory-summary">
              <div className="quetzal-icon">Q</div>

              <strong>
                {formatearDinero(resumen.montoTotalCompras)}
              </strong>

              <p>
                Monto acumulado de las compras registradas
                en el sistema.
              </p>
            </div>
          </div>
        </section>
      </>
    );
  };

  const renderProductos = () => {
    return (
      <>
        <header className="topbar">
          <div>
            <p className="section-label">
              Gestión de inventario
            </p>
            <h1>Productos</h1>
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
              <span>Inventario</span>
              <h2>Productos registrados</h2>

              <p>
                Administra los productos disponibles en
                Ferretería Occidente.
              </p>
            </div>
          </div>

          <button
            className="btn-nuevo"
            onClick={abrirNuevoProducto}
          >
            <Plus size={19} />
            Nuevo producto
          </button>
        </section>

        <section className="productos-panel">
          <div className="productos-panel-header">
            <div>
              <span>Listado general</span>
              <h3>Inventario de productos</h3>
            </div>

            <div className="contador-productos">
              {productos.length} productos
            </div>
          </div>

          <div className="tabla-contenedor">
            <table className="tabla-productos">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Producto</th>
                  <th>Categoría</th>
                  <th>Precio</th>
                  <th>Stock</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>

              <tbody>
                {productos.length > 0 ? (
                  productos.map((producto) => (
                    <tr key={producto.idProducto}>
                      <td>#{producto.idProducto}</td>

                      <td>
                        <div className="producto-nombre">
                          <div className="producto-icono">
                            <Package size={18} />
                          </div>

                          <strong>{producto.nombre}</strong>
                        </div>
                      </td>

                      <td>{producto.categoria}</td>

                      <td>
                        {formatearDinero(producto.precioVenta)}
                      </td>

                      <td>
                        <span
                          className={
                            Number(producto.stock) <= 5
                              ? "stock stock-bajo"
                              : "stock stock-normal"
                          }
                        >
                          {producto.stock}
                        </span>
                      </td>

                      <td>
                        <span
                          className={
                            producto.estado
                              ? "estado estado-activo"
                              : "estado estado-inactivo"
                          }
                        >
                          {producto.estado
                            ? "Activo"
                            : "Inactivo"}
                        </span>
                      </td>

                      <td>
                        <div className="acciones">
                          <button
                            className="btn-accion btn-editar"
                            title="Editar producto"
                            onClick={() =>
                              abrirEditarProducto(producto)
                            }
                          >
                            <Pencil size={17} />
                          </button>

                          <button
                            className="btn-accion btn-eliminar"
                            title="Eliminar producto"
                            onClick={() =>
                              eliminarProducto(producto.idProducto)
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
                      No hay productos registrados.
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
                      ? "Editar producto"
                      : "Nuevo producto"}
                  </h2>

                  <p>
                    {editando
                      ? "Modifica la información del producto seleccionado."
                      : "Ingresa la información del nuevo producto."}
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
                onSubmit={guardarProducto}
              >
                <div className="campo-formulario campo-completo">
                  <label>Nombre del producto</label>

                  <input
                    type="text"
                    name="nombre"
                    value={productoForm.nombre}
                    onChange={cambiarCampoProducto}
                    placeholder="Ej. Martillo profesional"
                    required
                  />
                </div>

                <div className="campo-formulario campo-completo">
                  <label>Categoría</label>

                  <input
                    type="text"
                    name="categoria"
                    value={productoForm.categoria}
                    onChange={cambiarCampoProducto}
                    placeholder="Ej. Herramientas"
                    required
                  />
                </div>

                <div className="campo-formulario">
                  <label>Precio de venta</label>

                  <div className="input-precio">
                    <span>Q</span>

                    <input
                      type="number"
                      name="precioVenta"
                      value={productoForm.precioVenta}
                      onChange={cambiarCampoProducto}
                      min="0"
                      step="0.01"
                      placeholder="0.00"
                      required
                    />
                  </div>
                </div>

                <div className="campo-formulario">
                  <label>Stock</label>

                  <input
                    type="number"
                    name="stock"
                    value={productoForm.stock}
                    onChange={cambiarCampoProducto}
                    min="0"
                    step="1"
                    placeholder="0"
                    required
                  />
                </div>

                <div className="campo-formulario campo-completo">
                  <label>Estado</label>

                  <select
                    value={String(productoForm.estado)}
                    onChange={cambiarEstadoProducto}
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
                        : "Guardar producto"}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </>
    );
  };

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="brand brand-with-logo">
          <img
            src="/images/logo-ferreteria.png"
            alt="Ferretería Occidente"
            className="brand-logo"
          />
        </div>

        <nav className="menu">
          <button
            className={`menu-item ${
              seccion === "dashboard" ? "active" : ""
            }`}
            onClick={() => setSeccion("dashboard")}
          >
            <LayoutDashboard size={20} />
            <span>Dashboard</span>
          </button>

          <button
            className={`menu-item ${
              seccion === "productos" ? "active" : ""
            }`}
            onClick={() => setSeccion("productos")}
          >
            <Package size={20} />
            <span>Productos</span>
          </button>

          <button
            className={`menu-item ${
              seccion === "proveedores" ? "active" : ""
            }`}
            onClick={() => setSeccion("proveedores")}
          >
            <Truck size={20} />
            <span>Proveedores</span>
          </button>

          <button
            className={`menu-item ${
              seccion === "compras" ? "active" : ""
            }`}
            onClick={() => setSeccion("compras")}
          >
            <ShoppingCart size={20} />
            <span>Compras</span>
          </button>

          <button
            className={`menu-item ${
              seccion === "usuarios" ? "active" : ""
            }`}
            onClick={() => setSeccion("usuarios")}
          >
            <Users size={20} />
            <span>Usuarios</span>
          </button>

          <button
            className={`menu-item ${
              seccion === "perfiles" ? "active" : ""
            }`}
            onClick={() => setSeccion("perfiles")}
          >
            <ShieldCheck size={20} />
            <span>Perfiles</span>
          </button>

          <button
            className={`menu-item ${
              seccion === "reportes" ? "active" : ""
            }`}
            onClick={() => setSeccion("reportes")}
          >
            <BarChart3 size={20} />
            <span>Reportes</span>
          </button>

          <button
            className={`menu-item ${
              seccion === "exportaciones" ? "active" : ""
            }`}
            onClick={() => setSeccion("exportaciones")}
          >
            <Download size={20} />
            <span>Exportaciones</span>
          </button>
        </nav>

        <div className="sidebar-footer">
          <Settings size={17} />
          <span>Sistema de Inventario</span>
        </div>
      </aside>

      <main className="main-content">
        {seccion === "dashboard" && renderDashboard()}

        {seccion === "productos" && renderProductos()}

        {seccion === "proveedores" && (
          <Proveedores
            actualizarDashboard={cargarDashboard}
          />
        )}

        {seccion === "compras" && (
          <Compras
            actualizarDashboard={cargarDashboard}
            actualizarProductos={cargarProductos}
          />
        )}

        {seccion === "usuarios" && (
          <Usuarios
            actualizarDashboard={cargarDashboard}
          />
        )}

        {seccion === "perfiles" && (
          <Perfiles />
        )}

        {seccion === "reportes" && (
          <Reportes />
        )}

        {seccion === "exportaciones" && (
          <Exportaciones />
        )}
      </main>
    </div>
  );
}

export default App;