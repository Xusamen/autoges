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
import com.cybersek.autoges.StructureDonnee.Groupe;

public class GestionGroupe {

    // 1. Création d'un groupe
    public static boolean CreerGroupe(String nomGroupe, int idAgence, double prixUnitaire, String observation) {
        String requete = "INSERT INTO groupe (nom_groupe, id_agence, prix_unitaire, observation) VALUES (?, ?, ?, ?)";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setString(1, nomGroupe);
            statement.setInt(2, idAgence);
            statement.setDouble(3, prixUnitaire);
            statement.setString(4, observation);
            Functions.Alerte_succes("Groupe " + nomGroupe + " creer");

            return statement.executeUpdate() > 0;

        } catch (SQLException erreur) {
            Functions.Alerte_erreur("Erreur creation groupe " + nomGroupe);
            System.err.println(erreur.getMessage());
        }
        return false;
    }

    // 2. Suppression d'un groupe par son ID
    public static boolean SupprimerGroupe(int id) {
        String requete = "DELETE FROM groupe WHERE id = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setInt(1, id);
            Functions.Alerte_succes("Groupe supprimer");
            return statement.executeUpdate() > 0;

        } catch (SQLException erreur) {
            Functions.Alerte_erreur("Erreur suppression groupe ");
            System.err.println(erreur.getMessage());
        }
        return false;
    }

    // 3. Liste complète des groupes (ObservableList pour TableView JavaFX)
    public static ObservableList<Groupe> ListerGroupe() {
        ObservableList<Groupe> liste = FXCollections.observableArrayList();
        String requete = "SELECT id, nom_groupe, id_agence, prix_unitaire, observation, date_creation FROM groupe";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete);
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Groupe groupe = new Groupe(
                        rs.getInt("id"),
                        rs.getString("nom_groupe"),
                        rs.getInt("id_agence"),
                        rs.getDouble("prix_unitaire"),
                        rs.getString("observation"),
                        rs.getTimestamp("date_creation"));
                liste.add(groupe);
            }

        } catch (SQLException erreur) {
            System.err.println(erreur.getMessage());
        }

        return liste;
    }
}