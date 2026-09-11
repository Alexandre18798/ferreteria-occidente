package com.ferreteriaoccidente.ferreteria.repository;
import com.ferreteriaoccidente.ferreteria.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
}