DROP TABLE IF EXISTS
    --items,
    requests,
    bookings
    CASCADE;

CREATE TABLE IF NOT EXISTS users (
    user_id serial,
    user_name text,
    email text,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS items (
    item_id serial,
    item_name text,
    description text,
    available boolean,
    item_owner int, --user
    request int, --request
    CONSTRAINT pk_item PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS requests (
    request_id serial,
    description text,
    requester int,  --user
    created date,
    CONSTRAINT pk_request PRIMARY KEY (request_id)
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id serial,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    item_id int,    --item
    booker_id int,  --user
    status text DEFAULT 'WAITING',
    CONSTRAINT pk_booking PRIMARY KEY (booking_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id serial,
    "text" text,
    item_id int,    --item
    author_id int,  --user
    CONSTRAINT pk_comments PRIMARY KEY (comment_id)
);

ALTER TABLE users
    DROP CONSTRAINT IF EXISTS email_uniq;

ALTER TABLE users
    ADD CONSTRAINT email_uniq UNIQUE (email);

ALTER TABLE items
    DROP CONSTRAINT IF EXISTS fk_item_item_owner;

ALTER TABLE items
    ADD CONSTRAINT fk_item_item_owner FOREIGN KEY (item_owner)
    REFERENCES users (user_id);

ALTER TABLE items
    DROP CONSTRAINT IF EXISTS fk_item_request;

ALTER TABLE requests
    DROP CONSTRAINT IF EXISTS fk_request_requester;

/*
ALTER TABLE items
    ADD CONSTRAINT fk_item_request FOREIGN KEY (request)
    REFERENCES requests (request_id);
*/

ALTER TABLE requests
    ADD CONSTRAINT fk_request_requester FOREIGN KEY (requester)
    REFERENCES users (user_id);

ALTER TABLE bookings
    DROP CONSTRAINT IF EXISTS fk_booking_item;

ALTER TABLE bookings
    ADD CONSTRAINT fk_booking_item FOREIGN KEY (item_id)
        REFERENCES items (item_id);

ALTER TABLE bookings
    DROP CONSTRAINT IF EXISTS fk_booking_user;

ALTER TABLE bookings
    ADD CONSTRAINT fk_booking_user FOREIGN KEY (booker_id)
        REFERENCES users (user_id);

ALTER TABLE comments
    DROP CONSTRAINT IF EXISTS fk_comment_item;

ALTER TABLE comments
    ADD CONSTRAINT fk_comment_item FOREIGN KEY (item_id)
        REFERENCES items (item_id);

ALTER TABLE comments
    DROP CONSTRAINT IF EXISTS fk_comment_user;

ALTER TABLE comments
    ADD CONSTRAINT fk_comment_user FOREIGN KEY (author_id)
        REFERENCES users (user_id);
