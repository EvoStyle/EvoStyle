# EvoStyle

## 🚀 서비스 소개
### ✨ 서비스 개요

<aside>

### evoStyle은 무신사의 구조를 참고하여 구축한 이커머스 서비스입니다
DDD 기반의 설계로 상품, 카테고리, 장바구니, 결제 등의 전반적인 커머스 흐름을 구현하여 실무와 유사한 도메인 설계 및 트랜잭션 안정성 확보에 중점을 두었습니다.
</aside>

--- 

### 🔑 핵심 기능

<aside>

#### 🔍 ** 회원, 비회원 장바구니 **

redis를 사용한 비회원, 회원 장바구니 관리</br>
로그인 시 자동 동기화 및, 병합</br>
장바구니 상품 개수/옵션 변경, 삭제 기능 지원</br>
장바구니에서 바로 주문 페이지로 이동 가능</br>

#### 🔥 **유연하고 명확한 배송 관리 기능**
주문 시 배송 정보 입력 가능 (주소, 연락처, 배송 메시지)</br>
배송 상태 관리: 배송준비중 → 배송중 → 배송완료 단계로 자동 변경</br>
배송 메시지는 관리자가 확인 가능하며, 알림 시스템과 연동 가능</br>


#### 🔖 **주문 & 결제**
장바구니에 담긴 상품을 한 번에 주문 가능</br>
주문 후 결제 성공 시, 주문 상태 및 상품 재고 업데이트</br>
Kafka 등 메시지 브로커를 통한 후속 처리 분리로 안정성 확보</br>
</aside>

--- 

## ⚡ 성능 개선, 어디까지 해봤니?

<details>
  <summary> <strong>🏎️ 성능 개선 1 제목</strong> </summary>

### 환경


### 비교


### 결론

</details>

<details>
  <summary> <strong>🏎️ 성능 개선 2 제목</strong> </summary>

###  환경


### 비교 


### 결론

</details>

</aside>

---

<aside>

## 🏗️ System Architecture


### 📝 **Wireframe**
<img width="6816" height="2033" alt="Image" src="https://github.com/user-attachments/assets/b4e13d55-369a-4adf-bd76-f751dff20bc1" />

