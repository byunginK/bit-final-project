package com.bit.paperhouse.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bit.paperhouse.service.CustomAuthenticationFailureHandler;
import com.bit.paperhouse.service.PrincipalOauth2UserService;

// 1.코드받기(인증) 2.엑세스토큰(권한) 
//3.사용자프로필 정보를 가져오고 4.그 정보를 토대로 회원강비을 자동으로 진행시키기도 함
//4-2.(이메일,전화번호, 이름, 아이디)쇼핑몰 ->(집주소), 백화점몰 -> (vip등급, 일반등급)



@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//http.csrf().disable();
	   
		http.authorizeRequests()
			.antMatchers("/notice/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/main/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/qna/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/article/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/writer/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/review/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/myLibrary/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/myPage/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/search/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")			
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/user/**").permitAll()
			.antMatchers("/email/**").permitAll()
			.antMatchers("/").permitAll()
		.and()
			.formLogin()
	        .loginPage("/user/login")
	        .defaultSuccessUrl("/user/login/result")
	        .loginProcessingUrl("/login")
	        .failureUrl("/user/login?error=true")
	        .failureHandler(authenticationFailureHandler())
	        .permitAll()
		.and()
			.logout()
	        .invalidateHttpSession(true)
	        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
			.logoutUrl("/user/logout")
	        .logoutSuccessUrl("/user/logout/result")
            .deleteCookies("JSESSIONID")
	        .permitAll()
	    .and()
	        .oauth2Login()
	        .defaultSuccessUrl("/user/login/oauth2/result")
	        .loginPage("/user/login") 
		    .userInfoEndpoint()
		    .userService(principalOauth2UserService);  //구글 로그인 된 뒤 후처리가 필요함  Tip.코드x (엑세스토큰+사용자프로필정보 O)
		http.exceptionHandling().accessDeniedPage("/");
		http.sessionManagement()
		    .invalidSessionUrl("/")
		    .maximumSessions(1)
		    .maxSessionsPreventsLogin(true)
		    .expiredUrl("/")
		    .sessionRegistry(sessionRegistry()); // 동시 로그인 불가	
		
	}
	
	
	



	 @Override
	 public void configure(WebSecurity webSecurity) throws Exception {
		 
	  webSecurity.ignoring().antMatchers("/static/**", "/css/**", "/fonts/**", "/js/**", "/less/**", "/scss/**", "/images/**", "/webjars/**"); 
	 }






	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	
	@Bean 
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() { 
		
		return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher()); 
		
		}
	
	 @Bean
	    public AuthenticationFailureHandler authenticationFailureHandler() {
	        return new CustomAuthenticationFailureHandler();
	    }
	
	
	@Bean
	public SessionRegistry sessionRegistry() {
		
		return new SessionRegistryImpl(); 
	}
	
	

	

}
