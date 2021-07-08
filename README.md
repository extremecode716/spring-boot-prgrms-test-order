주문관리 API 서버를 개발해야 합니다. 다행히 전임자가 `Apache Maven`을 활용해 필요한 라이브러리 설정, 프로젝트 기본 구조 셋업, spring-security 기반으로 JWT 인증 및 인가 처리 부분까지 완성해 두었습니다.

개발 요건을 잘 확인하고, 주문 관린 API 서버의 나머지 부분을 완성해주세요.

- Maven 파일 `pom.xml` 내용을 수정할 필요는 없습니다.
  * 전체 테스트 실행 명령: `mvn clean test`
- 총 4개의 테스트 클래스(29개의 테스트 케이스)가 있습니다. 모든 테스트를 통과해야 합니다. 개별 테스트 클래스 실행명령은 아래와 같습니다. 
  * `mvn clean test -Dtest=OrderRestControllerTest`
  * `mvn clean test -Dtest=ReviewRestControllerTest`
  * `mvn clean test -Dtest=ProductRestControllerTest`
  * `mvn clean test -Dtest=UserRestControllerTest`
- 특정 테스트 클래스의 테스트 케이스(메소드) 실행은 `mvn clean test -Dtest=TestClassName#TestMethodName` 명령으로 실행할 수 있습니다.
  * ProductRestControllerTest 클래스의 findAllSuccessTest 테스트 메소드 실행 예시: `mvn clean test -Dtest=ProductRestControllerTest#findAllSuccessTest` 
- 개발에 필요한 데이터베이스 스키마 및 샘플 데이터가 준비되 있습니다.
  * In-Memory 경량 RDBMS H2가 사용됩니다. **프로젝트를 재시작 할 때마다 데이c터가 초기화 됩니다.**
  * 데이터베이스 입출력을 위해 `SQL`을 직접 작성해야하고, `JdbcTemplate`으로 처리합니다. **(JPA 사용안함)**
  * 스키마: resources/schema-h2.sql
  * 스키마 및 API 출력 예시를 참고하여 적절한 Java 객체 모델링이 필요합니다.
  * 샘플 데이터: resources/data-h2.sql
  * 샘플 데이터에는 `ID: tester / 비밀번호: 1234` 사용자가 초기 셋팅되 있습니다.
  * 스키마 및 샘플 데이터 수정 없이 모든 테스트 통과가 가능합니다.
  
> com.github.prgrms.security 패키지의 클래스들은 JWT 인증처리를 위해 완성된 코드입니다. (해당 클래스들의 구현에 대해 이해가 없어도 힌트를 통해 과제 수행이 가능합니다.)
> 특히 JwtAuthenticationTokenFilter 클래스느 HTTP 요청헤더에서 JWT 값을 추출하고 해당 값이 올바르다면 인증된 사용자 정보(JwtAuthenticationToken)를 SecurityContextHolder에 set 합니다.
> RestController에서는 @AuthenticationPrincipal 어노테이션을 사용하여 인증된 사용자 정보(JwtAuthenticationToken.principal)에 접근할 수 있습니다.
> 본 과제에서 해당 패키지 아래 클래스를 직접 수정할 필요는 없습니다.

---

### 요건 1. API 응답 포맷

정상처리 및 오류처리에 대한 API 서버 공통 응답 포맷을 아래와 같이 정의 합니다.

- 정상처리 및 오류처리 모두 success 필드를 포함합니다.
  * 정상처리라면 true, 오류처리라면 false 값을 출력합니다.
- 정상처리는 response 필드를 포함하고 error 필드는 null 입니다.
  * 응답 데이터가 `단일 객체`라면, response 필드는 `JSON Object`로 표현됩니다.
  * 응답 데이터가 `스칼라 타입(string, int, boolean)`이라면, response 필드는 `string, int, boolean로 표현`됩니다.
  * 응답 데이터가 `Collection`이라면, response 필드는 `JSON Array`로 표협됩니다.
- 오류처리는 error 필드를 포함하고 response 필드는 null 입니다. error 필드는 status, message 필드를 포함합니다.
  * status : HTTP Response status code 값과 동일한 값을 출력해야 합니다.
  * message : 오류 메시지가 출력 됩니다.

> 힌트: @ControllerAdvice 어노테이션이 적용되있는 com.github.prgrms.errors.GeneralExceptionHandler 구현을 참고하세요.

이미 구현되 있는 아래 2개 API는 응답 포맷이 `요건 1` 정의에 맞지 않기 때문에 수정이 필요합니다.

