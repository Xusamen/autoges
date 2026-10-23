package com.cybersek.autoges;

import com.cybersek.autoges.StructureDonnee.TableauInfoTransaction;
import com.cybersek.autoges.ModuleComptabilite.Recette;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class BoiteDialogueModifierNumeroRecuControleur {

    @FXML
    private Button bouton_modifier;

    @FXML
    private TextField champ_numero_recu;

    @FXML
    private Text info_id_transaction;

    private TableauInfoTransaction InfoTransaction;
    private String numRec;
    private int flag = 0;

    @FXML
    void FermerFenetre(ActionEvent event) {
        if (flag == 1) {
            BoiteDialogueInfoVersementCaisseControleur.FermerBoiteDialogueModifierNumeroRecu();
        }

        if (flag == 2) {
            FenetreCaisseControleur.FermerBoiteDialogueModifierNumeroRecu();
        }
    }

    @FXML
    void ModifierNumeroRecu(ActionEvent event) {
        boolean statut = Recette.mettreAJourNumeroRecu(InfoTransaction.getIdTransaction(), numRec);
        if (statut) {
            Functions.Alerte_succes("Numero Reçu modifier");
            BoiteDialogueInfoVersementCaisseControleur.FermerBoiteDialogueModifierNumeroRecu();
        } else {
            Functions.Alerte_erreur("Erreur rencontre lors de la modification");
        }
    }

    public void chargerDonnees(TableauInfoTransaction transaction) {
        flag = 1;
        InfoTransaction = transaction;
        numRec = Recette.obtenirNumeroRecuParReference(transaction.getIdTransaction());
        if (numRec != null) {
            info_id_transaction.setText(transaction.getRef_transaction());
            champ_numero_recu.setText(numRec);
        }
    }

    public void chargerDonneesServTiers(long idServTier) {
        // Récupère un tableau contenant [reference, numero_recu]
        flag = 2;
        String[] infos = Recette.InfoServTier(idServTier);

        if (infos != null && infos.length >= 2) {
            String reference = infos[0];
            String numRec = infos[1];

            if (reference != null) {
                info_id_transaction.setText(reference);
            }
            if (numRec != null) {
                champ_numero_recu.setText(numRec);
            }
        }
    }

}
