package com.comedica.apihello.common;

import lombok.Data;

@Data
public class GenericResponse<T> {
    private int code;
    private String msg;
    private T content;
    private int status;
}
