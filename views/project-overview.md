# Java Web Server 프로젝트 개요

## 프로젝트 구조
- **server**: HTTP 서버, HttpRequest/HttpResponse, HttpStatus 등
- **spring**: 간단한 Spring-like DI 컨테이너(ApplicationContext, Bean 등록)
- **spring.web**: DispatcherServlet, HandlerMapping, HandlerAdapter, ViewResolver 등
- **spring.annotation**: @Controller, @Service, @Autowired, @GetMapping 등

## 주요 기능
1. HTTP 서버 기능 구현
2. ThreadPool을 이용한 동시 요청 처리
3. Controller/Service 자동 등록(@Controller, @Service)
4. DispatcherServlet → HandlerMapping → HandlerAdapter
5. ViewResolver + Model 지원
6. ExceptionResolver를 통한 예외 처리

## 참고 소스
- Java HTTP Server 구현 예제
- Spring Framework 구조 참고
- Markdown을 HTML로 렌더링하여 브라우저에 표시