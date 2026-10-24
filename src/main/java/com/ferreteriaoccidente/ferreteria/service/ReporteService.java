package com.ferreteriaoccidente.ferreteria.service;

import com.ferreteriaoccidente.ferreteria.entity.Compra;
import com.ferreteriaoccidente.ferreteria.repository.CompraRepository;
import com.ferreteriaoccidente.ferreteria.repository.ProductoRepository;
import com.ferreteriaoccidente.ferreteria.repository.ProveedorRepository;
import com.ferreteriaoccidente.ferreteria.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ReporteService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Map<String, Object> obtenerResumen() {

        Map<String, Object> resumen = new HashMap<>();

        long totalProductos = productoRepository.count();
        long totalProveedores = proveedorRepository.count();
        long totalCompras = compraRepository.count();
        long totalUsuarios = usuarioRepository.count();

        List<Compra> compras = compraRepository.findAll();

        BigDecimal montoTotalCompras = BigDecimal.ZERO;

        for (Compra compra : compras) {
            if (compra.getTotal() != null) {
                montoTotalCompras = montoTotalCompras.add(compra.getTotal());
            }
        }

        resumen.put("totalProductos", totalProductos);
        resumen.put("totalProveedores", totalProveedores);
        resumen.put("totalCompras", totalCompras);
        resumen.put("totalUsuarios", totalUsuarios);
        resumen.put("montoTotalCompras", montoTotalCompras);

        return resumen;
    }

    public Map<String, BigDecimal> obtenerComprasPorMes() {

        List<Compra> compras = compraRepository.findAll();

        Map<String, BigDecimal> comprasPorMes = new TreeMap<>();

        for (Compra compra : compras) {

            if (compra.getFecha() != null && compra.getTotal() != null) {

                String mes = compra.getFecha().getYear()
                        + "-"
                        + String.format("%02d", compra.getFecha().getMonthValue());

                BigDecimal totalActual = comprasPorMes.getOrDefault(
                        mes,
                        BigDecimal.ZERO
                );

                comprasPorMes.put(
                        mes,
                        totalActual.add(compra.getTotal())
                );
            }
        }

        return comprasPorMes;
    }

    public Map<String, Integer> obtenerInventarioPorCategoria() {

    List<com.ferreteriaoccidente.ferreteria.entity.Producto> productos =
            productoRepository.findAll();

    Map<String, Integer> inventarioPorCategoria = new TreeMap<>();

    for (com.ferreteriaoccidente.ferreteria.entity.Producto producto : productos) {

        if (producto.getCategoria() != null) {

            String categoria = producto.getCategoria();

            Integer stock = producto.getStock();

            if (stock == null) {
                stock = 0;
            }

            Integer stockActual = inventarioPorCategoria.getOrDefault(
                    categoria,
                    0
            );

            inventarioPorCategoria.put(
                    categoria,
                    stockActual + stock
            );
        }
    }

    return inventarioPorCategoria;
}

  public Map<String, Integer> obtenerComprasPorProveedor() {

    List<Compra> compras = compraRepository.findAll();

    Map<String, Integer> comprasPorProveedor = new TreeMap<>();

    for (Compra compra : compras) {

        if (compra.getProveedor() != null &&
                compra.getProveedor().getNombre() != null) {

            String proveedor = compra.getProveedor().getNombre();

            Integer cantidad = comprasPorProveedor.getOrDefault(
                    proveedor,
                    0
            );

            comprasPorProveedor.put(
                    proveedor,
                    cantidad + 1
            );
        }
    }

    return comprasPorProveedor;
}

}