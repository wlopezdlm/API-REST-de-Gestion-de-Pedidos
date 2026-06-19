package com.examen.pedidos.service;

import com.examen.pedidos.dto.request.ProductoRequest;
import com.examen.pedidos.dto.response.ProductoResponse;
import com.examen.pedidos.response.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductoService {

    ResponseEntity<BaseResponse<ProductoResponse>> crear(ProductoRequest request);

    ResponseEntity<BaseResponse<List<ProductoResponse>>> listar();

    ResponseEntity<BaseResponse<ProductoResponse>> buscarPorId(Long id);
}
