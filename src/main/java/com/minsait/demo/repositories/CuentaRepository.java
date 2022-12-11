package com.minsait.demo.repositories;

import com.minsait.demo.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta,Long> {
        Optional<Cuenta> findByPersona(String persona);
        /*
        * @Query("select c from Cuenta where c.persona=?1")//HQL
        * Optional<Cuenta> buscarPorPersona(String persona);
        * */
}
