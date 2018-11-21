-- ===============================================================
-- Wallet parser
-- Release 1.0
-- ===============================================================
-- Database Object Creation Script (Version 1.0)
-- ===============================================================

create database IF NOT EXISTS log_database;

use log_database;

DROP USER 'appuser'@'localhost';

create user IF NOT EXISTS 'appuser'@'localhost' identified by 'appuser2018';

grant all on log_database.* to 'appuser'@'localhost'; -- Gives all the privileges to the new user on the newly created database

-- ---------------------------------------------------------------
-- Regression
-- ---------------------------------------------------------------

drop table if exists web_server_request;

/*==============================================================*/
/* Table: web_server_request                                          */
/*==============================================================*/
create table web_server_request (
   id                   INT           NOT NULL AUTO_INCREMENT,
   log_date             TIMESTAMP     NOT NULL,
   ip                   VARCHAR(30)   NOT NULL,
   request_method       VARCHAR(50)   NOT NULL,
   http_status          VARCHAR(50)   NOT NULL,
   user_agent           VARCHAR(200)  NULL,
   comments             VARCHAR(200)  NULL,
   PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
