package io.github.Matheus251170.controllers;

import io.github.Matheus251170.DTO.AtualizacaoStatusPedidoDTO;
import io.github.Matheus251170.DTO.InfoItemPedidoDTO;
import io.github.Matheus251170.DTO.InfoPedidoDTO;
import io.github.Matheus251170.DTO.PedidoDTO;
import io.github.Matheus251170.enums.StatusPedido;
import io.github.Matheus251170.model.ItemPedido;
import io.github.Matheus251170.model.Pedido;
import io.github.Matheus251170.service.PedidoService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {


    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {

        this.pedidoService = pedidoService;
    }

    @PostMapping("/save")
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = pedidoService.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InfoPedidoDTO getById(@PathVariable Integer id) {
        return pedidoService.obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado"));
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@RequestBody AtualizacaoStatusPedidoDTO dto, @PathVariable Integer id){
        pedidoService.updateStatus(id, StatusPedido.valueOf(dto.getNovoStatus()));
    }

    private InfoPedidoDTO converter(Pedido pedido) {
        return InfoPedidoDTO.builder().codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getClient().getCpf())
                .nomeCliente(pedido.getClient().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatusPedido().name())
                .items(converter(pedido.getItens()))
                .build();
    }

    private List<InfoItemPedidoDTO> converter(List<ItemPedido> items){
        if(CollectionUtils.isEmpty(items)){
            return Collections.emptyList();
        }

        return items.stream().map( item -> InfoItemPedidoDTO.builder().descricaoProduto(item.getProduto().getDescricao())
                .precoUnitario(item.getProduto().getPreco_unitario())
                .quantidade(item.getQuantidade()).build()
        ).collect(Collectors.toList());
    }
}
