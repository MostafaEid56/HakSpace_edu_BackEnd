-- Flyway migration for Workshops feature
CREATE TABLE IF NOT EXISTS workshops (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(255),
    materials_link VARCHAR(255),
    workshop_date VARCHAR(100),
    start_time VARCHAR(50),
    end_time VARCHAR(50),
    duration VARCHAR(100),
    instructor_name VARCHAR(255),
    price DOUBLE PRECISION DEFAULT 0.0,
    max_capacity INT DEFAULT 30,
    current_participants INT DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS workshop_registrations (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    contact_method VARCHAR(50) DEFAULT 'WHATSAPP',
    contact_time VARCHAR(50) DEFAULT 'ANYTIME',
    notes TEXT,
    status VARCHAR(50) DEFAULT 'NEW',
    workshop_id BIGINT NOT NULL REFERENCES workshops(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
