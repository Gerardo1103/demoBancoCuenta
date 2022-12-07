package com.minsait.demo.services;

import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.repositories.BancoRepository;
import com.minsait.demo.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service
public class CuentaServiceImpl implements CuentaService{
    @Autowired
    private BancoRepository bancoRepository;
    @Autowired
    private CuentaRepository cuentaRepository;
    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer revisarTotalTransferencias(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional
    public void transferir(Long cuentaOrigen, Long cuentaDestino, BigDecimal monto, Long bancoId) {
        Cuenta origen = cuentaRepository.findById(cuentaOrigen).orElseThrow();
        origen.retirar(monto);
        cuentaRepository.save(origen);
        Cuenta destino = cuentaRepository.findById(cuentaDestino).orElseThrow();
        destino.depositar(monto);
        cuentaRepository.save(destino);

        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        int totalTransferencia = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencia);
        bancoRepository.save(banco);

    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }
}