- 단일 상품 조회: /api/products/{id}
  * 구현위치: com.github.prgrms.products.ProductRestController.findById 메소드
- 상품 목록 조회: /api/products
  * 구현위치: com.github.prgrms.products.ProductRestController.findAll 메소드

그 외 API는 `요건 1`을 만족하도록 개발을 시작하세요.

> 힌트: com.github.prgrms.users.UserRestController 구현을 참고하세요.

#### 1.1. 로그인 성공 응답 예시

```json
{
    "success": true,
    "response": {
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvZ3JhbW1lcnMiLCJuYW1lIjoidGVzdGVyIiwiaWF0IjoxNjExMTQxMjMxLCJ1c2VyS2V5IjoxfQ.XG9ehe1-Q0kWGRhJTi5pWQ-D6ymA5aMKGV7I0qnT9lkz1end0FuEByixkUNEgGY3yiCZiAh380fnf9Q38SiEzw",
        "user": {
            "name": "tester",
            "email": {
                "address": "tester@gmail.com"
            },
            "loginCount": 1,
            "lastLoginAt": "2021-01-20 20:13:51",
            "createAt": "2021-01-20 20:13:36"
        }
    },
    "error": null
}
```

#### 1.2 로그인 실패 응답 예시

- 로그인 아이디 누락 (HTTP STATUS 400)
```json
{
  "success": false,
  "response": null,
  "error": {
    "message": "principal must be provided",
    "status": 400
  }
}
```
- 로그인 아이디/비밀번호 미일치 (HTTP STATUS 401)
```json
{
  "success": false,
  "response": null,
  "error": {
    "message": "Bad credential",
    "status": 401
  }
}
```

### 요건 2. 공개용 API 및 인증 사용자용 API 구분

API는 사용자가 로그인하지 않아도 호출할 수 있는 `공개용 API`와 로그인 후 호출할 수 있는 `인증 사용자용 API`로 구분됩니다.

- 공개용 API
  * 로그인: /api/users/login
  * 단일 상품조회: /api/products/{id}
  * 상품 목록조회: /api/products
- 인증 사용자용 API
  * 내 정보 조회: /api/users/me
  * 주문 리뷰작성: /api/orders/{id}/review
  * 주문 접수처리: /api/orders/{id}/accept
  * 주문 배송처리: /api/orders/{id}/shipping
  * 주문 완료처리: /api/orders/{id}/complete
  * 주문 거절처리: /api/orders/{id}/reject
  * 단일 주문조회: /api/orders/{id}
  * 주문 목록조회: /api/orders

로그인 응답에는 `token` 필드가 포함되 있습니다.
```json
{
  "success": true,
  "response": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...이하생략...",
    "user": {
      "name": "tester",
      ...생략...
    }
  },
  "error": null
}
```
`인증 사용자용 API`를 호출하기 위해 요청 헤더에 `X-PRGRMS-AUTH` 항목을 추가하고, 값으로 로그인 후 전달받은 `token`에 `Bearer` 키워드를 앞에 붙여 입력합니다.
```
curl --request GET 'http://localhost:8080/api/users/me' \
--header 'Accept: application/json' \
--header 'X-PRGRMS-AUTH: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...이하생략...' 
```

인증 사용자용 API 호출시 `X-PRGRMS-AUTH` 헤더가 누락되거나 값이 올바르지 않다면 아래와 같은 오류 응답이 발생합니다.
```json
{
  "success": false,
  "response": null,
  "error": {
    "message": "Unauthorized",
    "status": 401
  }
}
```

### 요건 3. API 구현

아래 내용을 참고하여 각 API를 구현하세요.

#### 3.1. 주문 리뷰 작성

> ReviewRestController.review 메소드를 구현하세요.

인증된 사용자 본인의 주문에 대해 리뷰를 작성한다.

주문 상태 `state`가 `COMPLETED`라면 리뷰를 작성할 수 있다. 단, 동일한 주문에 대해 중복 리뷰를 작성할 수 없다. 정상적으로 리뷰가 작성되면 리뷰 대상 `Product`의 `reviewCount` 값이 1 증가한다.

리뷰 작성이 불가능하다면 `400 오류`를 응답한다.

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.github.prgrms.orders.ReviewRestController
- 컨트롤러 메소드명: review
- URL: POST /api/orders/{id}/review
  * {id}: 리뷰를 남기려는 주문의 PK
