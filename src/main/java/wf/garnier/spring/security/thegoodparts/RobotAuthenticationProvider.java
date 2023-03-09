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
		var authRequest = (RobotAuthentication) authentication;
		if (validPasswords.contains(authRequest.getPassword())) {
			return RobotAuthentication.authenticated();
		}
		throw new BadCredentialsException("You are not Ms Robot 🤖");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return RobotAuthentication.class.isAssignableFrom(authentication);
	}
}
