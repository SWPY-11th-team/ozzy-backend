package com.example.ozzy.common.filter;
import com.example.ozzy.oauth2.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtTokenUtil jwtTokenUtil;

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> authFilter() {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtTokenUtil);

        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthFilter);  // 만든 필터 클래스 등록
        registrationBean.addUrlPatterns("/api/*");  // 필터를 적용할 URL 패턴 지정
        registrationBean.setOrder(0);
        return registrationBean;
    }
}
