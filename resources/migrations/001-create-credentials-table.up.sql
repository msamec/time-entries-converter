CREATE TABLE credentialsx
(
  id int PRIMARY KEY,
  user_id varchar(64) NOT NULL UNIQUE,
  options JSON
);
