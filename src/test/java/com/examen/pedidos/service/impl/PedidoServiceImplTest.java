package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ItemPedidoRequest;
import com.examen.pedidos.dto.request.PedidoRequest;
import com.examen.pedidos.dto.response.PedidoResponse;
import com.examen.pedidos.entity.Cliente;
import com.examen.pedidos.entity.Pedido;
import com.examen.pedidos.entity.Producto;
import com.examen.pedidos.exception.PedidoNotFoundException;
import com.examen.pedidos.exception.StockInsuficienteException;
import com.examen.pedidos.mapper.PedidoMapper;
import com.examen.pedidos.repository.ClienteRepository;
import com.examen.pedidos.repository.PedidoRepository;
import com.examen.pedidos.repository.ProductoRepository;
import com.examen.pedidos.response.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Cliente cliente;
    private Producto producto;
    private PedidoRequest pedidoRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Walter");
        cliente.setApellido("Lopez");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Teclado mecánico");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(20);
        producto.setEstado(true);

        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProductoId(1L);
        item.setCantidad(2);

        pedidoRequest = new PedidoRequest();
        pedidoRequest.setClienteId(1L);
        pedidoRequest.setItems(List.of(item));
    }

    @Test
    void crearPedido_cuandoDatosSonValidos_retornaPedidoCreado() {
        PedidoResponse pedidoResponse = PedidoResponse.builder()
                .id(1L)
                .cliente("Walter Lopez")
                .estado("CREADO")
                .total(new BigDecimal("300.00"))
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenReturn(producto);
        when(pedidoRepository.save(any())).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });
        when(pedidoMapper.toResponse(any())).thenReturn(pedidoResponse);

        ResponseEntity<BaseResponse<PedidoResponse>> resultado = pedidoService.crear(pedidoRequest);

        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals(201, resultado.getBody().getCodigo());
        assertNotNull(resultado.getBody().getObjeto());
        assertEquals("CREADO", resultado.getBody().getObjeto().getEstado());
        assertEquals(new BigDecimal("300.00"), resultado.getBody().getObjeto().getTotal());
        verify(pedidoRepository, times(1)).save(any());
        verify(productoRepository, times(1)).save(any());
    }

    @Test
    void crearPedido_cuandoStockEsInsuficiente_lanzaStockInsuficienteException() {
        producto.setStock(1);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(StockInsuficienteException.class, () -> pedidoService.crear(pedidoRequest));

        verify(pedidoRepository, never()).save(any());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void buscarPedido_cuandoNoExiste_lanzaPedidoNotFoundException() {
        when(pedidoRepository.findByIdWithDetails(99L)).thenReturn(Optional.empty());

        assertThrows(PedidoNotFoundException.class, () -> pedidoService.buscarPorId(99L));
    }
}
