package com.cybersek.autoges;

import com.cybersek.autoges.ModuleComptabilite.Recette;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.cybersek.autoges.StructureDonnee.TableauRecette;
import com.cybersek.autoges.StructureDonnee.TableauServiceTier;
import com.cybersek.autoges.ModuleCandidats.DossierCandidat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FenetreCaisseControleur {

    // Versement

    @FXML
    private TextField champ_nom_versement;

    @FXML
    private TextField champ_prenom_versement;
    @FXML
    private ChoiceBox<Integer> choicebox_annee_versement;
    @FXML
    private TableView<TableauRecette> tableau_versement;

    @FXML
    private TableColumn<TableauRecette, String> colonne_matricule_versement;

    @FXML
    private TableColumn<TableauRecette, String> colonne_nom_versement;

    @FXML
    private TableColumn<TableauRecette, String> colonne_prenoms_versement;

    @FXML
    private TableColumn<TableauRecette, String> colonne_contact_versement;

    @FXML
    private TableColumn<TableauRecette, String> colonne_categorie_versement;

    @FXML
    private TableColumn<TableauRecette, String> colonne_forfait_versement;

    @FXML
    private TableColumn<TableauRecette, Integer> colonne_reste_versement;

    @FXML
    private TableColumn<TableauRecette, Void> colonne_actions_versement;

    // Decaissement

    @FXML
    private Button bouton_enregistrement_decaissement;
    @FXML
    private TextField montant_decaissement;
    @FXML
    private ChoiceBox<?> motif_de_decaissement;

    @FXML
    private ChoiceBox<?> type_de_decaissement;

    // Service tiers
    @FXML
    private TextField telephone_service_tiers;
    @FXML
    private TextField nom_service_tiers;
    @FXML
    private TextField prenom_service_tiers;
    @FXML
    private TextField description_service_tiers;
    @FXML
    private TextField montant_servicetiers;
    @FXML
    private ChoiceBox<String> type_de_paiement_service_tiers;
    @FXML
    private TextField nombresceance_servicetiers;
    @FXML
    private TextField champ_nom_servtier;
    @FXML
    private TextField champ_prenom_servtier;
    @FXML
    private ChoiceBox<Integer> choicebox_annee_servtier;

    @FXML
    private TableView<TableauServiceTier> tableau_servtier;
    @FXML
    private TableColumn<TableauServiceTier, String> colonne_nom_servtier;
    @FXML
    private TableColumn<TableauServiceTier, String> colonne_prenoms_servtier;
    @FXML
    private TableColumn<TableauServiceTier, String> colonne_prestserv_servtier;
    @FXML
    private TableColumn<TableauServiceTier, String> colonne_contact_servtier;
    @FXML
    private TableColumn<TableauServiceTier, Void> colonne_actions_servtier;
    @FXML
    private TableColumn<TableauServiceTier, Integer> colonne_montant_servtier;

    // BILAN FINANCIER JOURNALIER

    // BILAN FINANCIER Mensuel
    @FXML
    private TableColumn<?, ?> action_bilan_mensuel;
    @FXML
    private TableColumn<?, ?> colSolde_mois;
    @FXML
    private TableView<?> tableMensuelle;
    // BILAN FINANCIER Annuel

    @FXML
    private TableColumn<?, ?> action_bilan_annuel;

    @FXML
    private Button bouton_lancer_impress_bilan;

    @FXML
    private TableColumn<?, ?> colAnnee;

    @FXML
    private TableColumn<?, ?> colEntree_annee;

    @FXML
    private TableColumn<?, ?> colEntree_mois;

    @FXML
    private TableColumn<?, ?> colMois;

    @FXML
    private TableColumn<?, ?> colSolde_annee;

    @FXML
    private TableColumn<?, ?> colSortie_annee;

    @FXML
    private TableColumn<?, ?> colSortie_mois;

    @FXML
    private TableColumn<?, ?> colonne_compte_entrees;

    @FXML
    private TableColumn<?, ?> colonne_compte_sorties;

    @FXML
    private TableColumn<?, ?> colonne_description_entrees;

    @FXML
    private TableColumn<?, ?> colonne_description_sorties;

    @FXML
    private TableColumn<?, ?> colonne_modetransac_entrees;

    @FXML
    private TableColumn<?, ?> colonne_modetransac_sortie;

    @FXML
    private TableColumn<?, ?> colonne_numero_entrees;

    @FXML
    private TableColumn<?, ?> colonne_numero_sorties;

    @FXML
    private TableColumn<?, ?> colonne_somme_entrees;

    @FXML
    private TableColumn<?, ?> colonne_somme_sorties;

    @FXML
    private Text statistique_total_entrees;

    @FXML
    private Text statistique_total_global;

    @FXML
    private Text statistique_total_sorties;

    @FXML
    private TableView<?> tableAnnuelle;

    @FXML
    private TableView<?> tableau_entree;

    @FXML
    private TableView<?> tableau_sortie;

    private void InitialiserChoiceBoxAnnee() {
        ObservableList<Integer> annees = DossierCandidat.ObtenirAnneesInscription();
        int anneeEnCours = LocalDate.now().getYear();
        if (!annees.contains(anneeEnCours)) {
            annees.add(0, anneeEnCours);
        }
        choicebox_annee_versement.setItems(annees);
        choicebox_annee_versement.getSelectionModel().select(Integer.valueOf(anneeEnCours));
    }

    @FXML
    public void initialize() {
        // 1. Lier les colonnes aux propriétés de la classe TableauRecette
        colonne_matricule_versement.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colonne_nom_versement.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonne_prenoms_versement.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colonne_contact_versement.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colonne_categorie_versement.setCellValueFactory(new PropertyValueFactory<>("categoriePermis"));
        colonne_forfait_versement.setCellValueFactory(new PropertyValueFactory<>("forfait"));
        colonne_reste_versement.setCellValueFactory(new PropertyValueFactory<>("reste"));

        // 2. Configurer les boutons d'actions
        BoutonsActionsVersement();

        // 3. Initialiser les années D'ABORD (sans déclencher de listener prématuré)
        InitialiserChoiceBoxAnnee();

        // 4. Ajouter les listeners sur les champs de texte et la ChoiceBox ENSUITE
        champ_nom_versement.textProperty().addListener((obs, oldText, newText) -> rafraichirDonnees());
        champ_prenom_versement.textProperty().addListener((obs, oldText, newText) -> rafraichirDonnees());

        choicebox_annee_versement.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        rafraichirDonnees();
                    }
                });

        // 5. Forcer le chargement initial des données pour que le tableau ne soit pas
        // vide à l'ouverture
        rafraichirDonnees();
    }

    @FXML
    void Enregistrer_decaissement(ActionEvent event) {

    }

    @FXML
    void Enregistrer_service_tiers(ActionEvent event) {

        try {
            String nom = nom_service_tiers.getText().trim();
            String prenom = prenom_service_tiers.getText().trim();
            String telephone = telephone_service_tiers.getText().trim();
            String desc = description_service_tiers.getText().trim();
            int nombresceance = Integer.parseInt(nombresceance_servicetiers.getText().trim());
            BigDecimal montant = new BigDecimal(montant_servicetiers.getText().replaceAll("\\s", "").replace(',', '.'));
            String modePaiement = type_de_paiement_service_tiers.getValue();

            boolean succes = Recette.enregistrerServiceTiers(nom, prenom, telephone, desc, nombresceance, montant,
                    modePaiement);
            if (succes) {
                nom_service_tiers.clear();
                prenom_service_tiers.clear();
                description_service_tiers.clear();
                montant_servicetiers.clear();
                rafraichirDonnees();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le montant saisi est invalide.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void rafraichirDonnees() {
        Integer anneeAffichee = choicebox_annee_versement.getValue();
        if (anneeAffichee != null) {
            String nomSaisi = champ_nom_versement.getText();
            String prenomSaisi = champ_prenom_versement.getText(); // Si vous possédez ce champ, sinon passez null ou
                                                                   // vide

            // Si des filtres textuels sont présents, on utilise FiltrerCandidats, sinon la
            // liste complète
            if ((nomSaisi != null && !nomSaisi.trim().isEmpty())
                    || (prenomSaisi != null && !prenomSaisi.trim().isEmpty())) {
                tableau_versement.setItems(Recette.FiltrerCandidats(anneeAffichee, nomSaisi, prenomSaisi));
            } else {
                tableau_versement.setItems(Recette.ListerCandidatsParAnneeInscription(anneeAffichee));
            }

            // tableau_servtier.setItems(Recette.chargerServicesTiers(anneeAffichee,
            // champ_nom_servtier.getText()));
            // tableau_entree.setItems(Recette.chargerEntreesBilan(LocalDate.now(),
            // LocalDate.now()));

            // Double totalEntrees = Recette.calculerTotalEntrees(LocalDate.now(),
            // LocalDate.now());
            // statistique_total_entrees.setText(String.format("%.2f FCFA", totalEntrees));
        }
    }

    @FXML
    void Lancer_Impression_bilan_journalier(ActionEvent event) {

    }

    @FXML
    void initialisation_bilan_annuel(ActionEvent event) {

    }

    @FXML
    void initialisation_bilan_journalier(ActionEvent event) {

    }

    @FXML
    void initialisation_bilan_mensuel(ActionEvent event) {

    }

    private static Stage BoiteDialogueVersementCaisse;

    public static void FermerBoiteDialogueVersementCaisse() {
        BoiteDialogueVersementCaisse.close();
    }

    private static Stage BoiteDialogueInfoVersementCaisse;

    public static void FermerBoiteDialogueInfoVersementCaisse() {
        BoiteDialogueInfoVersementCaisse.close();
    }

    private void BoutonsActionsVersement() {
        colonne_actions_versement.setCellFactory(param -> new TableCell<TableauRecette, Void>() {
            private final Button boutonVersement = new Button("Versement");
            private final Button boutonEtat = new Button("État de paiement");
            private final HBox conteneurBoutons = new HBox(8, boutonVersement, boutonEtat);

            {
                conteneurBoutons.setAlignment(Pos.CENTER);

                // Style optionnel pour un meilleur rendu visuel
                boutonVersement.setStyle(
                        "-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12; -fx-cursor: hand; -fx-font-size: 12px;");
                boutonEtat.setStyle(
                        "-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12; -fx-cursor: hand; -fx-font-size: 12px;");

                // Action du bouton "Versement"
                boutonVersement.setOnAction(event -> {
                    TableauRecette candidat = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("BoiteDialogueVersementCaisse.fxml"));
                        Parent root = loader.load();
                        root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
                        root.setEffect(Functions.ArrierePlan());

                        StackPane contenaire = new StackPane(root);
                        contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
                        contenaire.setPadding(new Insets(20));

                        BoiteDialogueVersementCaisseControleur controleur = loader.getController();
                        controleur.chargerDonnees(candidat.getId());

                        BoiteDialogueVersementCaisse = new Stage();
                        BoiteDialogueVersementCaisse.setResizable(false);
                        BoiteDialogueVersementCaisse.initModality(Modality.APPLICATION_MODAL);
                        BoiteDialogueVersementCaisse
                                .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
                        BoiteDialogueVersementCaisse.initStyle(StageStyle.TRANSPARENT);

                        Scene scene = new Scene(contenaire);
                        scene.setFill(Color.TRANSPARENT);
                        BoiteDialogueVersementCaisse.setScene(scene);
                        BoiteDialogueVersementCaisse.showAndWait();
                    } catch (IOException erreur) {
                        System.err.println(erreur.getMessage());
                    }

                });

                // Action du bouton "État des versements"
                boutonEtat.setOnAction(event -> {
                    TableauRecette candidat = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("BoiteDialogueInfoVersementCaisse.fxml"));
                        Parent root = loader.load();
                        root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
                        root.setEffect(Functions.ArrierePlan());

                        StackPane contenaire = new StackPane(root);
                        contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
                        contenaire.setPadding(new Insets(20));

                        BoiteDialogueInfoVersementCaisseControleur controleur = loader.getController();
                        controleur.chargerDonnees(candidat.getId());

                        BoiteDialogueInfoVersementCaisse = new Stage();
                        BoiteDialogueInfoVersementCaisse.setResizable(false);
                        BoiteDialogueInfoVersementCaisse.initModality(Modality.APPLICATION_MODAL);
                        BoiteDialogueInfoVersementCaisse
                                .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
                        BoiteDialogueInfoVersementCaisse.initStyle(StageStyle.TRANSPARENT);

                        Scene scene = new Scene(contenaire);
                        scene.setFill(Color.TRANSPARENT);
                        BoiteDialogueInfoVersementCaisse.setScene(scene);
                        BoiteDialogueInfoVersementCaisse.showAndWait();
                    } catch (IOException erreur) {
                        System.err.println(erreur.getMessage());
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