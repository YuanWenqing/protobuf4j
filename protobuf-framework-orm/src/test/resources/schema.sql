create table `db_msg` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,

  `int32_v` INT(20) DEFAULT 0,
  `int64_v` BIGINT(20) DEFAULT 0,
  `float_v` DECIMAL(10,4) DEFAULT 0,
  `double_v` DECIMAL(20,6) DEFAULT 0,
  `bool_v` TINYINT(1) DEFAULT 0,
  `enuma_v` TINYINT(2) DEFAULT 0,
  `string_v` VARCHAR(100) DEFAULT '',

  `int32_arr` TEXT,
  `int64_arr` TEXT,
  `float_arr` TEXT,
  `double_arr` TEXT,
  `bool_arr` TEXT,
  `string_arr` TEXT,
  `enuma_arr` TEXT,

  `int32_map` TEXT,
  `int64_map` TEXT,
  `float_map` TEXT,
  `double_map` TEXT,
  `bool_map` TEXT,
  `string_map` TEXT,
  `enuma_map` TEXT,

  `create_time` DATETIME DEFAULT now(),

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
