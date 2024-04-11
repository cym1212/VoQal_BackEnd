package Capstone.VoQal.global.config;

import Capstone.VoQal.global.auth.filter.JwtAuthFilter;
import Capstone.VoQal.global.auth.handler.JwtAccessDeniedHandler;
import Capstone.VoQal.global.auth.handler.JwtAuthenticationEntryPoint;
import Capstone.VoQal.global.auth.handler.OAuth2FailureHandler;
import Capstone.VoQal.global.auth.handler.OAuth2SuccessHandler;
import Capstone.VoQal.global.auth.service.CustomOAuth2UserService;
import Capstone.VoQal.global.auth.service.JwtProvider;
import Capstone.VoQal.global.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtProvider jwtProvider;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
                    //ALL
//                    auth.requestMatchers(HttpMethod.GET, "/", "/members/*", "/members/*/posts", "/members/*/place-blocks", "/posts", "/posts/*", "/login/*", "/posts/*/comments", "/members/*/follows", "/members/*/followMembers", "/nickname-duplicate", "/swagger", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
//                    auth.requestMatchers(HttpMethod.PATCH, "/tokens").permitAll();
//                    auth.requestMatchers(HttpMethod.POST, "/app/login/*").permitAll();

                    auth.requestMatchers(HttpMethod.GET, "/members/*", "/posts", "/posts/*", "/login/*", "/posts/*/comments", "/members/*/follows", "/members/*/followMembers", "/nickname-duplicate", "/swagger", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    auth.requestMatchers(HttpMethod.PATCH, "/**","/tokens").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/**","/app/login/*").permitAll();
                    auth.requestMatchers(HttpMethod.DELETE, "/**").permitAll();

                    auth.requestMatchers(HttpMethod.GET, "/test").hasAuthority("GUEST");


                }).csrf(AbstractHttpConfigurer::disable).sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> {
                    oauth2Login.successHandler(oAuth2SuccessHandler);
                    oauth2Login.failureHandler(oAuth2FailureHandler);
                    oauth2Login.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService));
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(userAuthenticationEntryPoint);
                    exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler);
                })
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}