-- Generated by the protoc-gen-sql plugin.  DO NOT EDIT!
-- file: example.proto

-- example.Example
CREATE TABLE IF NOT EXISTS `example` (
  `id` VARCHAR(32) NOT NULL,
  `name` VARCHAR(100) DEFAULT '',
  `tag` TEXT,
  `payload` TEXT,
  `create_time` DATETIME DEFAULT now(),
  `update_time` DATETIME DEFAULT now() ON UPDATE now(),

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'example.Example';


