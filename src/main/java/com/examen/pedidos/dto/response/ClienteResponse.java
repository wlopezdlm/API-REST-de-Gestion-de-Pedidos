package com.examen.pedidos.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String correo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaRegistro;
}
