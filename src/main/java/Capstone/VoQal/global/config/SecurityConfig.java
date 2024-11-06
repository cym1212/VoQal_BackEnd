package Capstone.VoQal.global.config;

import Capstone.VoQal.global.jwt.filter.JwtAuthFilter;
import Capstone.VoQal.global.jwt.service.JwtProvider;
import Capstone.VoQal.infra.GeoIP.filter.IPAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private static final String GUEST = "GUEST";



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/**").permitAll();

                    //todo
                    //권한별로 엔드포인트 설정하기
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    // 모두
                    auth.requestMatchers(HttpMethod.GET).permitAll();
                    auth.requestMatchers(HttpMethod.POST,"/signup","/reset/password","/login","/find/*","/duplicate/*").permitAll();
                    auth.requestMatchers(HttpMethod.PATCH,"/tokens","/logout").permitAll();
                    auth.requestMatchers(HttpMethod.DELETE,"/delete/member").permitAll();


                    // 게스트
                    auth.requestMatchers(HttpMethod.GET,"/role/coach","/status/check").hasAnyAuthority(GUEST);
                    auth.requestMatchers(HttpMethod.POST,"/role/coach","/request").hasAnyAuthority(GUEST);
                    auth.requestMatchers(HttpMethod.PATCH,"/*/change-nickname").hasAnyAuthority(GUEST);
                    auth.requestMatchers(HttpMethod.DELETE).hasAnyAuthority(GUEST);

                    // 학생
                    auth.requestMatchers(HttpMethod.GET,"/lessonsongurl/student","/record/student","lessonNote/student").hasAnyAuthority(STUDENT);
                    auth.requestMatchers(HttpMethod.POST,"/chat/room").hasAnyAuthority(STUDENT);
                    auth.requestMatchers(HttpMethod.PATCH).hasAnyAuthority(STUDENT);
                    auth.requestMatchers(HttpMethod.DELETE).hasAnyAuthority(STUDENT);

                    // 코치
                    auth.requestMatchers(HttpMethod.GET,"/request","/student","/lessonsongurl","/record","/lessonNote").hasAnyAuthority(COACH);
                    auth.requestMatchers(HttpMethod.POST,"/reject","/approve","/lessonsongurl","/create/record","/create/note","/chat/room/*").hasAnyAuthority(COACH);
                    auth.requestMatchers(HttpMethod.PATCH,"/lessonsongurl/*","/record/*","/lessonNote/*").hasAnyAuthority(COACH);
                    auth.requestMatchers(HttpMethod.DELETE,"/*","/lessonsongurl/*","/record/*","/lessonNote/*").hasAnyAuthority(COACH);

                    // 학생 코치 둘다
                    auth.requestMatchers(HttpMethod.GET,"/liked","/count/likes","/user/details","/reservation","/reservation/*","/available-times","lessonNote/*","/chat/*/messages","/challenge","/challenge/*").hasAnyAuthority(STUDENT, COACH);
                    auth.requestMatchers(HttpMethod.POST,"/firebase-token","/**/like","/reservation","/fcm/token","/fcm/send","/chat/*/messages","/challenge").hasAnyAuthority(STUDENT, COACH);
                    auth.requestMatchers(HttpMethod.PATCH,"/reservation/*","/challenge/*").hasAnyAuthority(STUDENT, COACH);
                    auth.requestMatchers(HttpMethod.DELETE,"/*/unlike","/reservation/*","/challenge/*").hasAnyAuthority(STUDENT, COACH);


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