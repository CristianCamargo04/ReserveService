-- Tabla de roles
CREATE TABLE IF NOT EXISTS authorities (
    id SERIAL PRIMARY KEY,
    authority VARCHAR(20) NOT NULL
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    encrypted_password VARCHAR(255) NOT NULL,
    authority_id INTEGER NOT NULL,
    CONSTRAINT fk_authority_id FOREIGN KEY (authority_id) REFERENCES authorities (id)
);

-- Tabla de vuelos
CREATE TABLE IF NOT EXISTS flights (
    id SERIAL PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL,
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL
);

-- Tabla de reservaciones
CREATE TABLE IF NOT EXISTS reservations (
    id SERIAL PRIMARY KEY,
    passenger_name VARCHAR(255) NOT NULL,
    seat_number INTEGER NOT NULL,
    flight_id INTEGER NOT NULL,
    CONSTRAINT fk_flight_id FOREIGN KEY (flight_id) REFERENCES flights (id) ON DELETE CASCADE
);

-- Inserción de roles por defecto
INSERT INTO authorities (id, authority) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USUARIO')
ON CONFLICT DO NOTHING;

-- Inserción de vuelos por defecto
INSERT INTO flights (id, flight_number, total_seats, available_seats) VALUES
    (1, 'AV-2416', 10, 10),
    (2, 'AV-2479', 10, 10)
ON CONFLICT DO NOTHING;
