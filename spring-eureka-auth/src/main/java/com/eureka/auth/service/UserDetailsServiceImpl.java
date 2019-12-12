package com.eureka.auth.service;

import com.eureka.auth.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service   //  @Service anatasyonu ekledik.
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final BCryptPasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// Tüm şifreler encode edilmelidir.
		final List<AppUser> users = Arrays.asList(
			new AppUser(1, "omar", encoder.encode("12345"), "USER"),
			new AppUser(2, "admin", encoder.encode("12345"), "ADMIN")
		);
		

		for(AppUser appUser: users) {
			if(appUser.getUsername().equals(username)) {
				
				// Spring'in bu biçimde olması için roller gerektirdiğini unutmayın: "ROLE_" + userRole ("ROLE_ADMIN")
				// Dolayısıyla, bu biçime ayarlamamız gerekir, böylece rolleri doğrulayabilir ve karşılaştırabiliriz (yani hasRole ("ADMIN")).
				List<GrantedAuthority> grantedAuthorities = AuthorityUtils
		                	.commaSeparatedStringToAuthorityList("ROLE_" + appUser.getRole());
				
				//"User" sınıfı, Spring tarafından sağlanır ve UserDetailsService tarafından döndürülecek kullanıcı için bir model sınıfını temsil eder.
				// kullanıcı kimliklendirmeyi kontrol eder ve doğrular
				return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
			}
		}
		
		// Kullanıcı bulunamadıysa. Bu exception at.
		throw new UsernameNotFoundException("Username: " + username + " not found");
	}

}