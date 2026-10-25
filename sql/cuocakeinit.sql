



DROP TABLE IF EXISTS orderlines;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS toppings;
DROP TABLE IF EXISTS bottoms;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
    user_id  SERIAL PRIMARY KEY,                          -- Unikt id
            email    VARCHAR(100) NOT NULL UNIQUE,                -- Login navn
            password VARCHAR(100) NOT NULL,                       -- Bcrypt hash
            role     VARCHAR(20)  NOT NULL DEFAULT 'customer',    -- customer eller admin
            balance  INTEGER      NOT NULL DEFAULT 0              -- Saldo i hele kroner
);


CREATE TABLE bottoms (
    bottom_id SERIAL PRIMARY KEY,                         -- Unikt id
            name      VARCHAR(50) NOT NULL UNIQUE,                -- Navn på bund
            price     INTEGER     NOT NULL                        -- Pris i kr
);


CREATE TABLE toppings (
    topping_id SERIAL PRIMARY KEY,                        -- Unikt id
            name       VARCHAR(50) NOT NULL UNIQUE,               -- Navn på top
            price      INTEGER     NOT NULL                       -- Pris i kr
);


CREATE TABLE orders (
    order_id   SERIAL PRIMARY KEY,                                        -- Ordrenummer
            user_id    INTEGER   NOT NULL REFERENCES users (user_id) ON DELETE CASCADE, -- Hvem bestilte
            created_at TIMESTAMP NOT NULL DEFAULT NOW()                           -- Hvornår
);


CREATE TABLE orderlines (
    orderline_id SERIAL PRIMARY KEY,                                          -- Unikt id
            order_id     INTEGER NOT NULL REFERENCES orders (order_id) ON DELETE CASCADE, -- Hører til ordre
            bottom_id    INTEGER NOT NULL REFERENCES bottoms (bottom_id),             -- Valgt bund
            topping_id   INTEGER NOT NULL REFERENCES toppings (topping_id),           -- Valgt top
            quantity     INTEGER NOT NULL CHECK (quantity > 0),                       -- Antal
            unit_price   INTEGER NOT NULL                                             -- Stykpris da den blev købt
);


        INSERT INTO bottoms (name, price) VALUES
        ('Chokolade', 15),
('Vanilje', 15),
('Muskatnød', 15),
('Pistacie', 18),
('Mandel', 20);


        INSERT INTO toppings (name, price) VALUES
        ('Chokolade', 15),
('Blåbær', 15),
('Hindbær', 15),
('Crisp', 18),
('Jordbær', 18),
('Rom/rosin', 20),
('Appelsin', 22),
('Citron', 22),
('Blåskimmelost', 25);

