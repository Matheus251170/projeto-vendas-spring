package io.github.Matheus251170.repositories;

import io.github.Matheus251170.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
