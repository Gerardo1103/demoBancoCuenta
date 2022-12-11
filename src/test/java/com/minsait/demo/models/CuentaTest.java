package com.minsait.demo.models;

import com.minsait.demo.Datos;
import com.minsait.demo.exception.DineroInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testCuenta(){
        Optional<Cuenta> cuenta = Datos.crearCuenta1();
        assertNotNull(cuenta);
        assertEquals(1,cuenta.get().getId());
        assertEquals("Ricardo",cuenta.get().getPersona());
        assertEquals("1000",cuenta.get().getSaldo().toPlainString());

    }
    @Test
    void testRetirarDepositar(){
        Optional<Cuenta> cuenta1 =Datos.crearCuenta1();
        Optional<Cuenta> cuenta2 = Datos.crearCuenta2();
        BigDecimal monto = new BigDecimal(1000000);
        cuenta2.get().retirar(monto);
        cuenta1.get().depositar(monto);
        assertTrue(cuenta1.get().getPersona().equals("Ricardo"));
        assertTrue(cuenta2.get().getPersona().equals("Canelo"));
        assertEquals("4000000",cuenta2.get().getSaldo().toPlainString());
        //saldo depositado ok
        assertEquals("1001000",cuenta1.get().getSaldo().toPlainString());
    }
    @Test
    void testRetirarException(){
        Optional<Cuenta> cuenta2 = Datos.crearCuenta2();
        BigDecimal montoRetirar = new BigDecimal(6000000);

        Exception exception = assertThrows(DineroInsuficienteException.class,()->{
            cuenta2.get().retirar(montoRetirar);
        });
        assertNotNull(exception);
        assertTrue(exception.getMessage().equals("Dinero insuficiente"));
    }

}