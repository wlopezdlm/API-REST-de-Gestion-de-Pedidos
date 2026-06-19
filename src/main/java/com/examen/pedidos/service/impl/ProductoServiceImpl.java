package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ProductoRequest;
import com.examen.pedidos.dto.response.ProductoResponse;
import com.examen.pedidos.entity.Producto;
import com.examen.pedidos.exception.ProductoNotFoundException;
import com.examen.pedidos.mapper.ProductoMapper;
import com.examen.pedidos.repository.ProductoRepository;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    public ResponseEntity<BaseResponse<ProductoResponse>> crear(ProductoRequest request) {
        Producto producto = productoMapper.toEntity(request);
        Producto guardado = productoRepository.save(producto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<ProductoResponse>builder()
                        .codigo(201)
                        .mensaje("Producto registrado correctamente")
                        .objeto(productoMapper.toResponse(guardado))
                        .build());
    }

    @Override
    public ResponseEntity<BaseResponse<List<ProductoResponse>>> listar() {
        List<ProductoResponse> productos = productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(BaseResponse.<List<ProductoResponse>>builder()
                .codigo(200)
                .mensaje("Lista de productos obtenida correctamente")
                .objeto(productos)
                .build());
    }

    @Override
    public ResponseEntity<BaseResponse<ProductoResponse>> buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(
                        "No se encontró el producto con el id: " + id));

        return ResponseEntity.ok(BaseResponse.<ProductoResponse>builder()
                .codigo(200)
                .mensaje("Producto encontrado")
                .objeto(productoMapper.toResponse(producto))
                .build());
    }
}
