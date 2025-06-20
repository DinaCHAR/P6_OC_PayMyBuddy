-- Création de la base
CREATE DATABASE IF NOT EXISTS paymybuddy;
USE paymybuddy;

-- Table utilisateur
CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0.00
);

-- Table des relations (connections)
CREATE TABLE IF NOT EXISTS connection (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    connection_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_connection FOREIGN KEY (connection_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT uc_user_connection UNIQUE (user_id, connection_id)
);

-- Table des transactions
CREATE TABLE IF NOT EXISTS transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    fee DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES user(id),
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES user(id)
);
