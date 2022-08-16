package io.github.Matheus251170.service;

import io.github.Matheus251170.exception.SenhaInvalidaException;
import io.github.Matheus251170.model.Usuario;
import io.github.Matheus251170.repositories.UsuarioRepository;
import io.github.Matheus251170.security.JWTService;
import io.github.Matheus251170.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UserDetails autenticar(Usuario usuario){
        UserDetails userDetails = loadUserByUsername(usuario.getUsername());
        boolean isValid = encoder.matches(usuario.getSenha(), userDetails.getPassword());

        if(isValid){
            return userDetails;
        }

        throw new SenhaInvalidaException();
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if(!username.equals("Magnus")) {
//            throw new UsernameNotFoundException("Usuário não encontrado na base!");
//        }
//        return User.builder()
//                .username("Magnus")
//                .password(encoder.encode("123"))
//                .roles("USER", "ADM")
//                .build();
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuario não encontrado!"));

        String[] roles = usuario.isAdmin() ? new String[]{"ADM", "USER"} : new String[] {"USER"};

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}
