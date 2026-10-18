package com.cybersek.autoges;

import com.cybersek.autoges.StructureDonnee.Personnel;
import com.cybersek.autoges.ModuleParametre.GestionPersonnel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class BoiteDialogueModifierPersonnelControleur {

    @FXML
    private Button bouton_annuler;

    @FXML
    private Button bouton_modifier;

    @FXML
    private TextField champ_adresse;

    @FXML
    private TextField champ_coontact;

    @FXML
    private TextField champ_fonction;

    @FXML
    private TextField champ_nom;

    @FXML
    private TextField champ_prenoms;

    @FXML
    private TextField champ_salaire;

    private int idPersonnel;

    public void chargerDonnees(int id) {
        this.idPersonnel = id;
        Personnel personnel = GestionPersonnel.ObtenirPersonnel(id);

        if (personnel != null) {
            champ_nom.setText(personnel.getNom());
            champ_prenoms.setText(personnel.getPrenom());
            champ_coontact.setText(personnel.getTelephone());
            champ_adresse.setText(personnel.getAdresse());
            champ_fonction.setText(personnel.getFonction());
            champ_salaire.setText(String.valueOf(personnel.getSalaireFixe()));
        }
    }

    @FXML
    void FermerBoiteDialogueModifierPersonnel(ActionEvent event) {
        FenetreParametreControleur.FermerBoiteDialogueModifierPersonnel();
    }

    @FXML
    void ModifierInformationPersonnel(ActionEvent event) {
        String nom = champ_nom.getText().trim();
        String prenoms = champ_prenoms.getText().trim();
        String contact = champ_coontact.getText().trim();
        String adresse = champ_adresse.getText().trim();
        String fonction = champ_fonction.getText().trim();
        String salaireTxt = champ_salaire.getText().trim();

        // Vérification des champs obligatoires
        if (nom.isEmpty() || prenoms.isEmpty() || contact.isEmpty()) {
            Functions.Alerte_erreur("Veuillez remplir au moins le nom, les prénoms et le contact.");
            return;
        }

        // Conversion sécurisée du salaire
        double salaire = 0;
        try {
            salaire = Double.parseDouble(salaireTxt);
        } catch (NumberFormatException e) {
            Functions.Alerte_erreur("Veuillez saisir un salaire valide (nombre decimal).");
            return;
        }

        // Appel de la méthode de mise à jour dans la BDD
        boolean succes = GestionPersonnel.ModifierPersonnel(idPersonnel, nom, prenoms, contact, adresse, fonction, salaire);

        if (succes) {
            FermerBoiteDialogueModifierPersonnel(event);
        }
    }
}