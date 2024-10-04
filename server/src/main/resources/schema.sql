DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS item_request;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255)                            NOT NULL,
    email   VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT uk_user UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS item_request
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(255),
    requestor   BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    created     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_item_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items
(
    item_id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR,
    description VARCHAR,
    available   BOOLEAN,
    owner_id    BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    request_Id     BIGINT REFERENCES item_request(id) ON DELETE CASCADE,
    CONSTRAINT pk_items PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS booking
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT REFERENCES items (item_id) ON DELETE CASCADE,
    booker_id  BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    status     VARCHAR,
    CONSTRAINT pk_booking PRIMARY KEY (booking_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id    BIGINT REFERENCES items (item_id) ON DELETE CASCADE,
    author_id  BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    text       VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);