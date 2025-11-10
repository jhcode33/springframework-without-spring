# 🌱 Spring Framework Without Spring

> **“Framework를 사용하지 않고 Framework를 이해하라.”**  
> SpringFramework의 핵심 원리를 직접 구현하며, 웹 애플리케이션의 구조와 동작 방식을 근본부터 이해하는 것을 목표로 한다.

---

## 📌 프로젝트 개요

이 프로젝트는 **SpringFramework를 전혀 사용하지 않고**, 순수 Java로 Spring의 핵심 기능을 직접 구현하는 학습 프로젝트이다.  
최종 목표는 **@Controller, @Service, @Repository, @Autowired 등의 어노테이션 기반 MVC 웹 애플리케이션**을 완성하는 것이다.

---

## 🎯 학습 목표

1. **Spring의 내부 동작 원리 이해**
    - IoC(Inversion of Control)와 DI(Dependency Injection)의 구현 원리
    - DispatcherServlet, HandlerMapping, HandlerAdapter의 요청 처리 과정
    - 어노테이션 기반 Bean 관리 및 Reflection 활용
    - ClassLoader와 Annotation Processing의 실제 동작 이해

2. **Servlet 기반 웹 구조의 이해**
    - HttpServlet의 동작 방식
    - Controller → Service → Repository 계층 구조 직접 설계 및 구현

3. **Spring 핵심 기능 직접 구현**
    - Bean 등록 및 의존성 주입 컨테이너 구현
    - @Annotation 스캐닝 및 Bean 자동 등록 기능
    - DispatcherServlet을 통한 요청 흐름 제어
    - ViewResolver, Model 처리 및 응답 구조 구현

4. **테스트 중심 개발 (TDD)**
    - 각 계층별 단위 테스트 작성
    - 통합 테스트를 통한 기능 검증

---

## 🧩 주요 학습 포인트

- Reflection API를 통한 동적 객체 생성과 메서드 호출
- Annotation Parsing 및 ClassLoader 활용
- Servlet 동작 원리 및 Dispatcher 구조 이해
- Bean Lifecycle, Scope, DI 구조 직접 설계
- Spring MVC의 추상화 구조를 코드로 구현

---

## 🧠 프로젝트 환경 및 기술 스택

| 구분 | 내용 |
|------|------|
| **Language** | Java 21 |
| **Build Tool** | Gradle (의존성 추가 없음) |
| **Framework** | 사용하지 않음 (Pure Java) |
| **Server** | Socket HTTP Server |
| **Test** | JUnit 5, AssertJ |
| **IDE** | IntelliJ IDEA |
| **Docs** | Notion |


---

## 구현 기능 목록
### 1️. 애플리케이션 실행 및 서버 구성

- [ ] `ServerSocket` 기반의 HTTP 서버 직접 구현
    - 요청(Request) 수신 및 응답(Response) 송신
    - 요청 파싱 (HTTP Method, URL, Header, Body 분리)
    - 간단한 HTTP 프로토콜 파서 구현
- [ ] 다중 요청 처리를 위한 `Thread` 또는 `ExecutorService` 기반 구조
- [ ] 클라이언트의 요청 URL에 따라 적절한 Controller 호출

---

### 2️. IoC 컨테이너 구현

- [ ] Bean 등록 및 조회 기능 구현
    - 직접 구현한 `ApplicationContext` 클래스 생성
    - Bean의 이름(Key)과 인스턴스(Value)를 관리하는 Map 구조 설계
- [ ] Reflection을 통한 동적 객체 생성 및 의존성 주입
- [ ] Bean 초기화, 주입 순서, Singleton 관리

---

### 3️. 어노테이션 기반 Bean 관리

- [ ] `@Controller`, `@Service`, `@Repository`, `@Autowired` 어노테이션 직접 구현
- [ ] 지정된 패키지를 스캔하여 어노테이션이 붙은 클래스를 Bean으로 등록
- [ ] `@Autowired` 필드를 찾아 의존성 자동 주입
- [ ] Reflection을 이용해 접근 제어자(private 등) 무시하고 주입 가능하도록 구현

---

### 4️. DispatcherServlet 구조 구현

- [ ] `DispatcherServlet` 역할 클래스 직접 설계
- [ ] URL과 Controller 매핑 관리 (`HandlerMapping`)
- [ ] 요청에 맞는 Controller 메서드를 찾아 실행 (`HandlerAdapter`)
- [ ] Controller 메서드의 리턴 결과를 ViewResolver로 전달

---

### 5️. Controller 및 Handler 구조 구현

- [ ] `@RequestMapping` 어노테이션 구현
    - URL과 메서드를 매핑하는 어노테이션
    - ex. `@RequestMapping(path="/home", method="GET")`
- [ ] 요청 URL → 매핑된 Controller 메서드 실행 흐름 구현
- [ ] 요청 파라미터 매핑 및 메서드 인자 주입
- [ ] Response 데이터 처리 (문자열, JSON 등 단순 응답)

---

### 6️. View 처리 및 Model 구현

- [ ] 간단한 ViewResolver 구현
    - 문자열 기반 템플릿 파일(html, jsp 형태의 텍스트) 반환
    - Model 데이터를 바인딩하여 View 렌더링
- [ ] `Model` 객체 구현 (Map 형태로 key-value 데이터 전달)
- [ ] Controller 리턴값이 View 이름일 경우 ViewResolver를 통해 응답 생성

---

### 7️. 예외 처리 및 공통 로직 구조화

- [ ] 공통 예외 처리 구조 구현
    - Controller 내부 예외를 처리하는 `ExceptionResolver` 구현
- [ ] 404 Not Found, 500 Internal Server Error 응답 생성
- [ ] Logging 기능 구현 (요청, 응답, 예외)

---

### 8. 추가 학습 및 확장 목표

- [ ] AOP의 기본 원리 (Proxy 기반 메서드 실행 전후 로직 주입)
- [ ] 간단한 Bean Lifecycle 이벤트 구현 (init/destroy 메서드 호출)
- [ ] Session 관리 및 Cookie 처리 (선택 구현)
- [ ] 정적 자원 응답 (HTML, CSS, JS 파일 서빙)

---