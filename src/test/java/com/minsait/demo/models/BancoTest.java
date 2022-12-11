package com.minsait.demo.models;

import com.minsait.demo.Datos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BancoTest {
    Banco banco;

    @Test
    void testBanco(){
        Optional<Banco> banco = Datos.crearBanco();
        Optional<Banco> banco2 = Datos.crearBanco();
        Optional<Banco> banco3 = Optional.of(new Banco(2l,"Bancomer",0));
        assertNotNull(banco);
        assertEquals(1l,banco.get().getId());
        assertEquals("Banco Azteca",banco.get().getNombre());
        assertEquals(0,banco.get().getTotalTransferencias());
        assertEquals(banco,banco2);
        assertNotEquals(banco,banco3);
    }

}