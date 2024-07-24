CREATE TABLE application
(
    application_id BIGSERIAL PRIMARY KEY,
    name  TEXT NOT NULL
);
ALTER SEQUENCE application_application_id_seq RESTART 1000;