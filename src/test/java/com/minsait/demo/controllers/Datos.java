package com.minsait.demo.controllers;

import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.models.TransferirDTO;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
    public static Optional<Cuenta> crearCuenta1(){
    return Optional.of(new Cuenta(1L,"Ricardo",new BigDecimal(1000)));
    }
    public static Optional<Cuenta> crearCuenta2(){
        return Optional.of(new Cuenta(2L,"Canelo",new BigDecimal(5000000)));
    }
    public static Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L,"Banco Azteca",0));
    }

    public static TransferirDTO transferirDTO(){
        TransferirDTO transferirDTO = new TransferirDTO();
        transferirDTO.setCuentaOrigenId(crearCuenta2().get().getId());
        transferirDTO.setCuentaDestinoId(crearCuenta1().get().getId());
        transferirDTO.setMonto(new BigDecimal(5000000));
        transferirDTO.setBancoId(crearBanco().get().getId());
        return transferirDTO;
    }
}
