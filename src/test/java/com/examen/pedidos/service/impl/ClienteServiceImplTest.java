package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ClienteRequest;
import com.examen.pedidos.dto.response.ClienteResponse;
import com.examen.pedidos.entity.Cliente;
import com.examen.pedidos.exception.ClienteNotFoundException;
import com.examen.pedidos.mapper.ClienteMapper;
import com.examen.pedidos.repository.ClienteRepository;
import com.examen.pedidos.response.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequest clienteRequest;
    private ClienteResponse clienteResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Walter");
        cliente.setApellido("Lopez");
        cliente.setDni("12345678");
        cliente.setCorreo("walter.lopez@gmail.com");
        cliente.setFechaRegistro(new Date());

        clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Walter");
        clienteRequest.setApellido("Lopez");
        clienteRequest.setDni("12345678");
        clienteRequest.setCorreo("walter.lopez@gmail.com");

        clienteResponse = ClienteResponse.builder()
                .id(1L)
                .nombre("Walter")
                .apellido("Lopez")
                .dni("12345678")
                .correo("walter.lopez@gmail.com")
                .fechaRegistro(new Date())
                .build();
    }

    @Test
    void crear_cuandoDatosSonValidos_retornaClienteCreado() {
        when(clienteMapper.toEntity(any())).thenReturn(cliente);
        when(clienteRepository.save(any())).thenReturn(cliente);
        when(clienteMapper.toResponse(any())).thenReturn(clienteResponse);

        ResponseEntity<BaseResponse<ClienteResponse>> resultado = clienteService.crear(clienteRequest);

        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals(201, resultado.getBody().getCodigo());
        assertNotNull(resultado.getBody().getObjeto());
        assertEquals("Walter", resultado.getBody().getObjeto().getNombre());
        assertEquals("12345678", resultado.getBody().getObjeto().getDni());
        verify(clienteRepository, times(1)).save(any());
    }

    @Test
    void buscarPorId_cuandoClienteExiste_retornaCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponse(any())).thenReturn(clienteResponse);

        ResponseEntity<BaseResponse<ClienteResponse>> resultado = clienteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(200, resultado.getBody().getCodigo());
        assertEquals("Walter", resultado.getBody().getObjeto().getNombre());
        assertEquals("walter.lopez@gmail.com", resultado.getBody().getObjeto().getCorreo());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_cuandoClienteNoExiste_lanzaClienteNotFoundException() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ClienteNotFoundException.class, () -> clienteService.buscarPorId(99L));
        verify(clienteRepository, times(1)).findById(99L);
    }
}