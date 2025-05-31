package com.my_app.learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

//	@Bean
//	OpenAPI customOpenAPI() {
//		final String securitySchemeName = "basicAuth";
//		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
//				.components(new Components().addSecuritySchemes(securitySchemeName,
//						new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("basic")))
//				.info(new Info().title("Product API").version("1.0").description("API for managing products"));
//	}

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("My App API").version("1.0"))
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
				.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
						new SecurityScheme().name("Authorization").type(SecurityScheme.Type.HTTP).scheme("bearer")
								.bearerFormat("JWT")));
	}
}