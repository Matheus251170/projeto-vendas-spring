package io.github.Matheus251170.service;

import io.github.Matheus251170.DTO.ItemPedidoDTO;
import io.github.Matheus251170.DTO.PedidoDTO;
import io.github.Matheus251170.controllers.PedidoNotFoundException;
import io.github.Matheus251170.enums.StatusPedido;
import io.github.Matheus251170.exception.ServiceException;
import io.github.Matheus251170.model.Client;
import io.github.Matheus251170.model.ItemPedido;
import io.github.Matheus251170.model.Pedido;
import io.github.Matheus251170.model.Produto;
import io.github.Matheus251170.repositories.ClientRepository;
import io.github.Matheus251170.repositories.ItemPedidoRepository;
import io.github.Matheus251170.repositories.PedidoRepository;
import io.github.Matheus251170.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService{

    private final PedidoRepository pedidoRepository;

    private final ClientRepository clientRepository;

    private final ProdutoRepository produtoRepository;

    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Client client = clientRepository.findById(idCliente).orElseThrow( () ->
                new ServiceException("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setClient(client);
        pedido.setStatusPedido(StatusPedido.FINALIZADO);

        List<ItemPedido> itemPedidoList = convertItems(pedido, dto.getItems());

        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itemPedidoList);
        pedido.setItens(itemPedidoList);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItems(id);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id)
                .map(p -> {
                            p.setStatusPedido(statusPedido);
                            return pedidoRepository.save(p);
                        }
                ).orElseThrow(() -> new PedidoNotFoundException());
    }

    private List<ItemPedido> convertItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new ServiceException("Não é possível realizar um pedido sem items!");
        }

        return items.stream().map(dto -> {
            Integer idProduto = dto.getProduto();
            Produto produto = produtoRepository.findById(idProduto).orElseThrow( () ->
                    new ServiceException("Código de produto inválido: " + idProduto));

           ItemPedido itemPedido = new ItemPedido();
           itemPedido.setQuantidade(dto.getQuantidade());
           itemPedido.setPedido(pedido);
           itemPedido.setProduto(produto);

           return itemPedido;
        }).collect(Collectors.toList());
    }
}
