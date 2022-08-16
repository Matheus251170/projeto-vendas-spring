package io.github.Matheus251170.security;

import io.github.Matheus251170.VendasApplication;
import io.github.Matheus251170.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTService {

    @Value("${expiracao:30}")
    private String expiracao;

    @Value("${security.key.jwt")
    private String key;

    public String gerarToken(Usuario usuario){
        Long expString = Long.valueOf(expiracao);
        LocalDateTime dateExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dateExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return Jwts
                .builder()
                .setSubject(usuario.getUsername())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    private Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean tokenValido(String token){
        try {
            Claims claims = obterClaims(token);
            Date dtExpiracao = claims.getExpiration();
            LocalDateTime data = dtExpiracao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            return !LocalDateTime.now().isAfter(data);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return (String) obterClaims(token).getSubject();
    }

//    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(VendasApplication.class);
//        JWTService service = context.getBean(JWTService.class);
//        Usuario usuario = new Usuario();
//        usuario.setUsername("oi");
//        String token = service.gerarToken(usuario);
//        System.out.println(token);
//
//        boolean isValid = service.tokenValido(token);
//        System.out.println("Token valido: " + isValid);
//        System.out.println("Login: " + service.obterLoginUsuario(token));
//    }
}
