package com.ferreteriaoccidente.ferreteria.repository;

import com.ferreteriaoccidente.ferreteria.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

}