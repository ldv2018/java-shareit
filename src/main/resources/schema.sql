DROP TABLE IF EXISTS
    items,
    requests
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

/*
ALTER TABLE items
    ADD CONSTRAINT fk_item_request FOREIGN KEY (request)
    REFERENCES requests (request_id);
*/

ALTER TABLE requests
    DROP CONSTRAINT IF EXISTS fk_request_requester;

ALTER TABLE requests
    ADD CONSTRAINT fk_request_requester FOREIGN KEY (requester)
    REFERENCES users (user_id);