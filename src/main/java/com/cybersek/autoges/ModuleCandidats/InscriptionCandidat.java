package com.cybersek.autoges.ModuleCandidats;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;

import com.cybersek.autoges.App;
import com.cybersek.autoges.Functions;

public class InscriptionCandidat {

    private static final String PREFIXE = "NOURAT";
    public static String GenererIdentifiantCandidat(int numeroSequence) {
        int anneeEnCours = LocalDate.now().getYear();
        String sequenceFormatee = String.format("%04d", numeroSequence);
        return PREFIXE + anneeEnCours + "CAND" + sequenceFormatee;
    }

    public static String ObtenirProchainIdentifiantCandidat() {
        int anneeEnCours = LocalDate.now().getYear();
        String motifRecherche = PREFIXE + anneeEnCours + "CAND%";
        String requete = "SELECT COUNT(*) + 1 AS prochain_numero FROM candidat WHERE identifiant LIKE ?";
        int prochainNumero = 1;

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setString(1, motifRecherche);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    prochainNumero = rs.getInt("prochain_numero");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du prochain numéro candidat : " + e.getMessage());
        }

        return GenererIdentifiantCandidat(prochainNumero);
    }

    public static boolean EnregistrerInscription(
            // --- Infos Candidat ---
            String nom, String nomJeuneFille, String prenom, LocalDate dateNaissance, String lieuNaissance,
            String typeDoc, String numDoc, LocalDate dateDelivrance, String lieuDelivrance,
            String profession, String contact, String lieuResidence, String genre,
            String nomPap, String prenomPap, String contactPap,
            // --- Infos Inscription ---
            int idAgence, Integer idGroupe, String categoriePermis, String numAncienPermis,
            String forfait, int montantFormation) {

        String reqCandidat = "INSERT INTO candidat (identifiant, nom, nom_de_jeune_fille, prenom, date_de_naissance, "
                + "lieu_de_naissance, type_document_id, numero_document_id, date_de_delivrance, lieu_de_delivrance, "
                + "profession, contact, lieu_residence, genre, nom_pap, prenom_pap, contact_pap) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String reqInscription = "INSERT INTO inscription (id_candidat, id_groupe, id_agence, categorie_de_permis, "
                + "numero_ancien_permis, forfait, montant_formation) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connexion = null;

        try {
            connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
            connexion.setAutoCommit(false);

            String identifiantCandidat = ObtenirProchainIdentifiantCandidat();

            long idCandidatGenerated = -1;
            try (PreparedStatement stmtCandidat = connexion.prepareStatement(reqCandidat,
                    Statement.RETURN_GENERATED_KEYS)) {
                stmtCandidat.setString(1, identifiantCandidat);
                stmtCandidat.setString(2, nom);
                stmtCandidat.setString(3, (nomJeuneFille == null || nomJeuneFille.isBlank()) ? null : nomJeuneFille);
                stmtCandidat.setString(4, prenom);
                stmtCandidat.setDate(5, Date.valueOf(dateNaissance));
                stmtCandidat.setString(6, lieuNaissance);
                stmtCandidat.setString(7, typeDoc);
                stmtCandidat.setString(8, numDoc);
                stmtCandidat.setDate(9, Date.valueOf(dateDelivrance));
                stmtCandidat.setString(10, lieuDelivrance);
                stmtCandidat.setString(11, profession);
                stmtCandidat.setString(12, contact);
                stmtCandidat.setString(13, lieuResidence);
                stmtCandidat.setString(14, genre);
                stmtCandidat.setString(15, nomPap);
                stmtCandidat.setString(16, prenomPap);
                stmtCandidat.setString(17, contactPap);

                int rowsCandidat = stmtCandidat.executeUpdate();
                if (rowsCandidat == 0) {
                    connexion.rollback();
                    Functions.Alerte_erreur("Échec de la création du candidat.");
                    return false;
                }

                try (ResultSet rsKeys = stmtCandidat.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        idCandidatGenerated = rsKeys.getLong(1);
                    } else {
                        connexion.rollback();
                        Functions.Alerte_erreur("Impossible de récupérer la clé générée du candidat.");
                        return false;
                    }
                }
            }

            // 3. Insertion de l'Inscription (sans bind sur date_inscription)
            try (PreparedStatement stmtIns = connexion.prepareStatement(reqInscription)) {
                stmtIns.setLong(1, idCandidatGenerated);

                if (idGroupe != null) {
                    stmtIns.setInt(2, idGroupe);
                } else {
                    stmtIns.setNull(2, Types.INTEGER);
                }

                stmtIns.setInt(3, idAgence);
                stmtIns.setString(4, categoriePermis);
                stmtIns.setString(5, (numAncienPermis == null || numAncienPermis.isBlank()) ? null : numAncienPermis);
                stmtIns.setString(6, forfait);
                stmtIns.setInt(7, montantFormation);

                stmtIns.executeUpdate();
            }

            // Validation définitive de la transaction
            connexion.commit();
            Functions.Alerte_succes("Inscription validée avec succès ! (Matricule : " + identifiantCandidat + ")");
            return true;

        } catch (SQLException e) {
            if (connexion != null) {
                try {
                    connexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erreur lors du Rollback : " + ex.getMessage());
                }
            }
            Functions.Alerte_erreur("Erreur lors de l'enregistrement de l'inscription.");
            System.err.println("Erreur SQL : " + e.getMessage());
            return false;
        } finally {
            if (connexion != null) {
                try {
                    connexion.setAutoCommit(true);
                    connexion.close();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

}
