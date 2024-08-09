package Capstone.VoQal.global.config;

import Capstone.VoQal.global.jwt.filter.JwtAuthFilter;
import Capstone.VoQal.global.jwt.service.JwtProvider;
import Capstone.VoQal.infra.GeoIP.filter.IPAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final IPAuthenticationFilter ipAuthenticationFilter;

    private static final String COACH = "COACH";

    private static final String STUDENT = "STUDENT";
    private static final String ADMIN = "ADMIN";



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/login", "auth/signup").permitAll();
                    auth.requestMatchers("/**").permitAll();

                    //todo
                    //권한별로 엔드포인트 설정하기
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(ipAuthenticationFilter,JwtAuthFilter.class)
                .logout(Customizer.withDefaults());

        return httpSecurity.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}