package com.phx.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String email;
    private String password;
}
