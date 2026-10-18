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
import com.cybersek.autoges.StructureDonnee.Personnel;

public class GestionPersonnel {

    // 1. Création d'un membre du personnel
    public static boolean CreationPersonnel(String nom, String prenom, String telephone, String adresse,
            String fonction, double salaireFixe) {
        String requete = "INSERT INTO personnel (nom, prenom, telephone, adresse, fonction, salaire_fixe) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, telephone);
            statement.setString(4, adresse);
            statement.setString(5, fonction);
            statement.setDouble(6, salaireFixe);

            Functions.Alerte_succes(nom + " " + prenom + " Ajouté au personnel existant");
            return statement.executeUpdate() > 0;

        } catch (SQLException erreur) {
            Functions.Alerte_erreur("Erreur rencontré lors de l'ajout");
            System.err.println(erreur.getMessage());
        }
        return false;
    }

    // 2. Basculer le statut ('actif' <-> 'inactif')
    public static boolean ChangerStatutPersonnel(int id) {
        String requete = "UPDATE personnel SET statut = IF(statut = 'actif', 'inactif', 'actif') WHERE id = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException erreur) {
            System.err.println(erreur.getMessage());
        }
        return false;
    }

    // 3. Modification des informations d'un membre du personnel
    public static boolean ModifierPersonnel(int id, String nom, String prenom, String telephone, String adresse,
            String fonction, double salaireFixe) {
        String requete = "UPDATE personnel SET nom = ?, prenom = ?, telephone = ?, adresse = ?, fonction = ?, salaire_fixe = ? WHERE id = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, telephone);
            statement.setString(4, adresse);
            statement.setString(5, fonction);
            statement.setDouble(6, salaireFixe);
            statement.setInt(7, id);

            int lignesAffectees = statement.executeUpdate();

            if (lignesAffectees > 0) {
                Functions.Alerte_succes("Information modifiée avec succès");
                return true;
            }

        } catch (SQLException erreur) {
            Functions.Alerte_erreur("Erreur rencontrée lors de la modification");
            System.err.println(erreur.getMessage());
        }
        return false;
    }

    // 4. Liste complète du personnel (ObservableList pour TableView JavaFX)
    public static ObservableList<Personnel> ListerPersonnel() {
        ObservableList<Personnel> liste = FXCollections.observableArrayList();
        String requete = "SELECT id, nom, prenom, telephone, adresse, fonction, salaire_fixe, statut, created_at FROM personnel";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete);
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Personnel p = new Personnel(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getString("fonction"),
                        rs.getDouble("salaire_fixe"),
                        rs.getString("statut"),
                        rs.getTimestamp("created_at"));
                liste.add(p);
            }

        } catch (SQLException erreur) {
            System.err.println(erreur.getMessage());
        }

        return liste;
    }

    public static Personnel ObtenirPersonnel(int id) {
        String requete = "SELECT id, nom, prenom, telephone, adresse, fonction, salaire_fixe, statut FROM personnel WHERE id = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(requete)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Personnel(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("telephone"),
                            rs.getString("adresse"),
                            rs.getString("fonction"),
                            rs.getDouble("salaire_fixe"),
                            rs.getString("statut"));
                }
            }
        } catch (SQLException erreur) {
            System.err.println(erreur.getMessage());
        }
        return null;
    }
}