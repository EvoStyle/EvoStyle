# EvoStyle

## 🚀 서비스 소개
### ✨ 서비스 개요

<aside>

### ""


</aside>

### 🔑 핵심 기능

<aside>

#### 🔍 **기능 1 제목**
  - 
  - 
  - 

#### 🔥 **기능 2 제목**
  - 
  - 
  - 

#### 🔖 **기능 3 제목**
  - 
  -
  - 
  
<aside>

--- 

## ⚡ 성능 개선, 어디까지 해봤니?

<details>
  <summary> <strong>🏎️ 성능 개선 1 제목</strong> </summary>

### 환경
- **Elasticsearch 버전**: 8.17.2
- **QueryDSL**: MySQL을 이용한 데이터 조회
- **테스트 도구**: Apache JMeter
- **테스트 요청**: HTTP GET 요청
- **동시 요청**: 200개 (100개씩 총 2번 요청)
- **서버 환경**: 로컬 서버 (localhost)
- **클라이언트 환경**: JMeter 클라이언트

### 비교
| **테스트 항목**    | **MySQL 조회 (QueryDSL)**  | **Elasticsearch 조회**  | **성능 향상률**  |
|---------------|--------------------------|-----------------------|-------------|
| **평균 응답 시간**  | 48ms                     | 14ms                  | 70.83%      |
| **최소 응답 시간**  | 39ms                     | 9ms                   | 77%         |
| **최대 응답 시간**  | 117ms                    | 41ms                  | 65%         |
| **표준편차**      | 9.37ms                   | 3.28ms                | 64%         |
| **TPS**       | 8.63/sec                 | 9.5/sec               | +10.1%      |
| **수신량**       | 8.63KB/sec               | 65.01KB/sec           | +653.5%     |
| **전송된 데이터**   | 0.57KB                   | 3.94KB                | +591.2%     |
| **평균 바이트**    | 6300.9 Byte              | 6971.9 Byte           | +10.7%      |

### 결론
- **응답 시간**: 최소 응답 시간은 77%, 최대 응답 시간은 65% 향상됨
- **표준편차**: 약 64% 향상됨
- **TPS**: 약 10% 증가함
- **수신량 및 전송량**: 수신량은 653.5%, 전송량은 591.2% 증가함
- **대규모 데이터 조회 성능 개선이 필요할 때는 Elasticsearch 사용**
</details>

<details>
  <summary> <strong>🏎️ 성능 개선 2 제목</strong> </summary>

###  환경
- **Elasticsearch 버전**: 8.17.2
- **Elasticsearch QueryDSL**: Elasticsearch에서 데이터를 직접 조회하여 처리
- **테스트 도구**: Apache JMeter
- **테스트 요청**: HTTP GET 요청
- **동시 요청**: 100개 ~ 2100개까지 100씩 증가하며 테스트 (10초로 설정)
- **서버 환경**: 로컬 서버 (localhost)
- **클라이언트 환경**: JMeter 클라이언트

### 비교 
![키바나 모니터링 그래프 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-14%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%208.35.46.png?raw=true)

| **요청 개수**      | **시스템 상태**          | **응답 시간**            | **TPS**             | **기타 영향**       |
|-----------------|-------------------|--------------------|----------------|--------------|
| 🟢 **1600개 이하** | 안정적으로 처리 가능     | ⏳ 일정하게 유지 (8-7ms)  | 📈 일정하게 증가   | -            |
| 🟡 **1600개 이상** | 시스템 자원이 부족해짐   | ⏳ 일정하게 유지 (8-7ms)  | 📉 일부 구간 정체   | 성능 저하 발생  |
| 🔴 **2100개 이상** | 시스템 한계 초과       | ⏳ 느려짐 (11ms)         | 📉 처리량 감소     | 심각한 성능 저하  |

### 결론
- 스레드 수가 증가함에 따라 응답 시간과 TPS가 명확하게 변화함 
- 스레드 수가 1600개일 때까지는 시스템이 원활하게 요청을 처리 
- 스레드 수가 2100개 이상 늘어나면 성능이 저하됨 
- **성능 한계 전에 자원을 효율적으로 관리하고 병목을 예방해야 함**
</details>