- Request Body: 리뷰 내용
```json
{
  "content": "review test"
}
```
- Response Body: 작성된 리뷰 내용
```json
{
  "success": true,
  "response": {
    "seq": 2,
    "productId": 3,
    "content": "review test",
    "createAt": "2021-01-20 20:16:47"
  },
  "error": null
}
```
- 중복 리뷰 작성 오류 응답 예시
```json
{
  "success": false,
  "response": null,
  "error": {
    "message": "Could not write review for order 4 because have already written",
    "status": 400
  }
}
```
- 주문 상태 `state`가 `COMPLETED`가 아닌 경우 오류 응답 예시
```json
{
  "success": false,
  "response": null,
  "error": {
    "message": "Could not write review for order 1 because state(REQUESTED) is not allowed",
    "status": 400
  }
}
```

#### 3.2. 주문 목록 조회

> OrderRestController.findAll 메소드를 구현하세요.

인증된 사용자 본인의 주문 목록을 출력한다.

주문은 `Review`를 포함할 수 있다. (샘플 데이터에서 `seq=4`인 주문은 리뷰를 포함한다.)

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.github.prgrms.orders.OrderRestController
- 컨트롤러 메소드명: findAll
- URL: GET /api/orders?offset=0&size=5
  * offset: offset 기반 페이징 처리 파리미터 (최소값: 0, 최대값: Long.MAX_VALUE, 기본값: 0)
  * size: 출력할 아이템의 갯수 (최소값 1, 최대값: 5, 기본값: 5)
  * offset, size 값이 최소값~최대값 범위 밖이거나 주어지지 않는다면 기본값으로 대체
- Response Body: 주문 내용 목록 (아래 출력 예시는 offset=0, size=5 인 경우)
  * offset=2 라면, `seq=5`인 주문부터 출력되야함

> 힌트: 페이징 파라미터 (offset, size) 처리를 위해 com.github.prgrms.configures.web.SimplePageRequestHandlerMethodArgumentResolver 클래스 완성이 필요합니다.
> RestController에서는 Pageable 타입으로 페이징 파라미터 접근이 가능합니다.

```json
{
  "success": true,
  "response": [
    {
      "seq": 7,
      "productId": 3,
      "review": null,
      "state": "REQUESTED",
      "requestMessage": null,
      "rejectMessage": null,
      "completedAt": null,
      "rejectedAt": null,
      "createAt": "2021-01-20 20:13:36"
    },
    {
      "seq": 6,
      "productId": 3,
      "review": null,
      "state": "REJECTED",
      "requestMessage": null,
      "rejectMessage": "No stock",
      "completedAt": null,
      "rejectedAt": "2021-01-24 18:30:00",
      "createAt": "2021-01-20 20:13:36"
    },
    {
      "seq": 5,
      "productId": 3,
      "review": {
        "seq": 2,
        "productId": 3,
        "content": "review test",
        "createAt": "2021-01-20 20:16:47"
      },
      "state": "COMPLETED",
      "requestMessage": null,
      "rejectMessage": null,
      "completedAt": "2021-01-24 10:30:10",
      "rejectedAt": null,
      "createAt": "2021-01-20 20:13:36"
    },
    {
      "seq": 4,
      "productId": 2,
      "review": {
        "seq": 1,
        "productId": 2,
        "content": "I like it!",
        "createAt": "2021-01-20 20:13:36"
      },
      "state": "COMPLETED",
      "requestMessage": "plz send it quickly!",
      "rejectMessage": null,
      "completedAt": "2021-01-24 12:10:30",
      "rejectedAt": null,
      "createAt": "2021-01-20 20:13:36"
    },
    {
      "seq": 3,
      "productId": 2,
      "review": null,
      "state": "SHIPPING",
      "requestMessage": null,
      "rejectMessage": null,
      "completedAt": null,
      "rejectedAt": null,
      "createAt": "2021-01-20 20:13:36"
    }
  ],
  "error": null
}
```

#### 3.3. 개별 주문 조회

> OrderRestController.findById 메소드를 구현하세요.

인증된 사용자 본인의 개별 주문을 출력한다.

주문은 `Review`를 포함할 수 있다. (샘플 데이터에서 `seq=4`인 주문은 리뷰를 포함한다.)

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.github.prgrms.orders.OrderRestController
- 컨트롤러 메소드명: findById
- URL: GET /api/orders/{id}
  * {id}: 조회 대상 주문의 PK
