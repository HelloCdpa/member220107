package com.icia.member.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration : 설정정보를 스프링 실행 시 등록해줌
@Configuration //인터셉터 아직 안하고 싶으면 /@Configuration 만 주석처리
public class WebConfig implements WebMvcConfigurer {
    //로그인 여부에 따른 접속 가능 페이지 구분
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //체인 메서드
        registry.addInterceptor(new LoginCheckInterceptor()) //만든 LoginCheckInterceptor 클래스 내용을 넘김
                .order(1) // 해당 인터셉터가 적용되는 순서 (몇번째로 적용시켜줄까? 스프링이 우선순위를 배정해줌)
                .addPathPatterns("/**") //해당 프로젝트의 모든 주소에 대해 인터셉터를 적용
                .excludePathPatterns("/","/member/save","/member/login","/member/logout","/css/**"); //그 중에 이 주소는 제외
    }
    
}
