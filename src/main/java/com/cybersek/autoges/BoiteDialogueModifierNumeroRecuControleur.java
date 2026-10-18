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
    private  String numRec;

    @FXML
    void FermerFenetre(ActionEvent event) {
        BoiteDialogueInfoVersementCaisseControleur.FermerBoiteDialogueModifierNumeroRecu();
    }

    @FXML
    void ModifierNumeroRecu(ActionEvent event) {
        boolean statut = Recette.mettreAJourNumeroRecu(InfoTransaction.getIdTransaction(),  numRec);
        if(statut){
            Functions.Alerte_succes("Numero Reçu modifier");
            BoiteDialogueInfoVersementCaisseControleur.FermerBoiteDialogueModifierNumeroRecu();
        }else{
            Functions.Alerte_erreur("Erreur rencontre lors de la modification");
        }
    }

    public void chargerDonnees(TableauInfoTransaction transaction) {
        InfoTransaction = transaction;
        numRec = Recette.obtenirNumeroRecuParReference(transaction.getIdTransaction());
        if (numRec != null){
            info_id_transaction.setText(transaction.getRef_transaction());
            champ_numero_recu.setText(numRec);
        }
    }

}
