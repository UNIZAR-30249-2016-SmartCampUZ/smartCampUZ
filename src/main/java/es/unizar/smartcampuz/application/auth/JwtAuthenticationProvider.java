package es.unizar.smartcampuz.application.auth;

import es.unizar.smartcampuz.model.user.User;
import es.unizar.smartcampuz.application.exceptions.JwtAuthenticationException;
import es.unizar.smartcampuz.application.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;

    private static final Logger LOG = LoggerFactory
        .getLogger(JwtAuthenticationProvider.class);

    @SuppressWarnings("unused")
    public JwtAuthenticationProvider() {
        this(null);
    }

    @Autowired
    public JwtAuthenticationProvider (JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate (Authentication authentication) throws AuthenticationException {
        try{
            User user = jwtService.verify((String)authentication.getCredentials());
            //TODO: Aquí tendré que diferenciar qué usuario se ha logeado para saber qué 'JwtAuthenticated' devolver
            return new JwtAuthenticatedManager(user);
        }
        catch (Exception e){
            throw new JwtAuthenticationException("Failed to verify token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication){
        return JwtAuthToken.class.equals(authentication);
    }
}

