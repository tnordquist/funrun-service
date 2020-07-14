create sequence hibernate_sequence start with 1 increment by 1;
create table comment
(
    comment_id bigint        not null,
    text       varchar(1000) not null,
    author_id  bigint        not null,
    history_id bigint,
    primary key (comment_id)
);
create table event
(
    event_id     bigint       not null,
    display_name varchar(100) not null,
    distance     integer      not null,
    end          timestamp,
    skill_level  integer      not null,
    start        timestamp,
    primary key (event_id)
);
create table history
(
    history_id bigint    not null,
    distance   integer   not null,
    end        timestamp not null,
    start      timestamp not null,
    event_id   bigint,
    user_id    bigint    not null,
    primary key (history_id)
);
create table user_profile
(
    user_id      bigint       not null,
    display_name varchar(100) not null,
    oauth_key    varchar(100) not null,
    role         integer      not null,
    skill_level  integer      not null,
    primary key (user_id)
);
create table user_profile_comments
(
    user_user_id        bigint not null,
    comments_comment_id bigint not null
);
alter table event
    add constraint UK_q2bgshr0a6p6eosylv5x7m7nx unique (display_name);
alter table user_profile
    add constraint UK_j35xlx80xoi2sb176qdrtoy69 unique (display_name);
alter table user_profile
    add constraint UK_6f815wi5o4jq8p1q1w63o4mhd unique (oauth_key);
alter table user_profile_comments
    add constraint UK_row0hlrgc5mh8sapufh20348p unique (comments_comment_id);
alter table comment
    add constraint FK6ct4xwb3j3r7fslb5jaswyfy3 foreign key (author_id) references user_profile;
alter table comment
    add constraint FK3cr79uywtk3fgus9ylu87tkld foreign key (history_id) references history;
alter table history
    add constraint FKgyu84ci3y61dab4ylh0d6dmg5 foreign key (event_id) references event;
alter table history
    add constraint FKlxnwjq50kmdsvok8nilye9ulb foreign key (user_id) references user_profile;
alter table user_profile_comments
    add constraint FK49oy5ng81ld9gwruiph3p0j7g foreign key (comments_comment_id) references comment;
alter table user_profile_comments
    add constraint FKd91v4xhpiuwe4wrc0oelxb8x1 foreign key (user_user_id) references user_profile;
