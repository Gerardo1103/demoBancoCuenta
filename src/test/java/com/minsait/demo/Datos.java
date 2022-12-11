package com.minsait.demo;

import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.models.TransferirDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
    public static TransferirDTO transferirDTOException(){
        TransferirDTO transferirDTO = new TransferirDTO();
        transferirDTO.setCuentaOrigenId(crearCuenta2().get().getId());
        transferirDTO.setCuentaDestinoId(crearCuenta1().get().getId());
        transferirDTO.setMonto(new BigDecimal(6000000));
        transferirDTO.setBancoId(crearBanco().get().getId());
        return transferirDTO;
    }
    public static final List<Cuenta> CUENTAS= Arrays.asList(

            new Cuenta(1L,"Gerardo",new BigDecimal("10000")),
            new Cuenta(2L,"Canelo",new BigDecimal("5000000"))
    );
}
