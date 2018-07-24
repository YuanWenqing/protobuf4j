create table db_msg(
  id BIGINT PRIMARY KEY,
  int32_v INT DEFAULT 0,
  int64_v BIGINT DEFAULT 0,
  float_v DECIMAL DEFAULT 0,
  double_v DECIMAL DEFAULT 0,
  bool_v TINYINT DEFAULT 0,
  enuma_v TINYINT DEFAULT 0,
  string_v VARCHAR(100) DEFAULT '',

  create_time DATETIME DEFAULT now()
);
