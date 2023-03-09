package wf.garnier.spring.security.thegoodparts;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

class RobotAuthentication extends AbstractAuthenticationToken {
	public RobotAuthentication() {
		super(AuthorityUtils.createAuthorityList("ROLE_robot"));
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return "Ms Robot 🤖";
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		throw new RuntimeException("DON'T CHANGE THE AUTH STATUS 😱");
	}
}
