package com.cybersek.autoges;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import com.cybersek.autoges.StructureDonnee.Candidat;
import com.cybersek.autoges.StructureDonnee.Dossier;

import com.cybersek.autoges.ModuleCandidats.DossierCandidat;

public class BoiteDialogueDossierCandidatControleur {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private TextField champ_date_depot_dossier;
    @FXML private TextField champ_date_dossier_disponible;
    @FXML private TextField champ_numero_dossier;

    @FXML private Text info_matricule;
    @FXML private Text info_nom;
    @FXML private Text info_prenom;

    private long idCandidatCourant;
    private long idInscriptionCourante;
    private long idDossierCourant;

    private final DossierCandidat dossierDAO = new DossierCandidat();

    /**
     * Charge les informations du candidat et son dossier.
     * @param idCandidat L'identifiant unique du candidat
     */
    public void chargerDonnees(long idCandidat) {
        this.idCandidatCourant = idCandidat;

        // 1. Récupération du candidat
        Candidat candidat = dossierDAO.obtenirCandidatParId(idCandidat);
        if (candidat != null) {
            info_matricule.setText(candidat.getIdentifiant() != null ? candidat.getIdentifiant() : "-");
            info_nom.setText(candidat.getNom());
            info_prenom.setText(candidat.getPrenom());
        }

        // 2. Récupération du dossier existant
        Dossier dossier = dossierDAO.obtenirDossierParCandidatId(idCandidat);
        if (dossier != null) {
            this.idDossierCourant = dossier.getId();
            this.idInscriptionCourante = dossier.getIdInscription();

            champ_numero_dossier.setText(dossier.getPermisProvisoire());
            champ_date_depot_dossier.setText(dossier.getDateDepot());
            champ_date_dossier_disponible.setText(dossier.getDateSortie());
        } else {
            this.idDossierCourant = 0;
            this.idInscriptionCourante = dossierDAO.obtenirIdInscriptionParCandidatId(idCandidat);

            champ_numero_dossier.clear();
            champ_date_depot_dossier.clear();
            champ_date_dossier_disponible.clear();
        }
    }

    /**
     * Enregistre ou met à jour les informations du dossier.
     */
    @FXML
    void ModifierInformation(ActionEvent event) {
        String permisProvisoire = champ_numero_dossier.getText().trim();
        String strDateDepot = champ_date_depot_dossier.getText().trim();
        String strDateSortie = champ_date_dossier_disponible.getText().trim();

        LocalDate dateDepot = parseLocalDate(strDateDepot);
        LocalDate dateSortie = parseLocalDate(strDateSortie);

        // Validation simple
        if (!strDateDepot.isEmpty() && dateDepot == null) {
            Functions.Alerte_succes("La date de dépôt doit être au format jj/mm/aaaa.");
            return;
        }

        if (!strDateSortie.isEmpty() && dateSortie == null) {
            Functions.Alerte_erreur("La date de sortie doit être au format jj/mm/aaaa.");
            return;
        }

        Dossier dossier = new Dossier(
            idDossierCourant,
            idInscriptionCourante,
            dateDepot,
            permisProvisoire,
            dateSortie,
            "" // Commentaire par défaut
        );

        boolean succes = dossierDAO.sauvegarderOuMettreAJourDossier(idCandidatCourant, dossier);

        if (succes) {
            Functions.Alerte_succes( "Dossier enregistré avec succès.");
            FermerFenetre(event);
        } else {
            Functions.Alerte_erreur("Impossible d'enregistrer le dossier.");
        }
    }

    @FXML
    void FermerFenetre(ActionEvent event) {
        FenetreInscriptionControleur.FermerBoiteDialogueDossierCandidat();
    }

    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

}