package com.hfu.project_server.security.member_web_api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.hfu.project_server.exception.ApiExceptionFormat;
import com.hfu.project_server.security.exception.JwtInvalidException;
import com.hfu.project_server.utils.ResponseUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JwtVerifyFilter extends OncePerRequestFilter {

    private static final int INVALID_TOKEN = 498;
    private final JwtProperties jwtProperties;
    private String memberEmail;

    public JwtVerifyFilter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenPrefix = jwtProperties.getHeaderPrefix();
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        // 如果Authorization欄位為空或不是從預設的header prefix(Bearer)開始，直接跳過此filter
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(tokenPrefix)) {

            filterChain.doFilter(request, response);

        } else {

            try {
                // 解析jwt
                String jwt = authorizationHeader.substring(tokenPrefix.length());
                JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwtProperties.getSecret().getBytes()).build();
                Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwt);

                // 取得claims中夾帶的信箱，並作為principle放入驗證
                Claims body = claimsJws.getBody();
                String memberEmail = body.getSubject();
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(memberEmail, null, null);

                /*
                    直接在SecurityContextHolder設置security context（夾帶authentication）表示已驗證資訊

                    Security Context Holder不在乎驗證如何進行，只要在上面的登入資訊都被視為已通過驗證。
                 */
                // 直接將此驗證設在context上
                // SecurityContextHolder.getContext().setAuthentication(authentication);
                // 用下面的方法來新建立一個空的context，避免race condition
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);


            } catch (JwtException e) {

                if (memberEmail == null) {
                    ResponseUtils.exceptionWriter(response, HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                    log.warn("Jwt: Failed to Login, unauthenticated/unauthorized user.");
                } else {
                    ResponseUtils.exceptionWriter(response, INVALID_TOKEN, e.getMessage() );
                    log.warn("Jwt: Failed to Login, user: {} ; reason: {}", memberEmail, e.getMessage());
                }
            }

            filterChain.doFilter(request, response);
        }

    }
}
