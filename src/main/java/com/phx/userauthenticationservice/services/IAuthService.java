package com.phx.userauthenticationservice.services;

import com.phx.userauthenticationservice.models.User;

public interface IAuthService {
    User signup(String email, String password);

    User login(String email, String password);

    User lgout(String email);

}
