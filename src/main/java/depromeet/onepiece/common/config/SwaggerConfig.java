package depromeet.onepiece.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  private static final String SERVER_NAME = "One-Piece"; // 프로젝트명
  private static final String API_TITLE = "원피스 서버 API 문서";
  private static final String API_DESCRIPTION = "원피스 서버 API 문서입니다.";
  private static final String GITHUB_URL = "https://github.com/depromeet/16th-team1-BE";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .servers(
            List.of(
                new Server().url("http://localhost:8080").description("로컬 서버"),
                new Server().url("https://api.dev.onepiece.com").description("개발 서버"),
                new Server().url("https://api.prod.onepiece.com").description("운영 서버")))
        .addSecurityItem(securityRequirement())
        .components(authSetting())
        .info(swaggerInfo());
  }

  private Components authSetting() {
    return new Components()
        .addSecuritySchemes(
            "accessToken",
            new SecurityScheme()
                .type(Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(In.HEADER)
                .name("Authorization"));
  }

  private Info swaggerInfo() {
    License license = new License();
    license.setUrl(GITHUB_URL);
    license.setName(SERVER_NAME);

    return new Info().version("v1").title(API_TITLE).description(API_DESCRIPTION).license(license);
  }

  private SecurityRequirement securityRequirement() {
    return new SecurityRequirement().addList("accessToken");
  }
}
