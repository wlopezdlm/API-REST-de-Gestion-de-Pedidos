package com.examen.pedidos.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BaseResponse<T> {
    private Integer codigo;
    private String mensaje;
    private T objeto;
}
