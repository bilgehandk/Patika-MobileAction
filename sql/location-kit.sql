CREATE ROLE admin WITH
    LOGIN
    SUPERUSER
    CREATEDB
    CREATEROLE
    INHERIT
    REPLICATION
    CONNECTION LIMIT -1
    PASSWORD 'user';

DROP DATABASE Patika;

CREATE DATABASE Patika
    WITH
    OWNER = root
    ENCODING = 'UTF8'
    TABLESPACE = pg_default;

CREATE SCHEMA IF NOT EXISTS sc_example AUTHORIZATION root;

SET search_path TO sc_example;


CREATE TABLE location (
                          city_name VARCHAR(255) PRIMARY KEY,
                          lat DOUBLE NOT NULL,
                          lon DOUBLE NOT NULL
);

CREATE TABLE air_pollution (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               location_name VARCHAR(255) NOT NULL,
                               start_date DATE,
                               end_date DATE,
                               co VARCHAR(255),
                               o3 VARCHAR(255),
                               so2 VARCHAR(255),
                               CONSTRAINT uniq1 UNIQUE (location_name, start_date),
                               FOREIGN KEY (location_name) REFERENCES location(city_name)
);



