import { useEffect, useState } from "react";
import axios from "axios";
import {
  Plus,
  X,
  Save,
  Package,
  Trash2,
  Eye
} from "lucide-react";

function Compras({
  actualizarDashboard,
  actualizarProductos
}) {
  const [compras, setCompras] = useState([]);
  const [proveedores, setProveedores] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [productos, setProductos] = useState([]);

  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [mostrarDetalle, setMostrarDetalle] = useState(false);
  const [guardando, setGuardando] = useState(false);
  const [compraSeleccionada, setCompraSeleccionada] = useState(null);
  const [detallesCompra, setDetallesCompra] = useState([]);

  const [compraForm, setCompraForm] = useState({
    idProveedor: "",
    idUsuario: "",
    estado: true
  });

  const [detalleForm, setDetalleForm] = useState({
    idProducto: "",
    cantidad: 1,
    precioUnitario: ""
  });

  const [detallesNuevos, setDetallesNuevos] = useState([]);

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      const [
        respuestaCompras,
        respuestaProveedores,
        respuestaUsuarios,
        respuestaProductos
      ] = await Promise.all([
        axios.get("http://localhost:8080/compras"),
        axios.get("http://localhost:8080/proveedores"),
        axios.get("http://localhost:8080/usuarios"),
        axios.get("http://localhost:8080/productos")
      ]);

      setCompras(respuestaCompras.data);
      setProveedores(respuestaProveedores.data);
      setUsuarios(respuestaUsuarios.data);
      setProductos(respuestaProductos.data);
    } catch (error) {
      console.error("Error al cargar los datos de compras:", error);
    }
  };

  const formatearDinero = (valor) => {
    return new Intl.NumberFormat("es-GT", {
      style: "currency",
      currency: "GTQ"
    }).format(Number(valor || 0));
  };

  const formatearFecha = (fecha) => {
    if (!fecha) {
      return "";
    }

    return new Date(fecha).toLocaleString("es-GT", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit"
    });
  };

  const obtenerFechaActual = () => {
    const fecha = new Date();

    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, "0");
    const dia = String(fecha.getDate()).padStart(2, "0");
    const hora = String(fecha.getHours()).padStart(2, "0");
    const minuto = String(fecha.getMinutes()).padStart(2, "0");
    const segundo = String(fecha.getSeconds()).padStart(2, "0");

    return `${anio}-${mes}-${dia}T${hora}:${minuto}:${segundo}`;
  };

  const abrirNuevaCompra = () => {
    setCompraForm({
      idProveedor: "",
      idUsuario: "",
      estado: true
    });

    setDetalleForm({
      idProducto: "",
      cantidad: 1,
      precioUnitario: ""
    });

    setDetallesNuevos([]);
    setMostrarFormulario(true);
  };

  const cerrarFormulario = () => {
    setMostrarFormulario(false);

    setCompraForm({
      idProveedor: "",
      idUsuario: "",
      estado: true
    });

    setDetalleForm({
      idProducto: "",
      cantidad: 1,
      precioUnitario: ""
    });

    setDetallesNuevos([]);
  };

  const cambiarCompra = (event) => {
    const { name, value } = event.target;

    setCompraForm({
      ...compraForm,
      [name]: value
    });
  };

  const cambiarDetalle = (event) => {
    const { name, value } = event.target;

    if (name === "idProducto") {
      const producto = productos.find(
        (item) => String(item.idProducto) === value
      );

      setDetalleForm({
        ...detalleForm,
        idProducto: value,
        precioUnitario: producto
          ? Number(producto.precioVenta).toFixed(2)
          : ""
      });

      return;
    }

    setDetalleForm({
      ...detalleForm,
      [name]: value
    });
  };

  const agregarProducto = () => {
    if (
      !detalleForm.idProducto ||
      Number(detalleForm.cantidad) <= 0 ||
      Number(detalleForm.precioUnitario) <= 0
    ) {
      alert("Complete correctamente los datos del producto.");
      return;
    }

    const producto = productos.find(
      (item) =>
        String(item.idProducto) === String(detalleForm.idProducto)
    );

    if (!producto) {
      alert("Producto no encontrado.");
      return;
    }

    const existente = detallesNuevos.find(
      (item) =>
        String(item.idProducto) === String(detalleForm.idProducto)
    );

    if (existente) {
      alert("Este producto ya fue agregado a la compra.");
      return;
    }

    const cantidad = Number(detalleForm.cantidad);
    const precioUnitario = Number(detalleForm.precioUnitario);
    const subtotal = cantidad * precioUnitario;

    const nuevoDetalle = {
      idProducto: producto.idProducto,
      nombreProducto: producto.nombre,
      cantidad,
      precioUnitario,
      subtotal
    };

    setDetallesNuevos([
      ...detallesNuevos,
      nuevoDetalle
    ]);

    setDetalleForm({
      idProducto: "",
      cantidad: 1,
      precioUnitario: ""
    });
  };

  const quitarProducto = (idProducto) => {
    setDetallesNuevos(
      detallesNuevos.filter(
        (item) => item.idProducto !== idProducto
      )
    );
  };

  const calcularTotal = () => {
    return detallesNuevos.reduce(
      (total, detalle) => total + detalle.subtotal,
      0
    );
  };

  const guardarCompra = async (event) => {
    event.preventDefault();

    if (!compraForm.idProveedor || !compraForm.idUsuario) {
      alert("Seleccione el proveedor y el usuario.");
      return;
    }

    if (detallesNuevos.length === 0) {
      alert("Agregue al menos un producto a la compra.");
      return;
    }

    try {
      setGuardando(true);

      const datosCompra = {
        idProveedor: Number(compraForm.idProveedor),
        idUsuario: Number(compraForm.idUsuario),
        fecha: obtenerFechaActual(),
        total: 0,
        estado: true
      };

      const respuestaCompra = await axios.post(
        "http://localhost:8080/compras",
        datosCompra
      );

      const compraCreada = respuestaCompra.data;

      for (const detalle of detallesNuevos) {
        const datosDetalle = {
          idCompra: compraCreada.idCompra,
          idProducto: detalle.idProducto,
          cantidad: detalle.cantidad,
          precioUnitario: detalle.precioUnitario,
          subtotal: detalle.subtotal
        };

        await axios.post(
          "http://localhost:8080/detalle-compras",
          datosDetalle
        );
      }

      await cargarDatos();

      if (actualizarDashboard) {
        await actualizarDashboard();
      }

      if (actualizarProductos) {
        await actualizarProductos();
      }

      cerrarFormulario();

      alert("Compra registrada correctamente.");
    } catch (error) {
      console.error("Error al registrar la compra:", error);

      if (error.response) {
        console.error("Estado:", error.response.status);
        console.error("Respuesta:", error.response.data);
      }

      alert(
        "No se pudo registrar la compra. Revise los datos enviados al servidor."
      );
    } finally {
      setGuardando(false);
    }
  };

  const verDetalle = async (compra) => {
    try {
      const response = await axios.get(
        "http://localhost:8080/detalle-compras"
      );

      const detalles = response.data.filter(
        (detalle) =>
          detalle.compra &&
          detalle.compra.idCompra === compra.idCompra
      );

      setCompraSeleccionada(compra);
      setDetallesCompra(detalles);
      setMostrarDetalle(true);
    } catch (error) {
      console.error("Error al cargar el detalle de compra:", error);
      alert("No se pudo cargar el detalle de la compra.");
    }
  };

  const cerrarDetalle = () => {
    setMostrarDetalle(false);
    setCompraSeleccionada(null);
    setDetallesCompra([]);
  };

  return (
    <>
      <header className="topbar">
        <div>
          <p className="section-label">Gestión de abastecimiento</p>
          <h1>Compras</h1>
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
            <h2>Compras registradas</h2>

          <p>
            Registra las compras realizadas a proveedores y actualiza el
            inventario.
          </p>
          </div>
        </div>

        <button
          className="btn-nuevo"
          onClick={abrirNuevaCompra}
        >
          <Plus size={19} />
          Nueva compra
        </button>
      </section>

      <section className="productos-panel">
        <div className="productos-panel-header">
          <div>
            <span>Historial</span>
            <h3>Registro de compras</h3>
          </div>

          <div className="contador-productos">
            {compras.length} compras
          </div>
        </div>

        <div className="tabla-contenedor">
          <table className="tabla-productos">
            <thead>
              <tr>
                <th>ID</th>
                <th>Proveedor</th>
                <th>Usuario</th>
                <th>Fecha</th>
                <th>Total</th>
                <th>Estado</th>
                <th>Detalle</th>
              </tr>
            </thead>

            <tbody>
              {compras.length > 0 ? (
                compras.map((compra) => (
                  <tr key={compra.idCompra}>
                    <td>#{compra.idCompra}</td>

                    <td>
                      <strong>
                        {compra.proveedor?.nombre || "Sin proveedor"}
                      </strong>
                    </td>

                    <td>
                      {compra.usuario?.nombre || "Sin usuario"}
                    </td>

                    <td>
                      {formatearFecha(compra.fecha)}
                    </td>

                    <td>
                      <strong>
                        {formatearDinero(compra.total)}
                      </strong>
                    </td>

                    <td>
                      <span
                        className={
                          compra.estado
                            ? "estado estado-activo"
                            : "estado estado-inactivo"
                        }
                      >
                        {compra.estado ? "Activa" : "Inactiva"}
                      </span>
                    </td>

                    <td>
                      <button
                        className="btn-accion btn-editar"
                        title="Ver detalle"
                        onClick={() => verDetalle(compra)}
                      >
                        <Eye size={17} />
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colSpan="7"
                    className="sin-registros"
                  >
                    No hay compras registradas.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {mostrarFormulario && (
        <div className="modal-fondo">
          <div className="modal-producto modal-compra">
            <div className="modal-brand">
              <img
                src="/images/logo-ferreteria.png"
                alt="Ferretería Occidente"
                className="modal-brand-logo"
              />
            </div>

            <div className="modal-header">
              <div>
                <span>Nueva operación</span>
                <h2>Registrar compra</h2>

                <p>
                  Selecciona el proveedor y agrega los productos adquiridos.
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
              className="form-compra"
              onSubmit={guardarCompra}
            >
              <div className="compra-datos">
                <div className="campo-formulario">
                  <label>Proveedor</label>

                  <select
                    name="idProveedor"
                    value={compraForm.idProveedor}
                    onChange={cambiarCompra}
                    required
                  >
                    <option value="">
                      Seleccione un proveedor
                    </option>

                    {proveedores
                      .filter((proveedor) => proveedor.estado)
                      .map((proveedor) => (
                        <option
                          key={proveedor.idProveedor}
                          value={proveedor.idProveedor}
                        >
                          {proveedor.nombre}
                        </option>
                      ))}
                  </select>
                </div>

                <div className="campo-formulario">
                  <label>Usuario responsable</label>

                  <select
                    name="idUsuario"
                    value={compraForm.idUsuario}
                    onChange={cambiarCompra}
                    required
                  >
                    <option value="">
                      Seleccione un usuario
                    </option>

                    {usuarios
                      .filter((usuario) => usuario.estado)
                      .map((usuario) => (
                        <option
                          key={usuario.idUsuario}
                          value={usuario.idUsuario}
                        >
                          {usuario.nombre}
                        </option>
                      ))}
                  </select>
                </div>
              </div>

              <div className="seccion-productos-compra">
                <div className="titulo-detalle-compra">
                  <div>
                    <span>Productos</span>
                    <h3>Detalle de la compra</h3>
                  </div>
                </div>

                <div className="agregar-producto-compra">
                  <div className="campo-formulario">
                    <label>Producto</label>

                    <select
                      name="idProducto"
                      value={detalleForm.idProducto}
                      onChange={cambiarDetalle}
                    >
                      <option value="">
                        Seleccione un producto
                      </option>

                      {productos
                        .filter((producto) => producto.estado)
                        .map((producto) => (
                          <option
                            key={producto.idProducto}
                            value={producto.idProducto}
                          >
                            {producto.nombre}
                          </option>
                        ))}
                    </select>
                  </div>

                  <div className="campo-formulario">
                    <label>Cantidad</label>

                    <input
                      type="number"
                      name="cantidad"
                      min="1"
                      step="1"
                      value={detalleForm.cantidad}
                      onChange={cambiarDetalle}
                    />
                  </div>

                  <div className="campo-formulario">
                    <label>Precio unitario</label>

                    <div className="input-precio">
                      <span>Q</span>

                      <input
                        type="number"
                        name="precioUnitario"
                        min="0.01"
                        step="0.01"
                        value={detalleForm.precioUnitario}
                        onChange={cambiarDetalle}
                        placeholder="0.00"
                      />
                    </div>
                  </div>

                  <button
                    type="button"
                    className="btn-agregar-producto"
                    onClick={agregarProducto}
                  >
                    <Plus size={18} />
                    Agregar
                  </button>
                </div>

                <div className="tabla-contenedor">
                  <table className="tabla-productos tabla-detalle-compra">
                    <thead>
                      <tr>
                        <th>Producto</th>
                        <th>Cantidad</th>
                        <th>Precio</th>
                        <th>Subtotal</th>
                        <th></th>
                      </tr>
                    </thead>

                    <tbody>
                      {detallesNuevos.length > 0 ? (
                        detallesNuevos.map((detalle) => (
                          <tr key={detalle.idProducto}>
                            <td>
                              <div className="producto-nombre">
                                <div className="producto-icono">
                                  <Package size={17} />
                                </div>

                                <strong>
                                  {detalle.nombreProducto}
                                </strong>
                              </div>
                            </td>

                            <td>{detalle.cantidad}</td>

                            <td>
                              {formatearDinero(
                                detalle.precioUnitario
                              )}
                            </td>

                            <td>
                              <strong>
                                {formatearDinero(
                                  detalle.subtotal
                                )}
                              </strong>
                            </td>

                            <td>
                              <button
                                type="button"
                                className="btn-accion btn-eliminar"
                                onClick={() =>
                                  quitarProducto(detalle.idProducto)
                                }
                              >
                                <Trash2 size={16} />
                              </button>
                            </td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td
                            colSpan="5"
                            className="sin-registros"
                          >
                            Agrega productos a la compra.
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>

                <div className="total-compra">
                  <span>Total de la compra</span>

                  <strong>
                    {formatearDinero(calcularTotal())}
                  </strong>
                </div>
              </div>

              <div className="modal-acciones acciones-compra">
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
                    ? "Registrando..."
                    : "Registrar compra"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {mostrarDetalle && compraSeleccionada && (
        <div className="modal-fondo">
          <div className="modal-producto modal-compra">
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
                  Compra #{compraSeleccionada.idCompra}
                </span>

                <h2>Detalle de compra</h2>

                <p>
                  {compraSeleccionada.proveedor?.nombre}
                </p>
              </div>

              <button
                type="button"
                className="btn-cerrar-modal"
                onClick={cerrarDetalle}
              >
                <X size={21} />
              </button>
            </div>

            <div className="detalle-compra-contenido">
              <div className="resumen-compra">
                <div>
                  <span>Proveedor</span>

                  <strong>
                    {compraSeleccionada.proveedor?.nombre}
                  </strong>
                </div>

                <div>
                  <span>Responsable</span>

                  <strong>
                    {compraSeleccionada.usuario?.nombre}
                  </strong>
                </div>

                <div>
                  <span>Fecha</span>

                  <strong>
                    {formatearFecha(
                      compraSeleccionada.fecha
                    )}
                  </strong>
                </div>
              </div>

              <div className="tabla-contenedor">
                <table className="tabla-productos">
                  <thead>
                    <tr>
                      <th>Producto</th>
                      <th>Cantidad</th>
                      <th>Precio</th>
                      <th>Subtotal</th>
                    </tr>
                  </thead>

                  <tbody>
                    {detallesCompra.length > 0 ? (
                      detallesCompra.map((detalle) => (
                        <tr key={detalle.idDetalleCompra}>
                          <td>
                            {detalle.producto?.nombre ||
                              "Producto"}
                          </td>

                          <td>
                            {detalle.cantidad}
                          </td>

                          <td>
                            {formatearDinero(
                              detalle.precioUnitario
                            )}
                          </td>

                          <td>
                            <strong>
                              {formatearDinero(
                                detalle.subtotal
                              )}
                            </strong>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td
                          colSpan="4"
                          className="sin-registros"
                        >
                          Esta compra no tiene productos registrados.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>

              <div className="total-compra">
                <span>Total registrado</span>

                <strong>
                  {formatearDinero(
                    compraSeleccionada.total
                  )}
                </strong>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default Compras;