- Response Body: 주문 내용
```json
{
  "success": true,
  "response": {
    "seq": 4,
    "productId": 2,
    "review": {
      "seq": 1,
      "productId": 2,
      "content": "I like it!",
      "createAt": "2021-01-20 20:13:36"
    },
    "state": "COMPLETED",
    "requestMessage": "plz send it quickly!",
    "rejectMessage": null,
    "completedAt": "2021-01-24 12:10:30",
    "rejectedAt": null,
    "createAt": "2021-01-20 20:13:36"
  },
  "error": null
}
```

#### 3.4. 주문 접수 처리

> OrderRestController.accept 메소드를 구현하세요.

인증된 사용자 본인의 주문에 대해 상태를 변경한다.

주문이 최초 생성될 때 주문 상태 `state`는 `REQUESTED`이다. 주문 상태가 `REQUESTED`라면 접수 처리를 할 수 있다. 정상적으로 접수 처리 되면 주문 상태는 `ACCEPTED`로 변경된다.

상태 변경이 불가능하다면 예외를 발생시키지 말고 `false`를 정상 반환한다.

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.github.prgrms.orders.OrderRestController
- 컨트롤러 메소드명: accept
- URL: PATCH /api/orders/{id}/accept
  * {id}: 상태를 변경할 주문의 PK
- Response Body: true 라면, 상태변경 성공
```json
{
  "success": true,
  "response": true,
  "error": null
}
```
- 주문 상태 `state`가 `REQUESTED`가 아닌 경우 응답 예시
```json
{
  "success": true,
  "response": false,
  "error": null
}
```

#### 3.5. 주문 거절 처리

> OrderRestController.reject 메소드를 구현하세요.

인증된 사용자 본인의 주문에 대해 상태를 변경한다.

주문이 최초 생성될 때 주문 상태 `state`는 `REQUESTED`이다. 주문 상태가 `REQUESTED`라면 거절 처리를 할 수 있다. 정상적으로 거절 처리 되면 주문 상태는 `REJECTED`로 변경된다. 그리고 주문 거절 시각 `rejectedAt`은 현재 시각으로 설정된다.

상태 변경이 불가능하다면 예외를 발생시키지 말고 `false`를 정상 반환한다.

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.github.prgrms.orders.OrderRestController
- 컨트롤러 메소드명: reject
- URL: PATCH /api/orders/{id}/reject
  * {id}: 상태를 변경할 주문의 PK
- Request Body: 거절 메세지
```json
{
  "message": "reject message"
}
```
- Response Body: true 라면, 상태변경 성공
```json
{
  "success": true,
  "response": true,
  "error": null
}
```
- 주문 상태 `state`가 `REQUESTED`가 아닌 경우 응답 예시
```json
{
  "success": true,
  "response": false,
  "error": null
}
```

#### 3.6. 주문 배송 처리

> OrderRestController.shipping 메소드를 구현하세요.

인증된 사용자 본인의 주문에 대해 상태를 변경한다.

주문 상태 `state`가 `ACCEPTED`라면 배송 처리를 할 수 있다. 정상적으로 배송 처리 되면 주문 상태는 `SHIPPING`으로 변경된다.

상태 변경이 불가능하다면 예외를 발생시키지 말고 `false`를 정상 반환한다.

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.github.prgrms.orders.OrderRestController
- 컨트롤러 메소드명: shipping
- URL: PATCH /api/orders/{id}/shipping
  * {id}: 상태를 변경할 주문의 PK
- Response Body: true 라면, 상태변경 성공
```json
{
  "success": true,
  "response": true,
  "error": null
}
```
- 주문 상태 `state`가 `ACCEPTED`가 아닌 경우 응답 예시
```json
{
  "success": true,
  "response": false,
  "error": null
}
```

#### 3.7. 주문 완료 처리

> OrderRestController.complete 메소드를 구현하세요.

인증된 사용자 본인의 주문에 대해 상태를 변경한다.

주문 상태 `state`가 `SHIPPING`이라면 완료 처리를 할 수 있다. 정상적으로 완료 처리 되면 주문 상태는 `COMPLETED`로 변경된다. 그리고 주문 완료 시각 `completedAt`은 현재 시각으로 설정된다.

상태 변경이 불가능하다면 예외를 발생시키지 말고 `false`를 정상 반환한다.

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.github.prgrms.orders.OrderRestController
- 컨트롤러 메소드명: complete
- URL: PATCH /api/orders/{id}/complete
  * {id}: 상태를 변경할 주문의 PK
- Response Body: true 라면, 상태변경 성공
```json
{
  "success": true,
  "response": true,
  "error": null
}
```
- 주문 상태 `state`가 `SHIPPING`이 아닌 경우 응답 예시
```json
{
  "success": true,
  "response": false,
  "error": null
}
```