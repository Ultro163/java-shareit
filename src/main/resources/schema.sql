DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name    VARCHAR(255)   NOT NULL,
    email   VARCHAR UNIQUE NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS items
(
    item_id     BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name        VARCHAR,
    description VARCHAR,
    available   BOOLEAN,
    owner_id    BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    request     BIGINT,
    CONSTRAINT pk_items PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS booking
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT REFERENCES items (item_id) ON DELETE CASCADE,
    booker_id  BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    status     VARCHAR,
    CONSTRAINT pk_booking PRIMARY KEY (booking_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY,
    item_id    BIGINT REFERENCES items (item_id) ON DELETE CASCADE,
    author_id  BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    text       VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);