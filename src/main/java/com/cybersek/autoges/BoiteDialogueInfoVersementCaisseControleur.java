package com.cybersek.autoges;

import java.io.IOException;

import com.cybersek.autoges.StructureDonnee.Candidat;
import com.cybersek.autoges.StructureDonnee.DonneesVersementCandidat;
import com.cybersek.autoges.StructureDonnee.TableauInfoTransaction;
import com.cybersek.autoges.ModuleCandidats.DossierCandidat;
import com.cybersek.autoges.ModuleComptabilite.Recette;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BoiteDialogueInfoVersementCaisseControleur {

    @FXML
    private TableColumn<TableauInfoTransaction, Void> colonne_actions;

    @FXML
    private TableColumn<TableauInfoTransaction, Long> colonne_reference_versement;

    @FXML
    private TableColumn<TableauInfoTransaction, String> colonne_intitule_versement;

    @FXML
    private TableColumn<TableauInfoTransaction, String> colonne_numero_reçu_versement;

    @FXML
    private TableColumn<TableauInfoTransaction, String> colonne_date_versement; // Assurez-vous d'avoir cette colonne
                                                                                // dans votre FXML ou ajoutez-la si
                                                                                // besoin

    @FXML
    private Text info_matricule;

    @FXML
    private Text info_nom;

    @FXML
    private Text info_prenom;

    @FXML
    private Text info_reste_a_payer;

    long ID_CANDIDAT_REFRESH;

    @FXML
    private TableView<TableauInfoTransaction> tableeau_info_versement;

    @FXML
    public void initialize() {
        // Liaison des colonnes de la classe TableauInfoTransaction
        colonne_reference_versement.setCellValueFactory(new PropertyValueFactory<>("Ref_transaction"));
        colonne_intitule_versement.setCellValueFactory(new PropertyValueFactory<>("type_versement"));
        colonne_date_versement.setCellValueFactory(new PropertyValueFactory<>("dateVersement"));
        colonne_numero_reçu_versement.setCellValueFactory(new PropertyValueFactory<>("numeroRecu"));
        BoutonsActions();
    }

    @FXML
    void FermerFenetre(ActionEvent event) {
        FenetreCaisseControleur.FermerBoiteDialogueInfoVersementCaisse();
    }

    public void chargerDonnees(long idCandidat) {
        // 1. Charger les informations textuelles du candidat
        ID_CANDIDAT_REFRESH = idCandidat;
        Candidat candidat = DossierCandidat.obtenirCandidatParId(idCandidat);
        if (candidat != null) {
            info_matricule.setText(candidat.getIdentifiant());
            info_nom.setText(candidat.getNom());
            info_prenom.setText(candidat.getPrenom());
        } else {
            Functions.Alerte_erreur("Impossible de récupérer les informations du candidat ID : " + idCandidat);
            return;
        }

        // 2. Charger la liste des versements dans le TableView
        ObservableList<TableauInfoTransaction> historique = Recette.chargerHistoriqueVersementsCandidat(idCandidat);
        tableeau_info_versement.setItems(historique);

        // 3. Calculer et afficher le reste à payer global pour ce candidat
        double resteAPayer = Recette.calculerResteAPayerGlobal(idCandidat);
        info_reste_a_payer.setText(String.format("%.2f FCFA", resteAPayer));
    }

    private static Stage BoiteDialogueModifierNumeroRecu;

    public static void FermerBoiteDialogueModifierNumeroRecu() {
        BoiteDialogueModifierNumeroRecu.close();
    }

    private void BoutonsActions() {
        colonne_actions.setCellFactory(param -> new TableCell<TableauInfoTransaction, Void>() {
            private final Button boutonImprimer = new Button("Imprimer Reçu"); // ou "Imprimer"
            private final Button boutonAssocier = new Button("Modif N° Reçu"); // ou "N° Reçu"
            private final Button boutonAnnuler = new Button("Annuler Transac"); // ou "Annuler"

            private final HBox conteneurBoutons = new HBox(5, boutonImprimer, boutonAssocier, boutonAnnuler);

            {
                conteneurBoutons.setAlignment(Pos.CENTER);

                // Info-bulles (Tooltip) pour expliciter les icônes/boutons
                boutonImprimer.setTooltip(new javafx.scene.control.Tooltip("Imprimer le reçu"));
                boutonAssocier.setTooltip(new javafx.scene.control.Tooltip("Associer un numéro de reçu"));
                boutonAnnuler.setTooltip(new javafx.scene.control.Tooltip("Annuler la transaction"));

                // Styles CSS optionnels (couleurs)
                boutonImprimer.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-cursor: hand;");
                boutonAssocier.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-cursor: hand;");
                boutonAnnuler.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-cursor: hand;");

                // 1. Action : Imprimer le reçu
                boutonImprimer.setOnAction(event -> {
                    TableauInfoTransaction transaction = getTableView().getItems().get(getIndex());
                    DonneesVersementCandidat data = Recette.getDetailsVersement(transaction.getIdTransaction());
                    GenerateurPdf.CreationRecuVersement(data.getNom(), data.getNomDeJeuneFille(), data.getPrenom(),
                            data.getDateInscription(), data.getForfait(), data.getCategoriePermis(),
                            data.getMontantVerse(), data.getSommeRestante(), data.getDateVersement(), data.getTypeVersement());
                });

                // 2. Action : Associer un numéro de reçu
                boutonAssocier.setOnAction(event -> {
                    TableauInfoTransaction transaction = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("BoiteDialogueModifierNumeroRecu.fxml"));
                        Parent root = loader.load();
                        root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
                        root.setEffect(Functions.ArrierePlan());

                        StackPane contenaire = new StackPane(root);
                        contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
                        contenaire.setPadding(new Insets(20));

                        BoiteDialogueModifierNumeroRecuControleur controleur = loader.getController();
                        controleur.chargerDonnees(transaction);

                        BoiteDialogueModifierNumeroRecu = new Stage();
                        BoiteDialogueModifierNumeroRecu.setResizable(false);
                        BoiteDialogueModifierNumeroRecu.initModality(Modality.APPLICATION_MODAL);
                        BoiteDialogueModifierNumeroRecu
                                .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
                        BoiteDialogueModifierNumeroRecu.initStyle(StageStyle.TRANSPARENT);

                        Scene scene = new Scene(contenaire);
                        scene.setFill(Color.TRANSPARENT);
                        BoiteDialogueModifierNumeroRecu.setScene(scene);
                        BoiteDialogueModifierNumeroRecu.showAndWait();
                    } catch (IOException erreur) {
                        System.err.println(erreur.getMessage());
                    }
                });

                // 3. Action : Annuler la transaction
                boutonAnnuler.setOnAction(event -> {
                    TableauInfoTransaction transaction = getTableView().getItems().get(getIndex());
                    // annulerTransaction(transaction);
                    boolean statut = Recette.annulerTransaction(transaction.getIdTransaction(), "Erreur de saisi");
                    if (statut) {
                        Functions.Alerte_succes("Transaction : " + transaction.getRef_transaction() + " annuler");
                        chargerDonnees(ID_CANDIDAT_REFRESH);
                    } else {
                        Functions.Alerte_erreur("Erreur rencontré lors de l'anulation de la transaction "
                                + transaction.getRef_transaction());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(conteneurBoutons);
                }
            }
        });
    }
}