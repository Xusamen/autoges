package com.cybersek.autoges;


import com.cybersek.autoges.ModuleParametre.GestionAgence;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BoiteDialogueAjoutAgenceControleur {

    @FXML
    private TextField champ_localisation;

    @FXML
    private TextField champ_nom_agence;

    @FXML
    void AjouterAgence(ActionEvent event) {
        String nom_agence = champ_nom_agence.getText().trim();
        String localisation_agence = champ_localisation.getText().trim();

        if (nom_agence.isEmpty() || localisation_agence.isEmpty()) {
            Functions.Alerte_erreur("Veuillez remplir le nom et la localisation de l'agence.");
            return;
        }
        boolean statut = GestionAgence.CreationAgence(nom_agence, localisation_agence);
        
        if (statut) {
            FenetreParametreControleur.FermerBoiteDialogueAjouterAgence();
        }
    }

    @FXML
    void AnnulerAjoutAgence(ActionEvent event) {
        FenetreParametreControleur.FermerBoiteDialogueAjouterAgence();
    }
}