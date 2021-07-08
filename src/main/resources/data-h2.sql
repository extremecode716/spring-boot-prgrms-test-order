-- User 데이터 생성
INSERT INTO users(seq,name,email,passwd) VALUES (null,'tester','tester@gmail.com','$2a$10$mzF7/rMylsnxxwNcTsJTEOFhh1iaHv3xVox.vpf6JQybEhE4jDZI.');

-- Product 데이터 생성
INSERT INTO products(seq,name,details,review_count) VALUES (null,'Product A',null,0);
INSERT INTO products(seq,name,details,review_count) VALUES (null,'Product B','Almost sold out!',1);
INSERT INTO products(seq,name,details,review_count) VALUES (null,'Product C','Very good product',0);

-- Review 데이터 생성
INSERT INTO reviews(seq,user_seq,product_seq,content) VALUES (null,1,2,'I like it!');

-- Order 데이터 생성
INSERT INTO orders(seq,user_seq,product_seq,review_seq,state,request_msg,reject_msg,completed_at,rejected_at) VALUES (null,1,1,null,'REQUESTED',null,null,null,null);
INSERT INTO orders(seq,user_seq,product_seq,review_seq,state,request_msg,reject_msg,completed_at,rejected_at) VALUES (null,1,1,null,'ACCEPTED',null,null,null,null);
INSERT INTO orders(seq,user_seq,product_seq,review_seq,state,request_msg,reject_msg,completed_at,rejected_at) VALUES (null,1,2,null,'SHIPPING',null,null,null,null);
INSERT INTO orders(seq,user_seq,product_seq,review_seq,state,request_msg,reject_msg,completed_at,rejected_at) VALUES (null,1,2,1,'COMPLETED','plz send it quickly!',null,'2021-01-24 12:10:30',null);
INSERT INTO orders(seq,user_seq,product_seq,review_seq,state,request_msg,reject_msg,completed_at,rejected_at) VALUES (null,1,3,null,'COMPLETED',null,null,'2021-01-24 10:30:10',null);
INSERT INTO orders(seq,user_seq,product_seq,review_seq,state,request_msg,reject_msg,completed_at,rejected_at) VALUES (null,1,3,null,'REJECTED',null,'No stock',null,'2021-01-24 18:30:00');
INSERT INTO orders(seq,user_seq,product_seq,review_seq,state,request_msg,reject_msg,completed_at,rejected_at) VALUES (null,1,3,null,'REQUESTED',null,null,null,null);