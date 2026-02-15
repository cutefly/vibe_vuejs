# 임직원 정보 관리 시스템 (Vibe Employee Management System)

이 프로젝트는 임직원 정보를 관리하기 위한 웹 애플리케이션으로, Vue 3 프론트엔드와 Spring Boot 백엔드로 구성되어 있습니다. 보안을 위해 JWT(JSON Web Token)와 Redis를 이용한 Refresh Token 로직이 구현되어 있습니다.

## 🚀 기술 스택

### 프론트엔드 (Frontend)
- **Framework**: Vue 3 (Composition API)
- **Language**: TypeScript
- **Build Tool**: Vite
- **State Management**: Pinia
- **HTTP Client**: Axios
- **UI Styling**: Tailwind CSS (사용자 요청 시 적용 가능, 현재는 Vanilla CSS/Custom)

### 백엔드 (Backend)
- **Framework**: Spring Boot 3.2.2
- **Language**: Java 21
- **Security**: Spring Security (JWT Authentication)
- **Data Persistence**: Spring Data JPA (JPA/Hibernate)
- **Database**: 
  - Local: PostgreSQL (`db.club012.com:5432`)
  - Dev: MySQL
- **Caching/Storage**: Redis (Refresh Token 저장용, `db.club012.com:6379`)
- **Config Management**: HashiCorp Vault

## 📂 프로젝트 구조

```text
vibe-vuejs/
├── src/                # Vue.js 프론트엔드 소스 코드
│   ├── assets/         # 정적 자원 (이미지, 폰트 등)
│   ├── components/     # 재사용 가능한 Vue 컴포넌트
│   ├── pages/          # 페이지 단위 컴포넌트 (Login, Dashboard 등)
│   ├── stores/         # Pinia 상태 관리 (Auth 등)
│   ├── utils/          # 공통 유틸리티 (Axios 인터셉터 등)
│   └── App.vue         # 루트 컴포넌트
├── backend/            # Spring Boot 백엔드 소스 코드
│   ├── src/main/java/  # Java 소스 코드
│   │   └── com/vibe/employee/
│   │       ├── config/      # 설정 클래스 (Security, Redis, Data)
│   │       ├── controller/  # API 컨트롤러 (Auth, Employee)
│   │       ├── dto/         # 데이터 전송 객체 (Request, Response)
│   │       ├── model/       # 엔티티 및 모델 (Manager, Employee, RefreshToken)
│   │       ├── repository/  # 데이터 접근 인터페이스
│   │       ├── security/    # JWT 및 보안 필터 로직
│   │       └── service/     # 비즈니스 로직 (RefreshTokenService 등)
│   └── src/main/resources/  # 설정 파일 (application.yml, SQL 등)
└── package.json        # 프론트엔드 의존성 및 스크립트
```

## 🛠 실행 방법

### 1. 사전 준비 (Prerequisites)
- Node.js (v24 권장)
- Java 21
- PostgreSQL & Redis (설정된 호스트 `db.club012.com` 접근 가능 여부 확인)

### 2. 백엔드 실행 (Backend)
백엔드는 `local` 프로파일을 기본으로 사용합니다.

```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### 3. 프론트엔드 실행 (Frontend)
루트 디렉토리에서 실행합니다.

```bash
# 의존성 설치
npm install

# 개발 서버 실행 (기본 포트: 5173)
npm run dev
```

## 🔐 주요 보안 기능

- **JWT 인증**: 로그인 시 Access Token(1시간)과 Refresh Token(7일)을 발급합니다.
- **Refresh Token Rotation**: Access Token 만료 시 Redis에 저장된 Refresh Token을 확인하여 자동으로 갱신합니다.
- **Stateless**: 서버 세션을 사용하지 않고 토큰 기반의 무상태 인증을 제공합니다.
- **Axios Interceptor**: 프론트엔드에서 401(Unauthorized) 에러 발생 시 자동으로 토큰 갱신 요청을 수행합니다.

## 👤 기본 계정 정보
- **아이디**: `admin`
- **비밀번호**: `admin123`

## 📝 개발 가이드
- 프론트엔드 코딩 규칙은 `GEMINI.md` 파일을 참조하세요.
- 새로운 API 추가 시 `AuthController` 및 `SecurityConfig`의 접근 권한 설정을 확인하세요.
