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

CREATE TABLE "category"
(
    ca_id              BIGSERIAL PRIMARY KEY,
    title              VARCHAR(100) NOT NULL,
    content            TEXT,
    user_id            BIGINT NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_id_category FOREIGN KEY (user_id) REFERENCES users(u_id) ON DELETE CASCADE
);

CREATE TABLE "task"
(
    t_id              BIGSERIAL PRIMARY KEY,
    title             VARCHAR(100) NOT NULL,
    content           TEXT,
    status            BOOLEAN DEFAULT FALSE,
    category_id       BIGINT,
    user_id           BIGINT NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_category_id_post FOREIGN KEY (category_id) REFERENCES category(ca_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_id_post FOREIGN KEY (user_id) REFERENCES users(u_id) ON DELETE CASCADE
);