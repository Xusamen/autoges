package com.cybersek.autoges.ModuleCandidats;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.cybersek.autoges.App;
import com.cybersek.autoges.StructureDonnee.Candidat;
import com.cybersek.autoges.StructureDonnee.Dossier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DossierCandidat {

    public static ObservableList<Integer> ObtenirAnneesInscription() {
        ObservableList<Integer> annees = FXCollections.observableArrayList();
        String req = "SELECT DISTINCT YEAR(date_inscription) AS annee "
                + "FROM inscription "
                + "WHERE date_inscription IS NOT NULL "
                + "ORDER BY annee DESC";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                Statement statement = connexion.createStatement();
                ResultSet rs = statement.executeQuery(req)) {

            while (rs.next()) {
                annees.add(rs.getInt("annee"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des années : " + e.getMessage());
        }

        return annees;
    }

    public static ObservableList<Candidat> ListerCandidatsParAnneeInscription(Integer annee) {
        ObservableList<Candidat> liste = FXCollections.observableArrayList();

        StringBuilder sql = new StringBuilder(
                "SELECT c.*, i.categorie_de_permis " +
                        "FROM candidat c " +
                        "JOIN inscription i ON c.id = i.id_candidat " +
                        "WHERE 1=1 ");

        if (annee != null && annee > 0) {
            sql.append("AND YEAR(i.date_inscription) = ? ");
        }

        sql.append("ORDER BY c.id DESC");

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql.toString())) {

            if (annee != null && annee > 0) {
                statement.setInt(1, annee);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Date dateNaiss = rs.getDate("date_de_naissance");
                    Date dateDeliv = rs.getDate("date_de_delivrance");

                    LocalDate dateNaissance = (dateNaiss != null) ? dateNaiss.toLocalDate() : null;
                    LocalDate dateDelivrance = (dateDeliv != null) ? dateDeliv.toLocalDate() : null;

                    // Récupération de la catégorie de permis
                    String catPermis = rs.getString("categorie_de_permis");

                    Candidat candidat = new Candidat(
                            rs.getLong("id"),
                            rs.getString("identifiant"),
                            rs.getString("nom"),
                            rs.getString("nom_de_jeune_fille"),
                            rs.getString("prenom"),
                            dateNaissance,
                            rs.getString("lieu_de_naissance"),
                            rs.getString("type_document_id"),
                            rs.getString("numero_document_id"),
                            dateDelivrance,
                            rs.getString("lieu_de_delivrance"),
                            rs.getString("profession"),
                            rs.getString("contact"),
                            rs.getString("lieu_residence"),
                            rs.getString("genre"),
                            rs.getString("nom_pap"),
                            rs.getString("prenom_pap"),
                            rs.getString("contact_pap"),
                            catPermis // Transmis au constructeur
                    );

                    liste.add(candidat);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des candidats : " + e.getMessage());
        }

        return liste;
    }

    public static ObservableList<Candidat> FiltrerCandidats(Integer annee, String nom, String prenom) {
        ObservableList<Candidat> liste = FXCollections.observableArrayList();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT c.*, i.categorie_de_permis, i.date_inscription " +
                        "FROM candidat c " +
                        "JOIN inscription i ON c.id = i.id_candidat " +
                        "WHERE 1=1 ");

        List<Object> parametres = new ArrayList<>();

        // 1. Filtre par Année (ChoiceBox)
        if (annee != null && annee > 0) {
            sql.append("AND YEAR(i.date_inscription) = ? ");
            parametres.add(annee);
        }

        // 2. Filtre par Nom (Saisie utilisateur)
        if (nom != null && !nom.isBlank()) {
            sql.append("AND c.nom LIKE ? ");
            parametres.add("%" + nom.trim() + "%");
        }

        // 3. Filtre par Prénom (Saisie utilisateur)
        if (prenom != null && !prenom.isBlank()) {
            sql.append("AND c.prenom LIKE ? ");
            parametres.add("%" + prenom.trim() + "%");
        }

        // Tri du plus récent au plus ancien
        sql.append("ORDER BY i.date_inscription DESC, c.id DESC");

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql.toString())) {

            // Binding dynamique des paramètres
            for (int i = 0; i < parametres.size(); i++) {
                statement.setObject(i + 1, parametres.get(i));
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Date dateNaiss = rs.getDate("date_de_naissance");
                    Date dateDeliv = rs.getDate("date_de_delivrance");

                    LocalDate dateNaissance = (dateNaiss != null) ? dateNaiss.toLocalDate() : null;
                    LocalDate dateDelivrance = (dateDeliv != null) ? dateDeliv.toLocalDate() : null;

                    Candidat candidat = new Candidat(
                            rs.getLong("id"),
                            rs.getString("identifiant"),
                            rs.getString("nom"),
                            rs.getString("nom_de_jeune_fille"),
                            rs.getString("prenom"),
                            dateNaissance,
                            rs.getString("lieu_de_naissance"),
                            rs.getString("type_document_id"),
                            rs.getString("numero_document_id"),
                            dateDelivrance,
                            rs.getString("lieu_de_delivrance"),
                            rs.getString("profession"),
                            rs.getString("contact"),
                            rs.getString("lieu_residence"),
                            rs.getString("genre"),
                            rs.getString("nom_pap"),
                            rs.getString("prenom_pap"),
                            rs.getString("contact_pap"),
                            rs.getString("categorie_de_permis"));

                    liste.add(candidat);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du filtrage des candidats : " + e.getMessage());
        }

        return liste;
    }

    public static boolean ModifierCandidatInfo(Candidat candidat) {
        String sql = "UPDATE candidat SET " +
                "numero_document_id = ?, lieu_de_delivrance = ?, profession = ?, " +
                "contact = ?, lieu_residence = ?, nom_pap = ?, prenom_pap = ?, contact_pap = ? " +
                "WHERE id = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setString(1, candidat.getNumeroDocumentId());
            statement.setString(2, candidat.getLieuDelivrance());
            statement.setString(3, candidat.getProfession());
            statement.setString(4, candidat.getContact());
            statement.setString(5, candidat.getLieuResidence());
            statement.setString(6, candidat.getNomPap());
            statement.setString(7, candidat.getPrenomPap());
            statement.setString(8, candidat.getContactPap());
            statement.setLong(9, candidat.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du candidat : " + e.getMessage());
            return false;
        }
    }

    public static Candidat obtenirCandidatParId(long id) {
        String sql = "SELECT " +
                "  c.id, c.identifiant, c.nom, c.nom_de_jeune_fille, c.prenom, " +
                "  c.date_de_naissance, c.lieu_de_naissance, c.type_document_id, " +
                "  c.numero_document_id, c.date_de_delivrance, c.lieu_de_delivrance, " +
                "  c.profession, c.contact, c.lieu_residence, c.genre, " +
                "  c.nom_pap, c.prenom_pap, c.contact_pap, " +
                "  i.categorie_de_permis, i.numero_ancien_permis, i.forfait, i.montant_formation, " +
                "  a.nom_agence, g.nom_groupe " +
                "FROM candidat c " +
                "LEFT JOIN inscription i ON c.id = i.id_candidat " +
                "LEFT JOIN agence a ON i.id_agence = a.id_agence " +
                "LEFT JOIN groupe g ON i.id_groupe = g.id " +
                "WHERE c.id = ? " +
                "ORDER BY i.id DESC LIMIT 1";

        Candidat candidat = null;

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Conversion sécurisée des types java.sql.Date vers java.time.LocalDate
                    LocalDate dateNaissance = rs.getDate("date_de_naissance") != null
                            ? rs.getDate("date_de_naissance").toLocalDate()
                            : null;

                    LocalDate dateDelivrance = rs.getDate("date_de_delivrance") != null
                            ? rs.getDate("date_de_delivrance").toLocalDate()
                            : null;

                    // Instanciation de l'objet Candidat avec le constructeur complet (24
                    // paramètres)
                    candidat = new Candidat(
                            rs.getLong("id"),
                            rs.getString("identifiant"),
                            rs.getString("nom"),
                            rs.getString("nom_de_jeune_fille"),
                            rs.getString("prenom"),
                            dateNaissance,
                            rs.getString("lieu_de_naissance"),
                            rs.getString("type_document_id"),
                            rs.getString("numero_document_id"),
                            dateDelivrance,
                            rs.getString("lieu_de_delivrance"),
                            rs.getString("profession"),
                            rs.getString("contact"),
                            rs.getString("lieu_residence"),
                            rs.getString("genre"),
                            rs.getString("nom_pap"),
                            rs.getString("prenom_pap"),
                            rs.getString("contact_pap"),
                            rs.getString("categorie_de_permis"),
                            rs.getString("numero_ancien_permis"),
                            rs.getString("nom_agence"),
                            rs.getString("nom_groupe"),
                            rs.getString("forfait"),
                            rs.getDouble("montant_formation"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du candidat ID " + id + " : " + e.getMessage());
            e.printStackTrace();
        }

        return candidat;
    }

    // Récupérer le dossier lié au candidat
    public static Dossier obtenirDossierParCandidatId(long idCandidat) {
        String sql = "SELECT d.id, d.id_inscription, d.date_depot, d.permis_provisoire, " +
                "       d.date_sortie, d.commentaire " +
                "FROM dossier d " +
                "JOIN inscription i ON d.id_inscription = i.id " +
                "WHERE i.id_candidat = ? " +
                "ORDER BY d.id DESC LIMIT 1";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setLong(1, idCandidat);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    LocalDate dateDepot = rs.getDate("date_depot") != null ? rs.getDate("date_depot").toLocalDate()
                            : null;
                    LocalDate dateSortie = rs.getDate("date_sortie") != null ? rs.getDate("date_sortie").toLocalDate()
                            : null;

                    return new Dossier(
                            rs.getLong("id"),
                            rs.getLong("id_inscription"),
                            dateDepot,
                            rs.getString("permis_provisoire"),
                            dateSortie,
                            rs.getString("commentaire"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer l'ID inscription d'un candidat
    public long obtenirIdInscriptionParCandidatId(long idCandidat) {
        String sql = "SELECT id FROM inscription WHERE id_candidat = ? ORDER BY id DESC LIMIT 1";
        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setLong(1, idCandidat);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Enregistrer ou mettre à jour un objet Dossier
    public boolean sauvegarderOuMettreAJourDossier(long idCandidat, Dossier dossier) {
        String sql = "INSERT INTO dossier (id_inscription, date_depot, permis_provisoire, date_sortie, commentaire) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "date_depot = VALUES(date_depot), " +
                "permis_provisoire = VALUES(permis_provisoire), " +
                "date_sortie = VALUES(date_sortie), " +
                "commentaire = VALUES(commentaire)";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setLong(1, dossier.getIdInscription());
            statement.setDate(2,
                    dossier.getDateDepotRaw() != null ? java.sql.Date.valueOf(dossier.getDateDepotRaw()) : null);
            statement.setString(3, dossier.getPermisProvisoire());
            statement.setDate(4,
                    dossier.getDateSortieRaw() != null ? java.sql.Date.valueOf(dossier.getDateSortieRaw()) : null);
            statement.setString(5, dossier.getCommentaire());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
