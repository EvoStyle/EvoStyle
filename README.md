# EvoStyle

## 🚀 서비스 소개
### ✨ 서비스 개요

<aside>

### EvoStyle은 실제 이커머스 플랫폼의 구조를 참고하여 구축한 이커머스 서비스입니다.
도메인 주도 설계(DDD)를 기반으로 상품, 카테고리, 장바구니, 결제, 쿠폰 등 커머스의 핵심 도메인을 분리하고 각 도메인의 독립성과 확장 가능성을 고려해 시스템을 구축했습니다.</br>
실제 운영 환경을 염두에 두고 트랜잭션 안정성, 동시성 제어, 인증/인가, 쿠폰 발급 정책 등 복잡한 요구사항을 처리할 수 있도록 설계 및 구현에 중점을 두었습니다.

</aside>

--- 

### 🔑 핵심 기능

<aside>

#### 🔐 **인증 & 회원 관리**
- Spring Security 기반 로그인/회원가입 기능 구현</br>
- Access Token + Refresh Token 발급 및 검증</br>
- Redis를 통한 Refresh Token 저장, 자동 로그인 유지 및 세션 무효화 처리</br>
- 로그아웃 시 Redis에서 Refresh Token 제거하여 보안 강화</br>

#### 🔍 **회원 & 비회원 장바구니**
- redis를 사용한 비회원, 회원 장바구니 관리</br>
- 로그인 시 자동 동기화 및, 병합</br>
- 장바구니 상품 개수/옵션 변경, 삭제 기능 지원</br>
- 장바구니에서 바로 주문 페이지로 이동 가능</br>

#### 🔥 **유연하고 명확한 배송 관리**
- 주문 시 배송 정보 입력 가능 (주소, 연락처, 배송 메시지)</br>
- 배송 상태 관리: 배송준비중 → 배송중 → 배송완료 단계로 자동 변경</br>
- 배송 메시지는 관리자가 확인 가능하며, 알림 시스템과 연동 가능</br>

#### 🔖 **주문 & 결제**
- 장바구니에 담긴 상품을 한 번에 주문 가능</br>
- 주문 후 결제 성공 시, 주문 상태 및 상품 재고 업데이트</br>
- Kafka 등 메시지 브로커를 통한 후속 처리 분리로 안정성 확보</br>

#### 🎟️ **선착순 쿠폰 발급 기능**
- 다수 사용자 동시 요청을 고려한 쿠폰 발급 API 구현</br>
- 단일 서버 환경에서는 비관적 락을 적용해 중복 발급 방지</br>
- Redisson 분산 락 적용으로 다중 서버 환경에서도 일관성 유지</br>
- 쿠폰 발급 성공 시 사용자에게 발급 정보 반환</br>

</aside>

---

<aside>

## 🏗️ System Architecture


### 📝 **Wireframe**
<img width="6816" height="2033" alt="Image" src="https://github.com/user-attachments/assets/b4e13d55-369a-4adf-bd76-f751dff20bc1" />

### 💬 **ERD**
<img width="6816" height="2033" alt="Image" src="https://github.com/user-attachments/assets/6e968593-dee4-4c9b-ab2f-fdd3683b4731" />
<img width="50%" alt="Image" src="https://github.com/user-attachments/assets/a7eba96e-9882-4f98-a50e-e87cd601e3f8" />

---

## 📑 **API 명세서**
- [API 명세서](https://docs.google.com/spreadsheets/d/1FRqEAHNsMcFk38itVN7tMhlxURCjNa97qgwoGLnnBuU/edit?gid=0#gid=0&fvid=982380240)

</aside>

<aside>

## 🛠️ 기술 스택                                                                          

**🖥 Language**    
![Java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white)


**📲 Interface Description Language**     
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)

                                                                                                    
**🧑🏻‍💻 Backend**     
![Spring Framework](https://img.shields.io/badge/Spring_Framework-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

 
**🗃 Data Base & Optimization**    
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white) ![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)


 
**🔐 Security**     
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
 
