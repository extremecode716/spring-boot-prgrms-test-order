DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    seq           bigint      NOT NULL AUTO_INCREMENT, --사용자 PK
    name          varchar(10) NOT NULL,                --사용자명
    email         varchar(50) NOT NULL,                --로그인 이메일
    passwd        varchar(80) NOT NULL,                --로그인 비밀번호
    login_count   int         NOT NULL DEFAULT 0,      --로그인 횟수. 로그인시 마다 1 증가
    last_login_at datetime             DEFAULT NULL,   --최종 로그인 일자
    create_at     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (seq),
    CONSTRAINT unq_user_email UNIQUE (email)
);

CREATE TABLE products
(
    seq          bigint      NOT NULL AUTO_INCREMENT, --상품 PK
    name         varchar(50) NOT NULL,                --상품명
    details      varchar(1000)        DEFAULT NULL,   --상품설명
    review_count int         NOT NULL DEFAULT 0,      --리뷰 갯수. 리뷰가 새로 작성되면 1 증가
    create_at    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (seq)
);

CREATE TABLE reviews
(
    seq         bigint        NOT NULL AUTO_INCREMENT, --리뷰 PK
    user_seq    bigint        NOT NULL,                --리뷰 작성자 PK (users 테이블 참조)
    product_seq bigint        NOT NULL,                --리뷰 상품 PK (products 테이블 참조)
    content     varchar(1000) NOT NULL,                --리뷰 내용
    create_at   datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (seq),
    CONSTRAINT fk_reviews_to_users FOREIGN KEY (user_seq) REFERENCES users (seq) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_reviews_to_products FOREIGN KEY (product_seq) REFERENCES products (seq) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE orders
(
    seq          bigint   NOT NULL AUTO_INCREMENT, --주문 PK
    user_seq     bigint   NOT NULL,                --주문자 PK (users 테이블 참조)
    product_seq  bigint   NOT NULL,                --주문상품 PK (products 테이블 참조)
    review_seq   bigint            DEFAULT NULL,   --주문에 대한 리뷰 PK (reviews 테이블 참조)
    state        enum('REQUESTED','ACCEPTED','SHIPPING','COMPLETED','REJECTED') DEFAULT 'REQUESTED' NOT NULL,
    --주문상태
    request_msg  varchar(1000)     DEFAULT NULL,   --주문 요청 메시지
    reject_msg   varchar(1000)     DEFAULT NULL,   --주문 거절 메시지
    completed_at datetime          DEFAULT NULL,   --주문 완료 처리 일자
    rejected_at  datetime          DEFAULT NULL,   -- 주문 거절일자
    create_at    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (seq),
    CONSTRAINT unq_review_seq UNIQUE (review_seq),
    CONSTRAINT fk_orders_to_users FOREIGN KEY (user_seq) REFERENCES users (seq) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_orders_to_products FOREIGN KEY (product_seq) REFERENCES products (seq) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_orders_to_reviews FOREIGN KEY (review_seq) REFERENCES reviews (seq) ON DELETE RESTRICT ON UPDATE RESTRICT
);