### 💬 **ERD**
![ERD]([https://your-image-url.com/erd.png](https://www.erdcloud.com/d/dML5sNLqBBCgrp4iR))  

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
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white) ![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=flat&logo=apache-kafka&logoColor=white)


 
**🔐 Security**     
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
 
**📟 Test**    
![JMeter](https://img.shields.io/badge/JMeter-D20C0E?style=for-the-badge&logo=jmeter&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) 

 
**👥 Collaboration Tool**     
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) ![Figma](https://img.shields.io/badge/Figma-000000?style=for-the-badge&logo=figma&logoColor=white)

---

</aside>

<aside>
  
## 🔧 기술적 의사결정

<details>
  <summary> <strong>💡 비동기처리를 통한 결제 후속작업 분리</strong> </summary>
 
### 요구사항
후속 로직과 결제 흐름을 분리하여 시스템의 안정성을 확보할 것</br>
장애 전파 방지 및 재처리 가능성 확보</br>
확장성과 유지보수 용이성 고려</br>

###  고려한 대안

| 비교 항목   | 동기 처리 방식                    | 메시지 브로커 도입 (Kafka)             |
| ------- | --------------------------- | ------------------------------ |
| 결합도     | ❌ 높음 (결제와 후속 로직 강하게 연결)     | ✅ 낮음 (결제 성공 후 이벤트로 분리)         |
| 장애 전파   | ❌ 모든 로직 실패 시 전체 트랜잭션 실패 가능  | ✅ 한 로직 실패 시 격리 및 재처리 가능        |
| 확장성     | ❌ 신규 후속 로직 추가 시 기존 코드 수정 필요 | ✅ 새로운 Consumer만 추가하면 됨         |
| 재처리 유연성 | ❌ 거의 없음 (트랜잭션 실패 시 롤백 외 없음) | ✅ Dead Letter Queue 등으로 재처리 가능 |
| 구조 명확성  | ⚠️ 한 컨트롤러 내에 많은 역할이 섞임      | ✅ 각 책임이 독립되어 구조 명확             |

 
    
### 결정 및 근거
-메시지 브로커 도입결정</br>
->결제 흐름의 안정성을 확보하고, 후속 로직에서의 예외가 전체 결제 실패로 이어지지 않도록 하기 위함</br>
->비동기 메시지 처리 구조를 통해 장애 전파를 방지하고, 이벤트 기반 확장 구조를 설계할 수 있음</br>


</details>
<details>
    <summary><strong>💡 기술적 의사결정 제목2</strong> </summary>

### 요구사항

### 고려한 대안

 
</details>

</aside>

---

## 🚨 트러블슈팅

<details>
<summary><strong>🔩  외부 API & 내부 DB 정합성을 위해 분산 트랜잭션 도입</strong> </summary> 

### 문제 정의
결제 완료 시점에 외부 결제 서비스(Toss API)로부터 결제 성공 응답을 받은 후, 내부 시스템(DB)에 주문 상태 변경, 회원 포인트 적립 등의 로직을 처리하는 과정에서 **일부 후속 로직이 실패**하는 문제가 발생할 수 있음.</br>
이로 인해 실제 결제는 성공했지만 주문 상태는 '실패'로 남는 **정합성 불일치** 상황 발생.

### 원인 파악
- 외부 API(Toss)와 내부 DB는 **서로 다른 트랜잭션**으로 작동함.
- Toss 결제 컨펌 API 호출 → 결제 성공  
  → 이후 내부에서 주문 상태 변경, 포인트 적립 등의 후속 로직이 실행됨.
- 이때 DB 예외가 발생하면, **외부 결제는 성공했지만 내부 처리는 실패**하게 됨.
- 기존 구조에서는 이 모든 로직을 **동기/단일 트랜잭션**으로 처리하고 있어, 장애 시 **롤백 불가하거나 전체 실패** 처리됨.

### 해결 과정
- 외부 API 호출과 내부 로직을 **분리 처리**하기 위해 파사드 패턴 도입
- 결제 성공 이후에는 **Toss 결제 정보만 저장**하고,  
  후속 로직(주문 상태 변경, 포인트 적립 등)은 **메시지 브로커(Kafka)**를 통해 **비동기 이벤트로 분리**
- 후속 이벤트는 별도 Consumer가 처리하며, 예외 발생 시 **재시도 전략 및 장애 복구 대응 가능**
- 분산 트랜잭션 패턴 중 **Saga 패턴(선처리 → 보상 트랜잭션)** 형태로 설계  
  - Toss API 호출 전, 내부 상태를 선처리 후 임시 상태 저장  
  - Toss API 실패 시, 내부 처리 롤백 

### 결과
- 외부 결제 성공 여부와 내부 로직 실행을 분리함으로써 **시스템 결합도를 낮춤**
- 후속 로직 실패 시에도 **결제 상태를 정확히 유지하면서 유연하게 보상 처리** 가능
- 시스템 안정성 및 정합성 확보  
- 장애 전파 방지 및 **트랜잭션 길이 단축으로 성능 향상**

</details>

<details>
<summary><strong>🔩  트러블슈팅 제목2</strong></summary> 

### 문제 정의

### 원인 파악


### 해결 과정

### 결과

</details>


<details>
<summary><strong>🔩  트러블슈팅 제목3</strong> </summary> 

### 문제 정의

### 원인 파악


### 해결 과정

### 결과
 
</details>

</aside>

---


<aside>

## 🤝 **팀 구성원 소개**
| **구성원** | **직책**  | **장점**                                       | **GitHub**                                | **Blog**                                      | **담당**                                                                                                                                                           |
|---------|---------|----------------------------------------------|-------------------------------------------|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **이승찬** | `👑팀장`  | `🎨아이디어 뱅크`<br> `❄️냉철한 시선`<br> `😄즐거움의 달인`   | [tmdcksdl](https://github.com/tmdcksdl) | [블로그](https://carrot0911.tistory.com)         | 📌 인증,인가<br>📌 즐겨찾기 CRUD<br>📌 리뷰 CRUD<br>📌 선착순 할인 쿠폰 CRUD              |
| **신지현** | `🥇팀원` | `🔠오탈자 귀신`<br> `🎧인간GPT`<br> `💯MBTI J 100%` | [backswan0](https://github.com/backswan0) | [블로그](https://writingforever162.tistory.com/) | 📌 주문 CRUD<br>📌 브랜드 CRUD   <br>📌 카테고리 CRUD       |
| **이수진** | `🎖️팀원` | `🐞버그헌터`<br> `☯️워라밸 마스터`<br> `👦바른어린이`       | [jin18302](https://github.com/jin18302)   | [블로그](블로그 주소)         | 📌 상품crud<br>📌 옵션, 옵션 crud <br>📌 결제 연동및 crud<br>📌 회원, 비회원 장바구니 crud<br> 📌 좋아요 crud                 |
| **한성우** | `🏅팀원`  | `👂경청의 대가`<br> `🏆노력 끝판왕`<br> `✍️글의 조각가`     | [아이디](깃허브 주소)   | [블로그](블로그 주소)         | 📌 배송 crud<br>📌 알림<br> |
