CREATE TABLE IF NOT EXISTS properties
(
  p_key       VARCHAR(512),
  value       VARCHAR(4096),
  application VARCHAR(128),
  profile     VARCHAR(128),
  label       VARCHAR(128),
  PRIMARY KEY (`p_key`, `application`, `profile`, `label`)
);