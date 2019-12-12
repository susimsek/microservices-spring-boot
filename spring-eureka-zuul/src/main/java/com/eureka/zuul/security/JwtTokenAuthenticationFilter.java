package com.eureka.zuul.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    
	private final JwtConfig jwtConfig;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		// 1. authentication headerı alır. Tokenin authentication headerde geçilmesi gerekmektedir.
		String header = request.getHeader(jwtConfig.getHeader());
		
		// 2. headerı doğrulandı ve prefix kontrol edildi.
		if(header == null || !header.startsWith(jwtConfig.getPrefix())) {
			chain.doFilter(request, response);  		// Valid değilse, diğer filtere geç.
			return;
		}
		
		//Sağlanan hiçbir token yoksa ve dolayısıyla kullanıcı kimliği doğrulanmayacaktır.
		// Tamam. Belki kullanıcı herkese açık bir path giriyor veya bir token arıyor.
		
		// Tokenin gerektiren tüm güvenli pathleri, config sınıfında zaten tanımlanmış ve korunmuştur.
		// Kullanıcı erişim tokeni olmadan erişmeye çalıştığında, kimliği doğrulanmayacak ve bir exception atılacak.
		
		// 3. Token alındı
		String token = header.replace(jwtConfig.getPrefix(), "");
		
		try {	// Örneğin tokenin süresinin dolması durumunda request talebinin oluşturulmasında exceptionlar ortaya çıkabilir
			
			// 4. Token validate edildi
			Claims claims = Jwts.parser()
					.setSigningKey(jwtConfig.getSecret().getBytes())
					.parseClaimsJws(token)
					.getBody();
			
			String username = claims.getSubject();
			if(username != null) {
				@SuppressWarnings("unchecked")
				List<String> authorities = (List<String>) claims.get("authorities");
				
				// 5. auth objesi oluşturuldu
				// UsernamePasswordAuthenticationToken: Geçerli kimliği doğrulanmış / kimliği doğrulanmış kullanıcıyı temsil etmek için spring tarafından kullanılan yerleşik bir nesne.
				//authorities listesine ihtiyaç var, GrantedAuthority interface tipinde, SimpleGrantedAuthority bu interfacenin bir uygulamasıdır.
				 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
								 username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
				 
				 // 6. user autheticate edildi.
				 // Şuan, userin kimliği doğrulandı.
				 SecurityContextHolder.getContext().setAuthentication(auth);
			}
			
		} catch (Exception e) {
			// Başarısızlık durumunda. contexti temizleyin; bu nedenle, kullanıcının kimliği doğrulanmayacağının garantisi
			SecurityContextHolder.clearContext();
		}
		
		// Filtre zincirindeki bir sonraki filtreye git
		chain.doFilter(request, response);
	}

}