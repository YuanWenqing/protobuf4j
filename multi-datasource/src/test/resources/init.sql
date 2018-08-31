drop table user;
create table `user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) DEFAULT 0,

  `create_time` DATETIME DEFAULT now(),

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO user (name) values ('a');
INSERT INTO user (name) values ('b');
