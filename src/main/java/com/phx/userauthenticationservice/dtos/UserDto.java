package com.phx.userauthenticationservice.dtos;

import com.phx.userauthenticationservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;


@Setter
@Getter
public class UserDto {
    private String email;

    private Set<Role> roles = new HashSet<>();
}
