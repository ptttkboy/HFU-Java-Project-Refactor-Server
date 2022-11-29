package com.hfu.project_server.security;

import com.hfu.project_server.security.admin_mvc.AdminUserDetailsService;
import com.hfu.project_server.security.exception.Api403EntryPoint;
import com.hfu.project_server.security.member_web_api.jwt.JwtEmailPasswordAuthenticationFilter;
import com.hfu.project_server.security.member_web_api.jwt.JwtProperties;
import com.hfu.project_server.security.member_web_api.jwt.JwtVerifyFilter;
import com.hfu.project_server.security.member_web_api.MemberUserDetailService;
import com.hfu.project_server.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtProperties jwtProperties;
    private final MemberUserDetailService memberUserDetailService;
    private final AdminUserDetailsService adminUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final MemberService memberService;

    /**
     * 設定api(/api/**)的SecurityFilterChain
     *
     * @param http: httpSecurity，表示對於請求安全性設定。
     * @return httpSecurity
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {

        // EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter = new EmailPasswordAuthenticationFilter(authenticationManager());
        // emailPasswordAuthenticationFilter.setFilterProcessesUrl("/api/login");

        JwtVerifyFilter jwtVerifyFilter = new JwtVerifyFilter(jwtProperties);
        JwtEmailPasswordAuthenticationFilter jwtEmailPasswordAuthenticationFilter = new JwtEmailPasswordAuthenticationFilter(authenticationManager(apiProvider()), memberService, jwtProperties);

        http
                .requestMatchers()
                    .antMatchers("/api/**")
                .and()
                .csrf().disable() // 不設置csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(GET, "/api/v1/restaurants/latest").permitAll()
                .antMatchers(GET, "/api/v1/restaurants/**").authenticated()
                .antMatchers(POST, "/api/v1/members").permitAll()
                .anyRequest().authenticated()
                .and()
                // 自定義驗證 EmailPasswordAuthenticationFilter 替換掉 UserPasswordAuthenticationFilter
                .addFilterAt(jwtEmailPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtVerifyFilter, JwtEmailPasswordAuthenticationFilter.class)
                // 自定 403 物件
                .exceptionHandling()
                    .authenticationEntryPoint(new Api403EntryPoint());

        return http.build();
    }

    /**
     * 設定對管理後台(/admin/**)的SecurityFilterChain
     *
     * @param http: httpSecurity
     * @return httpSecurity
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain adminSecurity(HttpSecurity http) throws Exception {

        http
                .requestMatchers()
                .antMatchers("/admin", "/admin/**")
                .and()
                .authorizeHttpRequests()
                    .anyRequest().authenticated()
                .and()
                // 表單登入
                .formLogin()
                    .loginPage("/admin/login").permitAll()
                    // 預設登入成功後導向 /admin/restaurants
                    .defaultSuccessUrl("/admin/restaurants", true)
                .and()
                // 登出設定
                .logout()
                    .logoutUrl("/admin/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/admin/login")
                .and()
                // 記住我（未包含key加密，預設14天過期）
                .rememberMe()
                .userDetailsService(adminUserDetailsService)
                .and()
                .authenticationManager(authenticationManager(adminProvider()));


        return http.build();
    }

    /**
     *  AuthenticationManager和Provider
     *
     *  api的member和admin都共用此驗證規則
     */
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider){
        return new ProviderManager(authenticationProvider);
    }

    // 認證會員（api）
    public DaoAuthenticationProvider apiProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberUserDetailService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    // 認證管理員（mvc）
    public DaoAuthenticationProvider adminProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;

    }

}
