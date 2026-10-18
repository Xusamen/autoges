package com.cybersek.autoges;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.cybersek.autoges.ModuleCandidats.DossierCandidat;
import com.cybersek.autoges.ModuleCandidats.InscriptionCandidat;
import com.cybersek.autoges.ModuleParametre.GestionAgence;
import com.cybersek.autoges.ModuleParametre.GestionGroupe;
import com.cybersek.autoges.StructureDonnee.Agence;
import com.cybersek.autoges.StructureDonnee.Candidat;
import com.cybersek.autoges.StructureDonnee.Groupe;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class FenetreInscriptionControleur implements Initializable {

    // --- Options des listes déroulantes ---
    private static final String[] OPTIONS_DOC_ID = { "CNIB", "PASSEPORT" };
    private static final String[] OPTIONS_GENRE = { "MASCULIN", "FEMININ" };
    private static final String[] OPTIONS_FORFAIT = { "Tarif Normal", "Tarif Complet", "Tarif Promo",
            "Tarif Promo Complet", "Tarif Special", "Tarif Accelere" };
    private static final String[] OPTIONS_CATEGORIE = { "A1", "B1", "A1-A", "A1-B-C", "A1-B", "D", "E", "F" };

    // --- Composants Formulaire Inscription ---
    @FXML
    private ChoiceBox<Agence> Agence_ins;
    @FXML
    private Button Bouton_enre_ins;
    @FXML
    private ChoiceBox<String> Categorie_ins;
    @FXML
    private DatePicker Date_naiss_ins;
    @FXML
    private ChoiceBox<String> Forfait_ins;
    @FXML
    private ChoiceBox<String> Genre_ins;
    @FXML
    private TextField Lieu_naiss_ins;
    @FXML
    private TextField Montant_ins;
    @FXML
    private TextField Nom_ins;
    @FXML
    private TextField Nom_jeune_ins;
    @FXML
    private TextField Nom_pap_ins;
    @FXML
    private TextField Numero_ancien_permis_ins;
    @FXML
    private TextField Numero_doc_ins;
    @FXML
    private TextField Prenom_ins;
    @FXML
    private TextField Prenom_pap_ins;
    @FXML
    private TextField Profession_ins;
    @FXML
    private TextField Residence_ins;
    @FXML
    private ChoiceBox<Groupe> Suggestion_groupe;
    @FXML
    private TextField Telephone_ins;
    @FXML
    private TextField Telephone_pap_ins;
    @FXML
    private ChoiceBox<String> Type_doc_ins;
    @FXML
    private DatePicker date_delivrance_ins;
    @FXML
    private TextField lieu_delivrance_ins;

    // --- Composants Onglet Dossier Candidats ---
    @FXML
    private TextField champ_nom_dossier;
    @FXML
    private TextField champ_prenom_dossier;
    @FXML
    private ChoiceBox<Integer> choicebox_annee_dossier;

    @FXML
    private TableView<Candidat> tableau_dossier;
    @FXML
    private TableColumn<Candidat, String> colonne_matricule_dossier;
    @FXML
    private TableColumn<Candidat, String> colonne_nom_dossier;
    @FXML
    private TableColumn<Candidat, String> colonne_prenom_dossier;
    @FXML
    private TableColumn<Candidat, String> colonne_contact_dossier;
    @FXML
    private TableColumn<Candidat, String> colonne_categorie_dossier;
    @FXML
    private TableColumn<Candidat, Void> colonne_action_dossier;

    // --- Composants Onglet reinscription Candidats ---
    @FXML
    private TextField champ_nom_reinscription;
    @FXML
    private TextField champ_prenom_reinscription;
    @FXML
    private ChoiceBox<Integer> choicebox_annee_reinscription;

    @FXML
    private TableView<Candidat> tableau_reinscription;
    @FXML
    private TableColumn<Candidat, String> colonne_matricule_reinscription;
    @FXML
    private TableColumn<Candidat, String> colonne_nom_reinscription;
    @FXML
    private TableColumn<Candidat, String> colonne_prenom_reinscription;
    @FXML
    private TableColumn<Candidat, String> colonne_contact_reinscription;
    @FXML
    private TableColumn<Candidat, String> colonne_categorie_reinscription;
    @FXML
    private TableColumn<Candidat, Void> colonne_action_reinscription;

    // =========================================================================
    // INITIALISATION
    // =========================================================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitialiserFormulaireInscription();
        InitialiserOngletDossier();
    }



    /**
     * Remplit les ChoiceBox du formulaire d'inscription
     */
    private void InitialiserFormulaireInscription() {
        Type_doc_ins.getItems().addAll(OPTIONS_DOC_ID);
        Genre_ins.getItems().addAll(OPTIONS_GENRE);
        Forfait_ins.getItems().addAll(OPTIONS_FORFAIT);
        Categorie_ins.getItems().addAll(OPTIONS_CATEGORIE);
        Agence_ins.setItems(GestionAgence.ListerAgence());
        Suggestion_groupe.setItems(GestionGroupe.ListerGroupe());
    }

    /**
     * Configure le tableau, la ChoiceBox des années et les écouteurs de recherche
     */
    private void InitialiserOngletDossier() {
        // 1. Liaison des colonnes du tableau aux propriétés de Candidat
        colonne_matricule_dossier.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        colonne_nom_dossier.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonne_prenom_dossier.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colonne_contact_dossier.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colonne_categorie_dossier.setCellValueFactory(new PropertyValueFactory<>("categorieDePermis"));

        // 2. Configuration du bouton dans la colonne Action
        ColonneActionDossier();

        // 3. Alimentation de la ChoiceBox d'année
        InitialiserChoiceBoxAnnee();

        // 4. Listeners pour le filtrage en temps réel
        champ_nom_dossier.textProperty().addListener((obs, oldText, newText) -> RafraichirTableauDossier());
        champ_prenom_dossier.textProperty().addListener((obs, oldText, newText) -> RafraichirTableauDossier());
        choicebox_annee_dossier.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> RafraichirTableauDossier());

        // 5. Chargement initial des données
        RafraichirTableauDossier();
    }

    // =========================================================================
    // LOGIQUE ONGLET DOSSIER
    // =========================================================================

    private void InitialiserChoiceBoxAnnee() {
        ObservableList<Integer> annees = DossierCandidat.ObtenirAnneesInscription();
        int anneeEnCours = LocalDate.now().getYear();

        if (!annees.contains(anneeEnCours)) {
            annees.add(0, anneeEnCours);
        }

        choicebox_annee_dossier.setItems(annees);
        choicebox_annee_dossier.getSelectionModel().select(Integer.valueOf(anneeEnCours));
    }

    /**
     * Filtre et recharge la liste du tableau selon l'année, le nom et le prénom
     */
    private void RafraichirTableauDossier() {
        Integer anneeSelectionnee = choicebox_annee_dossier.getValue();
        String nom = champ_nom_dossier.getText();
        String prenom = champ_prenom_dossier.getText();

        ObservableList<Candidat> listeFiltree = DossierCandidat.FiltrerCandidats(anneeSelectionnee, nom, prenom);
        tableau_dossier.setItems(listeFiltree);
    }

    /**
     * Génère un bouton "Gérer Dossier" dans la colonne d'action de chaque ligne
     */

    private static Stage BoiteDialogueInfoCandidat;

    public static void FermerBoiteDialogueInfoCandidat() {
        BoiteDialogueInfoCandidat.close();
    }

    private static Stage BoiteDialogueDossierCandidat;

    public static void FermerBoiteDialogueDossierCandidat() {
        BoiteDialogueDossierCandidat.close();
    }

    private void ColonneActionDossier() {
        Callback<TableColumn<Candidat, Void>, TableCell<Candidat, Void>> cellFactory = param -> new TableCell<>() {
            private final String baseStyle = "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12; -fx-cursor: hand; -fx-font-size: 12px;";
            private final String styleBleu = baseStyle + " -fx-background-color: #2196F3;";
            private final String styleOrange = baseStyle + " -fx-background-color: #FF9800;";

            private final Button btnGestionDossier = new Button("Gérer Dossier");
            private final Button btnInfoCandidat = new Button("Infos Candidat");

            // Regroupement des 2 boutons dans un HBox avec un espacement de 8 pixels
            private final HBox container = new HBox(8, btnInfoCandidat, btnGestionDossier);

            {
                btnGestionDossier.setStyle(styleBleu);
                btnInfoCandidat.setStyle(styleOrange);

                btnGestionDossier.setOnMouseEntered(e -> btnGestionDossier
                        .setStyle(baseStyle + " -fx-background-color: #1E88E5;"));
                btnGestionDossier
                        .setOnMouseExited(e -> btnGestionDossier.setStyle(styleBleu));

                btnInfoCandidat.setOnMouseEntered(e -> btnInfoCandidat
                        .setStyle(baseStyle + " -fx-background-color: #FB8C00;"));
                btnInfoCandidat
                        .setOnMouseExited(e -> btnInfoCandidat.setStyle(styleOrange));
                // Alignement centré au milieu de la cellule
                container.setAlignment(Pos.CENTER);

                // Bouton 1 : Gérer Dossier
                btnGestionDossier.setOnAction(event -> {
                    Candidat data = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("BoiteDialogueDossierCandidat.fxml"));
                        Parent root = loader.load();
                        root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
                        root.setEffect(Functions.ArrierePlan());

                        StackPane contenaire = new StackPane(root);
                        contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
                        contenaire.setPadding(new Insets(20));

                        BoiteDialogueDossierCandidatControleur controleur = loader.getController();
                        controleur.chargerDonnees(data.getId());

                        BoiteDialogueDossierCandidat = new Stage();
                        BoiteDialogueDossierCandidat.setResizable(false);
                        BoiteDialogueDossierCandidat.initModality(Modality.APPLICATION_MODAL);
                        BoiteDialogueDossierCandidat
                                .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
                        BoiteDialogueDossierCandidat.initStyle(StageStyle.TRANSPARENT);

                        Scene scene = new Scene(contenaire);
                        scene.setFill(Color.TRANSPARENT);
                        BoiteDialogueDossierCandidat.setScene(scene);
                        BoiteDialogueDossierCandidat.showAndWait();
                    } catch (IOException erreur) {
                        System.err.println(erreur.getMessage());
                    }
                });

                // Bouton 2 : Infos Candidat
                btnInfoCandidat.setOnAction(event -> {
                    Candidat data = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("BoiteDialogueInfoCandidat.fxml"));
                        Parent root = loader.load();
                        root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
                        root.setEffect(Functions.ArrierePlan());

                        StackPane contenaire = new StackPane(root);
                        contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
                        contenaire.setPadding(new Insets(20));

                        BoiteDialogueInfoCandidatControleur controleur = loader.getController();
                        controleur.chargerDonnees(data.getId());

                        BoiteDialogueInfoCandidat = new Stage();
                        BoiteDialogueInfoCandidat.setResizable(false);
                        BoiteDialogueInfoCandidat.initModality(Modality.APPLICATION_MODAL);
                        BoiteDialogueInfoCandidat
                                .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
                        BoiteDialogueInfoCandidat.initStyle(StageStyle.TRANSPARENT);

                        Scene scene = new Scene(contenaire);
                        scene.setFill(Color.TRANSPARENT);
                        BoiteDialogueInfoCandidat.setScene(scene);
                        BoiteDialogueInfoCandidat.showAndWait();
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
                    setGraphic(container);
                }
            }
        };

        colonne_action_dossier.setCellFactory(cellFactory);
    }

    // =========================================================================
    // LOGIQUE FORMULAIRE INSCRIPTION
    // =========================================================================

    @FXML
    void enregistrer_inscription(ActionEvent event) {
        if (Nom_ins.getText().trim().isEmpty() ||
                Prenom_ins.getText().trim().isEmpty() ||
                Date_naiss_ins.getValue() == null ||
                Lieu_naiss_ins.getText().trim().isEmpty() ||
                Type_doc_ins.getValue() == null ||
                Numero_doc_ins.getText().trim().isEmpty() ||
                date_delivrance_ins.getValue() == null ||
                lieu_delivrance_ins.getText().trim().isEmpty() ||
                Profession_ins.getText().trim().isEmpty() ||
                Telephone_ins.getText().trim().isEmpty() ||
                Residence_ins.getText().trim().isEmpty() ||
                Genre_ins.getValue() == null ||
                Nom_pap_ins.getText().trim().isEmpty() ||
                Prenom_pap_ins.getText().trim().isEmpty() ||
                Telephone_pap_ins.getText().trim().isEmpty() ||
                Agence_ins.getValue() == null ||
                Categorie_ins.getValue() == null ||
                Forfait_ins.getValue() == null ||
                Montant_ins.getText().trim().isEmpty()) {

            Functions.Alerte_erreur("Veuillez remplir tous les champs obligatoires du formulaire.");
            return;
        }

        int montant = 0;
        try {
            montant = Integer.parseInt(Montant_ins.getText().trim());
        } catch (NumberFormatException e) {
            Functions.Alerte_erreur("Le montant de la formation doit être un nombre entier valide.");
            return;
        }

        int idAgence = Agence_ins.getValue().getIdAgence();
        Groupe groupeSelectionne = Suggestion_groupe.getValue();
        Integer idGroupe = (groupeSelectionne != null) ? groupeSelectionne.getId() : null;

        boolean succes = InscriptionCandidat.EnregistrerInscription(
                Nom_ins.getText().trim(),
                Nom_jeune_ins.getText().trim(),
                Prenom_ins.getText().trim(),
                Date_naiss_ins.getValue(),
                Lieu_naiss_ins.getText().trim(),
                Type_doc_ins.getValue(),
                Numero_doc_ins.getText().trim(),
                date_delivrance_ins.getValue(),
                lieu_delivrance_ins.getText().trim(),
                Profession_ins.getText().trim(),
                Telephone_ins.getText().trim(),
                Residence_ins.getText().trim(),
                Genre_ins.getValue(),
                Nom_pap_ins.getText().trim(),
                Prenom_pap_ins.getText().trim(),
                Telephone_pap_ins.getText().trim(),
                idAgence,
                idGroupe,
                Categorie_ins.getValue(),
                Numero_ancien_permis_ins.getText().trim(),
                Forfait_ins.getValue(),
                montant);

        if (succes) {
            reinitialiserFormulaire();
            // Actualise automatiquement le tableau de dossiers pour refléter l'ajout
            RafraichirTableauDossier();
        }
    }

    private void reinitialiserFormulaire() {
        Nom_ins.clear();
        Nom_jeune_ins.clear();
        Prenom_ins.clear();
        Date_naiss_ins.setValue(null);
        Lieu_naiss_ins.clear();
        Type_doc_ins.setValue(null);
        Numero_doc_ins.clear();
        date_delivrance_ins.setValue(null);
        lieu_delivrance_ins.clear();
        Profession_ins.clear();
        Telephone_ins.clear();
        Residence_ins.clear();
        Genre_ins.setValue(null);
        Nom_pap_ins.clear();
        Prenom_pap_ins.clear();
        Telephone_pap_ins.clear();
        Agence_ins.setValue(null);
        Suggestion_groupe.setValue(null);
        Categorie_ins.setValue(null);
        Numero_ancien_permis_ins.clear();
        Forfait_ins.setValue(null);
        Montant_ins.clear();
    }
}