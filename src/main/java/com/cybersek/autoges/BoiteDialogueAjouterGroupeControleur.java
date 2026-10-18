package com.cybersek.autoges;


import com.cybersek.autoges.StructureDonnee.Agence;

import com.cybersek.autoges.ModuleParametre.GestionAgence;
import com.cybersek.autoges.ModuleParametre.GestionGroupe;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class BoiteDialogueAjouterGroupeControleur {

    @FXML
    private ChoiceBox<Agence> choice_agence;

    @FXML
    private TextField montant_groupe;

    @FXML
    private TextField nom_groupe;

    @FXML
    private TextField observation_groupe;

    @FXML
    public void initialize() {
        choice_agence.setItems(GestionAgence.ListerAgence());
    }

    @FXML
    private void creation_groupe() {
        String nom = nom_groupe.getText().trim();
        Agence agenceSelectionnee = choice_agence.getValue();
        String observation = observation_groupe.getText().trim();

        double montant = 0.0;
        try {
            if (!montant_groupe.getText().isBlank()) {
                montant = Double.parseDouble(montant_groupe.getText().replace(",", "."));
            }
        } catch (NumberFormatException e) {
            Functions.Alerte_erreur("Montant Invalide");
            return;
        }

        if (!nom.isEmpty() && agenceSelectionnee != null) {
            boolean succes = GestionGroupe.CreerGroupe(nom, agenceSelectionnee.getIdAgence(), montant, observation);

            if (succes) {
                FenetreParametreControleur.FermerBoiteDialogueAjoutGroupe();
            }
        }
    }

    @FXML
    
    private void annuler_creation_groupe() {
        FenetreParametreControleur.FermerBoiteDialogueAjoutGroupe();
    }

}
