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
  name                      varchar(255),
  owner_id                  bigint,
  parent_id                 bigint,
  global_category_equivalent_id bigint,
  constraint pk_categories primary key (id))
;

create table component (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  count                     integer,
  warehouse_id              bigint,
  owner_id                  bigint,
  category_id               bigint,
  constraint pk_component primary key (id))
;

create table element_dictionary (
  id                        bigint auto_increment not null,
  element_name              varchar(255),
  global_category_id        bigint,
  constraint pk_element_dictionary primary key (id))
;

create table element_tag (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_element_tag primary key (id))
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

create table project (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  owner_id                  bigint,
  constraint pk_project primary key (id))
;

create table project_association (
  id                        bigint auto_increment not null,
  project_id                bigint,
  component_id              bigint,
  count                     integer,
  constraint uq_project_association_1 unique (project_id,component_id),
  constraint pk_project_association primary key (id))
;

create table project_component (
  id                        bigint auto_increment not null,
  project_id                bigint,
  component_id              bigint,
  count                     integer,
  constraint uq_project_component_1 unique (project_id,component_id),
  constraint pk_project_component primary key (id))
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


create table element_dictionary_element_tag (
  element_dictionary_id          bigint not null,
  element_tag_id                 bigint not null,
  constraint pk_element_dictionary_element_tag primary key (element_dictionary_id, element_tag_id))
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
alter table categories add constraint fk_categories_owner_1 foreign key (owner_id) references users (id) on delete restrict on update restrict;
create index ix_categories_owner_1 on categories (owner_id);
alter table categories add constraint fk_categories_parent_2 foreign key (parent_id) references categories (id) on delete restrict on update restrict;
create index ix_categories_parent_2 on categories (parent_id);
alter table categories add constraint fk_categories_globalCategoryEquivalent_3 foreign key (global_category_equivalent_id) references global_category (id) on delete restrict on update restrict;
create index ix_categories_globalCategoryEquivalent_3 on categories (global_category_equivalent_id);
alter table component add constraint fk_component_warehouse_4 foreign key (warehouse_id) references warehouse (id) on delete restrict on update restrict;
create index ix_component_warehouse_4 on component (warehouse_id);
alter table component add constraint fk_component_owner_5 foreign key (owner_id) references users (id) on delete restrict on update restrict;
create index ix_component_owner_5 on component (owner_id);
alter table component add constraint fk_component_category_6 foreign key (category_id) references categories (id) on delete restrict on update restrict;
create index ix_component_category_6 on component (category_id);
alter table element_dictionary add constraint fk_element_dictionary_globalCategory_7 foreign key (global_category_id) references global_category (id) on delete restrict on update restrict;
create index ix_element_dictionary_globalCategory_7 on element_dictionary (global_category_id);
alter table global_category add constraint fk_global_category_parent_8 foreign key (parent_id) references global_category (id) on delete restrict on update restrict;
create index ix_global_category_parent_8 on global_category (parent_id);
alter table linked_account add constraint fk_linked_account_user_9 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_account_user_9 on linked_account (user_id);
alter table project add constraint fk_project_owner_10 foreign key (owner_id) references users (id) on delete restrict on update restrict;
create index ix_project_owner_10 on project (owner_id);
alter table project_association add constraint fk_project_association_project_11 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_project_association_project_11 on project_association (project_id);
alter table project_association add constraint fk_project_association_component_12 foreign key (component_id) references component (id) on delete restrict on update restrict;
create index ix_project_association_component_12 on project_association (component_id);
alter table project_component add constraint fk_project_component_project_13 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_project_component_project_13 on project_component (project_id);
alter table project_component add constraint fk_project_component_component_14 foreign key (component_id) references component (id) on delete restrict on update restrict;
create index ix_project_component_component_14 on project_component (component_id);
alter table token_action add constraint fk_token_action_targetUser_15 foreign key (target_user_id) references users (id) on delete restrict on update restrict;
create index ix_token_action_targetUser_15 on token_action (target_user_id);
alter table warehouse add constraint fk_warehouse_owner_16 foreign key (owner_id) references users (id) on delete restrict on update restrict;
create index ix_warehouse_owner_16 on warehouse (owner_id);



alter table element_dictionary_element_tag add constraint fk_element_dictionary_element_tag_element_dictionary_01 foreign key (element_dictionary_id) references element_dictionary (id) on delete restrict on update restrict;

alter table element_dictionary_element_tag add constraint fk_element_dictionary_element_tag_element_tag_02 foreign key (element_tag_id) references element_tag (id) on delete restrict on update restrict;

alter table users_security_role add constraint fk_users_security_role_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_security_role add constraint fk_users_security_role_security_role_02 foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;

alter table users_user_permission add constraint fk_users_user_permission_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_user_permission add constraint fk_users_user_permission_user_permission_02 foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table case_style;

drop table categories;

drop table component;

drop table element_dictionary;

drop table element_dictionary_element_tag;

drop table element_tag;

drop table global_category;

drop table invoice;

drop table linked_account;

drop table project;

drop table project_association;

drop table project_component;

drop table security_role;

drop table token_action;

drop table users;

drop table users_security_role;

drop table users_user_permission;

drop table user_permission;

drop table warehouse;

SET FOREIGN_KEY_CHECKS=1;

