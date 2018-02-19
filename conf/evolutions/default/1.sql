# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table product (
  id                            bigint(10) auto_increment not null,
  title                         varchar(100),
  description                   varchar(500),
  price                         bigint(10),
  file                          varchar(20),
  constraint pk_product primary key (id)
);


# --- !Downs

drop table if exists product;

