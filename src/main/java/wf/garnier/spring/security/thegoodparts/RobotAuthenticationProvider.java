package wf.garnier.spring.security.thegoodparts;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

class RobotAuthenticationProvider implements AuthenticationProvider {

    private final List<String> validPasswords = List.of("beep-boop", "boop-beep");

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var authRequest = (RobotAuthenticationToken) authentication;
        if (validPasswords.contains(authRequest.getPassword())) {
            return RobotAuthenticationToken.authenticated();
        }
        throw new BadCredentialsException("You are not Ms Robot 🤖");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RobotAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
