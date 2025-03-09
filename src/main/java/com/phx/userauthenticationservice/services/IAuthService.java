package com.phx.userauthenticationservice.services;

import com.phx.userauthenticationservice.exceptions.InvalidPasswordException;
import com.phx.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.phx.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.util.MultiValueMap;

public interface IAuthService {
    User signup(String email, String password) throws UserAlreadyExistException;

    Pair<User, MultiValueMap<String,String>> login(String email, String password) throws InvalidPasswordException;

    User lgout(String email);

    Boolean validateToken(Long userId,String token);

}
