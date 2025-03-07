package com.phx.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

@Setter
@Getter
public class LogoutRequestDto {
    private String email;

}
