



DROP TABLE IF EXISTS orderlines;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS toppings;
DROP TABLE IF EXISTS bottoms;
DROP TABLE IF EXISTS users;


-- Brugere (både kunder og admin)
CREATE TABLE users (
    user_id  SERIAL PRIMARY KEY,
            email    VARCHAR(100) NOT NULL UNIQUE,
            password VARCHAR(100) NOT NULL,
            role     VARCHAR(20)  NOT NULL DEFAULT 'customer',
            balance  INTEGER      NOT NULL DEFAULT 0
);


-- Bunde
CREATE TABLE bottoms (
    bottom_id SERIAL PRIMARY KEY,
            name      VARCHAR(50) NOT NULL UNIQUE,
            price     INTEGER     NOT NULL
);


-- Toppe
CREATE TABLE toppings (
    topping_id SERIAL PRIMARY KEY,
            name       VARCHAR(50) NOT NULL UNIQUE,
            price      INTEGER     NOT NULL
);


-- Ordrer
CREATE TABLE orders (
    order_id   SERIAL PRIMARY KEY,
            user_id    INTEGER   NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
            created_at TIMESTAMP NOT NULL DEFAULT NOW()
);


-- Ordrelinjer (stykprisen gemmes, så gamle ordrer ikke ændrer sig)
CREATE TABLE orderlines (
    orderline_id SERIAL PRIMARY KEY,
            order_id     INTEGER NOT NULL REFERENCES orders (order_id) ON DELETE CASCADE,
            bottom_id    INTEGER NOT NULL REFERENCES bottoms (bottom_id),
            topping_id   INTEGER NOT NULL REFERENCES toppings (topping_id),
            quantity     INTEGER NOT NULL CHECK (quantity > 0),
            unit_price   INTEGER NOT NULL
);


-- Bunde med 2026 priser
        INSERT INTO bottoms (name, price) VALUES
        ('Chokolade', 15),
('Vanilje', 15),
('Muskatnød', 15),
('Pistacie', 18),
('Mandel', 20);


-- Toppe med 2026 priser
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

