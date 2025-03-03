package com.Zephir0g.kinacademy.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.swing.plaf.basic.BasicToolBarUI;

//@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("KIN Academy API")
                                .version("1.0")
                                .description("This is a REST API application made with Spring Boot. It is a part of the KIN Academy project. ")
                                .contact(
                                        new Contact()
                                                .name("Bohdan Sukhovarov")
                                                .email("nadgobvoravohus@gmail.com")
                                )
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")
                                )

                )
                .addSecurityItem(
                        new SecurityRequirement(
                        ).addList("Bearer token")
                );
    }
}
