# API REST de Gestión de Pedidos

API REST para gestionar clientes, productos y pedidos de una tienda.

## Tecnologías usadas

- Java 21
- Spring Boot 3.5.x
- Maven
- PostgreSQL
- Spring Data JPA
- Lombok
- JUnit 5
- Mockito

## Configuración de base de datos

Crear la base de datos antes de ejecutar la aplicación:

```sql
CREATE DATABASE db_pedidos;
```

Configuración en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_pedidos
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
```

## Instrucciones para ejecutar

```bash
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

La aplicación inicia en el puerto **8090**.

## Endpoints disponibles

| Método | Endpoint                          | Descripción               |
|--------|-----------------------------------|---------------------------|
| POST   | /api/clientes                     | Registrar cliente         |
| GET    | /api/clientes/{id}                | Buscar cliente por ID     |
| POST   | /api/productos                    | Registrar producto        |
| GET    | /api/productos                    | Listar productos          |
| GET    | /api/productos/{id}               | Buscar producto por ID    |
| POST   | /api/pedidos                      | Crear pedido              |
| GET    | /api/pedidos/{id}                 | Buscar pedido por ID      |
| GET    | /api/pedidos/cliente/{clienteId}  | Listar pedidos de cliente |

## Ejemplos de request JSON

### Crear cliente — POST /api/clientes

```json
{
  "nombre": "Walter",
  "apellido": "Lopez",
  "dni": "12345678",
  "correo": "walter.lopez@gmail.com"
}
```

### Crear producto — POST /api/productos

```json
{
  "nombre": "Teclado mecánico",
  "descripcion": "Teclado RGB",
  "precio": 100.00,
  "stock": 20
}
```

### Crear pedido — POST /api/pedidos

```json
{
  "clienteId": 1,
  "items": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 2,
      "cantidad": 1
    }
  ]
}
```

## Estructura del proyecto

```
src/main/java/com/examen/pedidos/
├── controller/       → ClienteController, ProductoController, PedidoController
├── service/          → interfaces de servicio
│   └── impl/         → ClienteServiceImpl, ProductoServiceImpl, PedidoServiceImpl
├── repository/       → ClienteRepository, ProductoRepository, PedidoRepository
├── entity/           → Cliente, Producto, Pedido, DetallePedido
├── dto/
│   ├── request/      → ClienteRequest, ProductoRequest, PedidoRequest, ItemPedidoRequest
│   └── response/     → ClienteResponse, ProductoResponse, PedidoResponse, DetallePedidoResponse
├── exception/        → PedidoNotFoundException, StockInsuficienteException, GlobalExceptionHandler
├── mapper/           → ClienteMapper, ProductoMapper, PedidoMapper
└── response/         → BaseResponse
```

## Pruebas unitarias

Ejecutar todos los tests:

```bash
.\mvnw.cmd test
```

Ejecutar una clase específica:

```bash
.\mvnw.cmd test -Dtest=ClienteServiceImplTest
.\mvnw.cmd test -Dtest=ProductoServiceImplTest
.\mvnw.cmd test -Dtest=PedidoServiceImplTest
```

**ClienteServiceImplTest** (3 tests):
- `crear_cuandoDatosSonValidos_retornaClienteCreado`
- `buscarPorId_cuandoClienteExiste_retornaCliente`
- `buscarPorId_cuandoClienteNoExiste_lanzaClienteNotFoundException`

**PedidoServiceImplTest** (3 tests):
- `crearPedido_cuandoDatosSonValidos_retornaPedidoCreado`
- `crearPedido_cuandoStockEsInsuficiente_lanzaStockInsuficienteException`
- `buscarPedido_cuandoNoExiste_lanzaPedidoNotFoundException`

**ProductoServiceImplTest** (5 tests):
- `crear_cuandoDatosSonValidos_retornaProductoCreado`
- `listar_cuandoExistenProductos_retornaLista`
- `listar_cuandoNoExistenProductos_retornaListaVacia`
- `buscarPorId_cuandoProductoExiste_retornaProducto`
- `buscarPorId_cuandoProductoNoExiste_lanzaProductoNotFoundException`
