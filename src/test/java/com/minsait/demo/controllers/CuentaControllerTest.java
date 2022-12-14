package com.minsait.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsait.demo.Datos;
import com.minsait.demo.exception.DineroInsuficienteException;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.models.TransferirDTO;
import com.minsait.demo.repositories.CuentaRepository;
import com.minsait.demo.services.CuentaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CuentaService service;
    @MockBean
    private CuentaRepository cuentaRepository;
    ObjectMapper mapper;
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void testListarId() throws Exception {
        when(service.findById(1l)).thenReturn(Datos.crearCuenta1().orElseThrow());
        //NOT FOUND
        when(service.findById(3L)).thenThrow(NoSuchElementException.class);
        mvc.perform(get("/api/cuentas/listar/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Ricardo"))
                .andExpect(jsonPath("$.saldo").value("1000"))
        ;
        //NOT FOUND
        mvc.perform(get("/api/cuentas/listar/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    verify(service).findById(1l);
    }
/*
    @Test
    void listarIdNotFound() throws Exception {
        when(service.findById(3L)).thenThrow(NoSuchElementException.class);
        mvc.perform(get("/api/cuentas/listar/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
*/
    @Test
    void testGuardar() throws Exception {
     Cuenta cuenta = new Cuenta(null, "Javier",new BigDecimal(1000000));
     when(service.save(any())).then(invocationOnMock -> {
        Cuenta cuenta1 = invocationOnMock.getArgument(0);
        cuenta1.setId(3l);
        return cuenta1;
     });
     mvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                     .content(mapper.writeValueAsBytes(cuenta)))
             .andExpect(status().isCreated())
             .andExpect(jsonPath("$.id", Matchers.is(3)))
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.persona",Matchers.is("Javier")))
             .andExpect(jsonPath("$.saldo").value(1000000));

    }
    @Test
    void testListarAll() throws Exception {
        when(service.findAll()).thenReturn(List.of(Datos.crearCuenta1().get(),
                                    Datos.crearCuenta2().get()));
        mvc.perform(get("/api/cuentas/listar").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].persona").value("Ricardo"))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].saldo").value(1000))
                .andExpect(jsonPath("$.[1].persona").value("Canelo"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].saldo").value(5000000));

        verify(service).findAll();
    }
@Disabled
    @Test
    void testTransferir() throws Exception {

        mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Datos.transferirDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(2))
                .andExpect(jsonPath("$.transaccion.cuentaDestinoId").value(1))
                .andExpect(jsonPath("$.transaccion.monto").value(5000000))
                .andExpect(jsonPath("$.transaccion.bancoId").value(1))
                .andExpect(jsonPath("$.message")
                        .value("Transferencia realizada con exito"))
                .andExpect(jsonPath("$.status").value("OK"))
        ;
    }


  @Test
  void testTransferirException() throws Exception {
      TransferirDTO dto = new TransferirDTO();
      dto.setCuentaOrigenId(2l);
      dto.setCuentaDestinoId(1l);
      dto.setMonto(new BigDecimal(6000000));
      dto.setBancoId(1l);

      Optional<Cuenta> cuenta = Datos.crearCuenta2();
      Exception exception = assertThrows(DineroInsuficienteException.class,()->{
          Datos.crearCuenta2().get().retirar(dto.getMonto());
      });
      doThrow(exception).when(service)
              .transferir( 2l,1l,new BigDecimal(60000000),1l);

      TransferirDTO transferencia = new TransferirDTO();
      transferencia.setCuentaOrigenId(Datos.crearCuenta2().get().getId());
      transferencia.setCuentaDestinoId(Datos.crearCuenta1().get().getId());
      transferencia.setMonto(new BigDecimal("60000000"));
      transferencia.setBancoId(Datos.crearBanco().get().getId());
      mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                      .content(mapper.writeValueAsString(transferencia)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.message")
              .value("Dinero insuficiente"))
              .andExpect(jsonPath("$.status").value("OK"));
  }
  @Test
    void testTransferencia() throws Exception {
      TransferirDTO dto = new TransferirDTO();
      dto.setCuentaOrigenId(2l);
      dto.setCuentaDestinoId(1l);
      dto.setMonto(new BigDecimal(5000000));
      dto.setBancoId(1l);

      Map <String,Object> respuesta = new HashMap<>();
      respuesta.put("date", LocalDate.now().toString());
      respuesta.put("transaccion",dto);
      respuesta.put("status","OK");
      respuesta.put("message","Transferencia realizada con exito");
      mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                      .content(mapper.writeValueAsString(dto)))
              .andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().json(mapper.writeValueAsString(respuesta)))
      ;

  }

}