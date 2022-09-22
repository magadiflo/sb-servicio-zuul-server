package com.magadiflo.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @RefreshScope, Si queremos actualiza la configuración (ejmpl. modificación en el servidor de configuración, 
 * luego hacer un comit, etc...) y nestra aplicación está levantada y no queremos reiniciarla,
 * entonces lo podríamos hacer mediante ACTUATOR, con Refresh
 * */
@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;

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
			.antMatchers("/api/productos/**", "/api/items/**", "/api/usuarios/**").hasRole("ADMIN")
			.anyRequest().authenticated();
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(this.accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(this.jwtKey);		
		return tokenConverter;
	}

}
