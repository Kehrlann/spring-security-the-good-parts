package wf.garnier.spring.security.thegoodparts;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						authorizeHttp -> {
							authorizeHttp.requestMatchers("/").permitAll();
							authorizeHttp.requestMatchers("/favicon.svg").permitAll();
							authorizeHttp.requestMatchers("/css/*").permitAll();
							authorizeHttp.requestMatchers("/error").permitAll();
							authorizeHttp.anyRequest().authenticated();
						}
				)
				.formLogin(l -> l.defaultSuccessUrl("/private"))
				.logout(l -> l.logoutSuccessUrl("/"))
				.oauth2Login(withDefaults())
				.httpBasic(withDefaults())
				.with(new RobotAccountConfigurer(), withDefaults())
				.addFilterBefore(new ForbiddenFilter(), LogoutFilter.class) // filter before auth/logout
				.authenticationProvider(new DanielAuthenticationProvider())
				.build();
	}

	@Bean
	UserDetailsService userDetailsService(){
		return new InMemoryUserDetailsManager(
				User.withUsername("user")
						.password("{noop}password")
						.roles("user")
						.build()
		);
	}

	@Bean
	ApplicationListener<AuthenticationSuccessEvent> successListener() {
		return event -> {
			System.out.println("🎉 [%s] %s".formatted(
					event.getAuthentication().getClass().getSimpleName(),
					event.getAuthentication().getName()
			));
		};
	}

}
