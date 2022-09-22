package com.magadiflo.zuul.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception { //Para el token
		resources.tokenStore(this.tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception { //para rutas
		http.authorizeRequests()
			.antMatchers("/api/security/oauth/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/productos/productos", "/api/items/items", "/api/usuarios/usuarios").permitAll()
			.antMatchers(HttpMethod.GET, "/api/productos/productos/{id}", "/api/items/items/producto/{id}/cantidad/{cantidad}", "/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN", "USER")
			.antMatchers("/api/productos/**", "/api/items/items/**", "/api/usuarios/**").hasRole("ADMIN")
			.anyRequest().authenticated();
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(this.accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("algun_codigo_secreto_123456");		
		return tokenConverter;
	}

}
