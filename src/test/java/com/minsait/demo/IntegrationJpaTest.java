package com.minsait.demo;

import com.minsait.demo.models.Cuenta;
import com.minsait.demo.repositories.CuentaRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
public class IntegrationJpaTest {
    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById(){
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Gerardo",cuenta.get().getPersona());
    }

    @Test
    void testFindByPersona(){
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Gerardo");
        assertTrue(cuenta.isPresent());
        assertEquals("Gerardo",cuenta.get().getPersona());
    }
    @Test
    void testByPersonaException(){
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Javier");
        assertThrows(NoSuchElementException.class,cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }
    @Test
    void testSave(){
        Cuenta cuentaJavier =new Cuenta(null,"Javier",new BigDecimal("1000000"));
        Cuenta cuenta=cuentaRepository.save(cuentaJavier);
        assertEquals("Javier",cuenta.getPersona());
        assertEquals("1000000",cuenta.getSaldo().toPlainString());
        //assertEquals(3,cuenta.getId());
    }

    @Test
    void testUpdate(){
        Cuenta cuentaJavier =new Cuenta(null,"Javier",new BigDecimal("1000000"));
        Cuenta cuenta=cuentaRepository.save(cuentaJavier);
        assertEquals("Javier",cuenta.getPersona());
        assertEquals("1000000",cuenta.getSaldo().toPlainString());
        //assertEquals(3,cuenta.getId());

        cuenta.setSaldo(new BigDecimal(10000));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        assertEquals("Javier",cuentaActualizada.getPersona());
        assertEquals("10000",cuentaActualizada.getSaldo().toPlainString());
       // assertEquals(3,cuentaActualizada.getId());


        //Optional<Cuenta> cuenta2 = cuentaRepository.findById(3L);
        //assertEquals("10000",cuenta2.get().getSaldo().toPlainString());

    }
    @Test
    void testDelete(){
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
        assertEquals("Canelo",cuenta.getPersona());
        cuentaRepository.delete(cuenta);
        assertThrows(NoSuchElementException.class,()->{
            cuentaRepository.findById(2l).orElseThrow();
        });
        assertEquals(1,cuentaRepository.findAll().size());
    }
}
