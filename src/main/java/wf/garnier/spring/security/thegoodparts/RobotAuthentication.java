package wf.garnier.spring.security.thegoodparts;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

class RobotAuthentication extends AbstractAuthenticationToken {
	private final String password;

	private RobotAuthentication() {
		super(AuthorityUtils.createAuthorityList("ROLE_robot"));
		super.setAuthenticated(true);
		this.password = null;
	}

	private RobotAuthentication(String password) {
		super(AuthorityUtils.NO_AUTHORITIES);
		super.setAuthenticated(false);
		this.password = password;
	}

	public static RobotAuthentication authenticated() {
		return new RobotAuthentication();
	}

	public static RobotAuthentication unauthenticated(String password) {
		return new RobotAuthentication(password);
	}

	@Override
	public Object getCredentials() {
		return this.getPassword();
	}

	public String getPassword() {
		return this.password;
	}

	@Override
	public Object getPrincipal() {
		return "Ms Robot 🤖";
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		throw new RuntimeException("DON'T CHANGE THE AUTH STATUS 😱");
	}
}