**📟 Test**    
![JMeter](https://img.shields.io/badge/JMeter-D20C0E?style=for-the-badge&logo=jmeter&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) 

 
**👥 Collaboration Tool**     
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) ![Figma](https://img.shields.io/badge/Figma-000000?style=for-the-badge&logo=figma&logoColor=white) ![Canva](https://img.shields.io/badge/Canva-00C4CC?style=for-the-badge&logo=canva&logoColor=white)

---

</aside>

<aside>
  
## 🔧 기술적 의사결정

<details>
  <summary> <strong>💡 비동기처리를 통한 결제 후속작업 분리</strong> </summary>
 
### ✅ 요구사항
- 후속 로직과 결제 흐름을 분리하여 시스템의 안정성을 확보할 것</br>
- 장애 전파 방지 및 재처리 가능성 확보</br>
- 확장성과 유지보수 용이성 고려</br>

### 🔍 고려한 대안

| 비교 항목   | 동기 처리 방식                    | 메시지 브로커 도입 (Kafka)             |
| ------- | --------------------------- | ------------------------------ |
| 결합도     | ❌ 높음 (결제와 후속 로직 강하게 연결)     | ✅ 낮음 (결제 성공 후 이벤트로 분리)         |
| 장애 전파   | ❌ 모든 로직 실패 시 전체 트랜잭션 실패 가능  | ✅ 한 로직 실패 시 격리 및 재처리 가능        |
| 확장성     | ❌ 신규 후속 로직 추가 시 기존 코드 수정 필요 | ✅ 새로운 Consumer만 추가하면 됨         |
| 재처리 유연성 | ❌ 거의 없음 (트랜잭션 실패 시 롤백 외 없음) | ✅ Dead Letter Queue 등으로 재처리 가능 |
| 구조 명확성  | ⚠️ 한 컨트롤러 내에 많은 역할이 섞임      | ✅ 각 책임이 독립되어 구조 명확             |

 
    
### 💡 결정 및 근거
- 메시지 브로커 도입결정</br>
-> 결제 흐름의 안정성을 확보하고, 후속 로직에서의 예외가 전체 결제 실패로 이어지지 않도록 하기 위함</br>
-> 비동기 메시지 처리 구조를 통해 장애 전파를 방지하고, 이벤트 기반 확장 구조를 설계할 수 있음</br>


</details>

<details>
  <summary> <strong>🎟️ 선착순 쿠폰 발급을 위한 동시성 제어 방식 선택</strong> </summary>
 
### ✅ 요구사항
대규모 트래픽에도 중복 발급 없이 정확하게 쿠폰 수량 제어</br>
단일 서버 환경뿐만 아니라 다중 서버 환경에서도 일관된 동시성 처리</br>
사용자 경험을 해치지 않으면서도 성능 저하 최소화</br>

### 🔍 고려한 대안

| 비교 항목   | DB 비관적 락                    | Redisson 분산 락             |
| ------- | --------------------------- | ------------------------------ |
| 서버 확장성     | ❌ 제한적 (단일 서버에서만 안정적)     | ✅ 수평 확장 가능         |
| 처리 성능   | ⚠️ 부하 증가 시 응답 지연 발생 가능  | ✅ 빠르고 경량화된 처리        |
| 동시성 제어 정확성     | ✅ 정확함 | ✅ 정확함         |
| 적용 난이도 | ✅ 비교적 쉬움 | ⚠️ Redis 인프라 및 설정 필요 |
| 시스템 복원력  | ❌ Deadlock 발생 가능     | ✅ 재시도 및 TTL 활용 가능             |

 
    
### 💡 결정 및 근거
- Redisson 분산 락을 도입하여 서버 수평 확장에도 일관된 동시성 제어를 가능하게 함</br>
- DB 기반 락 방식은 단일 서버에서는 안정적이나, 다중 서버 환경 전환 시 구조적 한계가 발생</br>
- Redisson은 TTL 및 재시도 기능을 활용하여 Deadlock 및 리소스 낭비 없이 운영 가능</br>
- 실제 부하 테스트(JMeter)에서 Deadlock 재현 → Redisson으로 해결</br>

</details>

<details>
  <summary> <strong>🔐 인증/인가 토큰 저장소로 Redis 선택</strong> </summary>
 
### ✅ 요구사항
로그인 후 빠른 인증 처리 및 세션 유지</br>
Refresh Token 저장으로 자동 로그인 기능 구현</br>
로그아웃 및 만료 시 토큰 무효화 및 보안 강화</br>

### 🔍 고려한 대안

| 비교 항목   | RDB (MySQL)                    | Redis             |
| ------- | --------------------------- | ------------------------------ |
| 처리 속도     | ❌ 느림 (디스크 I/O)     | ✅ 빠름 (In-Memory)         |
| 세션 무효화 처리   | ⚠️ 구현 복잡  | ✅ TTL로 자동 만료 처리        |
| 확장성     | ⚠️ 증가 시 I/O 병목 발생 | ✅ 대용량 트래픽 처리에 적합       |
| 운영 편의성 | ⚠️ 상태 관리 필요 | ✅ 캐시/세션 전용으로 최적화 |

 
    
### 💡 결정 및 근거
- 인증 서버의 성능 병목을 줄이기 위해 Redis 도입</br>
- TTL 기반 자동 만료 처리로 Refresh Token의 유효성 관리</br>
- 로그아웃 시 Redis에서 Token 삭제 → 보안성 강화</br>
- Spring Security와 Redis를 연계하여 세션 유지와 무효화를 통합적으로 처리</br>

</details>
<details>
  <summary> <strong>💡상품의 옵션관련 테이블 설계</strong> </summary>
 
### ✅ 요구사항
- 하나의 상품은 여러 개의 옵션 그룹을 가질 수 있다.</br>
- 각 옵션 그룹은 여러 개의 옵션 값을 포함한다. </br>
- 옵션 값들의 조합으로 실제 판매 단위인 상세 상품이 구성된다.</br>
- 상세 상품은 고유 ID, 가격, 재고 등의 속성을 가진다.</br>

### 🔍 고려한 대안
| 대안                                     | 설명                                                                   | 장점                     | 단점                        |
| -------------------------------------- | -------------------------------------------------------------------- | ---------------------- | ------------------------- |
| **1안. 상세상품이 옵션 컬럼 포함**               | 상세상품 테이블에 `color`, `size` 등의 컬럼을 직접 포함                                | 구현이 단순하고 직관적임          | 옵션 구조 변경이 불가능함 (유연성 부족)   |
| **2안. 옵션칼럼이 옵션그룹명을 포함**                 |      옵션그룹과 옵션테이블을 하나로 병합       | 테이블 구조 단순, 빠른 구현 가능     | 옵션 그룹/값 관리 비효율     |
| **3안. 정규화된 구조로 구성 (옵션 그룹/값/조합 별도 관리)** | `상품 ↔ 옵션그룹 ↔ 옵션 값 ↔ 중간테이블 ↔ 상세상품`로 구성 | 재사용성, 정렬, 관리 확장성 모두 확보 | 초기 설계와 구현이 복잡함            |

    
### 💡 결정 및 근거
3안 (정규화된 구조)**를 채택
- 다중 옵션 조합을 유연하게 관리 가능</br>
- 옵션 값 재사용 및 상품 간 옵션 공유 가능</br>
- 상세 상품 단위로 재고 및 가격 개별 관리 가능</br>
</details>
</aside>

---

## 🚨 트러블슈팅

<details>
<summary><strong>🔩  외부 API & 내부 DB 정합성을 위해 분산 트랜잭션 도입</strong> </summary> 

### 🔍 문제 정의
결제 완료 시점에 외부 결제 서비스(Toss API)로부터 결제 성공 응답을 받은 후, 내부 시스템(DB)에 주문 상태 변경, 회원 포인트 적립 등의 로직을 처리하는 과정에서 **일부 후속 로직이 실패**하는 문제가 발생할 수 있음.</br>
이로 인해 실제 결제는 성공했지만 주문 상태는 '실패'로 남는 **정합성 불일치** 상황 발생.

### 🧩 원인 파악
- 외부 API(Toss)와 내부 DB는 **서로 다른 트랜잭션**으로 작동함.
- Toss 결제 컨펌 API 호출 → 결제 성공  
  → 이후 내부에서 주문 상태 변경, 포인트 적립 등의 후속 로직이 실행됨.
- 이때 DB 예외가 발생하면, **외부 결제는 성공했지만 내부 처리는 실패**하게 됨.
- 기존 구조에서는 이 모든 로직을 **동기/단일 트랜잭션**으로 처리하고 있어, 장애 시 **롤백 불가하거나 전체 실패** 처리됨.

### 🔧 해결 과정
- 외부 API 호출과 내부 로직을 **분리 처리**하기 위해 파사드 패턴 도입
- 결제 성공 이후에는 **Toss 결제 정보만 저장**하고,  
  후속 로직(주문 상태 변경, 포인트 적립 등)은 **메시지 브로커(Kafka)**를 통해 **비동기 이벤트로 분리**
- 후속 이벤트는 별도 Consumer가 처리하며, 예외 발생 시 **재시도 전략 및 장애 복구 대응 가능**
- 분산 트랜잭션 패턴 중 **Saga 패턴(선처리 → 보상 트랜잭션)** 형태로 설계  
  - Toss API 호출 전, 내부 상태를 선처리 후 임시 상태 저장  
  - Toss API 실패 시, 내부 처리 롤백 

### ✅ 결과
- 외부 결제 성공 여부와 내부 로직 실행을 분리함으로써 **시스템 결합도를 낮춤**
- 후속 로직 실패 시에도 **결제 상태를 정확히 유지하면서 유연하게 보상 처리** 가능
- 시스템 안정성 및 정합성 확보  
- 장애 전파 방지 및 **트랜잭션 길이 단축으로 성능 향상**

</details>

<details>
<summary><strong>🎟️ 쿠폰 발급 동시 요청 시 Deadlock 발생 문제 해결</strong> </summary> 

### 🔍 문제 정의
선착순 쿠폰 발급 기능 구현 후, 많은 사용자가 동시에 쿠폰을 요청하는 상황에서 **데이터베이스 row-level lock 충돌**로 인해 **Deadlock**이 빈번하게 발생하였고,</br>
일부 트랜잭션은 롤백되지 않고 **정합성이 깨진 쿠폰 발급 결과**를 남기는 문제가 발생함.

### 🧩 원인 파악
- 단일 서버 환경에서 DB에 **비관적 락(PESSIMISTIC_WRITE)**을 적용하여 중복 발급을 막고 있었음
- 다수의 사용자가 동일 쿠폰에 동시 접근하면서 **row-level 락 충돌**로 인해 Deadlock 유발
- Deadlock 발생 시, 쿠폰 발급은 일부만 롤백되고 **DB 일관성 불일치** 발생
- 기존 구조에서는 트래픽 증가에 따른 예외 상황 처리가 고려되지 않았음

### 🔧 해결 과정
- **JMeter 부하 테스트**를 통해 실제 Deadlock 발생 구간과 재현 조건을 파악
- 트랜잭션 병목 지점을 분석하고, **쿠폰 발급 로직을 Redis 기반으로 전환**
- Redisson 분산 락을 도입하여 글로벌 락 방식으로 동시성 제어
- Redis의 **TTL 기능과 자동 만료 처리**를 활용해 자원 낭비 없이 락 해제 처리
- 트래픽 상황에서도 **재시도 및 예외 복구**가 가능하도록 구조 개선

### ✅ 결과
- Deadlock 현상이 제거되어 **트래픽 급증 상황에서도 안정적인 발급 처리 가능**
- **쿠폰 발급 데이터 정합성 유지** 및 **중복 발급 방지**
- 수평 확장 환경을 고려한 락 구조로 변경하여, **운영 유연성과 안정성 확보**  
- 쿠폰 발급 로직을 별도 서비스로 분리하여 **유지보수성과 구조 명확성 개선**

</details>

<details>
<summary><strong>🔐 JWT 인증 필터에서 요청이 정상 처리되지 않는 문제 해결</strong> </summary> 

### 🔍 문제 정의
JWT 기반 인증/인가 기능 구현 중, 로그인 및 인증 자체는 성공했지만 **인증 이후의 요청이 Controller에 도달하지 않고 200 OK만 반환**되는 문제가 발생함.</br>
응답은 있지만 실제 비즈니스 로직이 실행되지 않아 **인가 처리가 누락되고, API 응답도 빈 값으로 내려오는 비정상적인 흐름**이 반복되었음.

### 🧩 원인 파악
- Spring Security 필터 체인 내에서 작성한 **JwtAuthenticationFilter**가 정상 동작하지 않음
- 필터 내부에서 **예외가 발생했지만 try-catch가 누락되어**, 예외가 밖으로 전파되지 않고 **응답만 반환된 채 흐름이 끊김**
- FilterChain.doFilter() 호출이 누락되거나, 예외 처리 중간에서 **Controller로 흐름이 넘어가지 않음**
- Spring Security의 **ExceptionTranslationFilter, AuthenticationEntryPoint** 동작 흐름에 대한 이해 부족에서 기인

### 🔧 해결 과정
- 인증 흐름을 처음부터 끝까지 다시 설계
  - JwtUtil → JwtAuthenticationFilter → SecurityConfig 흐름을 점검
- 필터 내 예외 발생 시 적절한 응답 반환을 위해 **예외 처리 핸들러 구성**
  - JwtExceptionFilter 추가로 **토큰 만료, 유효성 검증 실패 등의 케이스에 맞는 에러 처리**
- FilterChain.doFilter() 누락 여부 및 필터 등록 순서도 재검토
- 요청 흐름이 끊기지 않도록 **Spring Security 설정(Config)** 재구성

### ✅ 결과
- **JWT 인증 이후 요청이 정상적으로 Controller까지 도달**하여 API 응답 처리 가능
- 토큰 만료 및 유효하지 않은 토큰 요청에 대해 **명확한 에러 메시지 반환**
- 예외 처리 흐름과 인증 체계가 분리되어 **유지보수성 향상**
- Spring Security 필터 체인에 대한 **구조적 이해도 상승**

</details>
</aside>

---


<aside>

## 🤝 **팀 구성원 소개**
| **구성원** | **직책**  | **장점**                                       | **GitHub**                                | **Blog**                                      | **담당**                                                                                                                                                           |
|---------|---------|----------------------------------------------|-------------------------------------------|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **이승찬** | `👑팀장`  | `👂경청의 대가`<br> `🏆노력 끝판왕`<br> `✍️글의 조각가`   | [tmdcksdl](https://github.com/tmdcksdl) | [블로그](https://carrot0911.tistory.com)         | 📌 인증,인가<br>📌 즐겨찾기 CRUD<br>📌 리뷰 CRUD<br>📌 선착순 할인 쿠폰 CRUD              |
| **이수진** | `🎖️팀원` | `🐞버그헌터`<br> `☯️워라밸 마스터`<br> `👦바른어린이`       | [jin18302](https://github.com/jin18302)   |     블로그     | 📌 상품 CRUD<br>📌 옵션, 옵션그룹 CRUD <br>📌 결제 연동및 CRUD<br>📌 회원, 비회원 장바구니 CRUD<br> 📌 좋아요 CRUD                 |
| **한성우** | `🏅팀원`  |   `🎨아이디어 뱅크`<br> `❄️냉철한 시선`<br> `😄즐거움의 달인`   | [hajoo0322](https://github.com/hajoo0322)   |     블로그     | 📌 배송 CRUD<br>📌 알림<br> |
