package com.hfu.project_server.security.member_web_api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfu.project_server.entity.Member;
import com.hfu.project_server.security.member_web_api.DontRedirectSuccessHandler;
import com.hfu.project_server.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 驗證Email+Password並核發Jwt的Filter
 *
 * 當使用者登入時，以信箱密碼登入，登入成功後核發一組Jwt(包含access_token和refresh_token)
 * 之後使用者可持該Jwt，通過JwtVerifyFilter驗證後獲取權限資源。
 */
@Slf4j
public class JwtEmailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOGIN_PATH = "/api/login";
    private final MemberService memberService;
    private final JwtProperties jwtProperties;
    private String email;
    private String password;

    public JwtEmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager, MemberService memberService, JwtProperties jwtProperties) {
        super(LOGIN_PATH, authenticationManager);
        this.memberService = memberService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 如果不是post方法就丟出AuthenticationServiceException
        if (!request.getMethod().equals(POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());

        } else {
            try {
                // 從request的inputStream取得信箱跟密碼
                Map<String, String> requestBody = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                email = requestBody.get("email");
                password = requestBody.get("password");

                // 將信箱跟密碼放入authentication物件
                // Authentication auth = new UsernamePasswordAuthenticationToken(emailAndPassword.getEmail(), emailAndPassword.getPassword());
                Authentication auth = UsernamePasswordAuthenticationToken.unauthenticated(email, password);

                // 將authentication物件交給AuthenticationManager驗證，回傳authentication物件
                // Authentication authenticated = authenticationManager.authenticate(auth);
                return this.getAuthenticationManager().authenticate(auth);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        /* ----- 可在此處自訂驗證成功後執行的方法（如果有自定義successHandler，也可以在它的onAuthenticationSuccess中定義）-----*/
        // 回傳登入成功會員資料
        Member memberDto = memberService.getMemberDtoByEmail(email);

        // 建立Jwt access token 和 refresh token
        String accessToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtProperties.getAccessTokenExpirationDays())))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtProperties.getRefreshTokenExpirationDays())))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();

        // 更新：改將access token放入body
        // (optional) 將access token 加到header內的Authorization屬性，注意add Header需要在寫入body之前完成。
        // response.addHeader(AUTHORIZATION, "Bearer " + accessToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());

        // 建立response body: member資料和refresh token
        /*
            格式：
                {
                    "accessToken": ...,
                    "refreshToken": ...,
                    "member": {
                       ...
                    }
                }
         */
        Map<String, Object> responseBody = Map.of(
                "member", memberDto,
                "access_token", accessToken,
                "refresh_token", refreshToken
                );
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);

        // 繼承父類方法，並將結果綁在SecurityContextHolder上
        // 但因父類方法會調用successHandler的onAuthenticationSuccess，將自動重定向至原本進到login前的url，若無則重定向到首頁（"/"）
        // android端不要重定向，所以需要重新給定一個不會重定向successHandler
        this.setAuthenticationSuccessHandler(new DontRedirectSuccessHandler());
        super.successfulAuthentication(request, response, chain, authResult);

        // response.addHeader("Authorization", "Bearer " + refreshToken);
        log.info("Login Successfully. User email: {}, access token: {}", email, accessToken);
    }
}
