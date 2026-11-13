package com.cybersek.autoges.ModuleExamens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

import com.cybersek.autoges.App;
import com.cybersek.autoges.StructureDonnee.InfoSessionExamen;

public class SessionExamen {

    /**
     * Récupérer la liste des sessions avec le nombre de candidats inscrits
     */
    public static ObservableList<InfoSessionExamen> recupererSessions(String typeExamen, String categorie) {
        ObservableList<InfoSessionExamen> sessions = FXCollections.observableArrayList();

        String sql = "SELECT s.*, (SELECT COUNT(*) FROM candidat_examen ce WHERE ce.id_session_examen = s.id) AS nb_candidats "
                +
                "FROM session_examen s WHERE s.type_examen = ?";

        boolean hasCategory = (categorie != null && !categorie.isEmpty());
        if (hasCategory) {
            sql += " AND s.categorie_examen = ?";
        }
        // Tri par ordre décroissant (les plus récentes / lointaines en premier)
        sql += " ORDER BY s.date_examen DESC";

        try (Connection cx = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setString(1, typeExamen);
            if (hasCategory) {
                ps.setString(2, categorie);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(new InfoSessionExamen(
                            rs.getInt("id"),
                            rs.getString("type_examen"),
                            rs.getString("categorie_examen"),
                            rs.getDate("date_examen") != null ? rs.getDate("date_examen").toLocalDate() : null,
                            rs.getString("statut"),
                            rs.getInt("nb_candidats")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessions;
    }

    /**
     * Créer une nouvelle session d'examen
     */
    public static boolean creerSession(String typeExamen, String categorieExamen, LocalDate dateExamen, String statut) {
        String sql = "INSERT INTO session_examen (type_examen, categorie_examen, date_examen, statut) VALUES (?, ?, ?, ?)";

        try (Connection cx = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setString(1, typeExamen);
            ps.setString(2, categorieExamen);
            ps.setDate(3, Date.valueOf(dateExamen));
            ps.setString(4, statut != null ? statut : "OUVERTE");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifier une session existante
     */
    public static boolean modifierSession(int id, LocalDate dateExamen, String statut) {
        String sql = "UPDATE session_examen SET date_examen = ?, statut = ? WHERE id = ?";

        try (Connection cx = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dateExamen));
            ps.setString(2, statut);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprimer une session d'examen
     */
    public static boolean supprimerSession(int id) {
        String sql = "DELETE FROM session_examen WHERE id = ?";

        try (Connection cx = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}