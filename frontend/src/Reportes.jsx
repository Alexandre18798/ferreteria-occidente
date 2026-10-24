import { useEffect, useState } from "react";
import axios from "axios";
import {
  BarChart3,
  Package,
  Truck,
  ShoppingCart,
  Users,
  AlertTriangle,
  Boxes
} from "lucide-react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  Legend
} from "recharts";

function Reportes() {
  const [resumen, setResumen] = useState({
    totalProductos: 0,
    totalProveedores: 0,
    totalCompras: 0,
    totalUsuarios: 0,
    montoTotalCompras: 0
  });

  const [comprasPorMes, setComprasPorMes] = useState([]);
  const [inventarioCategoria, setInventarioCategoria] = useState([]);
  const [comprasProveedor, setComprasProveedor] = useState([]);
  const [stockBajo, setStockBajo] = useState([]);
  const [cargando, setCargando] = useState(true);

  const colores = [
    "#1b5c3c",
    "#d6a84b",
    "#3f7d5c",
    "#8a6d35",
    "#2f6f4e",
    "#c18d35"
  ];

  useEffect(() => {
    cargarReportes();
  }, []);

  const cargarReportes = async () => {
    try {
      setCargando(true);

      const [
        respuestaResumen,
        respuestaMes,
        respuestaCategoria,
        respuestaProveedor,
        respuestaStock
      ] = await Promise.all([
        axios.get("https://ferreteria-occidente-backend.onrender.com/reportes/resumen"),
        axios.get("https://ferreteria-occidente-backend.onrender.com/reportes/compras-por-mes"),
        axios.get(
          "https://ferreteria-occidente-backend.onrender.com/reportes/inventario-por-categoria"
        ),
        axios.get(
          "https://ferreteria-occidente-backend.onrender.com/reportes/compras-por-proveedor"
        ),
        axios.get(
          "https://ferreteria-occidente-backend.onrender.com/productos/stock-bajo/5"
        )
      ]);

      setResumen(respuestaResumen.data);

      const datosMes = Object.entries(respuestaMes.data).map(
        ([mes, total]) => ({
          mes,
          total: Number(total)
        })
      );

      const datosCategoria = Object.entries(
        respuestaCategoria.data
      ).map(([categoria, stock]) => ({
        categoria,
        stock: Number(stock)
      }));

      const datosProveedor = Object.entries(
        respuestaProveedor.data
      ).map(([proveedor, compras]) => ({
        proveedor,
        compras: Number(compras)
      }));

      setComprasPorMes(datosMes);
      setInventarioCategoria(datosCategoria);
      setComprasProveedor(datosProveedor);
      setStockBajo(respuestaStock.data);
    } catch (error) {
      console.error("Error al cargar reportes:", error);

      alert(
        "No se pudieron cargar todos los reportes del sistema."
      );
    } finally {
      setCargando(false);
    }
  };

  const formatearDinero = (valor) => {
    return new Intl.NumberFormat("es-GT", {
      style: "currency",
      currency: "GTQ"
    }).format(Number(valor || 0));
  };

  if (cargando) {
    return (
      <>
        <header className="topbar">
          <div>
            <p className="section-label">
              Análisis del sistema
            </p>
            <h1>Reportes</h1>
          </div>

          <div className="user-box">
            <div className="user-avatar">OC</div>

            <div>
              <strong>Administrador</strong>
              <span>Ferretería Occidente</span>
            </div>
          </div>
        </header>

        <section className="productos-panel">
          <div className="sin-registros">
            Cargando reportes...
          </div>
        </section>
      </>
    );
  }

  return (
    <>
      <header className="topbar">
        <div>
          <p className="section-label">
            Análisis del sistema
          </p>
          <h1>Reportes</h1>
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
            <span>Información general</span>

            <h2>
              Reportes de Ferretería Occidente
            </h2>

            <p>
              Consulta información del inventario, compras,
              proveedores y movimientos registrados en el sistema.
            </p>
          </div>
        </div>

        <div className="contador-productos">
          <BarChart3 size={18} />
          Información actualizada
        </div>
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
            <span>Reporte financiero</span>
            <h3>Compras por mes</h3>
          </div>

          {comprasPorMes.length > 0 ? (
            <div
              style={{
                width: "100%",
                height: "300px",
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
              No hay compras registradas.
            </div>
          )}
        </div>

        <div className="panel">
          <div className="panel-header">
            <span>Resumen financiero</span>
            <h3>Monto total en compras</h3>
          </div>

          <div className="inventory-summary">
            <div className="quetzal-icon">Q</div>

            <strong>
              {formatearDinero(resumen.montoTotalCompras)}
            </strong>

            <p>
              Monto acumulado de todas las compras registradas
              en el sistema.
            </p>
          </div>
        </div>
      </section>

      <section className="dashboard-grid">
        <div className="panel">
          <div className="panel-header">
            <span>Inventario</span>
            <h3>Inventario por categoría</h3>
          </div>

          {inventarioCategoria.length > 0 ? (
            <div
              style={{
                width: "100%",
                height: "300px",
                marginTop: "25px"
              }}
            >
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={inventarioCategoria}
                    dataKey="stock"
                    nameKey="categoria"
                    cx="50%"
                    cy="45%"
                    outerRadius={90}
                    label
                  >
                    {inventarioCategoria.map((item, index) => (
                      <Cell
                        key={item.categoria}
                        fill={colores[index % colores.length]}
                      />
                    ))}
                  </Pie>

                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </div>
          ) : (
            <div className="empty-chart">
              No hay información de inventario.
            </div>
          )}
        </div>

        <div className="panel">
          <div className="panel-header">
            <span>Proveedores</span>
            <h3>Compras por proveedor</h3>
          </div>

          {comprasProveedor.length > 0 ? (
            <div
              style={{
                width: "100%",
                height: "300px",
                marginTop: "25px"
              }}
            >
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={comprasProveedor}>
                  <CartesianGrid
                    strokeDasharray="3 3"
                    vertical={false}
                  />

                  <XAxis
                    dataKey="proveedor"
                    tick={{ fontSize: 11 }}
                  />

                  <YAxis
                    allowDecimals={false}
                    tick={{ fontSize: 11 }}
                  />

                  <Tooltip />

                  <Bar
                    dataKey="compras"
                    fill="#d6a84b"
                    radius={[6, 6, 0, 0]}
                  />
                </BarChart>
              </ResponsiveContainer>
            </div>
          ) : (
            <div className="empty-chart">
              No hay información de proveedores.
            </div>
          )}
        </div>
      </section>

      <section className="productos-panel">
        <div className="productos-panel-header">
          <div>
            <span>Control de inventario</span>
            <h3>Productos con stock bajo</h3>
          </div>

          <div className="contador-productos">
            <AlertTriangle size={17} />
            {stockBajo.length} productos
          </div>
        </div>

        {stockBajo.length > 0 ? (
          <div className="tabla-contenedor">
            <table className="tabla-productos">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Producto</th>
                  <th>Categoría</th>
                  <th>Stock</th>
                  <th>Estado</th>
                </tr>
              </thead>

              <tbody>
                {stockBajo.map((producto) => (
                  <tr key={producto.idProducto}>
                    <td>#{producto.idProducto}</td>

                    <td>
                      <div className="producto-nombre">
                        <div className="producto-icono">
                          <Boxes size={18} />
                        </div>

                        <strong>{producto.nombre}</strong>
                      </div>
                    </td>

                    <td>{producto.categoria}</td>

                    <td>
                      <span className="stock stock-bajo">
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
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div
            style={{
              padding: "45px 25px",
              textAlign: "center"
            }}
          >
            <div
              className="producto-icono"
              style={{
                margin: "0 auto 15px",
                width: "48px",
                height: "48px"
              }}
            >
              <Package size={23} />
            </div>

            <strong>
              El inventario se encuentra en buen estado
            </strong>

            <p
              style={{
                marginTop: "8px",
                color: "#718078"
              }}
            >
              Actualmente no existen productos con 5 unidades
              o menos.
            </p>
          </div>
        )}
      </section>
    </>
  );
}

export default Reportes;