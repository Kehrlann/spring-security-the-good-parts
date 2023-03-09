package wf.garnier.spring.security.thegoodparts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

class RobotAuthenticationFilter extends OncePerRequestFilter {

	private final static String HEADER_NAME = "x-robot-password";

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {
		if (!Collections.list(request.getHeaderNames()).contains(HEADER_NAME)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!Objects.equals(request.getHeader(HEADER_NAME), "beep-boop")) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write("You are not Ms Robot 🤖🚫");
			return;
		}

		var newContext = SecurityContextHolder.createEmptyContext();
		newContext.setAuthentication(new RobotAuthentication());
		SecurityContextHolder.setContext(newContext);
		filterChain.doFilter(request, response);
	}
}
