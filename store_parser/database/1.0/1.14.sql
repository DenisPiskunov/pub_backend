create table users
(
	id serial not null
		constraint users_pkey
			primary key,
	username varchar(100) not null,
	password varchar(256) not null,
	roles varchar(256),
	enabled boolean default true not null
)
;

create unique index users_id_uindex
	on users (id)
;

create unique index users_username_uindex
	on users (username)
;


INSERT INTO users (username, password, roles, enabled) VALUES ('admin', '$2a$04$YS2RdVkwTUxLP3paPaNrD.5AaOdITiHVY.niiq177WB7AWzDR1lVe', 'ADMIN', true);

INSERT INTO version (version) VALUES ('1.14');