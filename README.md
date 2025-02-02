## 프로젝트 구조

### 📂 `config` (설정 관련)

- **`OAuth2SuccessHandler`**
  - OAuth2 로그인 성공 시 실행되는 핸들러.
  - 사용자 정보 저장 및 JWT 발급 등을 처리.

- **`SecurityConfig`**
  - Spring Security 설정을 관리하는 클래스.
  - JWT 필터 적용, CORS 설정, 인증 및 인가 관련 규칙 정의.

### 📂 `controller` (컨트롤러 - API 엔드포인트 관리)

- **`AuthController`**
  - 로그인 및 회원가입을 담당하는 인증 관련 API 제공.

- **`FridgeController`**
  - 사용자의 냉장고(보유 재료) CRUD를 처리하는 API.

- **`IngredientController`**
  - 일반적인 식재료 관련 API 제공 (예: 기본 재료 목록).

- **`IngredientSearchController`**
  - 외부 API(Spoonacular)를 활용한 식재료 검색 기능 제공.

- **`LoginController`**
  - JWT 기반 로그인 관련 기능을 담당.

- **`RecommendedIngredientsController`**
  - ChatGPT API를 활용하여 사용자에게 추천 재료를 제공하는 API.

- **`UserController`**
  - 사용자 관련 API (이메일 인증 등) 제공.

### 📂 `dto` (데이터 전송 객체)

- **`IngredientDto`**
  - 식재료의 기본 정보와 사용자 냉장고에 저장될 때 필요한 추가 필드를 포함하는 DTO.
  - 필드:
    - `Long id`: 재료의 고유 ID.
    - `String ingredientName`: 재료 이름.
    - `String imageUrl`: 재료 이미지 URL.
    - `Integer quantity`: 사용자 냉장고에 저장된 재료 수량.

- **`IngredientSearchDto`**
  - Spoonacular API의 재료 검색 결과를 감싸는 DTO.
  - 필드:
    - `List<IngredientSearchDto> results`: 검색된 재료들의 목록을 담는 리스트.

- **`UserDto`**
  - 회원가입 및 사용자 관련 요청에서 사용하는 DTO.
  - 필드:
    - `String email`: 사용자 이메일.
    - `String username`: 사용자 이름.
    - `String password`: 사용자 비밀번호.

- **`LoginResponseDto`**
  - 로그인 성공 시 반환되는 데이터 DTO.
  - 필드:
    - `String token`: JWT 토큰.
    - `String tokenType`: 토큰 유형 (예: "Bearer").

- **`RegisterResponseDto`**
  - 회원가입 처리 결과를 나타내는 DTO.
  - 필드:
    - `boolean success`: 가입 성공 여부.
    - `String message`: 상세 메시지.

- **`LoginRequest`**
  - 로그인 요청 시 필요한 데이터 DTO.
  - 필드:
    - `String email`: 사용자 이메일.
    - `String password`: 사용자 비밀번호.

### 📂 `entity` (JPA 엔티티)

- **`User`**
  - 사용자의 정보를 저장하는 JPA 엔티티.
  - 필드:
    - `Long id`: 사용자의 고유 ID. 데이터베이스에서 자동 생성됩니다.
    - `String email`: 사용자 이메일. 유일해야 하며 로그인 ID로 사용됩니다.
    - `String password`: 인코딩된 비밀번호.
    - `boolean enabled`: 이메일 인증 여부를 나타냅니다.
    - `String verificationToken`: 이메일 인증을 위한 토큰 (선택 사항).
    - `String username`: 사용자 이름.
  - 주요 메서드:
    - `setEnabled(boolean enabled)`: 이메일 인증 상태를 설정합니다.
    - `setVerificationToken(String verificationToken)`: 이메일 인증 토큰을 설정합니다.

- **`UserIngredient`**
  - 사용자의 냉장고에 저장된 식재료 정보를 관리하는 엔티티.
  - 필드:
    - `Long id`: 재료의 고유 ID. 데이터베이스에서 자동 생성됩니다.
    - `String ingredientName`: 재료 이름.
    - `int quantity`: 재료 수량.
    - `String imageUrl`: 재료 이미지 URL.
    - `User user`: 재료를 소유한 사용자. `@ManyToOne` 관계로 설정되어 있습니다.


