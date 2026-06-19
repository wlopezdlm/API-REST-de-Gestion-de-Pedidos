package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ItemPedidoRequest;
import com.examen.pedidos.dto.request.PedidoRequest;
import com.examen.pedidos.dto.response.PedidoResponse;
import com.examen.pedidos.entity.Cliente;
import com.examen.pedidos.entity.DetallePedido;
import com.examen.pedidos.entity.Pedido;
import com.examen.pedidos.entity.Producto;
import com.examen.pedidos.exception.ClienteNotFoundException;
import com.examen.pedidos.exception.PedidoNotFoundException;
import com.examen.pedidos.exception.ProductoNotFoundException;
import com.examen.pedidos.exception.StockInsuficienteException;
import com.examen.pedidos.mapper.PedidoMapper;
import com.examen.pedidos.repository.ClienteRepository;
import com.examen.pedidos.repository.PedidoRepository;
import com.examen.pedidos.repository.ProductoRepository;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<PedidoResponse>> crear(PedidoRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ClienteNotFoundException(
                        "No se encontró el cliente con el id: " + request.getClienteId()));

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ProductoNotFoundException(
                            "No se encontró el producto con el id: " + item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para '" + producto.getNombre() +
                        "'. Disponible: " + producto.getStock() +
                        ", solicitado: " + item.getCantidad());
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            detalles.add(DetallePedido.builder()
                    .productoId(producto.getId())
                    .nombreProducto(producto.getNombre())
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build());
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .fechaPedido(new Date())
                .estado("CREADO")
                .total(total)
                .build();

        detalles.forEach(d -> d.setPedido(pedido));
        pedido.setDetalles(detalles);

        Pedido guardado = pedidoRepository.save(pedido);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<PedidoResponse>builder()
                        .codigo(201)
                        .mensaje("Pedido creado correctamente")
                        .objeto(pedidoMapper.toResponse(guardado))
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<BaseResponse<PedidoResponse>> buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new PedidoNotFoundException(
                        "No se encontró el pedido con el id: " + id));

        return ResponseEntity.ok(BaseResponse.<PedidoResponse>builder()
                .codigo(200)
                .mensaje("Pedido encontrado")
                .objeto(pedidoMapper.toResponse(pedido))
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<BaseResponse<List<PedidoResponse>>> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNotFoundException("No se encontró el cliente con el id: " + clienteId);
        }

        List<PedidoResponse> pedidos = pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(BaseResponse.<List<PedidoResponse>>builder()
                .codigo(200)
                .mensaje("Pedidos del cliente obtenidos correctamente")
                .objeto(pedidos)
                .build());
    }
}
