package com.test.reactor;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InputSource {
    private final Object data;
    private final long id;
}