### 📂 `initializer` (초기화 관련)

- **`UserInitializer`**
  - 애플리케이션 시작 시점에 `test` 계정이 존재하지 않으면 자동으로 생성하는 초기화 클래스.
  - `UserRepository`와 `PasswordEncoder`를 사용하여 `test` 계정 생성.

### 📂 `jwt` (JWT 관련 클래스)

- **`JwtAuthenticationFilter`**
  - JWT 인증을 처리하는 필터.
  - 요청에서 JWT 추출 및 사용자 인증을 수행.

- **`JwtProperties`**
  - JWT 관련 설정값을 관리하는 클래스.
  - 필드:
    - `String secret`: JWT 비밀 키.
    - `long expirationMs`: JWT 만료 시간 (밀리초).

- **`JwtTokenProvider`**
  - JWT 생성, 검증 및 토큰 정보 추출 등의 기능을 제공하는 클래스.
  - 주요 메서드:
    - `String generateToken(Authentication authentication)`: JWT 토큰 생성.
    - `boolean validateToken(String token)`: JWT 토큰 유효성 검사.
    - `String getUserEmailFromJWT(String token)`: JWT 토큰에서 사용자 이메일 추출.

### 📂 `repository` (데이터베이스 인터페이스 - JPA)

- **`UserRepository`**
  - `User` 엔티티 관련 CRUD 기능을 제공하는 JPA Repository.
  - 주요 메서드:
    - `Optional<User> findByEmail(String email)`: 이메일로 사용자 찾기.

- **`UserIngredientRepository`**
  - `UserIngredient` 엔티티 관련 CRUD 기능을 제공하는 JPA Repository.
  - 주요 메서드:
    - `List<UserIngredient> findByUser(User user)`: 사용자의 재료 목록 조회.

### 📂 `service` (비즈니스 로직)

- **`ChatGPTService`**
  - OpenAI ChatGPT API를 활용하여 인기 식재료 목록을 가져오는 서비스.
  - 주요 메서드:
    - `List<IngredientDto> getPopularIngredientsWithImages()`: 인기 재료 목록과 이미지 URL 가져오기.

- **`CustomOAuth2UserService`**
  - OAuth2 로그인 시 사용자 정보를 처리하는 서비스.
  - 주요 메서드:
    - `OAuth2User loadUser(OAuth2UserRequest userRequest)`: OAuth2 사용자 정보 로드 및 처리.

- **`CustomUserDetailsService`**
  - Spring Security의 `UserDetailsService` 구현체.
  - 주요 메서드:
    - `UserDetails loadUserByUsername(String email)`: 이메일로 사용자 정보 로드.

- **`EmailService`**
  - 이메일 인증을 위한 메일 발송 기능을 제공하는 서비스.
  - 주요 메서드:
    - `void sendVerificationEmail(User user)`: 사용자에게 인증 이메일 발송.

- **`SpoonacularService`**
  - Spoonacular API를 호출하여 식재료 정보를 가져오는 서비스.
  - 주요 메서드:
    - `Mono<List<IngredientDto>> searchIngredients(String query)`: 재료 검색.
    - `Mono<IngredientDto> getIngredientDetails(String ingredientName)`: 재료 상세 정보 가져오기.

- **`UserIngredientService`**
  - 사용자의 냉장고 데이터를 관리하는 서비스.
  - 주요 메서드:
    - `void addIngredient(String userEmail, IngredientDto dto)`: 사용자 냉장고에 재료 추가.
    - `List<UserIngredient> getUserIngredients(String userEmail)`: 사용자 냉장고 재료 조회.
    - `void removeIngredient(String userEmail, Long ingredientId)`: 사용자 냉장고에서 재료 삭제.

- **`UserService`**
  - 사용자 계정 관련 서비스 (회원가입, 이메일 인증 등).
  - 주요 메서드:
    - `User registerUser(UserDto userDto)`: 사용자 등록.
    - `AuthenticationResponseDto authenticateUser(LoginRequest loginRequest)`: 사용자 인증.
