
# 💸 Pay My Buddy

Une application web permettant aux utilisateurs d’effectuer des **transferts d’argent** entre amis et avec leur banque.

Développée en **Java Spring Boot** avec **Thymeleaf**, **Spring Security**, **JPA**, et **MySQL**.

---

## 🚀 Fonctionnalités

- Connexion / Inscription d'utilisateurs
- Ajout de relations ("amis")
- Transfert d’argent vers une relation
- Historique des transactions
- Profil utilisateur (modification du mot de passe)

---

## 📦 Technologies

- **Spring Boot 3**
- **Spring Security**
- **Thymeleaf**
- **MySQL 8+**
- **Maven**
- **JPA / Hibernate**

---

## 🔐 Authentification

Utilise Spring Security avec une authentification par email + mot de passe.

---

## 📸 Interfaces (Thymeleaf)

- `/login` : Connexion  
- `/register` : Inscription  
- `/dashboard` : Tableau de bord des transactions  
- `/add-connection` : Ajouter une relation  
- `/connections` : Voir ses relations  
- `/profile` : Voir/modifier son profil  

---

## 🧠 ML - Architecture globale

Diagramme de classe UML :

![Diagramme UML](img/PayMyBuddy_UML.png)

---

## 🗃️ MPD - Modèle Physique de Données

```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DOUBLE DEFAULT 0.0
);

CREATE TABLE connections (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    connection_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (connection_id) REFERENCES users(id)
);

CREATE TABLE transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    description VARCHAR(255),
    fee DOUBLE,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);
```

---

## 👤 Auteur

Dina Charki dans le cadre d’un projet OpenClassrooms.
