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

public class BoiteDialogueAjouterDateExamenCodeControleur {

    @FXML
    private TextField champ_date_examen_conde;

    @FXML
    private ChoiceBox<String> choicebox_categorie;

    @FXML
    public void initialize() {
        // Initialisation de la ChoiceBox avec les options de catégorie
        choicebox_categorie.setItems(FXCollections.observableArrayList("TOUTES CATEGORIES","POID LEGER", "POID LOURD"));
        choicebox_categorie.setValue("TOUTES CATEGORIES");
    }

    @FXML
    void AjouterDateExamenCode(ActionEvent event) {
        String dateStr = champ_date_examen_conde.getText();
        String selectedCat = choicebox_categorie.getValue();

        if (dateStr == null || dateStr.trim().isEmpty()) {
            Functions.Alerte_erreur("Erreur de saisie ... Veuillez renseigner la date de l'examen.");
            return;
        }

        try {
            // Conversion du texte au format JJ/MM/AAAA en LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateExamen = LocalDate.parse(dateStr.trim(), formatter);

            // Si "TOUTES / INDÉPENDANT" est sélectionné, on envoie null en base
            String categorie = ("TOUTES CATEGORIES".equals(selectedCat)) ? null : selectedCat;

            // Appel de la méthode DAO pour insérer la session CODE
            boolean succes = SessionExamen.creerSession("CODE", categorie, dateExamen, "OUVERTE");

            if (succes) {
                FenetreListeExamenControleur.FermerBoiteDialogueAjouterDateExamenCode();
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
        FenetreListeExamenControleur.FermerBoiteDialogueAjouterDateExamenCode();
    }

}
