
-- Données de test

-- Utilisateurs
INSERT INTO user (email, password, balance) VALUES 
('d@test.com', 'password123', 100.00),
('bob@example.com', 'password456', 50.00),

-- Connexions
INSERT INTO user_connection (user_id, connection_email) VALUES 
(1, 'bob@example.com'),
(1, 'charlie@example.com'),

-- Transactions
INSERT INTO transaction (sender_id, receiver_id, amount, description) VALUES 
(1, 2, 20.00, 'Déjeuner'),
(2, 1, 10.00, 'Café'),
