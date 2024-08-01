package br.com.linkagrotech.userservice.filtro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import br.com.linkagrotech.userservice.dto.UsuarioRecord;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;

@Component
public class Filtro extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	    throws ServletException, IOException {

	try {
	    String authorization = obterJWT(request);

	    if (authorization == null) {
		throw new BadRequestException();
	    }
	    JWT jwt = JWTParser.parse(authorization);

	    var objectMapper = new ObjectMapper();
	    JsonNode json = objectMapper.readTree(((SignedJWT) jwt).getPayload().toBytes());

	    String username = json.get("preferred_username").textValue();
	    String email = json.get("email").textValue();

	    JsonNode propArray = json.get("realm_access").get("roles");
	    List<String> values = new ArrayList<>();
	    if (propArray != null && propArray.isArray()) {
		for (JsonNode element : propArray) {
		    values.add(element.asText());
		}
	    }

	    UsuarioRecord usuarioRecord = new UsuarioRecord(username, email);

	    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(usuarioRecord,
		    null, values.stream().map(SimpleGrantedAuthority::new).toList()));

	    filterChain.doFilter(request, response);

	} catch (Exception e) {
	    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    response.setHeader("Content-type", "application/json");
//            response.getWriter().write(convertObjectToJson(new ExcecaoDTO(e)));
	}

    }

    private String obterJWT(HttpServletRequest request) {
	String jwt = request.getHeader("Authorization");

	if (jwt != null) {
	    return jwt.replace("Bearer ", "");
	}

	return null;

    }

}
