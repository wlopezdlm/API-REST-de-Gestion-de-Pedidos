package com.examen.pedidos.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {

    private Long id;
    private String cliente;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaPedido;

    private String estado;
    private BigDecimal total;
    private List<DetallePedidoResponse> detalles;
}
