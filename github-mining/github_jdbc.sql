create schema github;
use github;

create table user (
	id bigint primary key,
	login varchar(30),
	name varchar(255),
	location varchar(255),
	company varchar(255),
	blog varchar(255),
	joined varchar(255),
	ts bigint
);

create table user_network(
	user_id bigint,
	follower_id bigint,
	ts bigint,
    PRIMARY KEY (user_id, follower_id)
);


create table visit_log (
	id bigint primary key,
	login varchar(30),
	status varchar(10),
	ts bigint
);