package es.unizar.smartcampuz.infrastructure.auth.jwt;

import es.unizar.smartcampuz.infrastructure.auth.Credential;
import es.unizar.smartcampuz.infrastructure.exceptions.JwtAuthenticationException;
import es.unizar.smartcampuz.infrastructure.service.JwtService;
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
            Credential credential = jwtService.verify((String)authentication.getCredentials());
            switch (credential.getRole()){

                case "professor": return new JwtAuthenticatedProfessor(credential);

                case "maintenance": return new JwtAuthenticatedMaintenance(credential);

                default: return new JwtAuthenticatedAdmin(credential);
            }

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

