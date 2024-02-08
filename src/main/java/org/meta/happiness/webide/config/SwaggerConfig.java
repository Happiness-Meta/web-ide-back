package org.meta.happiness.webide.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@OpenAPIDefinition(
        info = @Info(
                title = "Earth-IDE-N API 명세서",
                description = "Earth-IDE-N에 사용되는 API 명세서",
                version = "v1",
                contact= @Contact(
                        name = "j",
                        email = "j@gmail.com"
                )
        ),
        servers = {
                @Server(
                        description = "DEV",
                        url = "http://localhost:8089"
                ),
                @Server(
                        description = "TEST",
                        url = "http://localhost:8089"
                )
        }
)
@Configuration
public class SwaggerConfig {

//    private static final String BEARER_TOKEN_PREFIX = "Bearer";
//
//    @Bean
//    @Profile("!Prod")
//    public OpenAPI openAPI() {
//        String jwtSchemeName = JwtTokenProvider.AUTHORIZATION_HEADER;
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
//        Components components = new Components()
//                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
//                        .name(jwtSchemeName)
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme(BEARER_TOKEN_PREFIX)
//                        .bearerFormat(JwtTokenProvider.TYPE));
//
//
//        return new OpenAPI()
//                .addSecurityItem(securityRequirement)
//                .components(components);
//    }

}