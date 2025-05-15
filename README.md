# Spring-Plus Todo 관리 서비스 리팩토링

## 프로젝트 개요
기존에 만들어진 Todo 관리 서비스에 대해, 2025년 4월 30일부터 2025년 5월 15일까지 각 Level별 요구사항에 맞춰 리팩토링 및 기능을 도입하고 코드를 새로 작성하는 프로젝트입니다.

- **레포지토리**: [https://github.com/HeeMang-Lee/spring-plus](https://github.com/HeeMang-Lee/spring-plus)

## 개발 기간
- **2025-04-30** ~ **2025-05-15**

---

## 주요 기능 및 요구사항

### Level 1: 기본 리팩토링 및 기능 추가

1. **@Transactional의 이해**  
   클래스 레벨에 `@Transactional(readOnly = true)` 적용, 쓰기 메서드에만 `@Transactional` 재정의

2. **JWT의 이해**  
   `User` 엔티티에 `nickname` 필드 추가 및 JWT에서 닉네임 추출 기능 구현

3. **JPA의 이해**  
   `weather` 조건과 기간(수정일) 조건에 따른 검색 기능 JPQL로 구현

4. **컨트롤러 테스트 리팩토링**  
   `TodoControllerTest`에서 상태코드를 OK → BAD_REQUEST로 수정

5. **AOP 개선**  
   `AdminAccessLoggingAspect`의 `@After`를 `@Before`로 변경하여 `changeUserRole()` 호출 시점에 로깅

---

### Level 2: 고급 JPA 및 성능 최적화

6. **JPA Cascade 적용**  
   `CascadeType.PERSIST`를 이용해 Todo 저장 시 작성자를 Manager로 자동 등록

7. **N+1 문제 해결**  
   `Fetch Join`을 활용해 `Comment` 및 `User` 조회 시 N+1 문제 해결

8. **QueryDSL 적용**  
   `findByIdWithUser` 메서드를 QueryDSL로 재구현

9. **Spring Security 도입**  
   기존 Filter 기반 인증을 Spring Security 기반 구조로 전환  
   `@AuthenticationPrincipal` 사용으로 인증 객체 주입 방식 변경

---

### Level 3: 검색 기능 및 트랜잭션 고급

10. **QueryDSL 기반 검색 기능 구현**
    - 제목(keyword), 생성일 범위, 담당자 닉네임 조건으로 검색 가능
    - 필요한 필드만 조회하는 `Projections` 적용
    - 페이징 및 최신순 정렬 구현

11. **트랜잭션 심화 – 로그 테이블 분리 저장**
    - `@Transactional(propagation = REQUIRES_NEW)` 사용
    - 매니저 등록 시도, 성공, 실패 시 각각 로그 테이블에 로그 저장
    - 등록 실패 시 매니저는 롤백되더라도 로그는 항상 커밋됨

---

## 설치 및 실행 방법

### 사전 요구사항
- Java 17 이상
- Gradle Wrapper (로컬에 gradle 설치 불필요)

### 실행
```bash
# 1. 레포지토리 클론
git clone https://github.com/HeeMang-Lee/spring-plus.git
cd spring-plus

# 2. 빌드 및 실행
./gradlew clean build
./gradlew bootRun
```

### 애플리케이션 접속

- 기본 포트: [http://localhost:8080](http://localhost:8080)

---

### MySQL 사용 시 설정 방법

`src/main/resources/application.properties`에 다음 내용을 추가하세요:

```java
spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```


---

## 기술 스택

- **언어**: Java 17
- **프레임워크**: Spring Boot, Spring Data JPA, Spring Security
- **쿼리**: JPQL, QueryDSL
- **인증**: JWT (io.jsonwebtoken)
- **빌드 도구**: Gradle
- **테스트**: JUnit 5, Mockito, Spring MockMvc
- **DB**: H2 (개발), MySQL (운영)
- **기타**: Lombok

---

## 커밋 내역

- **Lv1-1**: 클래스 레벨은 readOnly=true로 두고 쓰기 메서드에만 재정의
- **Lv1-2**: nickname 필드 추가 및 JWT에서 유저 닉네임 추출
- **Lv1-3**: weather 조건별 검색 추가 리팩토링
- **Lv4**: 테스트 코드 OK → BAD_REQUEST로 수정하여 리팩토링
- **Lv5**: AOP 개선 – After를 Before로 수정 후 changeUserRole() 메서드로 코드 수정
- **Lv2-6**: JPA CascadeType.PERSIST 추가로 Todo 저장 시 Manager도 자동으로 Persist
- **Lv2-7**: N+1 문제 Fetch Join으로 한 번의 조회에서 Comment와 연관된 User를 모두 불러와 해결
- **Lv2-8**: QueryDSL 적용해 findByIdWithUser 구현 및 N+1 문제 해결
- **Lv2-9**: Spring Security 적용해서 컨트롤러 및 Config 패키지 리팩토링
- **Lv3-10**: QueryDSL을 사용하여 검색 기능 만들기
- **Lv3-11**: Transaction 심화 (propagation = REQUIRES_NEW)를 활용해 로그 저장

---

## 참고

- 트러블슈팅 : [트러블 슈팅 블로그 링크](https://thishope.inblog.ai/querydsl-q%ED%81%B4%EB%9E%98%EC%8A%A4-%EB%AF%B8%EC%83%9D%EC%84%B1-%EC%98%A4%EB%A5%98-%ED%95%B4%EA%B2%B0-%EB%B0%8F-findbyidwithuser-%EA%B5%AC%ED%98%84%EC%9C%BC%EB%A1%9C-n1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%EA%B8%B0-55379?traffic_type=internal)
