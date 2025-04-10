package kr.co.company.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class AppConfig {
	
	@Bean
	public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {
		Info info = new Info()
				.title("company swagger")
				.version(springdocVersion)
				.description("Company Application Open API Swagger");
		
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().addList("JWT"))
				.components(new Components().addSecuritySchemes("JWT", createApiKeyScheme()))
				.info(info);
	}
	
	private SecurityScheme createApiKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer");
	}

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
