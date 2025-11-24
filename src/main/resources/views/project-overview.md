### 11월 6일 — HttpServer 기본 구현

- **작업**
    - `HttpServer` 클래스 작성. `ServerSocket`으로 소켓 열고, `BufferedReader`로 요청 라인 읽음.
    - HTTP 응답 생성: 상태 라인, 헤더, 바디를 문자열로 조합해서 보냄.
    - 멀티스레드 처리: `ExecutorService` 고정 스레드 풀을 사용해서 동시에 여러 클라이언트 처리.
- **문제점 / 고민**
    - 응답이 매번 고정된 HTML이어서 사실상 “Hello, SimpleServer” 느낌.
    - HTTP 상태나 바디를 바꾸려면 코드 직접 수정해야 해서 유연성이 떨어짐.
- **해결 방식**
    - `ServerConfig` 클래스에 포트, 문자셋, 기본 바디 같은 설정 상수를 분리함.
    - `HttpResponse`를 Builder 패턴으로 변경해서 상태(status), 헤더(headers), 바디(body)를 유연하게 구성할 수 있게 설계.

**참고한 자료**

- [Baeldung – Simple HTTP Server with ServerSocket](https://www.baeldung.com/java-serversocket-simple-http-server)
- [Oracle Java Tutorials – Client/Server Sockets](https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html)
- [Medium – Building an HTTP Server from Scratch in Java](https://medium.com/@shishir-karki/building-an-http-server-from-scratch-in-java-what-i-learned-by-rebuilding-the-web-442c772336bb)
- https://tomcat.apache.org/tomcat-9.0-doc/index.html
- https://tomcat.apache.org/tomcat-9.0-doc/architecture/overview.html?utm_source=chatgpt.com

### 11월 7일 — HttpResponse Builder 패턴 적용

- **작업**
    - `HttpResponse.Builder` 구현: `status(int)`, `headers(String)`, `body(String)` 메소드 제공.
    - `toRawResponse()`로 “HTTP 상태라인 + 헤더 + 빈 줄 + 바디” 형태 문자열 생성.
- **이유**
    - 응답 구성을 더 유연하게 하고, 테스트나 향후 확장 시 편리하게 변경 가능하게 만들고 싶었음.
- **참고**
    - https://www.cloudflare.com/ko-kr/learning/ddos/glossary/hypertext-transfer-protocol-http/
    - https://www.geeksforgeeks.org/html/what-is-http/

### 11월 10일 — ApplicationContext 기본 구현

- **작업**
    - `ApplicationContext` 생성: `beanMap`을 이용해 Bean 객체를 보관.
    - `registerBean(name, Class)` 및 `registerBean(name, Object)` 구현.
    - `getBean(name)`과 `getAllBeans()` 제공.
- **고민**
    - 매번 Bean을 수동으로 등록하는 것은 번거롭고 실수할 가능성 있음.
    - 자동으로 패키지를 탐색해서 `@Controller`, `@Service` 붙은 클래스를 등록하는 기능이 필요.
- **결정**
    - 어노테이션 기반 자동 Bean 스캔 기능을 다음 단계로 추가하기로 계획
- **참고**
    - https://www.geeksforgeeks.org/java/spring-applicationcontext/
    - https://kyu-nahc.tistory.com/entry/Spring-boot-Application-Context
    - https://kyu-nahc.tistory.com/entry/Spring-boot-Application-Context
    - https://blog.naver.com/hello_world_study/222928828027
    - https://www.geeksforgeeks.org/springboot/beanfactory-vs-applicationcontext-in-spring/
    - https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html

### 11월 12일 — 어노테이션 추가 (`@Controller`, `@GetMapping` 등)

- **작업**
    - `spring.annotation` 패키지 생성.
    - `@Controller`, `@Service`, `@Repository`, `@GetMapping` 정의.
    - 리플렉션으로 어노테이션 붙은 클래스를 탐색하여 Bean으로 등록 준비.
- **이유**
    - Spring처럼 어노테이션 기반으로 구조를 잡으면 코드가 깔끔하고 유지보수가 용이.
    - 수동 등록 없이 자동 Bean 등록 가능.
- **참고**
    - [https://inpa.tistory.com/entry/JAVA-☕-누구나-쉽게-배우는-Reflection-API-사용법](https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EB%88%84%EA%B5%AC%EB%82%98-%EC%89%BD%EA%B2%8C-%EB%B0%B0%EC%9A%B0%EB%8A%94-Reflection-API-%EC%82%AC%EC%9A%A9%EB%B2%95)
    - https://www.geeksforgeeks.org/java/reflection-in-java/
    - https://www.geeksforgeeks.org/springboot/spring-boot-annotations/
    - https://www.geeksforgeeks.org/springboot/spring-boot-annotations/


### 11월 13일 — HandlerMapping & HandlerAdapter 구현

- **작업**
    - `HandlerMapping`: URL 경로 ↔ `HandlerMethod` 매핑(`Map<String, HandlerMethod>`).
    - `HandlerMethod`: Bean 인스턴스 + 메소드(`Method`) 저장.
    - `HandlerAdapter`: `handlerMethod.invoke(...)`로 실제 컨트롤러 메소드 호출.
- **문제 / 고민**
    - 단순 `@GetMapping`만 지원할지, 아니면 확장된 `@RequestMapping`처럼 할지.
    - HTTP 메서드 구분, 파라미터 바인딩 등은 추후 고려
- **참고**
    - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/HandlerMapping.html
    - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/HandlerAdapter.html


### 11월 17일 — DispatcherServlet 구현

- **작업**
    - `DispatcherServlet` 클래스 작성, `service(HttpRequest request)` 구현.
    - `HandlerMapping` → `HandlerAdapter` → `HttpResponse` 변환.
    - 예외 처리: `ExceptionResolver` 인터페이스 + 기본 구현 `DefaultExceptionResolver`.
- **문제 / 고민**
    - `String` 반환 시 뷰 이름인지 단순 문자열인지 결정 필요.
    - 뷰 렌더링(템플릿, 정적 파일 포함) 위치 결정 필요.
- **결정**
    - `String` 리턴은 기본적으로 뷰 이름으로 처리.
    - 추후 `ModelAndView` 지원 가능하게 설계.
- **참고**
    - https://mangkyu.tistory.com/18
    - https://www.geeksforgeeks.org/java/what-is-dispatcher-servlet-in-spring/

### 11월 18일 — ViewResolver & Model 구현

- **작업**
    - `Model`: `Map<String, Object>` 기반, `addAttribute()` 제공.
    - `ViewResolver`: `viewPathPrefix` + `viewPathSuffix`로 파일 경로 결정.
    - `${key}` placeholder 치환.
    - Markdown 지원: `.md` 파일이면 `<pre>`로 감싸 HTML 반환.
- **문제 / 고민**
    - 파일 읽기: `FileReader` vs 클래스패스 리소스.
    - JAR 패키징 시 `.md` 경로 문제.
- **참고**
    - https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/viewresolver.html

### 11월 21일 — 테스트 코드 작성

- **작업**
    - `HttpServerTest`: GET `/` 요청, 상태라인 확인.
    - `HttpServerThreadPoolTest`: 동시 클라이언트 요청 → 스레드 풀 동작 확인.
- **문제 / 고민**
    - `DispatcherServlet` 연결 전이라 통합 테스트 부족.
- **해결**
    - 우선 스레드/상태라인 동작 검증.
    - 추후 `DispatcherServlet` 연결 후 통합 테스트 보강.

### 11월 23일 — MarkdownController 추가 및 통합

- **작업**
    - `MarkdownController`: `@GetMapping("/")` → `"project-overview.md"` 반환.
    - `Main`: `HttpServer(dispatcherServlet)` 생성, 서버 기동.
- **결과**
    - IDE 실행 시 `/` 요청 → Markdown 렌더링 정상.
- **문제**
    - JAR 실행 시 404 또는 기본 바디 반환.
    - 원인: ApplicationContext 스캔 문제, ViewResolver 파일 접근 방식 문제.
- **참고**
    - [StackOverflow – Listing files in a JAR](https://stackoverflow.com/questions/58618956/listing-and-reading-all-files-in-a-java-package-within-a-jar-file-not-working)

### 11월 24일

- **문제 원인 분석**
    1. ApplicationContext 클래스 스캔: File 기반 → JAR 내부 클래스 미인식 → HandlerMethod 미등록 → 404 발생.
    2. ViewResolver 파일 읽기: FileReader 기반 → JAR 내부 Markdown 파일 미인식.
- **해결 접근 방식**
    1. ApplicationContext → JAR 대응 (`JarURLConnection` + `JarFile`).
    2. `scanJarEntries(...)`로 JAR 내부 클래스 열거.
    3. ViewResolver → 클래스패스 스트림(`getResourceAsStream`) 사용.
    4. 메소드 깊이 줄이고, 기능별로 분리.
- **참고 자료**
    - [Oracle Docs – JarURLConnection](https://docs.oracle.com/javase/8/docs/api/java/net/JarURLConnection.html)
    - https://m.blog.naver.com/pureb612b/10109980892
    - https://www.geeksforgeeks.org/java/class-getresourceasstream-method-in-java-with-examples/