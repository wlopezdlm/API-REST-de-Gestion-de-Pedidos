package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ProductoRequest;
import com.examen.pedidos.dto.response.ProductoResponse;
import com.examen.pedidos.entity.Producto;
import com.examen.pedidos.exception.ProductoNotFoundException;
import com.examen.pedidos.mapper.ProductoMapper;
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

class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private ProductoRequest productoRequest;
    private ProductoResponse productoResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Teclado mecánico");
        producto.setDescripcion("Teclado RGB");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(20);
        producto.setEstado(true);

        productoRequest = new ProductoRequest();
        productoRequest.setNombre("Teclado mecánico");
        productoRequest.setDescripcion("Teclado RGB");
        productoRequest.setPrecio(new BigDecimal("100.00"));
        productoRequest.setStock(20);

        productoResponse = ProductoResponse.builder()
                .id(1L)
                .nombre("Teclado mecánico")
                .descripcion("Teclado RGB")
                .precio(new BigDecimal("100.00"))
                .stock(20)
                .build();
    }

    @Test
    void crear_cuandoDatosSonValidos_retornaProductoCreado() {
        when(productoMapper.toEntity(any())).thenReturn(producto);
        when(productoRepository.save(any())).thenReturn(producto);
        when(productoMapper.toResponse(any())).thenReturn(productoResponse);

        ResponseEntity<BaseResponse<ProductoResponse>> resultado = productoService.crear(productoRequest);

        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals(201, resultado.getBody().getCodigo());
        assertNotNull(resultado.getBody().getObjeto());
        assertEquals("Teclado mecánico", resultado.getBody().getObjeto().getNombre());
        assertEquals(new BigDecimal("100.00"), resultado.getBody().getObjeto().getPrecio());
        verify(productoRepository, times(1)).save(any());
    }

    @Test
    void listar_cuandoExistenProductos_retornaLista() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));
        when(productoMapper.toResponse(any())).thenReturn(productoResponse);

        ResponseEntity<BaseResponse<List<ProductoResponse>>> resultado = productoService.listar();

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(200, resultado.getBody().getCodigo());
        assertEquals(1, resultado.getBody().getObjeto().size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void listar_cuandoNoExistenProductos_retornaListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());

        ResponseEntity<BaseResponse<List<ProductoResponse>>> resultado = productoService.listar();

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertTrue(resultado.getBody().getObjeto().isEmpty());
    }

    @Test
    void buscarPorId_cuandoProductoExiste_retornaProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoMapper.toResponse(any())).thenReturn(productoResponse);

        ResponseEntity<BaseResponse<ProductoResponse>> resultado = productoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(200, resultado.getBody().getCodigo());
        assertEquals("Teclado mecánico", resultado.getBody().getObjeto().getNombre());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_cuandoProductoNoExiste_lanzaProductoNotFoundException() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class, () -> productoService.buscarPorId(99L));
        verify(productoRepository, times(1)).findById(99L);
    }
}