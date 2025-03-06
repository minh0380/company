package kr.co.company.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {
		Info info = new Info()
				.title("company swagger")
				.version(springdocVersion)
				.description("Company Application Open API Swagger");
		
		return new OpenAPI()
				.components(new Components())
				.info(info);
	}

}
