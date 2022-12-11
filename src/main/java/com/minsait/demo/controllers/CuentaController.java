package com.minsait.demo.controllers;

import com.minsait.demo.exception.DineroInsuficienteException;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.models.TransferirDTO;
import com.minsait.demo.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    @Autowired
    private CuentaService service;
    @GetMapping("/listar")    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> findAll(){
        return service.findAll();
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<Cuenta> findById(@PathVariable Long id){
       try{
           Cuenta cuenta = service.findById(id);
           return ResponseEntity.ok(cuenta);
       }catch (NoSuchElementException e){
           return ResponseEntity.notFound().build();
       }
    }
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta){
        return service.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransferirDTO dto){
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("transaccion",dto);
        try {
            service.transferir(dto.getCuentaOrigenId(),dto.getCuentaDestinoId(),
                    dto.getMonto(),dto.getBancoId());
            response.put("status","OK");
            response.put("message","Transferencia realizada con exito");
            return  ResponseEntity.ok(response);
        }catch(DineroInsuficienteException exception){
            response.put("status","OK");
            response.put("message",exception.getMessage());
            return ResponseEntity.ok(response);
        }


    }
}
