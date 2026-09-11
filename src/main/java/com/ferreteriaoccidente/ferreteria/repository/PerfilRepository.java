package com.ferreteriaoccidente.ferreteria.repository;
import com.ferreteriaoccidente.ferreteria.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
}