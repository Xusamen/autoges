# DDL FINAL PROJET
CREATE TABLE `utilisateurs` (
  `id_utilisateur` INT NOT NULL AUTO_INCREMENT,
  `nom` VARCHAR(100) DEFAULT NULL,
  `prenom` VARCHAR(100) DEFAULT NULL,
  `identifiant` VARCHAR(100) DEFAULT NULL,
  `mot_de_passe` VARCHAR(255) DEFAULT NULL,
  `fonction` VARCHAR(30) DEFAULT NULL,
  `actif` TINYINT(1) NOT NULL DEFAULT 1,
  `tentatives_echouees` INT NOT NULL DEFAULT 0,
  `verrouille_jusqua` DATETIME NULL,
  `date_creation` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_utilisateur`),
  UNIQUE KEY `uk_identifiant` (`identifiant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `historique_connexions` (
  `id_historique` INT NOT NULL AUTO_INCREMENT,
  `id_utilisateur` INT NULL, 
  `identifiant_tente` VARCHAR(255) NOT NULL,
  `succes` TINYINT(1) NOT NULL,
  `adresse_ip` VARCHAR(45) NULL,
  `date_tentative` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_historique`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `agence` (
  `id_agence` INT NOT NULL AUTO_INCREMENT,
  `nom_agence` varchar(255) NOT NULL,
  `position_agence` varchar(255) NOT NULL,
  PRIMARY KEY (`id_agence`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `agence` (`id_agence`, `nom_agence`, `position_agence`) VALUES
(1, 'Agence SIEGE', 'Secteur 17'),
(2, 'Agence LAFIABOUGOU', 'Secteur 20'),
(3, 'Agence HOUNDE', 'HOUNDE');





# Module Candidat
CREATE TABLE `candidat` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `identifiant` varchar(50) DEFAULT NULL,
  `nom` varchar(255) NOT NULL,
  `nom_de_jeune_fille` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) NOT NULL,
  `date_de_naissance` date NOT NULL,
  `lieu_de_naissance` varchar(255) NOT NULL,
  `type_document_id` varchar(255) NOT NULL,
  `numero_document_id` varchar(255) NOT NULL,
  `date_de_delivrance` date NOT NULL,
  `lieu_de_delivrance` varchar(255) NOT NULL,    
  `profession` varchar(255) NOT NULL,
  `contact` varchar(255) NOT NULL,
  `lieu_residence` varchar(255) NOT NULL,
  `genre` varchar(255) NOT NULL,
  `nom_pap` varchar(255) NOT NULL,
  `prenom_pap` varchar(255) NOT NULL,
  `contact_pap` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifiant` (`identifiant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `groupe` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_groupe` varchar(150) NOT NULL,
  `id_agence` int NOT NULL,
  `date_creation` date NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_agence`) REFERENCES `agence`(`id_agence`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `inscription` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `id_candidat` BIGINT NOT NULL,
  `id_groupe` int DEFAULT NULL,
  `id_agence` int NOT NULL,
  `categorie_de_permis` varchar(10) NOT NULL,
  `numero_ancien_permis` varchar(255) DEFAULT NULL,
  `date_inscription` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `forfait` varchar(255) NOT NULL,
  `montant_formation` int NOT NULL,
  `montant_formation_total_solder` tinyint(1) NOT NULL DEFAULT 0,
  `frais_de_dossier` tinyint(1) NOT NULL DEFAULT 0,
  `fin_formation` date DEFAULT NULL,
  `formation_terminee` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_candidat`) REFERENCES `candidat`(`id`),
  FOREIGN KEY (`id_groupe`) REFERENCES `groupe`(`id`),
  FOREIGN KEY (`id_agence`) REFERENCES `agence`(`id_agence`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `dossier` (
  `id`                 BIGINT NOT NULL AUTO_INCREMENT,
  `id_inscription`     BIGINT NOT NULL,
  `date_depot`         date DEFAULT NULL,
  `permis_provisoire`  varchar(255) DEFAULT 'Aucun',
  `date_sortie`        date DEFAULT NULL,
  `commentaire`        text DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_inscription`) REFERENCES `inscription`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Module Comptabilité

CREATE TABLE `categories_transaction` (
  `id`     INT AUTO_INCREMENT PRIMARY KEY,
  `nom`    VARCHAR(100) NOT NULL,
  `type`   ENUM('recette','depense') NOT NULL,
  `statut` ENUM('actif','inactif') DEFAULT 'actif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `categories_transaction` (`nom`, `type`, `statut`) VALUES
('Frais de formation', 'recette', 'actif'),
('Heures de conduite supplémentaires', 'recette', 'actif'),
('Frais de dossier', 'recette', 'actif'),
('Frais d''examen', 'recette', 'actif'),
('Vente de fournitures', 'recette', 'actif'),
('Recyclage / Perfectionnement à la conduite', 'recette', 'actif'),
('Autres recettes diverses', 'recette', 'actif'),
('Carburant et lubrifiants', 'depense', 'actif'),
('Entretien et réparations véhicules', 'depense', 'actif'),
('Assurance automobile', 'depense', 'actif'),
('Visite technique et vignettes', 'depense', 'actif'),
('Achat de pièces et consommables véhicules', 'depense', 'actif'),
('Lavage et entretien esthétique', 'depense', 'actif'),
('Salaires moniteurs / formateurs', 'depense', 'actif'),
('Salaires du personnel administratif', 'depense', 'actif'),
('Charges sociales et patronales', 'depense', 'actif'),
('Loyer locaux', 'depense', 'actif'),
('Factures d''électricité, eau et internet', 'depense', 'actif'),
('Fournitures de bureau', 'depense', 'actif'),
('Abonnements logiciels / Plateformes de code en ligne', 'depense', 'actif'),
('Frais et taxes administratives d''examen', 'depense', 'actif'),
('Impôts et taxes professionnelles', 'depense', 'actif'),
('Publicité, marketing et communication', 'depense', 'actif'),
('Frais bancaires et commissions de paiement', 'depense', 'actif'),
('Honoraires (Comptable, Juridique)', 'depense', 'actif'),
('Autres dépenses diverses', 'depense', 'actif');

CREATE TABLE `transactions` (
  `id`                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  `reference`          VARCHAR(30) UNIQUE NOT NULL,
  `type`               ENUM('recette', 'depense') NOT NULL,
  `montant`            DECIMAL(12,2) NOT NULL,
  `mode_paiement`      ENUM('especes','cheque','cb','virement','mobile_money','autre') NOT NULL,
  `libelle`            VARCHAR(255) NOT NULL,
  `date_transaction`   DATE NOT NULL,
  `heure_transaction`  TIME DEFAULT (CURRENT_TIME),
  `justificatif`       VARCHAR(255) NULL,
  `id_utilisateur`     INT NOT NULL,
  `statut`             ENUM('validee','annulee','en_attente') DEFAULT 'validee',
  `motif_annulation`   VARCHAR(255) NULL,
  `created_at`         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at`         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`id_utilisateur`) REFERENCES `utilisateurs`(`id_utilisateur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recette_candidat` (
  `id`               BIGINT AUTO_INCREMENT PRIMARY KEY,
  `id_transaction`   BIGINT NOT NULL UNIQUE,
  `id_candidat`      BIGINT NOT NULL,
  `id_inscription`   BIGINT NULL,
  `type_versement`   ENUM('inscription', 'acompte', 'solde', 'paiement_unique') NOT NULL,
  `reste_a_payer`    DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
  `numero_recu`      VARCHAR(30) UNIQUE NULL,
  `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`id_transaction`) REFERENCES `transactions`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`id_candidat`) REFERENCES `candidat`(`id`),
  FOREIGN KEY (`id_inscription`) REFERENCES `inscription`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `autres_recettes` (
  `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
  `id_transaction`  BIGINT NOT NULL UNIQUE,
  `nom_client`      VARCHAR(255) NULL,
  `prenom_client`   VARCHAR(255) NULL,
  `contact_client`  VARCHAR(100) NULL,
  `source`          VARCHAR(150) NULL,
  `description`     VARCHAR(255) NULL,
  `nombre_seances`  INT NULL,
  `numero_recu`     VARCHAR(30) UNIQUE NULL,
  `created_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `chk_nombre_seances` CHECK (`nombre_seances` IS NULL OR `nombre_seances` > 0),
  FOREIGN KEY (`id_transaction`) REFERENCES `transactions`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `depense` (
  `id`               BIGINT AUTO_INCREMENT PRIMARY KEY,
  `id_transaction`   BIGINT NOT NULL UNIQUE,
  `id_categorie`     INT NULL,
  `id_vehicule`      INT NULL,
  `id_fournisseur`   INT NULL,
  `id_personnel`     INT NULL,
  `description`      TEXT NULL,
  `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`id_transaction`) REFERENCES `transactions`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`id_categorie`) REFERENCES `categories_transaction`(`id`),
  FOREIGN KEY (`id_vehicule`) REFERENCES `vehicules`(`id`),
  FOREIGN KEY (`id_fournisseur`) REFERENCES `fournisseurs`(`id`),
  FOREIGN KEY (`id_personnel`) REFERENCES `personnel`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `vehicules` (
  `id`               INT AUTO_INCREMENT PRIMARY KEY,
  `immatriculation`  VARCHAR(20) UNIQUE NOT NULL,
  `marque`           VARCHAR(50) NOT NULL,
  `modele`           VARCHAR(50) NOT NULL,
  `type_vehicule`    ENUM('voiture','moto','poids_lourd','autre') DEFAULT 'voiture',
  `transmission`     ENUM('manuelle','automatique') DEFAULT 'manuelle',
  `statut`           ENUM('actif','en_reparation','hors_service','vendu') DEFAULT 'actif',
  `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `personnel` (
  `id`               INT AUTO_INCREMENT PRIMARY KEY,
  `nom`              VARCHAR(100) NOT NULL,
  `prenom`           VARCHAR(255) NOT NULL,
  `telephone`        VARCHAR(20) NULL,
  `email`            VARCHAR(150) NULL,
  `adresse`          VARCHAR(255) NULL,
  `fonction`         ENUM('moniteur','secretaire','comptable','directeur','autre') NOT NULL,
  `type_contrat`     ENUM('salarie','vacataire') DEFAULT 'salarie',
  `date_embauche`    DATE NULL,
  `salaire_fixe`     DECIMAL(12,2) NULL,
  `statut`           ENUM('actif','inactif','suspendu') DEFAULT 'actif',
  `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `fournisseurs` (
  `id`               INT AUTO_INCREMENT PRIMARY KEY,
  `nom`              VARCHAR(150) NOT NULL,
  `type_prestation`  VARCHAR(100) NULL,
  `telephone`        VARCHAR(20) NULL,
  `email`            VARCHAR(150) NULL,
  `adresse`          VARCHAR(255) NULL,
  `statut`           ENUM('actif','inactif') DEFAULT 'actif',
  `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
