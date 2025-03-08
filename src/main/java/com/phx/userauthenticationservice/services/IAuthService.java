package com.phx.userauthenticationservice.services;

import com.phx.userauthenticationservice.exceptions.InvalidPasswordException;
import com.phx.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.phx.userauthenticationservice.models.User;

public interface IAuthService {
    User signup(String email, String password) throws UserAlreadyExistException;

    User login(String email, String password) throws InvalidPasswordException;

    User lgout(String email);

}
