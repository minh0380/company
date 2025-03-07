package kr.co.company.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kr.co.company.jwt.JwtAuthenticationFilter;
import kr.co.company.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtTokenProvider jwtTokenProvider;
	
	public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Rest API 이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        // 해당 API에 대해서는 모든 요청을 허가
                        .requestMatchers("/h2-console/**"
                        		, "/swagger.html"
                        		, "/swagger-ui/index.html"
                        		, "/api-docs/**"
                        		, "/check/**"
                        		, "/api/member/id-check"
                        		, "/api/member/join"
                        		, "/api/member/sign-in")
                        .permitAll()
                        // ROLE_USER 권한이 있어야 요청할 수 있음 -> prefix로 'ROLE_'이 있어야 하는 점 유의
                        .requestMatchers("/api/**").hasRole("USER")
                        // 이 밖의 모든 요청에 대해서 인증을 필요로 한다는 설정
                        .anyRequest().authenticated())
                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                // h2-console 이용을 위해 x-frame-options same origin 허용
                .headers(headers -> headers
                		.frameOptions(frameOptions -> frameOptions
                				.sameOrigin())
                		);
		
		return httpSecurity.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
				.requestMatchers(PathRequest
						.toStaticResources()
						.atCommonLocations())
				.requestMatchers("/swagger-ui/**");
	}

}
