-- EXTENSIONS --------------------------
-- Exntesion for remove accents from strings, useful for get users by name.
CREATE EXTENSION IF NOT EXISTS unaccent;
-- Extension just for the first user insertion.
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ROLES --------------------------
CREATE TABLE roles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL
);

INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('OWNER');


-- USERS --------------------------

CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  role_id UUID NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  login VARCHAR(100) NOT NULL UNIQUE,
  hashed_password VARCHAR(100) NOT NULL,
  created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamptz,

  CONSTRAINT fk_role
    FOREIGN KEY (role_id)
    REFERENCES roles(id)
    ON DELETE RESTRICT
);

CREATE TRIGGER update_user_timestamp
  BEFORE UPDATE ON users
  FOR EACH ROW
  EXECUTE FUNCTION trigger_set_timestamp();


-- ADDRESS --------------------
CREATE TABLE address (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    cep VARCHAR(10) NOT NULL,
    state VARCHAR(2) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    address_number VARCHAR(10) NOT NULL,
    complement VARCHAR(100),
    updated_at timestamptz,

    CONSTRAINT fk_user
      FOREIGN KEY (user_id)
      REFERENCES users(id)
      ON DELETE CASCADE
);

CREATE TRIGGER update_address_timestamp
  BEFORE UPDATE ON address
  FOR EACH ROW
  EXECUTE FUNCTION trigger_set_timestamp();


-- INSERT ADMIN USER -------------------------------------
WITH insert_user AS (
  INSERT INTO users (role_id, name, email, login, hashed_password)
  VALUES (
    (SELECT id FROM roles WHERE name = 'ADMIN' LIMIT 1),
    'Admin',             
    'admin@aifudi.com',  
    'admin',
    crypt('admin123', gen_salt('bf', 10))
  )
  RETURNING id           
)
INSERT INTO address (user_id, cep, state, city, address, address_number, complement)
SELECT 
    id,                 
    '12345-678',
    'PR',
    'Curitiba',
    'Rua Teste',
    '123',
    'Apto 101'
FROM insert_user;


-- INSERT 'USER' USER -------------------------------------
WITH insert_user AS (
  INSERT INTO users (role_id, name, email, login, hashed_password)
  VALUES (
    (SELECT id FROM roles WHERE name = 'USER' LIMIT 1),
    'Maria Santos',             
    'mariasantos@gmail.com',   
    'mariasantos',
    crypt('maria123', gen_salt('bf', 10))
  )
  RETURNING id           
)
INSERT INTO address (user_id, cep, state, city, address, address_number)
SELECT 
    id,                 
    '12345-678',
    'PR',
    'Curitiba',
    'Rua XV de Novembro',
    '22222'
FROM insert_user;

