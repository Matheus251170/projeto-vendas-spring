package io.github.Matheus251170.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemPedidoDTO {

    private Integer produto;
    private Integer quantidade;
}
