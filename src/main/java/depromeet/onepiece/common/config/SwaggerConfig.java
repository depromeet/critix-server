package depromeet.onepiece.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
  private static final String SERVER_NAME = "Critix-server"; // 프로젝트명
  private static final String API_TITLE = "Critix 서버 API 문서";
  private static final String API_DESCRIPTION = "Critix 서버 API 문서입니다.";
  private static final String GITHUB_URL = "https://github.com/depromeet/16th-team1-BE";
  private final Environment environment;

  private final Map<String, String> PROFILE_SERVER_URL_MAP = new HashMap<>();

  static {
    SpringDocUtils.getConfig().replaceWithSchema(ObjectId.class, new StringSchema());
  }

  @PostConstruct
  public void init() {
    PROFILE_SERVER_URL_MAP.put("local", "http://localhost:8080");
    PROFILE_SERVER_URL_MAP.put("dev", "https://dev.critix.kr");
    PROFILE_SERVER_URL_MAP.put("prod", "https://api.critix.kr");
  }

  @Bean
  public OpenAPI openAPI() {
    SpringDocUtils.getConfig().replaceWithSchema(ObjectId.class, new StringSchema());

    return new OpenAPI()
        .servers(initializeServers())
        .addSecurityItem(securityRequirement())
        .components(authSetting())
        .info(swaggerInfo());
  }

  private List<Server> initializeServers() {
    return PROFILE_SERVER_URL_MAP.entrySet().stream()
        // .filter(entry -> environment.matchesProfiles(entry.getKey()))
        .map(entry -> newOpenApiServer(entry.getValue(), SERVER_NAME + " " + entry.getKey()))
        .collect(Collectors.toList());
  }

  private Server newOpenApiServer(String url, String description) {
    return new Server().url(url).description(description);
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
