package com.phx.userauthenticationservice.services;

import com.phx.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.phx.userauthenticationservice.models.State;
import com.phx.userauthenticationservice.models.User;
import com.phx.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signup(String email, String password) throws UserAlreadyExistException {
        Optional<User> optionalUser = Optional.ofNullable(userRepo.findUserByEmail(email));
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException("Email already registered!!");
        }

        User user = new User();
        user.setEmail(email);
        user.setState(State.ACTIVE);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);

        return user;
    }

    @Override
    public User login(String email, String password) {
        return null;
    }

    @Override
    public User lgout(String email) {
        return null;
    }
}
