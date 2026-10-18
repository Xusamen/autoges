package com.cybersek.autoges;

import com.cybersek.autoges.ModuleParametre.GestionPersonnel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BoiteDialogueAjouterPersonnelControleur {

    @FXML
    private TextField champ_adresse;

    @FXML
    private TextField champ_contact;

    @FXML
    private TextField champ_fonction;

    @FXML
    private TextField champ_nom;

    @FXML
    private TextField champ_prenom;

    @FXML
    private TextField champ_salaire;

    @FXML
    void AjouterPersonnel(ActionEvent event) {
        String nom = champ_nom.getText().trim();
        String prenom = champ_prenom.getText().trim();
        String contact = champ_contact.getText().trim();
        String adresse = champ_adresse.getText().trim();
        String fonction = champ_fonction.getText().trim().toLowerCase();

        // Validation simple des champs obligatoires
        if (nom.isEmpty() || prenom.isEmpty() || fonction.isEmpty()) {
            System.err.println("Veuillez remplir au moins le nom, le prénom et la fonction.");
            return;
        }
        double salaireFixe = 0.0;
        try {
            if (!champ_salaire.getText().isBlank()) {
                salaireFixe = Double.parseDouble(champ_salaire.getText().trim().replace(",", "."));
            }
        } catch (NumberFormatException e) {
            Functions.Alerte_erreur("Montant Invalide");
            return;
        }
        boolean succes = GestionPersonnel.CreationPersonnel(nom, prenom, contact, adresse, fonction, salaireFixe);

        if (succes) {
            FenetreParametreControleur.FermerBoiteDialogueAjouterPersonnel();
        }
    }

    @FXML
    void AnnulerAjouterPersonnel(ActionEvent event) {
        FenetreParametreControleur.FermerBoiteDialogueAjouterPersonnel();
    }

}
