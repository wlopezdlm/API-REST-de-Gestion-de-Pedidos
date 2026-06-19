package com.examen.pedidos.repository;

import com.examen.pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente JOIN FETCH p.detalles WHERE p.id = :id")
    Optional<Pedido> findByIdWithDetails(@Param("id") Long id);
}
