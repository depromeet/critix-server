package depromeet.onepiece.common.auth.presentation.filter;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

import depromeet.onepiece.common.auth.application.jwt.TokenInjector;
import depromeet.onepiece.common.auth.application.service.RefreshTokenService;
import depromeet.onepiece.common.auth.domain.jwt.TokenResolver;
import depromeet.onepiece.common.auth.presentation.exception.AuthenticationRequiredException;
import depromeet.onepiece.common.auth.presentation.exception.RefreshTokenNotValidException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  private final TokenResolver tokenResolver;
  private final TokenInjector tokenInjector;
  private final UserDetailsService userDetailsService;
  private final RefreshTokenService refreshTokenService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    processTokenAuthentication(request, response);
    filterChain.doFilter(request, response);
  }

  private void processTokenAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    try {
      String token = resolveTokenFromRequest(request, response);
      setAuthentication(request, getUserDetails(token, request, response));
    } catch (ExpiredJwtException | AuthenticationRequiredException e) {
      log.debug("Failed to authenticate", e);
      invalidateCookie(ACCESS_TOKEN, response);
    } catch (RefreshTokenNotValidException e) {
      log.warn("Failed to authenticate", e);
      invalidateCookie(ACCESS_TOKEN, response);
      invalidateCookie(REFRESH_TOKEN, response);
    } catch (Exception e) {
      log.error("Failed to authenticate", e);
      invalidateCookie(ACCESS_TOKEN, response);
    }
  }

  private String resolveTokenFromRequest(HttpServletRequest request, HttpServletResponse response) {
    try {
      return tokenResolver
          .resolveTokenFromRequest(request)
          .orElseGet(
              () ->
                  refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken());
    } catch (ExpiredJwtException e) {
      return refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken();
    }
  }

  private UserDetails getUserDetails(
      String token, HttpServletRequest request, HttpServletResponse response) {
    try {
      String subject = tokenResolver.getSubjectFromToken(token);
      return userDetailsService.loadUserByUsername(subject);
    } catch (ExpiredJwtException e) {
      String accessToken =
          refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken();
      String subject = tokenResolver.getSubjectFromToken(accessToken);
      return userDetailsService.loadUserByUsername(subject);
    }
  }

  private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private void invalidateCookie(String cookieName, HttpServletResponse response) {
    tokenInjector.invalidateCookie(cookieName, response);
    SecurityContextHolder.clearContext();
  }
}
