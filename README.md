# Unix Systems Term Project

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green) ![Java](https://img.shields.io/badge/Java-23-blue) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7.5-blue)

## 📖 프로젝트 소개

"Unix Systems Term Project"는 Spring Boot 기반으로 구현한 웹 애플리케이션입니다. 학습 포트폴리오 웹사이트의 "Unix System" 섹션을 구현하며, 다음과 같은 핵심 기능을 제공합니다.

## 🚀 주요 기능

* **단원 관리(CRUD)**: 챕터 생성, 조회, 수정, 삭제 기능
* **커뮤니티 게시판**: 글·댓글 작성, 수정, 삭제, 검색, 좋아요 및 조회수 집계
* **권한 제어**: 작성자 및 관리자 권한 기반 수정·삭제 제어
* **실시간 1:1 채팅**: WebSocket 기반 채팅, 입장·종료 메시지, DB 저장
* **회원 관리**: 회원 가입·로그인, 닉네임 설정 및 변경

## 🛠️ 기술 스택

* **Server**: Java 23, Spring Boot 3.4.3
* **Web**: Spring Web, Thymeleaf(Mustache) 템플릿, Spring WebSocket
* **Database**: PostgreSQL, Spring Data JPA (Hibernate)
* **Security**: Spring Security (인증·인가, CSRF)
* **CI/CD & Deployment**: Maven, AWS EC2, AWS RDS

## ⚙️ 설치 및 실행

1. 리포지토리 클론

   ```bash
   git clone https://github.com/ChanDevOps02/25-1-Unix-Systems-Term-Project.git
   cd 25-1-Unix-Systems-Term-Project
   ```
2. 의존성 다운로드 및 빌드

   ```bash
   mvn clean package
   ```
3. 애플리케이션 실행

   ```bash
   java -jar target/UnixPractice-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

## 🌐 배포 구성

* **EC2**: Spring Boot Jar 배포 및 실행
* **RDS**: PostgreSQL 인스턴스 연동
* **보안 그룹**: SSH(22), HTTP(8080), DB(5432) 포트 설정

## 🎓 프로젝트 구조

```
├─ src/main/java
│  └─ com.example.unixpractice
│     ├─ config       # WebSocket, Security 설정
│     ├─ controller   # 요청 처리 컨트롤러
│     ├─ service      # 비즈니스 로직
│     ├─ repository   # JPA 리포지토리
│     └─ entity       # JPA 엔티티 클래스
├─ src/main/resources
│  ├─ templates      # Mustache 템플릿(.mustache)
│  └─ application-*.properties # 프로파일별 설정
└─ pom.xml
```

## 👤 작성자

* **강민찬 (ChanDevOps02)**
* GitHub: [https://github.com/ChanDevOps02](https://github.com/ChanDevOps02)
* 이메일: [gangminchan5@gmail.com](mailto:gangminchan5@gmail.com)
