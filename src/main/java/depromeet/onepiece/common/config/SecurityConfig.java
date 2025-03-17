package depromeet.onepiece.common.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import depromeet.onepiece.common.auth.infrastructure.SecurityProperties;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final DefaultOAuth2UserService defaultOAuth2UserService;
  private final AuthenticationSuccessHandler authenticationSuccessHandler;
  private final AuthenticationFailureHandler authenticationFailureHandler;
  private final SecurityProperties securityProperties;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    disabledConfigurations(httpSecurity);
    configurationSessionManagement(httpSecurity);
    configurationCors(httpSecurity);
    configureAuthorizeHttpRequests(httpSecurity);
    configurationOAuth2Login(httpSecurity);
    return httpSecurity.build();
  }

  private void disabledConfigurations(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable);
  }

  private void configurationSessionManagement(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
  }

  private void configurationCors(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.cors(
        corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()));
  }

  private void configureAuthorizeHttpRequests(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(
        auth ->
            auth.requestMatchers(SWAGGER_PATTERNS)
                .permitAll()
                .requestMatchers(STATIC_RESOURCES_PATTERNS)
                .permitAll()
                .requestMatchers(PERMIT_ALL_PATTERNS)
                .permitAll()
                .anyRequest()
                .authenticated());
  }

  private void configurationOAuth2Login(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.oauth2Login(
        oauth2 ->
            oauth2
                .loginPage(securityProperties.loginUrl())
                .userInfoEndpoint(userInfo -> userInfo.userService(defaultOAuth2UserService))
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler));
  }

  private static final String[] SWAGGER_PATTERNS = {
    "/swagger-ui/**", "/actuator/**", "/v3/api-docs/**",
  };

  private static final String[] STATIC_RESOURCES_PATTERNS = {
    "/img/**", "/css/**", "/js/**", "/static/**",
  };

  private static final String[] PERMIT_ALL_PATTERNS = {
    "/error", "/index.html", "/login/**", "/api/v1/reissue", "/api/v1/**"
  };

  CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedHeaders(Collections.singletonList("*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setAllowedOriginPatterns(
          List.of(
              "http://localhost:3000", "https://dev.critix.kr", "https://onepiece-fe.vercel.app"));
      config.setAllowCredentials(true);
      return config;
    };
  }
}
