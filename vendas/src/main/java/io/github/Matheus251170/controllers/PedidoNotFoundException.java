package io.github.Matheus251170.controllers;

public class PedidoNotFoundException extends RuntimeException {

    public PedidoNotFoundException(){
        super("Pedido não encontrado!");
    }
}
