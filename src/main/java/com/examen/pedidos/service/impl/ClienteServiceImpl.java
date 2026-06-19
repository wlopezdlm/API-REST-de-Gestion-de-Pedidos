package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ClienteRequest;
import com.examen.pedidos.dto.response.ClienteResponse;
import com.examen.pedidos.entity.Cliente;
import com.examen.pedidos.exception.ClienteNotFoundException;
import com.examen.pedidos.mapper.ClienteMapper;
import com.examen.pedidos.repository.ClienteRepository;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public ResponseEntity<BaseResponse<ClienteResponse>> crear(ClienteRequest request) {
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente guardado = clienteRepository.save(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<ClienteResponse>builder()
                        .codigo(201)
                        .mensaje("Cliente registrado correctamente")
                        .objeto(clienteMapper.toResponse(guardado))
                        .build());
    }

    @Override
    public ResponseEntity<BaseResponse<ClienteResponse>> buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(
                        "No se encontró el cliente con el id: " + id));

        return ResponseEntity.ok(BaseResponse.<ClienteResponse>builder()
                .codigo(200)
                .mensaje("Cliente encontrado")
                .objeto(clienteMapper.toResponse(cliente))
                .build());
    }
}
