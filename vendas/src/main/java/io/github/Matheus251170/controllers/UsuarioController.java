package io.github.Matheus251170.controllers;

import io.github.Matheus251170.DTO.CredentialsDTO;
import io.github.Matheus251170.DTO.TokenDTO;
import io.github.Matheus251170.exception.SenhaInvalidaException;
import io.github.Matheus251170.model.Usuario;
import io.github.Matheus251170.security.JWTService;
import io.github.Matheus251170.service.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {


    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder encoder;

    private final JWTService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario save(@RequestBody @Valid Usuario usuario) {
        String encryptPass = encoder.encode(usuario.getSenha());
        usuario.setSenha(encryptPass);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredentialsDTO credentialsDTO){
        try {

            Usuario usuario = Usuario.builder()
                    .username(credentialsDTO.getUsername())
                    .senha(credentialsDTO.getSenha()).build();

            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(usuario.getUsername(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException e){

            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        }
    }
}
