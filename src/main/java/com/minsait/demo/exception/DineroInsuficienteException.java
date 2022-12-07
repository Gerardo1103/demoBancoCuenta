package com.minsait.demo.exception;

public class DineroInsuficienteException extends RuntimeException{
    public DineroInsuficienteException(String mensaje){
        super(mensaje);
    }
}
