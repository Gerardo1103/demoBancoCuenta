package com.minsait.demo.repositories;

import com.minsait.demo.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco,Long> {
}
