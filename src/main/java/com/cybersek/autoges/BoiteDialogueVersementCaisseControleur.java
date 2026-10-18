package com.cybersek.autoges;


import com.cybersek.autoges.StructureDonnee.Candidat;
import com.cybersek.autoges.ModuleCandidats.DossierCandidat;
import com.cybersek.autoges.ModuleComptabilite.Recette;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class BoiteDialogueVersementCaisseControleur {

    @FXML
    private Button bouton_enregistrement_versement;

    @FXML
    private ChoiceBox<String> choicebox_mode_paiement;

    @FXML
    private ChoiceBox<String> choicebox_motif_paiement;

    @FXML
    private Text info_matricule;

    @FXML
    private Text info_nom;

    @FXML
    private Text info_prenom;

    @FXML
    private TextField somme_verse;

    // Stockage interne pour lier le candidat et son inscription lors de l'enregistrement
    private long candidatId;
    private long inscriptionId;

    @FXML
    public void initialize() {
        // 1. Initialiser les choix de modes de paiement (basés sur l'ENUM de votre table transactions)
        choicebox_mode_paiement.setItems(FXCollections.observableArrayList(
            "especes", "cheque", "virement", "mobile_money"
        ));
        choicebox_mode_paiement.getSelectionModel().selectFirst();
        choicebox_motif_paiement.setItems(FXCollections.observableArrayList(
            "Paiement Tranche 1 ","Paiement Tranche 2", "Paiement Tranche 3","Frais Dossier", "Prestation Service", "Frais Examen", "Paiement Tranche 4", "Paiement Tranche 5"
        ));
        choicebox_motif_paiement.getSelectionModel().selectFirst();
    }

    /**
     * Charge les données du candidat et récupère par la même occasion son id_inscription associé.
     */
    public void chargerDonnees(long idCandidat) {
        this.candidatId = idCandidat;
        Candidat candidat = DossierCandidat.obtenirCandidatParId(idCandidat);
        if (candidat != null) {
            info_matricule.setText(candidat.getIdentifiant());
            info_nom.setText(candidat.getNom());
            info_prenom.setText(candidat.getPrenom());
        } else {
            Functions.Alerte_erreur("Impossible de récupérer les informations du candidat ID : " + idCandidat);
            return;
        }
        this.inscriptionId = Recette.obtenirIdInscriptionParCandidat(idCandidat);
    }

    @FXML
    void Enregistrement_versement(ActionEvent event) {
        try {
            // Validation du montant
            String texteMontant = somme_verse.getText();
            if (texteMontant == null || texteMontant.trim().isEmpty()) {
                Functions.Alerte_erreur("Veuillez saisir un montant valide.");
                return;
            }

            double montant = Double.parseDouble(texteMontant.replaceAll("\\s", "").replace(',', '.'));
            if (montant <= 0) {
                Functions.Alerte_erreur("Le montant du versement doit être supérieur à zéro.");
                return;
            }

            String modePaiement = choicebox_mode_paiement.getValue();
            String typeVersement = choicebox_motif_paiement.getValue();

            // ID de l'utilisateur connecté (à adapter selon votre gestion de session, ex: App.utilisateurConnecteId)
            int utilisateurId = NomUser.getIdUtilisateur(); 

            // Appel de la méthode d'insertion en base de données
            boolean succes = Recette.enregistrerVersementCandidat(
                candidatId, inscriptionId, montant, modePaiement, typeVersement, utilisateurId
            );

            if (succes) {
                Functions.Alerte_succes("Versement de " + montant + " FCFA enregistrer ");
                FenetreCaisseControleur.FermerBoiteDialogueVersementCaisse();
            } else {
                Functions.Alerte_erreur("Erreur lors de l'enregistrement du versement en base de données.");
            }

        } catch (NumberFormatException e) {
            Functions.Alerte_erreur("Le format du montant saisi est incorrect.");
        }
    }

    @FXML
    void FermerFenetre(ActionEvent event) {
        Stage stage = (Stage) bouton_enregistrement_versement.getScene().getWindow();
        stage.close();
    }

}