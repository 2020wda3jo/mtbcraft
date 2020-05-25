package com.mtbcraft.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/**").permitAll() // 紐⑤뱺 沅뚰븳�쓣 以�.=濡쒓렇�씤 �븘�슂 �뾾�쓬.
		.antMatchers("/files/**").hasRole("USER") // user 沅뚰븳留� �젒洹� 媛��뒫.
		.antMatchers("/logout").permitAll()
		.anyRequest().authenticated() // 濡쒓렇�씤 泥댄겕�븿.
		.and()
		.formLogin()
		.loginPage("/login") //�씠 以꾩쓣 吏��슦硫� �뒪�봽留곸씠 �젣怨듯븯�뒗 �뤌�씠 異쒕젰�맖.
		.permitAll()
		.successHandler(successHandler())
		.and()
		.logout()
		.logoutUrl("/logout")
		.logoutSuccessUrl("/")
		.invalidateHttpSession(true)
		.and()
		.csrf();
		//api�씠�썑�쓽 二쇱냼�뱾�� csrf�넗�겙寃��궗瑜� �뙣�벐�븳�떎
		http.csrf().ignoringAntMatchers("/api/**");
		http.csrf().ignoringAntMatchers("/riding/**");
		http.csrf().ignoringAntMatchers("/androidUpload/**");
		//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		
		}

	public AuthenticationSuccessHandler successHandler() {
		SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
		return handler;
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception
    {
        // static �뵒�젆�꽣由ъ쓽 �븯�쐞 �뙆�씪 紐⑸줉�� �씤利� 臾댁떆 ( = �빆�긽�넻怨� )
        web.ignoring().antMatchers("/css/**", "/js/**", "/imgs/**");
    }
}