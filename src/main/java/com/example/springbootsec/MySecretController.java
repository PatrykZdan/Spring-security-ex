package com.example.springbootsec;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

@RestController
public class MySecretController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public MySecretController(UserRepository userRepository,
                              AuthenticationManager authenticationManager,
                              JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping
    @RequestMapping("secret")
    @PreAuthorize("hasAuthority('SECRET')")
    public String mySecret() {
        return "secret!";
    }

    @GetMapping
    @RequestMapping("secret2")
    @PreAuthorize("hasAuthority('SECRET2')")
    public String mySecret2() {
        return "secret2!";
    }

    @GetMapping
    @RequestMapping("not-secret")
    public String notSecret() {
        return "not-secret!";
    }

    @GetMapping("/signin/{name}/{password}")
    public String login(@PathVariable String name,@PathVariable String password) {
        return signin(name, password).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
    }

    private Optional<String> signin(String username, String password) {
        Optional<String> token = Optional.empty();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = Optional.of(jwtProvider.createToken(username, user.get().userAuthorities));
            } catch (AuthenticationException e) {

            }
        }
        return token;
    }
}
