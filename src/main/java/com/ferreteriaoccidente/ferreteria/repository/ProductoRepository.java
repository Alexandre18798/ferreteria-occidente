package com.ferreteriaoccidente.ferreteria.repository;

import com.ferreteriaoccidente.ferreteria.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByStockLessThanEqual(Integer stock);
}