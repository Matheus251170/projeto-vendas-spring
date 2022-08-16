package io.github.Matheus251170.security;

import io.github.Matheus251170.service.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

;import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthFilter extends OncePerRequestFilter {

    private JWTService jwtService;

    private UsuarioServiceImpl usuarioService;


    public JwtAuthFilter(JWTService jwtService, UsuarioServiceImpl usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(Objects.nonNull(authorization) && authorization.startsWith("Bearer")){

            String token = authorization.split(" ")[1];
            boolean isValid = jwtService.tokenValido(token);

            if(isValid){
                String loginUsuario = jwtService.obterLoginUsuario(token);
                UserDetails user = usuarioService.loadUserByUsername(loginUsuario);
                UsernamePasswordAuthenticationToken usuario = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                usuario.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usuario);
            }
        }
        filterChain.doFilter(request, response);
    }
}
