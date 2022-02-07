CREATE TABLE entries 
(
  id int PRIMARY KEY,
  toggl_key varchar(64) NOT NULL,
  jira_key varchar(64) NOT NULL,
  account_id varchar(64) NOT NULL
);
