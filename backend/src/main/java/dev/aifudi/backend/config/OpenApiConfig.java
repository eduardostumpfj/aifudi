package dev.aifudi.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI Aifudi() {
        return new OpenAPI()
                .info(
                        new Info().title("Aifudi API")
                                .description("This project was developed for the Tech Challenge of the first module of the Arquitetura e Desenvolvimento em JAVA course.")
                                .version("v0.0.1")
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .name("basicAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                ))
                .tags(List.of(
                        new Tag().name("Register User").description("Create new accounts"),
                        new Tag().name("Get all users").description("List system users"),
                        new Tag().name("Update user profile").description("Modify profile data"),
                        new Tag().name("Update Password").description("Change credentials"),
                        new Tag().name("Delete user profile").description("Remove accounts")
                ))
                ;
    }
}
