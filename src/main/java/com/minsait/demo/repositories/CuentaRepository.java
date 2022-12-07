package com.minsait.demo.repositories;

import com.minsait.demo.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta,Long> {

}
