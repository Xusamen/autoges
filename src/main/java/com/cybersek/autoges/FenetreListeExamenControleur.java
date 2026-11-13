package com.cybersek.autoges;

import java.io.IOException;
import java.time.LocalDate;

import com.cybersek.autoges.StructureDonnee.InfoSessionExamen;
import com.cybersek.autoges.ModuleExamens.SessionExamen;
import com.cybersek.autoges.ModuleParametre.GestionAgence;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FenetreListeExamenControleur {

    @FXML
    private TableColumn<InfoSessionExamen, Void> Colonne_action_date_code;

    @FXML
    private TableColumn<InfoSessionExamen, Void> Colonne_action_date_conduite_leger;

    @FXML
    private TableColumn<InfoSessionExamen, Void> Colonne_action_date_conduite_lourd;

    @FXML
    private TableColumn<InfoSessionExamen, LocalDate> colonne_date_code;

    @FXML
    private TableColumn<InfoSessionExamen, LocalDate> colonne_date_conduite_leger;

    @FXML
    private TableColumn<InfoSessionExamen, LocalDate> colonne_date_conduite_lourd;

    @FXML
    private TableColumn<InfoSessionExamen, String> colonne_etat_code;

    @FXML
    private TableColumn<InfoSessionExamen, String> colonne_etat_conduite_leger;

    @FXML
    private TableColumn<InfoSessionExamen, String> colonne_etat_conduite_lourd;

    @FXML
    private TableColumn<InfoSessionExamen, Integer> colonne_nombre_code;

    @FXML
    private TableColumn<InfoSessionExamen, Integer> colonne_nombre_conduite_leger;

    @FXML
    private TableColumn<InfoSessionExamen, Integer> colonne_nombre_conduite_lourd;

    @FXML
    private TableView<InfoSessionExamen> tableau_date_examen_code;

    @FXML
    private TableView<InfoSessionExamen> tableau_date_examen_conduite_leger;

    @FXML
    private TableView<InfoSessionExamen> tableau_date_examen_conduite_lourd;

    @FXML
    private ChoiceBox<String> choicebox_filtre_code;

    @FXML
    private ChoiceBox<String> choicebox_filtre_conduite_poids_lourd;

    @FXML
    private ChoiceBox<String> choicebox_filtre_conduite_poids_leger;
    

    private static Stage BoiteDialogueAjouterDateExamenCode;

    public static void FermerBoiteDialogueAjouterDateExamenCode() {
        BoiteDialogueAjouterDateExamenCode.close();
    }

    @FXML
    void ouvrir_boite_de_dialogue_ajout_date_code(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("BoiteDialogueAjouterDateExamenCode.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
            root.setEffect(Functions.ArrierePlan());
            StackPane contenaire = new StackPane(root);
            contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
            contenaire.setPadding(new Insets(20));
            @SuppressWarnings("unused")
            BoiteDialogueAjouterDateExamenCodeControleur controleur = loader.getController();
            BoiteDialogueAjouterDateExamenCode = new Stage();
            BoiteDialogueAjouterDateExamenCode.setResizable(false);
            BoiteDialogueAjouterDateExamenCode.initModality(Modality.APPLICATION_MODAL); // Modality.WINDOW_MODAL
            BoiteDialogueAjouterDateExamenCode
                    .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            BoiteDialogueAjouterDateExamenCode.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(contenaire);
            scene.setFill(Color.TRANSPARENT);
            BoiteDialogueAjouterDateExamenCode.setScene(scene);
            BoiteDialogueAjouterDateExamenCode.showAndWait();
        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
        tableau_date_examen_code.setItems(SessionExamen.recupererSessions("CODE", null));
    }

    private static Stage BoiteDialogueAjouterDateExamenConduite;

    public static void FermerBoiteDialogueAjouterDateExamenConduite() {
        BoiteDialogueAjouterDateExamenConduite.close();
    }

    @FXML
    void ouvrir_boite_de_dialogue_ajout_date_conduiteLeger(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("BoiteDialogueAjouterDateExamenConduite.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
            root.setEffect(Functions.ArrierePlan());
            StackPane contenaire = new StackPane(root);
            contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
            contenaire.setPadding(new Insets(20));
            @SuppressWarnings("unused")
            BoiteDialogueAjouterDateExamenConduiteControleur controleur = loader.getController();
            BoiteDialogueAjouterDateExamenConduite = new Stage();
            BoiteDialogueAjouterDateExamenConduite.setResizable(false);
            BoiteDialogueAjouterDateExamenConduite.initModality(Modality.APPLICATION_MODAL); // Modality.WINDOW_MODAL
            BoiteDialogueAjouterDateExamenConduite
                    .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            BoiteDialogueAjouterDateExamenConduite.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(contenaire);
            scene.setFill(Color.TRANSPARENT);
            BoiteDialogueAjouterDateExamenConduite.setScene(scene);
            BoiteDialogueAjouterDateExamenConduite.showAndWait();
        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
        tableau_date_examen_conduite_leger.setItems(SessionExamen.recupererSessions("CONDUITE", "POID LEGER"));

    }

    @FXML
    void ouvrir_boite_de_dialogue_ajout_date_conduiteLourd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("BoiteDialogueAjouterDateExamenConduite.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
            root.setEffect(Functions.ArrierePlan());
            StackPane contenaire = new StackPane(root);
            contenaire.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
            contenaire.setPadding(new Insets(20));
            @SuppressWarnings("unused")
            BoiteDialogueAjouterDateExamenConduiteControleur controleur = loader.getController();
            BoiteDialogueAjouterDateExamenConduite = new Stage();
            BoiteDialogueAjouterDateExamenConduite.setResizable(false);
            BoiteDialogueAjouterDateExamenConduite.initModality(Modality.APPLICATION_MODAL); // Modality.WINDOW_MODAL
            BoiteDialogueAjouterDateExamenConduite
                    .initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            BoiteDialogueAjouterDateExamenConduite.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(contenaire);
            scene.setFill(Color.TRANSPARENT);
            BoiteDialogueAjouterDateExamenConduite.setScene(scene);
            BoiteDialogueAjouterDateExamenConduite.showAndWait();
        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
        tableau_date_examen_conduite_lourd.setItems(SessionExamen.recupererSessions("CONDUITE", "POID LOURD"));
    }

    @FXML
    public void initialize() {
        InitDateExamenCode();
        InitDateExamenConduiteLeger();
        InitDateExamenConduiteLourd();
    }

    public void InitDateExamenCode() {
        colonne_date_code.setCellValueFactory(new PropertyValueFactory<>("dateExamen"));
        colonne_nombre_code.setCellValueFactory(new PropertyValueFactory<>("nombreCandidats"));
        colonne_etat_code.setCellValueFactory(new PropertyValueFactory<>("statut"));
        tableau_date_examen_code.setItems(SessionExamen.recupererSessions("CODE", null));
    }

    public void InitDateExamenConduiteLeger() {
        colonne_date_conduite_leger.setCellValueFactory(new PropertyValueFactory<>("dateExamen"));
        colonne_nombre_conduite_leger.setCellValueFactory(new PropertyValueFactory<>("nombreCandidats"));
        colonne_etat_conduite_leger.setCellValueFactory(new PropertyValueFactory<>("statut"));
        tableau_date_examen_conduite_leger.setItems(SessionExamen.recupererSessions("CONDUITE", "POID LEGER"));
    }

    public void InitDateExamenConduiteLourd() {
        colonne_date_conduite_lourd.setCellValueFactory(new PropertyValueFactory<>("dateExamen"));
        colonne_nombre_conduite_lourd.setCellValueFactory(new PropertyValueFactory<>("nombreCandidats"));
        colonne_etat_conduite_lourd.setCellValueFactory(new PropertyValueFactory<>("statut"));
        tableau_date_examen_conduite_lourd.setItems(SessionExamen.recupererSessions("CONDUITE", "POID LOURD"));
    }

}
