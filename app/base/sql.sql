CREATE TABLE fonction (
    id_fonction SERIAL PRIMARY KEY,
    fonction VARCHAR(50) NOT NULL -- ex : agent, bureau, admin
);

CREATE TABLE "user" (
    id_user SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    adresse VARCHAR(50) NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    id_fonction INT REFERENCES fonction(id_fonction)
);

CREATE TABLE site (
    id_site SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    region VARCHAR(100),
    surface DECIMAL(15,2),
    decree VARCHAR(50)
);

CREATE TABLE status_agent (
    id_status_agent SERIAL PRIMARY KEY,
    status VARCHAR(50) UNIQUE NOT NULL 
);

CREATE TABLE agent (
    id_agent SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    id_site INT REFERENCES site(id_site),
    date_recrutement DATE
);

CREATE TABLE user_app (
    id_user_app SERIAL PRIMARY KEY,
    id_agent INT UNIQUE REFERENCES agent(id_agent),
    login VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL
);

CREATE TABLE historique_agent_status (
    id_historique SERIAL PRIMARY KEY,
    id_agent INT REFERENCES agent(id_agent),
    id_status_agent INT REFERENCES status_agent(id_status_agent),
    date_changement TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categorie (
    id_categorie SERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL, 
    nom VARCHAR(100) NOT NULL,
    caractere TEXT
);

CREATE TABLE faits (
    id_fait SERIAL PRIMARY KEY,
    date_fait TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_categorie INT REFERENCES categorie(id_categorie)
);

CREATE TABLE status_message (
    id_status SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL -- ex : En cours, Maîtrisée, Terminée
);

CREATE TABLE message (
    id_message SERIAL PRIMARY KEY,
    date_commencement TIMESTAMP,
    date_signalement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    point_repere TEXT,
    description TEXT,
    surface_approximative DECIMAL,
    direction VARCHAR(20) NOT NULL, -- sud, nord, est, ouest
    elements_visibles VARCHAR(100),
    degats TEXT,
    id_categorie INT REFERENCES categorie(id_categorie),
    id_user_app INT REFERENCES user_app(id_user_app),
    contenu_code TEXT
);

CREATE TABLE historique_message_status (
    id_historique SERIAL PRIMARY KEY,
    id_message INT REFERENCES message(id_message),
    id_status INT REFERENCES status_message(id_status),
    date_changement TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
