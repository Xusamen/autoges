package com.cybersek.autoges;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.cybersek.autoges.ModuleParametre.GestionAgence;

public class StructureDonnee {

    public static class Utilisateur {
        private final int id;
        private final String nom;
        private final String prenom;
        private final String identifiant;
        private String motDePasse;
        private String fonction;
        private int statut;

        public Utilisateur(int id, String nom, String prenom, String identifiant, String motDePasse, String fonction,
                int statut) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.identifiant = identifiant;
            this.motDePasse = motDePasse;
            this.fonction = fonction;
            this.statut = statut;
        }

        public Utilisateur(int id, String nom, String prenom, String identifiant, String fonction,
                int statut) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.identifiant = identifiant;
            this.fonction = fonction;
            this.statut = statut;
        }

        public Utilisateur(int id, String nom, String prenom, String identifiant, int statut) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.identifiant = identifiant;
            this.statut = statut;
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getIdentifiant() {
            return identifiant;
        }

        public String getMotDePasse() {
            return motDePasse;
        }

        public String getFonction() {
            return fonction;
        }

        public String getStatut() {
            return (statut == 1) ? "Actif" : "Non-Actif";
        }
    }

    public static class Agence {
        private final int idAgence;
        private final String nomAgence;
        private final String positionAgence;

        public Agence(int idAgence, String nomAgence, String positionAgence) {
            this.idAgence = idAgence;
            this.nomAgence = nomAgence;
            this.positionAgence = positionAgence;
        }

        public int getIdAgence() {
            return idAgence;
        }

        public String getNomAgence() {
            return nomAgence;
        }

        public String getPositionAgence() {
            return positionAgence;
        }

        @Override
        public String toString() {
            return nomAgence;
        }
    }

    public static class Groupe {
        private final int id;
        private final String nomGroupe;
        private final int idAgence;
        private final double prixUnitaire;
        private final String observation;
        private final Timestamp dateCreation;

        public Groupe(int id, String nomGroupe, int idAgence, double prixUnitaire, String observation,
                Timestamp dateCreation) {
            this.id = id;
            this.nomGroupe = nomGroupe;
            this.idAgence = idAgence;
            this.prixUnitaire = prixUnitaire;
            this.observation = observation;
            this.dateCreation = dateCreation;
        }

        public int getId() {
            return id;
        }

        public String getNomGroupe() {
            return nomGroupe;
        }

        public int getIdAgence() {
            return idAgence;
        }

        public double getPrixUnitaire() {
            return prixUnitaire;
        }

        public String getObservation() {
            return observation;
        }

        public String getNomAgence() {
            return GestionAgence.ObtenirNomAgence(this.idAgence);
        }

        public String getDateCreation() {
            if (dateCreation != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                return formatter.format(dateCreation);
            }
            return "";
        }

        @Override
        public String toString() {
            return this.nomGroupe;
        }
    }

    public static class Personnel {
        private final int id;
        private final String nom;
        private final String prenom;
        private final String telephone;
        private final String adresse;
        private final String fonction;
        private final double salaireFixe;
        private final String statut;
        private final Timestamp dateCreation;

        // Formateur statique réutilisable (thread-safe et plus performant)
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Constructeur principal
        public Personnel(int id, String nom, String prenom, String telephone, String adresse, String fonction,
                double salaireFixe, String statut, Timestamp dateCreation) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.telephone = telephone;
            this.adresse = adresse;
            this.fonction = fonction;
            this.salaireFixe = salaireFixe;
            this.statut = statut;
            this.dateCreation = dateCreation;
        }

        // Constructeur secondaire (utilise le chaînage this(...))
        public Personnel(int id, String nom, String prenom, String telephone, String adresse, String fonction,
                double salaireFixe, String statut) {
            this(id, nom, prenom, telephone, adresse, fonction, salaireFixe, statut, null);
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getTelephone() {
            return telephone;
        }

        public String getAdresse() {
            return adresse;
        }

        public String getFonction() {
            return fonction;
        }

        public double getSalaireFixe() {
            return salaireFixe;
        }

        public String getStatut() {
            return statut;
        }

        public Timestamp getDateCreationRaw() {
            return dateCreation;
        }

        // Retourne la date formatée pour la TableView : jj/mm/aaaa
        public String getDateCreation() {
            if (dateCreation != null) {
                return dateCreation.toLocalDateTime().format(DATE_FORMATTER);
            }
            return "";
        }
    }

    public static class Dossier {

        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        private final long id;
        private final long idInscription;
        private final LocalDate dateDepot;
        private final String permisProvisoire;
        private final LocalDate dateSortie;
        private final String commentaire;
        private final String matricule;
        private final String nom;
        private final String prenom;
        private final String contact;
        private final String categoriePermis;

        public Dossier(long id, long idInscription, LocalDate dateDepot, String permisProvisoire,
                LocalDate dateSortie, String commentaire, String matricule,
                String nom, String prenom, String contact, String categoriePermis) {
            this.id = id;
            this.idInscription = idInscription;
            this.dateDepot = dateDepot;
            this.permisProvisoire = (permisProvisoire != null) ? permisProvisoire : "Aucun";
            this.dateSortie = dateSortie;
            this.commentaire = commentaire;
            this.matricule = matricule;
            this.nom = nom;
            this.prenom = prenom;
            this.contact = contact;
            this.categoriePermis = categoriePermis;
        }

        public Dossier(long id, long idInscription, LocalDate dateDepot, String permisProvisoire,
                LocalDate dateSortie, String commentaire) {
            this(id, idInscription, dateDepot, permisProvisoire, dateSortie, commentaire, "", "", "", "", "");
        }

        // --- Getters BDD bruts ---
        public long getId() {
            return id;
        }

        public long getIdInscription() {
            return idInscription;
        }

        public LocalDate getDateDepotRaw() {
            return dateDepot;
        }

        public String getPermisProvisoire() {
            return permisProvisoire;
        }

        public LocalDate getDateSortieRaw() {
            return dateSortie;
        }

        public String getCommentaire() {
            return commentaire;
        }

        // --- Getters pour les colonnes TableView JavaFX ---
        public String getMatricule() {
            return matricule;
        }

        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getContact() {
            return contact;
        }

        public String getCategoriePermis() {
            return categoriePermis;
        }

        // --- Getters de dates formatées (jj/mm/aaaa) ---
        public String getDateDepot() {
            return (dateDepot != null) ? dateDepot.format(DATE_FORMATTER) : "";
        }

        public String getDateSortie() {
            return (dateSortie != null) ? dateSortie.format(DATE_FORMATTER) : "";
        }
    }

    public static class Candidat {

        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // --- Informations personnelles ---
        private long id;
        private String identifiant;
        private String nom;
        private String nomDeJeuneFille;
        private String prenom;
        private LocalDate dateDeNaissance;
        private String lieuDeNaissance;
        private String genre;
        private String profession;
        private String contact;
        private String lieuResidence;

        // --- Pièce d'identité ---
        private String typeDocumentId;
        private String numeroDocumentId;
        private LocalDate dateDeDelivrance;
        private String lieuDeDelivrance;
        private String numeroAncienPermis;

        // --- Personne à Prévenir (PAP) ---
        private String nomPap;
        private String prenomPap;
        private String contactPap;

        // --- Inscription & Formation ---
        private String categorieDePermis;
        private String nomAgence;
        private String nomGroupe;
        private String forfait;
        private double montantFormation; // Type double pour correspondre aux getters/setters

        // =========================================================================
        // CONSTRUCTEUR 1 : COMPLET (24 paramètres)
        // =========================================================================
        public Candidat(long id, String identifiant, String nom, String nomDeJeuneFille, String prenom,
                LocalDate dateDeNaissance, String lieuDeNaissance, String typeDocumentId,
                String numeroDocumentId, LocalDate dateDeDelivrance, String lieuDeDelivrance,
                String profession, String contact, String lieuResidence, String genre,
                String nomPap, String prenomPap, String contactPap, String categorieDePermis,
                String numeroAncienPermis, String nomAgence, String nomGroupe, String forfait,
                double montantFormation) {
            this.id = id;
            this.identifiant = identifiant;
            this.nom = nom;
            this.nomDeJeuneFille = nomDeJeuneFille;
            this.prenom = prenom;
            this.dateDeNaissance = dateDeNaissance;
            this.lieuDeNaissance = lieuDeNaissance;
            this.typeDocumentId = typeDocumentId;
            this.numeroDocumentId = numeroDocumentId;
            this.dateDeDelivrance = dateDeDelivrance;
            this.lieuDeDelivrance = lieuDeDelivrance;
            this.profession = profession;
            this.contact = contact;
            this.lieuResidence = lieuResidence;
            this.genre = genre;
            this.nomPap = nomPap;
            this.prenomPap = prenomPap;
            this.contactPap = contactPap;
            this.categorieDePermis = categorieDePermis;
            this.numeroAncienPermis = numeroAncienPermis;
            this.nomAgence = nomAgence;
            this.nomGroupe = nomGroupe;
            this.forfait = forfait;
            this.montantFormation = montantFormation;
        }

        // =========================================================================
        // CONSTRUCTEUR 2 : STANDARD (19 paramètres) - Délègue au constructeur complet
        // =========================================================================
        public Candidat(long id, String identifiant, String nom, String nomDeJeuneFille, String prenom,
                LocalDate dateDeNaissance, String lieuNaissance, String typeDocumentId,
                String numeroDocumentId, LocalDate dateDeDelivrance, String lieuDelivrance,
                String profession, String contact, String lieuResidence, String genre,
                String nomPap, String prenomPap, String contactPap, String categorieDePermis) {
            this(id, identifiant, nom, nomDeJeuneFille, prenom, dateDeNaissance, lieuNaissance,
                    typeDocumentId, numeroDocumentId, dateDeDelivrance, lieuDelivrance, profession,
                    contact, lieuResidence, genre, nomPap, prenomPap, contactPap, categorieDePermis,
                    null, null, null, null, 0.0);
        }

        // =========================================================================
        // GETTERS & SETTERS
        // =========================================================================

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getIdentifiant() {
            return identifiant;
        }

        public void setIdentifiant(String identifiant) {
            this.identifiant = identifiant;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getNomDeJeuneFille() {
            return nomDeJeuneFille;
        }

        public String getNomJeuneFille() {
            return nomDeJeuneFille;
        }

        public void setNomDeJeuneFille(String nomDeJeuneFille) {
            this.nomDeJeuneFille = nomDeJeuneFille;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public LocalDate getDateDeNaissanceRaw() {
            return dateDeNaissance;
        }

        public LocalDate getDateNaissance() {
            return dateDeNaissance;
        }

        public void setDateDeNaissance(LocalDate dateDeNaissance) {
            this.dateDeNaissance = dateDeNaissance;
        }

        public String getLieuDeNaissance() {
            return lieuDeNaissance;
        }

        public String getLieuNaissance() {
            return lieuDeNaissance;
        }

        public void setLieuDeNaissance(String lieuDeNaissance) {
            this.lieuDeNaissance = lieuDeNaissance;
        }

        public String getTypeDocumentId() {
            return typeDocumentId;
        }

        public void setTypeDocumentId(String typeDocumentId) {
            this.typeDocumentId = typeDocumentId;
        }

        public String getNumeroDocumentId() {
            return numeroDocumentId;
        }

        public void setNumeroDocumentId(String numeroDocumentId) {
            this.numeroDocumentId = numeroDocumentId;
        }

        public LocalDate getDateDeDelivranceRaw() {
            return dateDeDelivrance;
        }

        public LocalDate getDateDelivrance() {
            return dateDeDelivrance;
        }

        public void setDateDeDelivrance(LocalDate dateDeDelivrance) {
            this.dateDeDelivrance = dateDeDelivrance;
        }

        public String getLieuDeDelivrance() {
            return lieuDeDelivrance;
        }

        public String getLieuDelivrance() {
            return lieuDeDelivrance;
        }

        public void setLieuDeDelivrance(String lieuDeDelivrance) {
            this.lieuDeDelivrance = lieuDeDelivrance;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getLieuResidence() {
            return lieuResidence;
        }

        public void setLieuResidence(String lieuResidence) {
            this.lieuResidence = lieuResidence;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getNomPap() {
            return nomPap;
        }

        public void setNomPap(String nomPap) {
            this.nomPap = nomPap;
        }

        public String getPrenomPap() {
            return prenomPap;
        }

        public void setPrenomPap(String prenomPap) {
            this.prenomPap = prenomPap;
        }

        public String getContactPap() {
            return contactPap;
        }

        public void setContactPap(String contactPap) {
            this.contactPap = contactPap;
        }

        public String getCategorieDePermis() {
            return (categorieDePermis != null) ? categorieDePermis : "-";
        }

        public void setCategorieDePermis(String categorieDePermis) {
            this.categorieDePermis = categorieDePermis;
        }

        public String getNumeroAncienPermis() {
            return numeroAncienPermis;
        }

        public void setNumeroAncienPermis(String numeroAncienPermis) {
            this.numeroAncienPermis = numeroAncienPermis;
        }

        public String getNomAgence() {
            return nomAgence;
        }

        public void setNomAgence(String nomAgence) {
            this.nomAgence = nomAgence;
        }

        public String getNomGroupe() {
            return nomGroupe;
        }

        public void setNomGroupe(String nomGroupe) {
            this.nomGroupe = nomGroupe;
        }

        public String getForfait() {
            return forfait;
        }

        public void setForfait(String forfait) {
            this.forfait = forfait;
        }

        public double getMontantFormation() {
            return montantFormation;
        }

        public void setMontantFormation(double montantFormation) {
            this.montantFormation = montantFormation;
        }

        // =========================================================================
        // FORMATAGE POUR LES TABLEVIEW JAVAFX
        // =========================================================================

        public String getDateDeNaissance() {
            return (dateDeNaissance != null) ? dateDeNaissance.format(DATE_FORMATTER) : "";
        }

        public String getDateDeDelivrance() {
            return (dateDeDelivrance != null) ? dateDeDelivrance.format(DATE_FORMATTER) : "";
        }

        public String getNomComplet() {
            return ((nom != null ? nom : "") + " " + (prenom != null ? prenom : "")).trim();
        }

        @Override
        public String toString() {
            return getNomComplet() + " (" + (identifiant != null ? identifiant : "Sans ID") + ")";
        }
    }

    public static class TableauRecette {
        private final long id;
        private final long id_inscription;
        private final String matricule;
        private final String nom;
        private final String prenom;
        private final String contact;
        private final String categoriePermis;
        private final String forfait;
        private final int reste_paiement;

        public TableauRecette(long id, long id_inscription, String matricule, String nom, String prenom, String contact,
                String categorieDePermis, String forfait, int reste_paiement) {
            this.id = id;
            this.id_inscription = id_inscription;
            this.matricule = matricule;
            this.nom = nom;
            this.prenom = prenom;
            this.contact = contact;
            this.categoriePermis = categorieDePermis;
            this.forfait = forfait;
            this.reste_paiement = reste_paiement;
        }

        public long getId() {
            return id;
        }

        public long getIdInscription() {
            return id_inscription;
        }

        public String getMatricule() {
            return matricule;
        }

        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getContact() {
            return contact;
        }

        public String getCategoriePermis() {
            return categoriePermis;
        }

        public String getForfait() {
            return forfait;
        }

        public int getReste() {
            return reste_paiement;
        }
    }

    public static class TableauInfoTransaction {
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        private final long id_transaction;
        private final String ref_transaction;
        private final String numero_reçu;
        private final String type_versement;
        private final Timestamp dateVersement;

        public TableauInfoTransaction(long id_transaction, String ref_transaction, String numero_reçu,
                String type_versement, Timestamp dateVersement) {
            this.id_transaction = id_transaction;
            this.ref_transaction = ref_transaction;
            this.numero_reçu = numero_reçu;
            this.type_versement = type_versement;
            this.dateVersement = dateVersement;
        }

        public long getIdTransaction() {
            return id_transaction;
        }

        public String getRef_transaction() {
            return ref_transaction;
        }

        public String getNumeroRecu() {
            return numero_reçu;
        }

        public String getType_versement() {
            return type_versement;
        }

        public String getDateVersement() {
            if (dateVersement != null) {
                return dateVersement.toLocalDateTime().format(DATE_FORMATTER);
            }
            return "";
        }

    }

    // structure pour reçu versement
    public static class DonneesVersementCandidat {
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Les ID des tables
        private long idCandidat;
        private long idInscription;
        private long idTransaction;
        private long idRecetteCandidat;
        private int idAgence;

        // Les données demandées
        private String nom;
        private String nomDeJeuneFille;
        private String prenom;
        private String dateInscription;
        private String forfait;
        private String categoriePermis;
        private double montantVerse; // ou BigDecimal
        private double sommeRestante; // ou BigDecimal
        private String dateVersement;
        private String typeVersement;
        private String numeroRecu;

        // Constructeur, Getters et Setters
        public DonneesVersementCandidat(long idCandidat, long idInscription, long idTransaction, long idRecetteCandidat,
                int idAgence, String nom, String nomDeJeuneFille, String prenom,
                String dateInscription, String forfait, String categoriePermis,
                double montantVerse, double sommeRestante, String dateVersement, String typeVersement,
                String numeroRecu) {
            this.idCandidat = idCandidat;
            this.idInscription = idInscription;
            this.idTransaction = idTransaction;
            this.idRecetteCandidat = idRecetteCandidat;
            this.idAgence = idAgence;
            this.nom = nom;
            this.nomDeJeuneFille = nomDeJeuneFille;
            this.prenom = prenom;
            this.dateInscription = dateInscription;
            this.forfait = forfait;
            this.categoriePermis = categoriePermis;
            this.montantVerse = montantVerse;
            this.sommeRestante = sommeRestante;
            this.dateVersement = dateVersement;
            this.typeVersement = typeVersement;
            this.numeroRecu = numeroRecu;
        }

        // Getters
        public long getIdCandidat() {
            return idCandidat;
        }

        public long getIdInscription() {
            return idInscription;
        }

        public long getIdTransaction() {
            return idTransaction;
        }

        public long getIdRecetteCandidat() {
            return idRecetteCandidat;
        }

        public int getIdAgence() {
            return idAgence;
        }

        public String getNom() {
            return nom;
        }

        public String getNomDeJeuneFille() {
            return nomDeJeuneFille;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getDateInscription() {
            return dateInscription;
        }

        public String getForfait() {
            return forfait;
        }

        public String getCategoriePermis() {
            return categoriePermis;
        }

        public double getMontantVerse() {
            return montantVerse;
        }

        public double getSommeRestante() {
            return sommeRestante;
        }

        public String getDateVersement() {
            return dateVersement;
        }

        public String getTypeVersement() {
            return typeVersement;
        }

        public String getNumeroRecu() {
            return numeroRecu;
        }
    }

    public static class TableauServiceTier {
        private int id_service_tiers;
        private String nom;
        private String prenom;
        private String contact;
        private String description;
        private int nombreSceance;
        private BigDecimal montant;
        private int annee;

        public TableauServiceTier(int id_service_tiers, String nom, String prenom, String contact, String description,
                int nombreSceance, BigDecimal montant,
                int annee) {
            this.id_service_tiers = id_service_tiers;
            this.nom = nom;
            this.prenom = prenom;
            this.contact = contact;
            this.description = description;
            this.montant = montant;
            this.annee = annee;
            this.nombreSceance = nombreSceance;
        }

        // Getters indispensables pour PropertyValueFactory et les filtres
        public int getIdServTiers() {
            return id_service_tiers;
        }

        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getContact() {
            return contact;
        }

        public String getDescription() {
            return description;
        }

        public BigDecimal getMontant() {
            return montant;
        }

        public int getAnnee() {
            return annee;
        }

        public int getNombreSceance() {
            return nombreSceance;
        }
    }

    public static  class InfoRecuServiceTier {
        private String nom;
        private String prenom;
        private String datePaiement;
        private String description;
        private int montant;
        private String numeroRecu;

        public InfoRecuServiceTier(String nom, String prenom, String datePaiement, String description, int montant,
                String numeroRecu) {
            this.nom = nom;
            this.prenom = prenom;
            this.datePaiement = datePaiement;
            this.description = description;
            this.montant = montant;
            this.numeroRecu = numeroRecu;
        }

        // Getters
        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getDatePaiement() {
            return datePaiement;
        }

        public String getDescription() {
            return description;
        }

        public int getMontant() {
            return montant;
        }

        public String getNumeroRecu() {
            return numeroRecu;
        }
    }

}
