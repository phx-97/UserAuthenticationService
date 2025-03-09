package com.phx.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateTokenDto {
    private Long userId;
    private String token;
}
