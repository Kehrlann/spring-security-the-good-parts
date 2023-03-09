package wf.garnier.spring.security.thegoodparts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

class ForbiddenFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		if (Objects.equals(request.getHeader("x-forbidden"), "true")) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.getWriter().write("That's forbidden. 🚫🚫🚫🚫");
			return;
		}

		filterChain.doFilter(request, response);
	}
}
