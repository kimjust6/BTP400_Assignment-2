package application;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//	@Autowired
//	DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource)
//				.usersByUsernameQuery("select username,password, enabled from walletusers where username=?")
//				.authoritiesByUsernameQuery("select username, role from user_roles where username=?");

		auth.inMemoryAuthentication()
			.withUser("harsh")
			.password("123")
			.roles("USER");
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").hasRole("USER").and().formLogin().and().csrf().disable();
//				.permitAll().and().logout().permitAll();
//		http.exceptionHandling().accessDeniedPage("/403");
	}

	
}