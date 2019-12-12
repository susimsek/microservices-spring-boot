package com.eureka.zuul.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity    //security config aktif edili. Bu anatasyon, spring security  config anlamına gelir.
@RequiredArgsConstructor
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	private final JwtConfig jwtConfig;
 
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
		   // Her istekte tokeni doğrulamak için filtre ekleyin.
		   .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
		// istekleri yetkilendirme yapılandırması
		.authorizeRequests()
		   // "auth" servisine erişen herkese izin verir
		   .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
		   // admin alanına erişmeye çalışıyorsanız yönetici olmalısınız (burada ayrıca kimlik doğrulaması gerekir)   .antMatchers("/gallery" + "/admin/**").hasRole("ADMIN")
		   // Başka bir isteğin kimliği doğrulanmalıdır.
		   .anyRequest().authenticated(); 
	}

}