package com.cybersek.autoges;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.cybersek.autoges.ModuleExamens.SessionExamen;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class BoiteDialogueAjouterDateExamenConduiteControleur {

    @FXML
    private TextField champ_date_examen_conduite;

    @FXML
    private ChoiceBox<String> choicebox_categorie;

    @FXML
    private ChoiceBox<String> choicebox_description_examen_conduite;

    @FXML
    public void initialize() {
        // Initialisation des catégories (Poids Léger / Poids Lourd)
        choicebox_categorie.setItems(FXCollections.observableArrayList("POID LEGER", "POID LOURD"));
        choicebox_categorie.setValue("POID LEGER");
        // Choix du type d'examen exact correspondant à l'ENUM ('CRENEAU' ou 'CONDUITE')
        choicebox_description_examen_conduite.setItems(FXCollections.observableArrayList("CRENEAU", "CONDUITE"));
        choicebox_description_examen_conduite.setValue("CRENEAU");
    }

    @FXML
    void AjouterDateExamenConduite(ActionEvent event) {
        String dateStr = champ_date_examen_conduite.getText();
        String categorie = choicebox_categorie.getValue();
        String typeExamen = choicebox_description_examen_conduite.getValue(); // Récupère 'CRENEAU' ou 'CONDUITE'

        if (dateStr == null || dateStr.trim().isEmpty()) {
            Functions.Alerte_erreur("Erreur de saisie ... Veuillez renseigner la date de l'examen.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateExamen = LocalDate.parse(dateStr.trim(), formatter);

            // Appel direct à la méthode DAO standard avec le type d'examen choisi
            boolean succes = SessionExamen.creerSession(typeExamen, categorie, dateExamen, "OUVERTE");

            if (succes) {
                FenetreListeExamenControleur.FermerBoiteDialogueAjouterDateExamenConduite();
            } else {
                Functions.Alerte_erreur("Erreur Base de Données ... Impossible d'enregistrer la session d'examen.");
            }

        } catch (DateTimeParseException e) {
            Functions.Alerte_erreur(
                    "Format de date invalide ... Veuillez respecter le format JJ/MM/AAAA (ex: 17/11/2026).");
        }
    }

    @FXML
    void FermerFenetre(ActionEvent event) {
        FenetreListeExamenControleur.FermerBoiteDialogueAjouterDateExamenConduite();
    }

}
