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
├── frontend/           # Vue.js 프론트엔드 프로젝트
│   ├── src/            # 소스 코드
│   ├── public/         # 정적 자원
│   ├── package.json    # 의존성 설정
│   ├── vite.config.ts  # Vite 설정
│   ├── Dockerfile      # 프론트엔드용 Dockerfile
│   └── nginx.conf      # 프론트엔드용 Nginx 설정
├── backend/            # Spring Boot 백엔드 프로젝트
│   ├── src/main/java/  # Java 소스 코드
│   ├── src/main/resources/ # 설정 파일
│   ├── pom.xml         # Maven 설정
│   └── Dockerfile      # 백엔드용 Dockerfile
└── k8s/                # Kubernetes Manifest 파일
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

```bash
cd frontend

# 의존성 설치
npm install

# 개발 서버 실행 (기본 포트: 5173)
npm run dev
```

## 🐳 Docker & Kubernetes 배포

### 1. Docker 이미지 빌드

```bash
# 백엔드 이미지 빌드
cd backend
docker build -t cutefly/vibe-backend:latest .

# 프론트엔드 이미지 빌드
cd ../frontend
docker build -t cutefly/vibe-frontend:latest .
```

### 2. Kubernetes 배포

Kubernetes Manifest 파일은 `k8s/` 디렉토리에 위치해 있습니다.

```bash
# Namespace 생성
kubectl apply -f k8s/namespace.yaml

# ConfigMap 적용 (DB, Redis 설정 등)
kubectl apply -f k8s/configmap.yaml

# 백엔드 및 프론트엔드 배포
kubectl apply -f k8s/backend.yaml
kubectl apply -f k8s/frontend.yaml
```

배포 후 다음 명령어로 상태를 확인할 수 있습니다:
```bash
kubectl get pods -n vibe-system
kubectl get svc -n vibe-system
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
