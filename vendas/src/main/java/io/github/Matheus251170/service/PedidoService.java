package io.github.Matheus251170.service;

import io.github.Matheus251170.DTO.PedidoDTO;
import io.github.Matheus251170.enums.StatusPedido;
import io.github.Matheus251170.model.Pedido;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void updateStatus(Integer id, StatusPedido statusPedido);
}
