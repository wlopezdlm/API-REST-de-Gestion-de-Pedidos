package com.examen.pedidos.service;

import com.examen.pedidos.dto.request.ClienteRequest;
import com.examen.pedidos.dto.response.ClienteResponse;
import com.examen.pedidos.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface ClienteService {

    ResponseEntity<BaseResponse<ClienteResponse>> crear(ClienteRequest request);

    ResponseEntity<BaseResponse<ClienteResponse>> buscarPorId(Long id);
}
