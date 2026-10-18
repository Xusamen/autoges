package com.cybersek.autoges.ModuleParametre;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cybersek.autoges.App;
import com.cybersek.autoges.Functions;
import com.cybersek.autoges.StructureDonnee.Agence;

public class GestionAgence {

    // 1. Création d'une agence
    public static boolean CreationAgence(String nomAgence, String positionAgence) {
        String requete = "INSERT INTO agence (nom_agence, position_agence) VALUES (?, ?)";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setString(1, nomAgence);
            statement.setString(2, positionAgence);
            Functions.Alerte_succes("Agence " + nomAgence + "Creer");
            return statement.executeUpdate() > 0;

        } catch (SQLException erreur) {
            Functions.Alerte_erreur("Erreur de creation");
            System.err.println(erreur.getMessage());
        }
        return false;
    }

    // 2. Suppression d'une agence par son ID
    public static boolean SupprimerAgence(Agence data) {
        String requete = "DELETE FROM agence WHERE id_agence = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setInt(1, data.getIdAgence());
            Functions.Alerte_succes("Agence " + data.getNomAgence() + " Supprimer");
            return statement.executeUpdate() > 0;

        } catch (SQLException erreur) {
            Functions.Alerte_erreur("Erreur supression agence");
            System.err.println(erreur.getMessage());
        }
        return false;
    }

    // 3. Récupération de la liste des agences (ObservableList pour TableView)
    public static ObservableList<Agence> ListerAgence() {
        ObservableList<Agence> liste = FXCollections.observableArrayList();
        String requete = "SELECT id_agence, nom_agence, position_agence FROM agence";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete);
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Agence agence = new Agence(
                        rs.getInt("id_agence"),
                        rs.getString("nom_agence"),
                        rs.getString("position_agence"));
                liste.add(agence);
            }

        } catch (SQLException erreur) {
            System.err.println(erreur.getMessage());
        }

        return liste;
    }

    public static String ObtenirNomAgence(int idAgence) {
        String requete = "SELECT nom_agence FROM agence WHERE id_agence = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setInt(1, idAgence);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_agence");
                }
            }

        } catch (SQLException erreur) {
            System.err.println(erreur.getMessage());
        }
        return "Agence inconnue";
    }
}
