DROP TABLE authorities IF EXISTS;
DROP TABLE users IF EXISTS;

CREATE TABLE users (id SERIAL PRIMARY KEY,
                    username VARCHAR(20) unique not null,
                    password VARCHAR(255) not null,
                    enabled BOOLEAN not null);

CREATE TABLE authorities (username VARCHAR(20),
                          authority VARCHAR(20) not null,
                          FOREIGN KEY (username) references users (username) ON DELETE CASCADE);