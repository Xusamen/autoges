package com.cybersek.autoges;

import java.io.IOException;

import com.cybersek.autoges.ModuleParametre.GestionAgence;
import com.cybersek.autoges.ModuleParametre.GestionGroupe;
import com.cybersek.autoges.ModuleParametre.GestionPersonnel;
import com.cybersek.autoges.ModuleParametre.GestionUtilisateur;

import com.cybersek.autoges.StructureDonnee.Agence;
import com.cybersek.autoges.StructureDonnee.Groupe;
import com.cybersek.autoges.StructureDonnee.Personnel;
import com.cybersek.autoges.StructureDonnee.Utilisateur;

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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class FenetreParametreControleur {

    // Gestion User
    @FXML
    private Button bouton_ajouter_utilisateur;

    @FXML
    private TableColumn<Utilisateur, Void> colonne_actions;

    @FXML
    private TableColumn<Utilisateur, String> colonne_identifiant;

    @FXML
    private TableColumn<Utilisateur, String> colonne_nom;

    @FXML
    private TableColumn<Utilisateur, String> colonne_prenoms;

    @FXML
    private TableColumn<Utilisateur, String> colonne_statut;

    @FXML
    private TableView<Utilisateur> tableau_utilisateur;

    // Gestion Agence
    @FXML
    private Button bouton_ajouter_agence;

    @FXML
    private TableColumn<Agence, String> colonne_localisation_agence;

    @FXML
    private TableColumn<Agence, Void> colonne_action_agence;

    @FXML
    private TableColumn<Agence, String> colonne_nom_agence;

    @FXML
    private TableView<Agence> tableau_agence;

    // Gestion Groupe
    @FXML
    private Button bouton_ajouter_groupe;

    @FXML
    private TableColumn<Groupe, Void> colonne_action_groupe;

    @FXML
    private TableColumn<Groupe, String> colonne_agence_groupe;

    @FXML
    private TableColumn<Groupe, String> colonne_nom_groupe;

    @FXML
    private TableView<Groupe> tableau_groupe;

    // Gestion Personnel
    @FXML
    private Button bouton_ajouter_personnel;

    @FXML
    private TableColumn<Personnel, Void> colonne_action_personnel;

    @FXML
    private TableColumn<Personnel, String> colonne_date_personnel;

    @FXML
    private TableColumn<Personnel, String> colonne_fonction_personnel;

    @FXML
    private TableColumn<Personnel, String> colonne_nom_personnel;

    @FXML
    private TableColumn<Personnel, String> colonne_prenoms_personnel;

    @FXML
    private TableColumn<Personnel, String> colonne_statut_personnel;

    @FXML
    private TableView<Personnel> tableau_personnel;

    private DropShadow ArrierePlan() {
        DropShadow ombre = new DropShadow();
        ombre.setHeight(45.07);
        ombre.setWidth(53.25);
        ombre.setRadius(24.08);
        ombre.setColor(Color.BLACK);
        return ombre;
    }

    @FXML
    public void initialize_utilisateur() {
        colonne_identifiant.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        colonne_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonne_prenoms.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colonne_statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        action_bouton_gestion_user();
        tableau_utilisateur.setItems(GestionUtilisateur.ListeUtilisateurs());
    }

    @FXML
    public void initialize_agence() {
        colonne_nom_agence.setCellValueFactory(new PropertyValueFactory<>("nomAgence"));
        colonne_localisation_agence.setCellValueFactory(new PropertyValueFactory<>("positionAgence"));
        action_bouton_gestion_agence();
        tableau_agence.setItems(GestionAgence.ListerAgence());
    }

    @FXML
    public void initialize_personnel() {
        colonne_nom_personnel.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonne_prenoms_personnel.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colonne_fonction_personnel.setCellValueFactory(new PropertyValueFactory<>("fonction"));
        colonne_statut_personnel.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colonne_date_personnel.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        action_bouton_gestion_personnel();
        tableau_personnel.setItems(GestionPersonnel.ListerPersonnel());
    }

    @FXML
    public void initialize_groupes() {
        colonne_nom_groupe.setCellValueFactory(new PropertyValueFactory<>("nomGroupe"));
        colonne_agence_groupe.setCellValueFactory(new PropertyValueFactory<>("nomAgence"));
        action_bouton_gestion_groupe();
        tableau_groupe.setItems(GestionGroupe.ListerGroupe());
    }

    private static Stage BoiteDialogueAjouterUtilisateur;

    public static void FermerBoiteDialogueAjouterUtilisateur() {
        BoiteDialogueAjouterUtilisateur.close();
    }

    @FXML
    void OuvrirBoiteDialogueAjouterUtilisateur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("BoiteDialogueAjouterUtilisateur.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
            root.setEffect(ArrierePlan());
            StackPane contenaire = new StackPane(root);
            contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
            contenaire.setPadding(new Insets(20));
            @SuppressWarnings("unused")
            BoiteDialogueAjouterUtilisateurControleur controleur = loader.getController();
            BoiteDialogueAjouterUtilisateur = new Stage();
            BoiteDialogueAjouterUtilisateur.setResizable(false);
            BoiteDialogueAjouterUtilisateur.initModality(Modality.APPLICATION_MODAL); // Modality.WINDOW_MODAL
            BoiteDialogueAjouterUtilisateur
                    .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            BoiteDialogueAjouterUtilisateur.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(contenaire);
            scene.setFill(Color.TRANSPARENT);
            BoiteDialogueAjouterUtilisateur.setScene(scene);
            BoiteDialogueAjouterUtilisateur.showAndWait();
        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
        tableau_utilisateur.setItems(GestionUtilisateur.ListeUtilisateurs());
    }

    private static Stage BoiteDialogueAjouterAgence;

    public static void FermerBoiteDialogueAjouterAgence() {
        BoiteDialogueAjouterAgence.close();
    }

    @FXML
    void OuvrirBoiteDialogueAjouterAgence(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("BoiteDialogueAjouterAgence.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
            root.setEffect(ArrierePlan());
            StackPane contenaire = new StackPane(root);
            contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
            contenaire.setPadding(new Insets(20));
            @SuppressWarnings("unused")
            BoiteDialogueAjoutAgenceControleur controleur = loader.getController();
            BoiteDialogueAjouterAgence = new Stage();
            BoiteDialogueAjouterAgence.setResizable(false);
            BoiteDialogueAjouterAgence.initModality(Modality.APPLICATION_MODAL); // Modality.WINDOW_MODAL
            BoiteDialogueAjouterAgence
                    .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            BoiteDialogueAjouterAgence.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(contenaire);
            scene.setFill(Color.TRANSPARENT);
            BoiteDialogueAjouterAgence.setScene(scene);
            BoiteDialogueAjouterAgence.showAndWait();
        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
        tableau_agence.setItems(GestionAgence.ListerAgence());

    }

    private static Stage BoiteDialogueModifierUtilisateur;

    public static void FermerBoiteDialogueModifierUtilisateur() {
        BoiteDialogueModifierUtilisateur.close();
    }

    private void action_bouton_gestion_user() {
        Callback<TableColumn<Utilisateur, Void>, TableCell<Utilisateur, Void>> cellFactory = new Callback<TableColumn<Utilisateur, Void>, TableCell<Utilisateur, Void>>() {
            @Override
            public TableCell<Utilisateur, Void> call(final TableColumn<Utilisateur, Void> param) {
                final TableCell<Utilisateur, Void> cell = new TableCell<Utilisateur, Void>() {
                    // Initialisation des boutons
                    private final Button boutonDesactiverUtilisateur = new Button("Désactiver");
                    private final Button boutonModifierInfoUtilisateur = new Button("Modifier");
                    private final Button boutonSupprimerUtilisateur = new Button("Supprimer");

                    // Styles CSS pour les boutons
                    private final String baseStyle = "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12; -fx-cursor: hand; -fx-font-size: 12px;";
                    private final String styleBleu = baseStyle + " -fx-background-color: #2196F3;";
                    private final String styleOrange = baseStyle + " -fx-background-color: #FF9800;";
                    private final String styleRouge = baseStyle + " -fx-background-color: #F44336;";

                    {
                        // Application des styles de couleur
                        boutonDesactiverUtilisateur.setStyle(styleBleu);
                        boutonModifierInfoUtilisateur.setStyle(styleOrange);
                        boutonSupprimerUtilisateur.setStyle(styleRouge);

                        // --- Événements au survol (Effet Hover) ---
                        boutonDesactiverUtilisateur.setOnMouseEntered(e -> boutonDesactiverUtilisateur
                                .setStyle(baseStyle + " -fx-background-color: #1E88E5;"));
                        boutonDesactiverUtilisateur
                                .setOnMouseExited(e -> boutonDesactiverUtilisateur.setStyle(styleBleu));

                        boutonModifierInfoUtilisateur.setOnMouseEntered(e -> boutonModifierInfoUtilisateur
                                .setStyle(baseStyle + " -fx-background-color: #FB8C00;"));
                        boutonModifierInfoUtilisateur
                                .setOnMouseExited(e -> boutonModifierInfoUtilisateur.setStyle(styleOrange));

                        boutonSupprimerUtilisateur.setOnMouseEntered(e -> boutonSupprimerUtilisateur
                                .setStyle(baseStyle + " -fx-background-color: #E53935;"));
                        boutonSupprimerUtilisateur
                                .setOnMouseExited(e -> boutonSupprimerUtilisateur.setStyle(styleRouge));

                        // --- Action Désactiver / Activer ---
                        boutonDesactiverUtilisateur.setOnAction(event -> {
                            Utilisateur data = getTableView().getItems().get(getIndex());
                            GestionUtilisateur.ChangerStatutUtilisateur(data.getId());
                            tableau_utilisateur.setItems(GestionUtilisateur.ListeUtilisateurs());
                        });

                        // --- Action Modifier ---
                        boutonModifierInfoUtilisateur.setOnAction(event -> {
                            Utilisateur data = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(
                                        getClass().getResource("BoiteDialogueModifierUtilisateur.fxml"));
                                Parent root = loader.load();
                                root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
                                root.setEffect(ArrierePlan());

                                StackPane contenaire = new StackPane(root);
                                contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
                                contenaire.setPadding(new Insets(20));

                                BoiteDialogueModifierUtilisateurControleur controleur = loader.getController();
                                controleur.chargerDonnees(data.getId());

                                BoiteDialogueModifierUtilisateur = new Stage();
                                BoiteDialogueModifierUtilisateur.setResizable(false);
                                BoiteDialogueModifierUtilisateur.initModality(Modality.APPLICATION_MODAL);
                                BoiteDialogueModifierUtilisateur
                                        .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
                                BoiteDialogueModifierUtilisateur.initStyle(StageStyle.TRANSPARENT);

                                Scene scene = new Scene(contenaire);
                                scene.setFill(Color.TRANSPARENT);
                                BoiteDialogueModifierUtilisateur.setScene(scene);
                                BoiteDialogueModifierUtilisateur.showAndWait();
                            } catch (IOException erreur) {
                                System.err.println(erreur.getMessage());
                            }

                            tableau_utilisateur.setItems(GestionUtilisateur.ListeUtilisateurs());
                        });

                        // --- Action Supprimer ---
                        boutonSupprimerUtilisateur.setOnAction(event -> {
                            Utilisateur data = getTableView().getItems().get(getIndex());
                            GestionUtilisateur.SupprimerUtilisateur(data);
                            tableau_utilisateur.setItems(GestionUtilisateur.ListeUtilisateurs());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                        } else {
                            Utilisateur user = getTableView().getItems().get(getIndex());

                            if (user != null) {
                                if ("Actif".equalsIgnoreCase(user.getStatut())) {
                                    boutonDesactiverUtilisateur.setText("Désactiver");
                                } else {
                                    boutonDesactiverUtilisateur.setText("Activer");
                                }
                            }

                            HBox box = new HBox(6, boutonDesactiverUtilisateur, boutonModifierInfoUtilisateur,
                                    boutonSupprimerUtilisateur);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
                return cell;
            }
        };

        colonne_actions.setCellFactory(cellFactory);
    }

    private void action_bouton_gestion_agence() {
        Callback<TableColumn<Agence, Void>, TableCell<Agence, Void>> cellFactory = new Callback<TableColumn<Agence, Void>, TableCell<Agence, Void>>() {
            @Override
            public TableCell<Agence, Void> call(final TableColumn<Agence, Void> param) {
                final TableCell<Agence, Void> cell = new TableCell<Agence, Void>() {
                    private final Button boutonSupprimerAgence = new Button("Supprimer");

                    {
                        boutonSupprimerAgence
                                .setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-cursor: hand;");
                        boutonSupprimerAgence.setOnAction(event -> {
                            Agence data = getTableView().getItems().get(getIndex());
                            if (data != null) {
                                GestionAgence.SupprimerAgence(data);
                                tableau_agence.setItems(GestionAgence.ListerAgence());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                        } else {
                            HBox box = new HBox(boutonSupprimerAgence);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
                return cell;
            }
        };

        colonne_action_agence.setCellFactory(cellFactory);
    }

    private void action_bouton_gestion_groupe() {
        Callback<TableColumn<Groupe, Void>, TableCell<Groupe, Void>> cellFactory = new Callback<TableColumn<Groupe, Void>, TableCell<Groupe, Void>>() {
            @Override
            public TableCell<Groupe, Void> call(final TableColumn<Groupe, Void> param) {
                final TableCell<Groupe, Void> cell = new TableCell<Groupe, Void>() {
                    private final Button boutonSupprimerGroupe = new Button("Supprimer");

                    {
                        // Style du bouton suppression (rouge)
                        boutonSupprimerGroupe.setStyle(
                                "-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 5;");

                        boutonSupprimerGroupe.setOnAction(event -> {
                            Groupe data = getTableView().getItems().get(getIndex());
                            if (data != null) {
                                // Appel de la méthode de suppression par l'ID du groupe
                                GestionGroupe.SupprimerGroupe(data.getId());
                                // Rafraîchissement du tableau des groupes
                                tableau_groupe.setItems(GestionGroupe.ListerGroupe());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                        } else {
                            HBox box = new HBox(boutonSupprimerGroupe);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
                return cell;
            }
        };

        colonne_action_groupe.setCellFactory(cellFactory);
    }

    private static Stage BoiteDialogueModifierPersonnel;

    public static void FermerBoiteDialogueModifierPersonnel() {
        BoiteDialogueModifierPersonnel.close();
    }

    private void action_bouton_gestion_personnel() {
        Callback<TableColumn<Personnel, Void>, TableCell<Personnel, Void>> cellFactory = new Callback<TableColumn<Personnel, Void>, TableCell<Personnel, Void>>() {
            @Override
            public TableCell<Personnel, Void> call(final TableColumn<Personnel, Void> param) {
                final TableCell<Personnel, Void> cell = new TableCell<Personnel, Void>() {
                    // Initialisation des boutons
                    private final Button boutonStatut = new Button();
                    private final Button boutonModifier = new Button("Modifier");

                    // Styles CSS
                    private final String baseStyle = "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12; -fx-cursor: hand; -fx-font-size: 12px;";
                    private final String styleOrange = baseStyle + " -fx-background-color: #FF9800;";
                    private final String styleBleu = baseStyle + " -fx-background-color: #2196F3;";
                    private final String styleGris = baseStyle + " -fx-background-color: #757575;";

                    {
                        boutonModifier.setStyle(styleOrange);
                        boutonStatut.setOnAction(event -> {
                            Personnel data = getTableView().getItems().get(getIndex());
                            if (data != null) {
                                GestionPersonnel.ChangerStatutPersonnel(data.getId());
                                tableau_personnel.setItems(GestionPersonnel.ListerPersonnel());
                            }
                        });

                        boutonModifier.setOnAction(event -> {
                            Personnel data = getTableView().getItems().get(getIndex());
                            if (data != null) {
                                try {
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("BoiteDialogueModifierPersonnel.fxml"));
                                    Parent root = loader.load();
                                    root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
                                    root.setEffect(ArrierePlan());

                                    StackPane contenaire = new StackPane(root);
                                    contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
                                    contenaire.setPadding(new Insets(20));

                                    BoiteDialogueModifierPersonnelControleur controleur = loader.getController();
                                    controleur.chargerDonnees(data.getId());

                                    BoiteDialogueModifierPersonnel = new Stage();
                                    BoiteDialogueModifierPersonnel.setResizable(false);
                                    BoiteDialogueModifierPersonnel.initModality(Modality.APPLICATION_MODAL);
                                    BoiteDialogueModifierPersonnel
                                            .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
                                    BoiteDialogueModifierPersonnel.initStyle(StageStyle.TRANSPARENT);

                                    Scene scene = new Scene(contenaire);
                                    scene.setFill(Color.TRANSPARENT);
                                    BoiteDialogueModifierPersonnel.setScene(scene);
                                    BoiteDialogueModifierPersonnel.showAndWait();

                                    tableau_personnel.setItems(GestionPersonnel.ListerPersonnel());
                                } catch (IOException erreur) {
                                    System.err.println(erreur.getMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                        } else {
                            Personnel person = getTableView().getItems().get(getIndex());

                            if (person != null) {
                                // Adaptation dynamique du libellé et de la couleur selon le statut actuel
                                if ("actif".equalsIgnoreCase(person.getStatut())) {
                                    boutonStatut.setText("Désactiver");
                                    boutonStatut.setStyle(styleGris);
                                } else {
                                    boutonStatut.setText("Activer");
                                    boutonStatut.setStyle(styleBleu);
                                }
                            }

                            HBox box = new HBox(6, boutonStatut, boutonModifier);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
                return cell;
            }
        };

        colonne_action_personnel.setCellFactory(cellFactory);
    }

    private static Stage BoiteDialogueAjouterGroupe;

    public static void FermerBoiteDialogueAjoutGroupe() {
        BoiteDialogueAjouterGroupe.close();
    }

    @FXML
    void OuvrirBoiteDialogueAjouterGroupe(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("BoiteDialogueAjouterGroupe.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
            root.setEffect(ArrierePlan());
            StackPane contenaire = new StackPane(root);
            contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
            contenaire.setPadding(new Insets(20));
            @SuppressWarnings("unused")
            BoiteDialogueAjouterGroupeControleur controleur = loader.getController();
            BoiteDialogueAjouterGroupe = new Stage();
            BoiteDialogueAjouterGroupe.setResizable(false);
            BoiteDialogueAjouterGroupe.initModality(Modality.APPLICATION_MODAL); // Modality.WINDOW_MODAL
            BoiteDialogueAjouterGroupe
                    .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            BoiteDialogueAjouterGroupe.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(contenaire);
            scene.setFill(Color.TRANSPARENT);
            BoiteDialogueAjouterGroupe.setScene(scene);
            BoiteDialogueAjouterGroupe.showAndWait();
        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
        tableau_groupe.setItems(GestionGroupe.ListerGroupe());

    }

    private static Stage BoiteDialogueAjouterPersonnel;

    public static void FermerBoiteDialogueAjouterPersonnel() {
        BoiteDialogueAjouterPersonnel.close();
    }

    @FXML
    void OuvrirBoiteDialogueAjouterPersonnel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("BoiteDialogueAjouterPersonnel.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
            root.setEffect(ArrierePlan());
            StackPane contenaire = new StackPane(root);
            contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
            contenaire.setPadding(new Insets(20));
            @SuppressWarnings("unused")
            BoiteDialogueAjouterPersonnelControleur controleur = loader.getController();
            BoiteDialogueAjouterPersonnel = new Stage();
            BoiteDialogueAjouterPersonnel.setResizable(false);
            BoiteDialogueAjouterPersonnel.initModality(Modality.APPLICATION_MODAL); // Modality.WINDOW_MODAL
            BoiteDialogueAjouterPersonnel
                    .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            BoiteDialogueAjouterPersonnel.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(contenaire);
            scene.setFill(Color.TRANSPARENT);
            BoiteDialogueAjouterPersonnel.setScene(scene);
            BoiteDialogueAjouterPersonnel.showAndWait();
        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
        tableau_personnel.setItems(GestionPersonnel.ListerPersonnel());
    }

}
