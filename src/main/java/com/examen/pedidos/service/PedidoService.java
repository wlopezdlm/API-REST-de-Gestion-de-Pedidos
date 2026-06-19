package com.examen.pedidos.service;

import com.examen.pedidos.dto.request.PedidoRequest;
import com.examen.pedidos.dto.response.PedidoResponse;
import com.examen.pedidos.response.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PedidoService {

    ResponseEntity<BaseResponse<PedidoResponse>> crear(PedidoRequest request);

    ResponseEntity<BaseResponse<PedidoResponse>> buscarPorId(Long id);

    ResponseEntity<BaseResponse<List<PedidoResponse>>> listarPorCliente(Long clienteId);
}
