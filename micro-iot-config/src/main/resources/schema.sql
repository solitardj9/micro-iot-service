create table `properties` (
	'p_key'			VARCHAR(512)		NOT NULL,
	'value'			VARCHAR(4096)		NULL DEFAULT NULL,
	'application'		VARCHAR(128)		NOT NULL,
	'profile'			VARCHAR(128)		NOT NULL,
	'label'			VARCHAR(128)		NOT NULL,
	PRIMARY KEY ('p_key', 'application', 'profile', 'label')
);