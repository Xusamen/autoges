package com.cybersek.autoges;

import com.cybersek.autoges.ModuleComptabilite.Recette;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.cybersek.autoges.StructureDonnee.InfoRecuServiceTier;
import com.cybersek.autoges.StructureDonnee.TableauRecette;
import com.cybersek.autoges.StructureDonnee.TableauServiceTier;
import com.cybersek.autoges.ModuleCandidats.DossierCandidat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

    private ObservableList<TableauServiceTier> listeServicesTiers = FXCollections.observableArrayList();

    private void InitialiserChoiceBoxAnnee() {
        ObservableList<Integer> annees = DossierCandidat.ObtenirAnneesInscription();
        int anneeEnCours = LocalDate.now().getYear();
        if (!annees.contains(anneeEnCours)) {
            annees.add(0, anneeEnCours);
        }
        choicebox_annee_versement.setItems(annees);
        choicebox_annee_versement.getSelectionModel().select(Integer.valueOf(anneeEnCours));
    }

    public void InitTableauServicesTiers() {
        // 1. Configuration des liaisons avec les propriétés de la classe
        // TableauServiceTier
        colonne_nom_servtier.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonne_prenoms_servtier.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colonne_prestserv_servtier.setCellValueFactory(new PropertyValueFactory<>("description")); // ou prestation
                                                                                                   // selon votre modèle
        colonne_contact_servtier.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colonne_montant_servtier.setCellValueFactory(new PropertyValueFactory<>("montant"));

        // 2. Charger les années disponibles dans la ChoiceBox (ex: de 2023 à l'année
        // courante)
        int anneeCourante = LocalDate.now().getYear();
        ObservableList<Integer> annees = FXCollections.observableArrayList();
        for (int y = anneeCourante; y >= anneeCourante - 5; y--) {
            annees.add(y);
        }
        choicebox_annee_servtier.setItems(annees);
        choicebox_annee_servtier.setValue(anneeCourante); // Sélection par défaut

        // 3. Charger les données depuis la base de données
        chargerDonneesServicesTiers();

        // 4. Configurer le filtrage dynamique (Nom, Prénom, Année)
        FilteredList<TableauServiceTier> filteredData = new FilteredList<>(listeServicesTiers, b -> true);

        // Écouteur sur le champ Nom
        champ_nom_servtier.textProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltres(filteredData);
        });

        // Écouteur sur le champ Prénom
        champ_prenom_servtier.textProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltres(filteredData);
        });

        // Écouteur sur la ChoiceBox Année
        choicebox_annee_servtier.valueProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltres(filteredData);
        });

        // 5. Associer la liste filtrée au TableView
        tableau_servtier.setItems(filteredData);
    }

    private void appliquerFiltres(FilteredList<TableauServiceTier> filteredData) {
        filteredData.setPredicate(service -> {
            // Filtre par Année (si la ligne possède une date ou un champ année)
            Integer anneeSelectionnee = choicebox_annee_servtier.getValue();
            if (anneeSelectionnee != null) {
                // Supposons que votre objet possède une méthode getDate() ou getAnnee()
                // Adaptez "service.getAnnee()" selon la structure de votre classe
                // TableauServiceTier
                if (service.getAnnee() != anneeSelectionnee) {
                    return false;
                }
            }

            // Filtre par Nom
            String filtreNom = champ_nom_servtier.getText();
            if (filtreNom != null && !filtreNom.trim().isEmpty()) {
                String nomLower = filtreNom.toLowerCase().trim();
                if (service.getNom() == null || !service.getNom().toLowerCase().contains(nomLower)) {
                    return false;
                }
            }

            // Filtre par Prénom
            String filtrePrenom = champ_prenom_servtier.getText();
            if (filtrePrenom != null && !filtrePrenom.trim().isEmpty()) {
                String prenomLower = filtrePrenom.toLowerCase().trim();
                if (service.getPrenom() == null || !service.getPrenom().toLowerCase().contains(prenomLower)) {
                    return false;
                }
            }

            return true; // Garder l'élément si tous les critères correspondent
        });
    }

    // Méthode de chargement depuis la base de données (exemple)
    private void chargerDonneesServicesTiers() {
        listeServicesTiers.clear();
        listeServicesTiers.addAll(Recette.recupererTousLesServicesTiers());
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
        type_de_paiement_service_tiers.setItems(FXCollections.observableArrayList(
                "especes", "cheque", "virement", "mobile_money"));
        type_de_paiement_service_tiers.getSelectionModel().selectFirst();

        // 5. Forcer le chargement initial des données pour que le tableau ne soit pas
        // vide à l'ouverture
        rafraichirDonnees();
        InitTableauServicesTiers();
        BoutonsActionsSevicesTiers();
    }

    @FXML
    void Enregistrer_decaissement(ActionEvent event) {

    }

    @FXML
    void Enregistrer_service_tiers(ActionEvent event) {
        // 1. Récupération des valeurs textuelles
        String nom = nom_service_tiers.getText() != null ? nom_service_tiers.getText().trim() : "";
        String prenom = prenom_service_tiers.getText() != null ? prenom_service_tiers.getText().trim() : "";
        String telephone = telephone_service_tiers.getText() != null ? telephone_service_tiers.getText().trim() : "";
        String desc = description_service_tiers.getText() != null ? description_service_tiers.getText().trim() : "";
        String strNombreSeance = nombresceance_servicetiers.getText() != null
                ? nombresceance_servicetiers.getText().trim()
                : "";
        String strMontant = montant_servicetiers.getText() != null ? montant_servicetiers.getText().trim() : "";
        String modePaiement = type_de_paiement_service_tiers.getValue();

        // 2. Contrôles de validation (Champs obligatoires et formats)
        if (nom.isEmpty()) {
            Functions.Alerte_erreur("Veuillez saisir le nom.");
            nom_service_tiers.requestFocus();
            return;
        }
        if (prenom.isEmpty()) {
            Functions.Alerte_erreur("Veuillez saisir le prénom.");
            prenom_service_tiers.requestFocus();
            return;
        }
        if (telephone.isEmpty()) {
            Functions.Alerte_erreur("Veuillez saisir le numéro de téléphone.");
            telephone_service_tiers.requestFocus();
            return;
        }
        // Optionnel : Validation basique du format du téléphone (chiffres, espaces,
        // tirets, optionnel "+")
        if (!telephone.matches("^\\+?[0-9\\s-]{8,15}$")) {
            Functions.Alerte_erreur("Le format du numéro de téléphone est invalide.");
            telephone_service_tiers.requestFocus();
            return;
        }
        if (desc.isEmpty()) {
            Functions.Alerte_erreur("Veuillez saisir la description.");
            description_service_tiers.requestFocus();
            return;
        }
        if (modePaiement == null || modePaiement.trim().isEmpty()) {
            Functions.Alerte_erreur("Veuillez sélectionner un mode de paiement.");
            type_de_paiement_service_tiers.requestFocus();
            return;
        }
        if (strNombreSeance.isEmpty()) {
            Functions.Alerte_erreur("Veuillez saisir le nombre de séances.");
            nombresceance_servicetiers.requestFocus();
            return;
        }
        if (strMontant.isEmpty()) {
            Functions.Alerte_erreur("Veuillez saisir le montant.");
            montant_servicetiers.requestFocus();
            return;
        }

        // 3. Traitement et conversion numérique sécurisés
        try {
            int nombresceance = Integer.parseInt(strNombreSeance);
            if (nombresceance <= 0) {
                Functions.Alerte_erreur("Le nombre de séances doit être supérieur à zéro.");
                nombresceance_servicetiers.requestFocus();
                return;
            }

            BigDecimal montant = new BigDecimal(strMontant.replaceAll("\\s", "").replace(',', '.'));
            if (montant.compareTo(BigDecimal.ZERO) <= 0) {
                Functions.Alerte_erreur("Le montant doit être supérieur à zéro.");
                montant_servicetiers.requestFocus();
                return;
            }

            // 4. Enregistrement en base de données
            boolean succes = Recette.enregistrerServiceTiers(nom, prenom, telephone, desc, nombresceance, montant,
                    modePaiement);

            if (succes) {
                Functions.Alerte_succes("Enregistré avec succès !");

                // Nettoyage de tous les champs du formulaire
                nom_service_tiers.clear();
                prenom_service_tiers.clear();
                telephone_service_tiers.clear();
                description_service_tiers.clear();
                nombresceance_servicetiers.clear();
                montant_servicetiers.clear();
                type_de_paiement_service_tiers.getSelectionModel().clearSelection();

                rafraichirDonnees();
            }

        } catch (NumberFormatException e) {
            Functions.Alerte_erreur("Erreur de format : veuillez vérifier le nombre de séances ou le montant.");
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

    private void BoutonsActionsSevicesTiers() {
        colonne_actions_servtier.setCellFactory(param -> new TableCell<TableauServiceTier, Void>() {
            private final Button boutonImprimer = new Button("Imprimer");
            private final Button boutonModifierRecu = new Button("Modifier reçu");

            // Conteneur regroupant tous les boutons dans l'ordre souhaité
            private final HBox conteneurBoutons = new HBox(6, boutonImprimer, boutonModifierRecu);

            {
                conteneurBoutons.setAlignment(Pos.CENTER);

                // Styles des boutons (vous pouvez ajuster les couleurs selon votre charte
                // graphique)
                boutonImprimer.setStyle(
                        "-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 10; -fx-cursor: hand; -fx-font-size: 11px;");
                boutonModifierRecu.setStyle(
                        "-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 10; -fx-cursor: hand; -fx-font-size: 11px;");
                // 1. Action du bouton "Imprimer"
                boutonImprimer.setOnAction(event -> {
                    TableauServiceTier service = getTableView().getItems().get(getIndex());
                    // TODO: Implémenter la logique d'impression du reçu ici
                    System.out.println("Imprimer le reçu pour l'ID : " + service.getIdServTiers());
                    InfoRecuServiceTier info = Recette.recupererInfoRecuParId(service.getIdServTiers());

                    if (info != null) {
                        GenerateurPdf.CreationRecuServiceTier(
                                info.getNom(),
                                info.getPrenom(),
                                info.getDatePaiement(),
                                info.getDescription(),
                                info.getMontant(),
                                info.getNumeroRecu()
                        );
                    } else {
                        Functions.Alerte_erreur("Impossible de récupérer les informations pour l'impression.");
                    }
                });

                // 2. Action du bouton "Modifier reçu"
                boutonModifierRecu.setOnAction(event -> {
                    TableauServiceTier service = getTableView().getItems().get(getIndex());
                    // TODO: Implémenter la logique de modification du numéro de reçu ici
                    System.out.println("Modifier le numéro de reçu pour l'ID : " + service.getIdServTiers());
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