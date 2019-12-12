package com.eureka.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity    //security config aktif edili. Bu anatasyon, spring security  config anlamına gelir.
@RequiredArgsConstructor
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {
	

	private final UserDetailsService userDetailsService;

	private final JwtConfig jwtConfig;

	private final BCryptPasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    .csrf().disable()
				// stateless sessionu kullandığımızdan emin olun; session kullanıcının durumunu tutmayacak.
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and()
				// yetkili girşimler ayarlandı
	            .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
	        .and()
		    // Her istekte user credentials doğrulamak ve token almak için filtre eklendi.
			
		    // authenticationManager() Nedir?
		    // WebSecurityConfigurerAdapter tarafından sağlanan, kullanıcının kimlik bilgilerini doğrulamak için kullanılan bir nesne
		    // Filtrenin kullanıcının kimliğini doğrulaması için bu auth managere ihtiyacı var.
		    .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))	
		.authorizeRequests()
		    // tüm POST isteklerine izin ver
		    .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
		    // diğer isteklerin kimliği doğrulanmalıdır
		    .anyRequest().authenticated();
	}
	
	//Spring, kullanıcıyı veritabanından (veya başka bir kaynaktan) almaasını UserDetailsService interfacesine sahiptir.
	// UserDetailsService nesnesi, kullanıcıyı veritabanından yüklemek için auth manager tarafından kullanılır.
	// Ek olarak, şifre kodlayıcısını da tanımlamamız gerekir. Böylece, auth manager şifreleri karşılaştırabilir ve doğrulayabilir.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

}