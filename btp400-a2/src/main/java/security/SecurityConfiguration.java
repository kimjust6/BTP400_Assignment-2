package security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//	@Autowired
//	DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource)
//				.usersByUsernameQuery("select username,password, enabled from walletusers where username=?")
//				.authoritiesByUsernameQuery("select username, role from user_roles where username=?");

		auth.inMemoryAuthentication().withUser("harsh").password("123").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").hasRole("USER").and().formLogin();
//				.permitAll().and().logout().permitAll();
//		http.exceptionHandling().accessDeniedPage("/403");
	}

	// could work on password encoder using Bean
}