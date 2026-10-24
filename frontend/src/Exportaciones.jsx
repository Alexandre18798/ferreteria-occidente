import {
  Package,
  Truck,
  ShoppingCart,
  ListChecks,
  Users,
  ShieldCheck,
  BarChart3,
  FileText,
  FileSpreadsheet,
  Download
} from "lucide-react";

function Exportaciones() {
  const API = "https://ferreteria-occidente-backend.onrender.com/exportaciones";

  const opciones = [
    {
      titulo: "Productos",
      descripcion: "Exporta el inventario completo de productos registrados.",
      icono: Package,
      ruta: "productos"
    },
    {
      titulo: "Proveedores",
      descripcion: "Exporta la información de todos los proveedores.",
      icono: Truck,
      ruta: "proveedores"
    },
    {
      titulo: "Compras",
      descripcion: "Exporta el historial general de compras realizadas.",
      icono: ShoppingCart,
      ruta: "compras"
    },
    {
      titulo: "Detalle de compras",
      descripcion: "Exporta los productos, cantidades, precios y subtotales de las compras.",
      icono: ListChecks,
      ruta: "detalles"
    },
    {
      titulo: "Usuarios",
      descripcion: "Exporta los usuarios del sistema sin incluir contraseñas.",
      icono: Users,
      ruta: "usuarios"
    },
    {
      titulo: "Perfiles",
      descripcion: "Exporta los perfiles y su estado dentro del sistema.",
      icono: ShieldCheck,
      ruta: "perfiles"
    },
    {
      titulo: "Reporte general",
      descripcion: "Exporta el resumen general, compras, inventario y proveedores.",
      icono: BarChart3,
      ruta: "general"
    }
  ];

  const descargar = (ruta, formato) => {
    window.open(`${API}/${ruta}/${formato}`, "_blank");
  };

  return (
    <>
      <header className="topbar">
        <div>
          <p className="section-label">Documentos del sistema</p>
          <h1>Exportaciones</h1>
        </div>

        <div className="user-box">
          <div className="user-avatar">OC</div>

          <div>
            <strong>Administrador</strong>
            <span>Ferretería Occidente</span>
          </div>
        </div>
      </header>

      <section className="productos-header exportaciones-header">
        <div className="section-brand-content">
          <img
            src="/images/logo-ferreteria.png"
            alt="Ferretería Occidente"
            className="section-brand-logo"
          />

          <div>
            <span>PDF y Excel</span>
            <h2>Centro de exportaciones</h2>
            <p>
              Descarga la información de Ferretería Occidente en formato PDF o Excel.
            </p>
          </div>
        </div>

        <div className="exportaciones-header-icon">
          <Download size={34} />
        </div>
      </section>

      <section className="exportaciones-panel">
        <div className="exportaciones-panel-header">
          <div>
            <span>Documentos disponibles</span>
            <h3>Selecciona la información que deseas exportar</h3>
          </div>

          <div className="contador-exportaciones">
            {opciones.length} reportes
          </div>
        </div>

        <div className="exportaciones-grid">
          {opciones.map((opcion) => {
            const Icono = opcion.icono;

            return (
              <article className="exportacion-card" key={opcion.ruta}>
                <div className="exportacion-card-top">
                  <div className="exportacion-icono">
                    <Icono size={24} />
                  </div>

                  <div>
                    <span>Exportar información</span>
                    <h3>{opcion.titulo}</h3>
                  </div>
                </div>

                <p className="exportacion-descripcion">
                  {opcion.descripcion}
                </p>

                <div className="exportacion-acciones">
                  <button
                    type="button"
                    className="btn-exportar btn-exportar-pdf"
                    onClick={() => descargar(opcion.ruta, "pdf")}
                  >
                    <FileText size={18} />
                    Descargar PDF
                  </button>

                  <button
                    type="button"
                    className="btn-exportar btn-exportar-excel"
                    onClick={() => descargar(opcion.ruta, "excel")}
                  >
                    <FileSpreadsheet size={18} />
                    Descargar Excel
                  </button>
                </div>
              </article>
            );
          })}
        </div>
      </section>
    </>
  );
}

export default Exportaciones;
