CREATE TABLE "users"
(
    u_id              BIGSERIAL PRIMARY KEY,
    firstname         VARCHAR(30) NOT NULL,
    lastname          VARCHAR(30) NOT NULL,
    email             VARCHAR(64) NOT NULL,
    password          VARCHAR(60) NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT unique_email UNIQUE (email)
);
