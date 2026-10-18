package com.cybersek.autoges;

import java.time.format.DateTimeFormatter;

import com.cybersek.autoges.ModuleCandidats.DossierCandidat;
import com.cybersek.autoges.StructureDonnee.Candidat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class BoiteDialogueInfoCandidatControleur {

    // --- Format de date pour l'affichage ---
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Stockage du candidat actuellement affiché
    private Candidat candidatActuel;

    // --- Composants Text (Lecture seule / En-têtes) ---
    @FXML private Text info_nom;
    @FXML private Text info_prenoms;
    @FXML private Text info_nom_jeune_fille;
    @FXML private Text info_daate_naissance; // Conserve le nom FXML tel quel
    @FXML private Text info_lieu_naissance;
    @FXML private Text info_genre;
    @FXML private Text info_type_doc;
    @FXML private Text info_num_ancien_permis;
    @FXML private Text info_agence;
    @FXML private Text info_groupe;
    @FXML private Text info_categorie;
    @FXML private Text info_forfait;
    @FXML private Text info_montant_formation;

    // --- Composants TextField (Modifiables) ---
    @FXML private TextField info_champ_num_doc;
    @FXML private TextField info_champ_date_delivr;
    @FXML private TextField info_champ_lieu_delivr;
    @FXML private TextField info_champ_profession;
    @FXML private TextField info_champ_telephone;
    @FXML private TextField info_champ_residence;
    @FXML private TextField info_champ_nom_pap;
    @FXML private TextField info_champ_prenom_pap;
    @FXML private TextField info_champ_telephone_pap;

    // =========================================================================
    // CHARGEMENT DES DONNÉES
    // =========================================================================

    /**
     * Charge les données du candidat à partir de son ID (en interrogeant la BDD)
     */
    public void chargerDonnees(long id) {
        Candidat candidat = DossierCandidat.obtenirCandidatParId(id);
        if (candidat != null) {
            chargerDonnees(candidat);
        } else {
            Functions.Alerte_erreur("Impossible de récupérer les informations du candidat ID : " + id);
        }
    }

    /**
     * Surcharge : remplit les champs de la vue directement à partir d'un objet Candidat
     */
    public void chargerDonnees(Candidat candidat) {
        this.candidatActuel = candidat;

        if (candidat == null) return;

        // 1. Textes non modifiables
        info_nom.setText(valeurSecurisee(candidat.getNom()));
        info_prenoms.setText(valeurSecurisee(candidat.getPrenom()));
        info_nom_jeune_fille.setText(valeurSecurisee(candidat.getNomDeJeuneFille()));
        
        info_daate_naissance.setText(candidat.getDateDeDelivrance() != null 
                ? candidat.getDateDeDelivrance().formatted(DATE_FORMATTER) : "-");
        
        info_lieu_naissance.setText(valeurSecurisee(candidat.getDateDeNaissance()));
        info_genre.setText(valeurSecurisee(candidat.getGenre()));
        info_type_doc.setText(valeurSecurisee(candidat.getTypeDocumentId()));
        info_num_ancien_permis.setText(valeurSecurisee(candidat.getNumeroAncienPermis()));
        info_categorie.setText(valeurSecurisee(candidat.getCategorieDePermis()));
        info_agence.setText(valeurSecurisee(candidat.getNomAgence()));
        info_groupe.setText(valeurSecurisee(candidat.getNomGroupe()));
        info_forfait.setText(valeurSecurisee(candidat.getForfait()));
        info_montant_formation.setText(candidat.getMontantFormation() > 0 
                ? String.valueOf(candidat.getMontantFormation()) + " FCFA" : "-");

        // 2. Champs modifiables (TextField)
        info_champ_num_doc.setText(valeurSecurisee(candidat.getNumeroDocumentId()));
        info_champ_date_delivr.setText(candidat.getDateDeDelivrance() != null 
                ? candidat.getDateDeDelivrance().formatted(DATE_FORMATTER) : "");
        info_champ_lieu_delivr.setText(valeurSecurisee(candidat.getLieuDeDelivrance()));
        info_champ_profession.setText(valeurSecurisee(candidat.getProfession()));
        info_champ_telephone.setText(valeurSecurisee(candidat.getContact()));
        info_champ_residence.setText(valeurSecurisee(candidat.getLieuResidence()));
        info_champ_nom_pap.setText(valeurSecurisee(candidat.getNomPap()));
        info_champ_prenom_pap.setText(valeurSecurisee(candidat.getPrenomPap()));
        info_champ_telephone_pap.setText(valeurSecurisee(candidat.getContactPap()));
    }

    // =========================================================================
    // ACTIONS BOUTONS
    // =========================================================================

    @FXML
    void ModifierCandidatInfo(ActionEvent event) {
        if (candidatActuel == null) {
            Functions.Alerte_erreur("Aucun candidat n'est chargé pour la modification.");
            return;
        }

        // 1. Validation minimale
        if (info_champ_telephone.getText().trim().isEmpty() || 
            info_champ_num_doc.getText().trim().isEmpty()) {
            Functions.Alerte_erreur("Le numéro de téléphone et le numéro de document sont obligatoires.");
            return;
        }

        // 2. Mise à jour des attributs de l'objet
        candidatActuel.setNumeroDocumentId(info_champ_num_doc.getText().trim());
        candidatActuel.setLieuDeDelivrance(info_champ_lieu_delivr.getText().trim());
        candidatActuel.setProfession(info_champ_profession.getText().trim());
        candidatActuel.setContact(info_champ_telephone.getText().trim());
        candidatActuel.setLieuResidence(info_champ_residence.getText().trim());
        candidatActuel.setNomPap(info_champ_nom_pap.getText().trim());
        candidatActuel.setPrenomPap(info_champ_prenom_pap.getText().trim());
        candidatActuel.setContactPap(info_champ_telephone_pap.getText().trim());

        // 3. Appel DAO pour sauvegarder en BDD
        boolean succes = DossierCandidat.ModifierCandidatInfo(candidatActuel);

        if (succes) {
            Functions.Alerte_succes("Les informations du candidat ont été mises à jour avec succès.");
            FermerFenetre(event);
        } else {
            Functions.Alerte_erreur("Erreur lors de la mise à jour des informations en base de données.");
        }
    }

    @FXML
    void FermerFenetre(ActionEvent event) {
        FenetreInscriptionControleur.FermerBoiteDialogueInfoCandidat();
    }

    // =========================================================================
    // UTILITAIRES
    // =========================================================================

    private String valeurSecurisee(String valeur) {
        return (valeur != null && !valeur.isBlank()) ? valeur : "";
    }
}