</aside>

---

<aside>

## 🏗️ System Architecture
### ☁️ Cloud Architecture

### ⛓️ CI/CD Pipeline

### 📝 **Wireframe**

### 💬 **ERD**

---

## 📑 **API 명세서**
- [API 명세서](링크 첨부하기)

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
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white) ![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)

 
**🔐 Security**     
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)


**🚢 Deployment & Distribution**     
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white) ![EC2](https://img.shields.io/badge/EC2-FF6C37?style=for-the-badge&logo=amazon&logoColor=white) ![Route 53](https://img.shields.io/badge/Route_53-365E02?style=for-the-badge&logo=amazon&logoColor=white) ![Application Load Balancer](https://img.shields.io/badge/Application_Load_Balancer-00A1E4?style=for-the-badge&logo=amazon&logoColor=white) ![ASG](https://img.shields.io/badge/ASG-00B4D6?style=for-the-badge&logo=amazon&logoColor=white) ![RDS](https://img.shields.io/badge/RDS-FF6C37?style=for-the-badge&logo=amazon&logoColor=white) ![ElasticCache](https://img.shields.io/badge/ElasticCache-FF9900?style=for-the-badge&logo=amazon&logoColor=white) ![Certificate Manager](https://img.shields.io/badge/Certificate_Manager-FF9900?style=for-the-badge&logo=amazon&logoColor=white)  ![Github Actions](https://img.shields.io/badge/Github_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white) ![Elastic Cloud](https://img.shields.io/badge/Elastic_Cloud-005571?style=for-the-badge&logo=elasticsearch&logoColor=white) ![Ubuntu Linux](https://img.shields.io/badge/Ubuntu-263238?style=for-the-badge&logo=ubuntu&logoColor=white)  ![WAF](https://img.shields.io/badge/WAF-000000?style=for-the-badge&logo=cloudflare&logoColor=white) 

 
**📟 Test**    
![JMeter](https://img.shields.io/badge/JMeter-D20C0E?style=for-the-badge&logo=jmeter&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) 

 
**👥 Collaboration Tool**     
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) ![Jira](https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=jira&logoColor=white) ![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) ![Figma](https://img.shields.io/badge/Figma-000000?style=for-the-badge&logo=figma&logoColor=white) ![Canva](https://img.shields.io/badge/Canva-00C4CC?style=for-the-badge&logo=canva&logoColor=white) ![ERD Cloud](https://img.shields.io/badge/ERD_Cloud-00B8D9?style=for-the-badge&logo=cloud&logoColor=white)


**📧 Notification Service**    
![SendGrid](https://img.shields.io/badge/SendGrid-00B3E6?style=for-the-badge&logo=sendgrid&logoColor=white) 


**📈 Logging & Monitoring & Analytics**     
![Kibana](https://img.shields.io/badge/Kibana-005571?style=for-the-badge&logo=kibana&logoColor=white) ![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white) ![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white) ![Fluentd](https://img.shields.io/badge/Fluentd-2B64B0?style=for-the-badge&logo=fluentd&logoColor=white) 

---

</aside>

<aside>
  
## 🔧 기술적 의사결정

<details>
  <summary> <strong>💡 기술적 의사결정 제목1</strong> </summary>
 
### 요구사항
- **검색 성능**: 대규모 데이터셋에 대한 빠르고 효율적인 검색을 제공해야 함  
- **한글 텍스트 처리**: 한글 형태소 분석을 통한 정확한 검색 필요  
- **확장성**: 서버의 부하를 분산하고 대규모 트래픽도 원활하게 처리하는 수평 확장이 가능해야 함  
- **자동 완성 및 부분 일치**: 사용자 경험을 향상시킬 수 있는 기능 구현이 가능해야 함  
- **성능 최적화**: 검색 성능을 최적화하여 효율적인 데이터 처리 및 빠른 응답 시간을 보장해야 함  

###  고려한 대안
  | **비교 항목**         | **MySQL**                 | **Elasticsearch**                |
  |-------------------|---------------------------|----------------------------------|
  | **검색 성능**         | ❌ 비효율적, `Full Table Scan` | ✅ 빠르고 효율적, 인덱스 기반                |
  | **한글 텍스트 처리**     | ❌ 비효율적 (형태소 분석 부족)        | ✅ `Nori Analyzer`로 가능함           |
  | **확장성**           | ❌ 수직 확장만 가능               | ✅ 분산 처리로 수평 확장이 가능함              |
  | **자동 완성 및 부분 일치** | ❌ 어려움                     | ✅ 가능                             |
  | **성능 최적화**        | ❌ 제한적                     | ✅ `Ngram`, `Edge-Ngram`으로 최적화 가능 |
    
### 결정 및 근거
**✅ Elasticsearch 선택**
  - 분산형 아키텍처와 인덱싱을 활용해 빠르고 효율적인 검색 성능을 제공함
  - `Nori Analyzer`로 한글 형태소 분석이 가능함
</details>

<details>
    <summary> <strong>💡 기술적 의사결정 제목2</strong> </summary>

### 요구사항
  - **쉬운 관리**: 스레드 풀을 간단하게 관리할 수 있어야 함
  - **사용 편의성**: 설정 및 관리가 직관적이고 복잡하지 않아야 함

### 고려한 대안

| **비교 항목** | **@Async 어노테이션** | **비동기 로직 직접 구현**                                                      |
| --- | --- |-----------------------------------------------------------------------|
| **관리 난이도** | ✅ 설정이 간단하고 스프링이 관리 | ❌ 별도의 스케줄러 관리 및 설정 필요                                                 |
| **사용 편의성** | ✅ 직관적이고 간편한 설정 | ❌ 설정과 관리가 다소 복잡할 수 있음                                                 |
| **장점** | 🌟 직관적이고 가독성이 좋음 | 🌟 우리가 원하는 대로 비동기 처리 가능                                               |
| **단점** | ⚠️ 별도 설정 없으면 매번 새로운 스레드가 생성되어 자원 낭비 가능 | ⚠️ 초기 설정이 복잡하고 관리가 어려움 <br> ⚠️ 비즈니스 로직과 같은 선상에 로직이 추가되어 가독성이 떨어질 수 있음 |

### 결정 및 근거
✅ **`@Async` 어노테이션 선택**
  - 직관적이고 가독성이 좋음  
  - 스프링이 내부적으로 스레드 풀을 관리하여 직접 관리할 필요가 없고, 관리 부담이 적음  
  - 다른 비동기 작업에도 어노테이션을 사용해 쉽게 비동기 처리 가능
</details>


<details>
    <summary><strong>💡 기술적 의사결정 제목3</strong> </summary>

### 요구사항
  - **사용자 검색어 저장**: 입력한 검색어를 저장하고 빠르게 조회할 수 있도록 관리해야 함
  - **최신 검색어 조회**: 사용자 별 최근 검색어를 최대 10개까지 최신순으로 정렬하여 제공해야 함
  - **빠른 응답 속도**: 대량의 요청에도 즉각적으로 검색어를 반환할 수 있어야 함
  - **불필요한 데이터 관리**: 오래된 검색어는 자동으로 제거하여 불필요하게 축적되지 않도록 해야 함

### 고려한 대안

  | **비교 항목**    | **MySQL 인덱스 적용**                          | **Redis 적용**                                 |
  |--------------|-------------------------------------------|----------------------------------------------|
  | **저장 방식**    | 테이블을 사용하여 인덱스로 검색 최적화                     | Key-Value 방식으로 사용자별 검색어 관리                   |
  | **조회 방식**    | ❌ 인덱스를 활용하여 빠르게 조회가 가능하지만, 요청 증가 시 부하 증가  | ✅ 메모리 기반 저장으로 매우 빠른 조회 속도 제공                 |
  | **데이터 관리**   | ❌ 불필요한 데이터는 수동으로 정리 필요                    | ✅ 최신 10개만 유지하며 초과 데이터는 자동 삭제                 |
  | **데이터 최신성**  | ✅ 실시간 데이터 반영 가능                           | ✅ TTL 설정으로 자동 만료 관리 가능                       |
  | **시스템 부하**   | ❌ 검색 요청 증가 시 데이터베이스 부하 증가                 | ✅ 데이터베이스 부하를 최소화하면서 빠른 응답 제공                 |
  | **확장성**      | ❌ 수직 확장(Scale-Up) 필요, 분산 환경에서 성능 저하 가능    | ✅ 수평 확장(Scale-Out) 가능, 분산 환경에서도 안정적으로 운영 가능  |
    
### 결정 및 근거
**✅ Redis 적용을 선택**
  - 메모리 기반 저장소로 빠른 데이터 조회 및 검색 가능
  - TTL 기능을 활용하여 일정 시간이 지나면 데이터를 자동으로 삭제하여 저장 공간을 효율적으로 활용 가능 
  - Sorted Set 자료구조를 활용하여 검색어를 삽입할 때 자동 정렬 및 중복 제거 가능
  - 수평 확장이 가능하여 대량의 요청을 안정적으로 처리
  - 분산 환경에서도 높은 성능과 안정성을 유지 가능
</details>

</aside>

---

## 🚨 트러블슈팅

<details>
<summary><strong>🔩  트러블슈팅 제목 1</strong> </summary> 

### 문제 정의

- Elasticsearch를 도입한 다음, 스레드를 5,000개로 설정했을 때 평균 응답 속도는 단축되었으나, TPS는 오히려 감소함

| **비교 항목**  | **Elasticsearch 적용 전**  | **Elasticsearch 적용 후**  | **배율**       |
|------------|-------------------------|-------------------------|--------------|
| **응답 속도**  | 28ms                    | 7ms                     | **4배 단축**    |
| **TPS**    | 249.1건/sec              | 35.1건/sec               | **7.1배 감소**  |

![초당처리량 감소 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-16%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%207.22.01.png?raw=true)

### 원인 파악
- 너무 많은 동시 요청이 들어와서 CPU와 메모리 같은 자원이 한계에 도달하는 병목 현상이 발생함

### 해결 과정
- 스레드 수를 5,000개에서 200개로 줄인 다음 테스트를 다시 진행함

### 결과
- 스레드 수를 200개로 조정하여 성능이 안정되면서 TPS가 증가함

![초당처리량 증가 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%207.14.59.png?raw=true)

| **비교 항목**  | **Elasticsearch 적용 전**  | **Elasticsearch 적용 후**  | **배율**       |
|------------|-------------------------|-------------------------|--------------|
| **응답 속도**  | 48ms                    | 14ms                    | **3.4배 단축**  |
| **TPS**    | 1.4건/sec                | 9.5건/sec                | **6.8배 증가**  |

</details>

<details>
<summary><strong>🔩  트러블슈팅 제목2</strong></summary> 

### 문제 정의
인스턴스 내부 애플리케이션별로 스케줄러가 실행되는데, 다중 인스턴스 환경이라면 이메일 알림 발송 스케줄러가 동시에 실행될 수 있는 위험 존재

### 원인 파악
- 애플리케이션에 등록된 스케줄러가 인스턴스의 개수만큼 실행됨

![스크린샷 2025-03-12 오후 5.19.12.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%205.19.12.png?raw=true)

### 해결
- **Redis 활용**
    - 스케줄링 실행 자체를 ‘단일 인스턴스 환경’에 맡겨서, 중앙에서 제어하는 방식이 필요
    - Redis를 이용하여 작업들을 queue에 넣고, 하나의 인스턴스만 실행되는 구조로 변경
    - 스케줄링을 짧은 주기로 확인해야 하는 만큼, 빠른 읽기 I/O가 요구되는 구조이므로 Redis 사용
- **다중 인스턴스용 스케줄러 아키텍처 적용**
  
![스크린샷 2025-03-12 오후 5.38.37.png](https://github.com/llRosell/sparta/blob/main/%E1%84%83%E1%85%A1%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%89%E1%85%B3%E1%84%90%E1%85%A5%E1%86%AB%E1%84%89%E1%85%B3%20%E1%84%89%E1%85%B3%E1%84%8F%E1%85%A6%E1%84%8C%E1%85%B2%E1%86%AF%E1%84%85%E1%85%A5%20%E1%84%83%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%A5%E1%84%80%E1%85%B3%E1%84%85%E1%85%A2%E1%86%B7.png?raw=true)

- **누구나 쉽게 사용할 수 있도록 인터페이스 구현**
</details>


<details>
<summary><strong>🔩  트러블슈팅 제목3</strong> </summary> 

### 문제 정의
- ‘오류 이전의 데이터는 저장이 되었으려나?’ 하였지만 반복문 전체에 트랜잭션을 걸어버린 탓에 데이터가 모두 유실됨
 
### 원인
- 페이지를 순회하는 `forEach`문 전체를 한 트랜잭션으로 감싸버림  
  - 오류 원인 자체는 트랜잭션과 상관없지만, 오류에 대응할 수 있는 코드가 필요해짐  
- 긴 트랜잭션은 다양한 문제를 일으킬 수 있었음  
  - 긴 트랜잭션은 데이터에 오랜 시간 락을 유지하여 동시성 저하와 처리량 감소 가능성 존재  
  - 대기 중인 트랜잭션들은 여전히 시스템 리소스를 점유함
  - 긴 트랜잭션이 더 많은 자원을 더 오래 점유하기 때문에 데드락이 발생할 수 있음
  - 긴 트랜잭션이 많은 수의 행에 락을 설정해서 락 에스컬레이션 발생 확률이 높아짐
        
### 해결
- 트랜잭션 전파 속성을 사용함
- forEach문 내부의 반복되는 코드를 메서드로 분리
- 트랜잭션 전파 속성 중 하나인 `REQUIRES_NEW` 사용
- 페이지마다 새로운 트랜잭션이 생성되도록 리팩토링 진행
  - 한 페이지에서 예외 발생 시 해당 페이지의 작업만 롤백되고 다른 페이지의 크롤링 작업은 정상적으로 진행되도록 함
</details>

</aside>

---

<aside>

## 🏦비즈니스적 의사결정

<details>
<summary><strong>💰 비즈니스적 의사결정 제목 1</strong></summary>
    
### 문제 정의
- 현재 서비스의 회원가입 및 로그인 절차가 너무 간단하여 누구나 쉽게 가입 및 로그인 가능 
- 서비스에서 사용자의 나이와 경력처럼 민감한 정보를 다루기 때문에, 무분별한 아이디-비밀번호 입력 시도를 방지하는 보안 조치가 필요함

### 설계 및 구현
- 이메일 알림 인증을 받은 사용자에게만 이메일 알림을 발송하도록 개선
- 이메일 서비스 남용 방지 차원에서 한 이메일 당 하루 최대 3번으로 제한
- 인증코드 실패 횟수 체크 및 3회 이상 실패 시 코드 무효화
- 밴 당한 IP는 모든 API를 사용할 수 없도록 전체 차단
- 아이피 밴
  - 한 IP에서 4개 이상의 계정에 로그인 시도 시 밴, 시도 실패 정보는 Redis에 저장
  - IPv6을 우선적으로 수집
  - 공유 네트워크를 고려하여 30초만 Redis에 밴 정보 저장
- 이메일 밴
  - 3일 내에 로그인 5회 이상 실패하면 해당 이메일 밴
  - 실패 정보는 Redis, 밴 정보는 DB에 저장

### 결과
- 이메일 인증으로 회원가입, 이메일 알림 구독, 비밀번호 리셋 가능 ⬇️   
![스크린샷 2025-03-12 오후 11.16.03.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2011.16.03.png?raw=true) 
- Kibana의 로그 대시보드로 이상 사용자의 IP를 확인하고, 지속적으로 올바르지 않은 요청을 보낸 IP는 WAF에서 차단 가능 ⬇️   
![스크린샷 2025-03-12 오후 10.35.06.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2010.35.06.png?raw=trueE)
 
</details>

<details>
<summary><strong>💰 비즈니스적 의사결정 제목2</strong> </summary> 

### 배경
- 채용 공고의 제목, 회사명, 자격 요건 등의 필드에 동일한 우선순위가 적용되어 사용자가 원하는 결과를 빠르게 찾기 어려움

### 요구사항
- **검색 결과의 우선순위 설정**: 채용 공고 검색 시, 중요한 필드인 제목을 가장 높은 우선순위로 설정
- **검색 품질 향상**: 중요한 필드들이 상위에 노출되어 사용자가 보다 빠르게 원하는 검색 결과를 찾을 수 있도록 함

### 고려한 대안

  | **비교 항목**   | **기존 검색 시스템**               | **Boost 기능 적용 후**              |
  |-------------|-----------------------------|--------------------------------|
  | **우선순위 설정** | ❌ 모든 필드에 동일한 우선순위 적용        | ✅ 각 필드에 점수를 부여하여 우선순위 적용       |
  | **검색 품질**   | ❌ 동일한 우선순위로 인해 중요한 정보 노출 부족 | ✅ 중요한 정보(제목)가 먼저 노출되어 검색 품질 향상 |

### 결정 및 근거
✅Elasticsearch의 Boost 기능을 적용
- Title 필드: `boost(2.0f)`를 사용하여 다른 필드들보다 더 높은 우선순위 부여
- Company 필드: `boost(1.5f)`를 적용하여 제목 다음으로 높은 우선순위 부여
- RequiredSkills 필드:`boost(1.0f)`를 설정하여 회사명보다 낮은 우선순위 부여

### 결과
- Spring 으로 검색 시 ⬇️ 
![Spring 으로 검색 시](https://github.com/llRosell/sparta/blob/main/Spring%20%E1%84%8B%E1%85%B3%E1%84%85%E1%85%A9%20%E1%84%80%E1%85%A5%E1%86%B7%E1%84%89%E1%85%A2%E1%86%A8%20%E1%84%89%E1%85%B5.png?raw=true)

- Java 로 검색 시 ⬇️
![Java 로 검색 시](https://github.com/llRosell/sparta/blob/main/Java%20%E1%84%85%E1%85%A9%20%E1%84%80%E1%85%A5%E1%86%B7%E1%84%89%E1%85%A2%E1%86%A8%20%E1%84%89%E1%85%B5.png?raw=true)
</details>

</aside>



<aside>
</aside>

## 🤝 **팀 구성원 소개**
| **구성원** | **직책**  | **장점**                                       | **GitHub**                                | **Blog**                                      | **담당**                                                                                                                                                           |
|---------|---------|----------------------------------------------|-------------------------------------------|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **이승찬** | `👑팀장`  | `🎨아이디어 뱅크`<br> `❄️냉철한 시선`<br> `😄즐거움의 달인`   | [tmdcksdl](https://github.com/tmdcksdl) | [블로그](https://carrot0911.tistory.com)         | 📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기                   |
| **신지현** | `🥇팀원` | `🔠오탈자 귀신`<br> `🎧인간GPT`<br> `💯MBTI J 100%` | [backswan0](https://github.com/backswan0) | [블로그](https://writingforever162.tistory.com/) | 📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기                   |
| **이수진** | `🎖️팀원` | `🐞버그헌터`<br> `☯️워라밸 마스터`<br> `👦바른어린이`       | [아이디](깃허브 주소)   | [블로그](블로그 주소)         | 📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기                   |
| **한성우** | `🏅팀원`  | `👂경청의 대가`<br> `🏆노력 끝판왕`<br> `✍️글의 조각가`     | [아이디](깃허브 주소)   | [블로그](블로그 주소)         | 📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기<br>📌 입력하기                   |
