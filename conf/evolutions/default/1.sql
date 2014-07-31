# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table case_style (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  number_of_pins            integer,
  constraint pk_case_style primary key (id))
;

create table categories (
  id                        bigint auto_increment not null,
  names                     varchar(255),
  parent_id                 bigint,
  owner_id                  bigint,
  constraint pk_categories primary key (id))
;

create table component (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  count                     integer,
  warehouse_id              bigint,
  constraint pk_component primary key (id))
;

create table global_category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  parent_id                 bigint,
  constraint pk_global_category primary key (id))
;

create table invoice (
  id                        bigint auto_increment not null,
  constraint pk_invoice primary key (id))
;

create table linked_account (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  provider_user_id          varchar(255),
  provider_key              varchar(255),
  constraint pk_linked_account primary key (id))
;

create table security_role (
  id                        bigint auto_increment not null,
  role_name                 varchar(255),
  constraint pk_security_role primary key (id))
;

create table token_action (
  id                        bigint auto_increment not null,
  token                     varchar(255),
  target_user_id            bigint,
  type                      varchar(2),
  created                   datetime,
  expires                   datetime,
  constraint ck_token_action_type check (type in ('EV','PR')),
  constraint uq_token_action_token unique (token),
  constraint pk_token_action primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  name                      varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  last_login                datetime,
  active                    tinyint(1) default 0,
  email_validated           tinyint(1) default 0,
  constraint pk_users primary key (id))
;

create table user_permission (
  id                        bigint auto_increment not null,
  value                     varchar(255),
  constraint pk_user_permission primary key (id))
;

create table warehouse (
  id                        bigint auto_increment not null,
  owner_id                  bigint,
  created                   datetime,
  name                      varchar(255),
  comment                   varchar(255),
  is_private                tinyint(1) default 0,
  constraint uq_warehouse_1 unique (owner_id,name),
  constraint pk_warehouse primary key (id))
;


create table users_security_role (
  users_id                       bigint not null,
  security_role_id               bigint not null,
  constraint pk_users_security_role primary key (users_id, security_role_id))
;

create table users_user_permission (
  users_id                       bigint not null,
  user_permission_id             bigint not null,
  constraint pk_users_user_permission primary key (users_id, user_permission_id))
;
alter table categories add constraint fk_categories_parent_1 foreign key (parent_id) references categories (id) on delete restrict on update restrict;
create index ix_categories_parent_1 on categories (parent_id);
alter table categories add constraint fk_categories_owner_2 foreign key (owner_id) references users (id) on delete restrict on update restrict;
create index ix_categories_owner_2 on categories (owner_id);
alter table component add constraint fk_component_warehouse_3 foreign key (warehouse_id) references warehouse (id) on delete restrict on update restrict;
create index ix_component_warehouse_3 on component (warehouse_id);
alter table global_category add constraint fk_global_category_parent_4 foreign key (parent_id) references global_category (id) on delete restrict on update restrict;
create index ix_global_category_parent_4 on global_category (parent_id);
alter table linked_account add constraint fk_linked_account_user_5 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_account_user_5 on linked_account (user_id);
alter table token_action add constraint fk_token_action_targetUser_6 foreign key (target_user_id) references users (id) on delete restrict on update restrict;
create index ix_token_action_targetUser_6 on token_action (target_user_id);
alter table warehouse add constraint fk_warehouse_owner_7 foreign key (owner_id) references users (id) on delete restrict on update restrict;
create index ix_warehouse_owner_7 on warehouse (owner_id);



alter table users_security_role add constraint fk_users_security_role_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_security_role add constraint fk_users_security_role_security_role_02 foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;

alter table users_user_permission add constraint fk_users_user_permission_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_user_permission add constraint fk_users_user_permission_user_permission_02 foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table case_style;

drop table categories;

drop table component;

drop table global_category;

drop table invoice;

drop table linked_account;

drop table security_role;

drop table token_action;

drop table users;

drop table users_security_role;

drop table users_user_permission;

drop table user_permission;

drop table warehouse;

SET FOREIGN_KEY_CHECKS=1;

