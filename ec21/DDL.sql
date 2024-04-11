-- 고객 (customer)
drop table customer;

create table customer (
    customer_id varchar2(20) primary key,
    customer_pw varchar2(30) not null,
    customer_name varchar2(100) not null,
    customer_department varchar2(100) not null,
    email varchar2(100) not null,
    customer_gubun char(1) not null check(customer_gubun in('B','S','A')),
    zip_no varchar1(20) not null,
    fax_no varchar2(20),
    busi_no varchar2(20),
    trade_no varchar2(20),
    comp_name varchar2(50) not null,
    comp_desc varchar2(2000),
    comp_url varchar2(50),
    address varchar2(100) not null,
    remote_ip varchar2(20) not null,
    country varchar2(20),
    create_date date default sysdate,
    update_date date,
    roles varchar2(10) default 'ROLE_USER' check(roles in ('ROLE_USER','ROLE_ADMIN')),
    enabled char(1) default 'N' check(enabled in ('N','Y')),
    reported_cnt number default 0,
    blacklist_check char(1) default 'N' check(blacklist_check in ('N','Y'))
);

select * from customer;



-- 인콰이어리 차단 회원관리 (inquiry_block)
drop table inquiry_block;
drop sequence inquiry_block_seq;

create table inquiry_block(
    inquiry_block number primary key,
    customer_id varchar2(20) references customer(customer_id) on delete casecade,
    blocked_id varchar2(20)
);

create sequence inquiry_block_seq;

select * from inquiry_block;



-- 신고 회원 관리 (report_customer)
drop table report_customer;
drop sequence report_customer_seq;

create table report_customer(
    report_customer_id number primary key,
    reported_id varchar2(20) not null,
    report_category varchar2(20) not null check(report_category in ('drug', 'IPR','spam','etc')),
    report_reason varchar2(5000),
    report_date date default sysdate,
    manager_check char(1) default 'N' check(manager_check in ('N','Y'))
);

create sequence report_customer_seq;

select * from report_customer;




-- 블랙 회원 관리(blacklist)
drop table blacklist;
drop sequence blacklist_seq;

create table blacklist(
    blacklist_id number primary key,
    customer_id varchar2(20) not null,
    comp_name varchar2(100) not null,
    remote_ip varchar2(20) not null,
    country varchar2(20),
    black_type varchar2(20) not null check(black_type in ('drug', 'IPR','spam','etc')),
    black_reason varchar2(5000),
    input_date date default sysdate
);

create sequence blacklist_seq;

select * from blacklist;




-- 상품 (product)

DROP TABLE product;

CREATE TABLE product
(
    product_id VARCHAR2(50) PRIMARY KEY  
    , product_name VARCHAR2(200) NOT NULL 
    , product_desc CLOB NOT NULL
    , product_img VARCHAR2(5000) NOT NULL
    , price NUMBER NOT NULL
    , origin VARCHAR2(30) NOT NULL
    , moq NUMBER NOT NULL
    , unit VARCHAR2(10) NOT NULL
    , category VARCHAR2(20) NOT NULL check(category in ('Food&Beverage','Chemical','Health&Medical','Electronic','Cosmetic'))
    , create_date date DEFAULT sysdate
    , update_date date
    , remote_ip VARCHAR2(20) NOT NULL
    , country VARCHAR2(50)
    , hit_count NUMBER DEFAULT 0
    , lstm_predict_proba NUMBER(5,2) NOT NULL
    , lstm_predict CHAR(1)  NOT NULL check(lstm_predict in ('0','1'))
    , judge CHAR(1) check(judge in ('N','Y'))
    , customer_id VARCHAR2(20) NOT NULL references customer(customer_id) on delete casecade
    , product_delete CHAR(1) DEFAULT 'N' check(product_delete in ('N','Y'))
);
select * from product;




-- 금지어 (prohibit_word)
DROP TABLE prohibit_word;
drop sequence prohibit_word_seq;

CREATE TABLE prohibit_word
(
    prohibit_word_id NUMBER PRIMARY KEY
    , prohibit_reason VARCHAR2(20) NOT NULL check(prohibit_reason in ('IPR','drug','prohibited_items','explicit_adult'))
    , prohibit_word VARCHAR2(200) NOT NULL
);

create sequence prohibit_word_seq;

select * from prohibit_word; 





-- 금지어유사도 (prohibit_similar_word)
DROP TABLE prohibit_similar_word;
drop sequence prohibit_similar_word_seq;

CREATE TABLE prohibit_similar_word
(
    prohibit_similar_id NUMBER PRIMARY KEY
    , similar_word VARCHAR2(100) NOT NULL
    , similar_proba NUMBER(5,2) NOT NULL
    , prohibit_word_id NUMBER references prohibit_word(prohibit_word_id) on delete casecade
    , product_id VARCHAR2(50) references product(product_id) on delete casecade
);
create sequence prohibit_similar_word_seq;
select * from prohibit_similar_word;



-- 인콰이어리 (inquiry)
drop table inquiry;
drop sequence inquiry_seq;

create table inquiry(
    inquiry_id number primary key,
    sender_id varchar2(20) not null references customer(customer_id) on delete casecade,
    receiver_id varchar2(20) not null,
    product_id varchar2(30) not null references product(product_id) on delete casecade,
    quantity number,
    inquiry_title varchar2(1000) not null,
    inquiry_content varchar2(5000) not null,
    send_date date default sysdate,
    file varchar2(100)
    saved char(2) default 'NN' check(saved in ('NN','NY','YN','YY')),
    trash char(2) default 'NN' check(trash in ('NN','NY','YN','YY')),
    spam char(2) default 'NN' check(spam in ('NN','NY','YN','YY'))
);

create sequence inquiry_seq;

select * from inquiry;


