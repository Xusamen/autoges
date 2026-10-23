package com.cybersek.autoges.ModuleComptabilite;

import com.cybersek.autoges.App;
import com.cybersek.autoges.NomUser;
import com.cybersek.autoges.StructureDonnee.DonneesVersementCandidat;
import com.cybersek.autoges.StructureDonnee.InfoRecuServiceTier;
import com.cybersek.autoges.StructureDonnee.TableauInfoTransaction;
import com.cybersek.autoges.StructureDonnee.TableauRecette;
import com.cybersek.autoges.StructureDonnee.TableauServiceTier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class Recette {

    /**
     * Enregistre une recette hors-candidat (Service Tiers / Prestation externe)
     */
    public static boolean enregistrerServiceTiers(String nom, String prenom, String telephone,
            String description, int nombreSeances,
            BigDecimal montant, String modePaiement) {

        String sqlTransaction = "INSERT INTO transactions "
                + "(reference, type, montant, mode_paiement, libelle, id_utilisateur, statut) "
                + "VALUES (?, 'recette', ?, ?, ?, ?, 'validee')";

        // Requête ajustée sans id_categorie
        String sqlAutreRecette = "INSERT INTO autres_recettes "
                + "(id_transaction, nom_client, prenom_client, contact_client, source, description, nombre_seances, numero_recu) "
                + "VALUES (?, ?, ?, ?, 'Service Tiers', ?, ?, ?)";

        String referenceTx = "TRX"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
        String numeroRecu = "REC-" + System.currentTimeMillis();

        String libelleTx = tronquer("Prestation Service Tiers - " + nom + " " + prenom, 255);
        String details = tronquer(description, 255);

        int idUtilisateur = NomUser.getIdUtilisateur();

        try (Connection cx = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse)) {
            cx.setAutoCommit(false);
            try {
                long idTransaction;

                // 1. Insertion dans transactions
                try (PreparedStatement ps = cx.prepareStatement(sqlTransaction, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, referenceTx);
                    ps.setBigDecimal(2, montant);
                    ps.setString(3, normaliserModePaiement(modePaiement));
                    ps.setString(4, libelleTx);
                    ps.setInt(5, idUtilisateur);
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("ID de transaction non généré");
                        }
                        idTransaction = rs.getLong(1);
                    }
                }

                // 2. Insertion dans autres_recettes
                try (PreparedStatement ps = cx.prepareStatement(sqlAutreRecette)) {
                    ps.setLong(1, idTransaction);
                    ps.setString(2, nom);
                    ps.setString(3, prenom);
                    ps.setString(4, telephone);
                    ps.setString(5, details);
                    ps.setInt(6, nombreSeances);
                    ps.setString(7, numeroRecu);
                    ps.executeUpdate();
                }

                cx.commit();
                return true;

            } catch (SQLException e) {
                cx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String tronquer(String s, int max) {
        if (s == null) {
            return null;
        }
        return s.length() <= max ? s : s.substring(0, max);
    }

    private static String normaliserModePaiement(String mode) {
        if (mode == null) {
            return "especes";
        }
        switch (mode.toLowerCase(Locale.ROOT).trim()) {
            case "espèces":
            case "especes":
                return "especes";
            case "chèque":
            case "cheque":
                return "cheque";
            case "virement":
                return "virement";
            case "mobile money":
            case "mobile_money":
                return "mobile_money";
            default:
                return "especes"; // Valeur par défaut compatible avec l'ENUM SQL
        }
    }

    /**
     * Charge les candidats avec leur état de paiement pour l'année sélectionnée
     */
    public static ObservableList<TableauRecette> chargerVersementsCandidats(Integer annee, String filtreNom) {
        ObservableList<TableauRecette> liste = FXCollections.observableArrayList();

        // Requête SQL joignant les tables 'candidat' et 'inscription'
        StringBuilder sql = new StringBuilder(
                "SELECT c.id, i.id AS id_inscription, c.identifiant AS matricule, " +
                        "c.nom, c.prenom, c.contact, i.categorie_de_permis, i.forfait " +
                        "FROM inscription i " +
                        "JOIN candidat c ON i.id_candidat = c.id " +
                        "WHERE YEAR(i.date_inscription) = ?");

        // Ajout dynamique du filtre textuel si l'utilisateur a saisi un nom
        boolean hasFiltre = filtreNom != null && !filtreNom.trim().isEmpty();
        if (hasFiltre) {
            sql.append(" AND (c.nom LIKE ? OR c.prenom LIKE ?) ");
        }

        sql.append(" ORDER BY i.date_inscription DESC");

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql.toString())) {

            statement.setInt(1, annee);

            if (hasFiltre) {
                String motifRecherche = "%" + filtreNom.trim() + "%";
                statement.setString(2, motifRecherche);
                statement.setString(3, motifRecherche);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    long idCandidat = rs.getLong("id");
                    long idInscription = rs.getLong("id_inscription");
                    String matricule = rs.getString("matricule");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String contact = rs.getString("contact");
                    String categoriePermis = rs.getString("categorie_de_permis");
                    String forfait = rs.getString("forfait");

                    // Valeur par défaut pour l'instant
                    int restePaiement = 0;

                    TableauRecette recette = new TableauRecette(
                            idCandidat,
                            idInscription,
                            matricule,
                            nom,
                            prenom,
                            contact,
                            categoriePermis,
                            forfait,
                            restePaiement);

                    liste.add(recette);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    /**
     * Charge l'historique des recettes de services tiers
     */
    public ObservableList<TableauServiceTier> chargerServicesTiers(Integer annee, String filtreNom) {
        ObservableList<TableauServiceTier> liste = FXCollections.observableArrayList();
        // Requête BDD sur autres_recettes + transactions
        return liste;
    }

    /**
     * Récupère la liste des transactions d'entrées pour le Bilan Financier
     */
    // public ObservableList<TableauTransaction> chargerEntreesBilan(LocalDate
    // dateDebut, LocalDate dateFin) {
    // ObservableList<TableauTransaction> entrees =
    // FXCollections.observableArrayList();
    // Requête BDD sur la table transactions WHERE type = 'recette' ET statut =
    // 'validee'
    // return entrees;
    // }

    /**
     * Calcule le total cumulé des recettes sur une période donnée
     */
    public double calculerTotalEntrees(LocalDate dateDebut, LocalDate dateFin) {
        // Requête BDD : SELECT SUM(montant) FROM transactions WHERE type='recette'
        return 0.0;
    }

    /**
     * Liste les candidats et calcule leur reste à payer par année d'inscription.
     */
    public static ObservableList<TableauRecette> ListerCandidatsParAnneeInscription(Integer annee) {
        ObservableList<TableauRecette> liste = FXCollections.observableArrayList();

        StringBuilder sql = new StringBuilder(
                "SELECT c.id, i.id AS id_inscription, c.identifiant AS matricule, " +
                        "c.nom, c.prenom, c.contact, i.categorie_de_permis, i.forfait, " +
                        "i.montant_formation, COALESCE(SUM(t.montant), 0) AS total_verse " +
                        "FROM candidat c " +
                        "JOIN inscription i ON c.id = i.id_candidat " +
                        "LEFT JOIN recette_candidat rc ON i.id = rc.id_inscription AND rc.type_versement LIKE 'Paiement Tranche%' "
                        +
                        "LEFT JOIN transactions t ON rc.id_transaction = t.id AND t.statut = 'validee' " +
                        "WHERE 1=1 ");

        if (annee != null && annee > 0) {
            sql.append("AND YEAR(i.date_inscription) = ? ");
        }

        sql.append(
                "GROUP BY c.id, i.id, c.identifiant, c.nom, c.prenom, c.contact, i.categorie_de_permis, i.forfait, i.montant_formation ");
        sql.append("ORDER BY c.id DESC");

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql.toString())) {

            if (annee != null && annee > 0) {
                statement.setInt(1, annee);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    long idCandidat = rs.getLong("id");
                    long idInscription = rs.getLong("id_inscription");
                    String matricule = rs.getString("matricule");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String contact = rs.getString("contact");
                    String categoriePermis = rs.getString("categorie_de_permis");
                    String forfait = rs.getString("forfait");

                    // Calcul dynamique du reste à payer (Montant formation - Total versé)
                    int montantFormation = rs.getInt("montant_formation");
                    int totalVerse = rs.getInt("total_verse");
                    int restePaiement = montantFormation - totalVerse;

                    TableauRecette recette = new TableauRecette(
                            idCandidat, idInscription, matricule, nom, prenom,
                            contact, categoriePermis, forfait, restePaiement);

                    liste.add(recette);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des candidats : " + e.getMessage());
        }

        return liste;
    }

    /**
     * Filtre les candidats et calcule leur reste à payer par année, nom et/ou
     * prénom.
     */
    public static ObservableList<TableauRecette> FiltrerCandidats(Integer annee, String nomFiltre,
            String prenomFiltre) {
        ObservableList<TableauRecette> liste = FXCollections.observableArrayList();

        StringBuilder sql = new StringBuilder(
                "SELECT c.id, i.id AS id_inscription, c.identifiant AS matricule, " +
                        "c.nom, c.prenom, c.contact, i.categorie_de_permis, i.forfait, " +
                        "i.montant_formation, COALESCE(SUM(t.montant), 0) AS total_verse " +
                        "FROM candidat c " +
                        "JOIN inscription i ON c.id = i.id_candidat " +
                        "LEFT JOIN recette_candidat rc ON i.id = rc.id_inscription " +
                        "LEFT JOIN transactions t ON rc.id_transaction = t.id AND t.statut = 'validee' " +
                        "WHERE 1=1 ");

        if (annee != null && annee > 0) {
            sql.append("AND YEAR(i.date_inscription) = ? ");
        }

        boolean hasNom = nomFiltre != null && !nomFiltre.trim().isEmpty();
        boolean hasPrenom = prenomFiltre != null && !prenomFiltre.trim().isEmpty();

        if (hasNom) {
            sql.append("AND c.nom LIKE ? ");
        }
        if (hasPrenom) {
            sql.append("AND c.prenom LIKE ? ");
        }

        sql.append(
                "GROUP BY c.id, i.id, c.identifiant, c.nom, c.prenom, c.contact, i.categorie_de_permis, i.forfait, i.montant_formation ");
        sql.append("ORDER BY c.id DESC");

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql.toString())) {

            int indexParam = 1;
            if (annee != null && annee > 0) {
                statement.setInt(indexParam++, annee);
            }
            if (hasNom) {
                statement.setString(indexParam++, "%" + nomFiltre.trim() + "%");
            }
            if (hasPrenom) {
                statement.setString(indexParam++, "%" + prenomFiltre.trim() + "%");
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    long idCandidat = rs.getLong("id");
                    long idInscription = rs.getLong("id_inscription");
                    String matricule = rs.getString("matricule");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String contact = rs.getString("contact");
                    String categoriePermis = rs.getString("categorie_de_permis");
                    String forfait = rs.getString("forfait");

                    // Calcul dynamique du reste à payer
                    int montantFormation = rs.getInt("montant_formation");
                    int totalVerse = rs.getInt("total_verse");
                    int restePaiement = montantFormation - totalVerse;

                    TableauRecette recette = new TableauRecette(
                            idCandidat, idInscription, matricule, nom, prenom,
                            contact, categoriePermis, forfait, restePaiement);

                    liste.add(recette);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du filtrage des candidats : " + e.getMessage());
        }

        return liste;
    }

    public static boolean enregistrerVersementCandidat(long idCandidat, long idInscription, double montantVersement,
            String modePaiement, String typeVersement, int utilisateurId) {
        String insertTransaction = "INSERT INTO transactions (reference, type, montant, mode_paiement, libelle, id_utilisateur, statut) VALUES (?, 'recette', ?, ?, ?, ?, 'validee')";
        String insertRecetteCandidat = "INSERT INTO recette_candidat (id_transaction, id_candidat, id_inscription, type_versement, reste_a_payer, numero_recu) VALUES (?, ?, ?, ?, ?, ?)";

        // Génération d'une référence unique (ex: timestamp ou aléatoire)
        String reference = "TX-" + System.currentTimeMillis();
        String numeroRecu = "REC-" + System.currentTimeMillis();

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse)) {
            connexion.setAutoCommit(false); // Début de la transaction

            long idTransaction = -1;

            // 1. Insertion dans la table transactions
            try (PreparedStatement stmtTrans = connexion.prepareStatement(insertTransaction,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtTrans.setString(1, reference);
                stmtTrans.setBigDecimal(2, java.math.BigDecimal.valueOf(montantVersement));
                stmtTrans.setString(3, modePaiement);
                stmtTrans.setString(4, "Versement formation candidat ID: " + idCandidat);
                stmtTrans.setInt(5, utilisateurId);
                stmtTrans.executeUpdate();

                try (ResultSet generatedKeys = stmtTrans.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idTransaction = generatedKeys.getLong(1);
                    } else {
                        connexion.rollback();
                        return false;
                    }
                }
            }

            // 2. Calcul du nouveau reste à payer pour cette inscription
            // (On récupère le montant total de la formation et la somme des versements)
            int montantFormation = 0;
            int totalVerseActuel = 0;

            String queryMontant = "SELECT montant_formation FROM inscription WHERE id = ?";
            try (PreparedStatement stmt = connexion.prepareStatement(queryMontant)) {
                stmt.setLong(1, idInscription);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        montantFormation = rs.getInt("montant_formation");
                }
            }
            String queryVerse = "SELECT COALESCE(SUM(t.montant), 0) AS total FROM recette_candidat rc JOIN transactions t ON rc.id_transaction = t.id WHERE rc.id_inscription = ? AND t.statut = 'validee' AND rc.type_versement LIKE 'Paiement Tranche%'";
            try (PreparedStatement stmt = connexion.prepareStatement(queryVerse)) {
                stmt.setLong(1, idInscription);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        totalVerseActuel = rs.getInt("total");
                }
            }

            double resteAPayer = montantFormation - (totalVerseActuel + montantVersement);

            // 3. Insertion dans la table recette_candidat
            try (PreparedStatement stmtRecette = connexion.prepareStatement(insertRecetteCandidat)) {
                stmtRecette.setLong(1, idTransaction);
                stmtRecette.setLong(2, idCandidat);
                stmtRecette.setLong(3, idInscription);
                stmtRecette.setString(4, typeVersement); // 'acompte', 'solde', etc.
                stmtRecette.setBigDecimal(5, java.math.BigDecimal.valueOf(resteAPayer));
                stmtRecette.setString(6, numeroRecu);
                stmtRecette.executeUpdate();
            }

            // 4. Validation finale (Commit)
            connexion.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static long obtenirIdInscriptionParCandidat(long idCandidat) {
        long idInscription = -1;
        String sql = "SELECT id FROM inscription WHERE id_candidat = ? ORDER BY date_inscription DESC LIMIT 1";
        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setLong(1, idCandidat);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idInscription = rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idInscription;
    }

    public static ObservableList<TableauInfoTransaction> chargerHistoriqueVersementsCandidat(long idCandidat) {
        ObservableList<TableauInfoTransaction> liste = FXCollections.observableArrayList();

        String sql = "SELECT t.id, t.reference, rc.numero_recu, rc.type_versement, t.created_at " +
                "FROM recette_candidat rc " +
                "JOIN transactions t ON rc.id_transaction = t.id " +
                "WHERE rc.id_candidat = ? AND t.statut = 'validee' " +
                "ORDER BY t.created_at DESC";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setLong(1, idCandidat);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    long id_transaction = rs.getLong("id");
                    String refTransaction = rs.getString("reference");
                    String numeroRecu = rs.getString("numero_recu");
                    String typeVersement = rs.getString("type_versement");
                    Timestamp dateVersement = rs.getTimestamp("created_at");

                    TableauInfoTransaction transaction = new TableauInfoTransaction(
                            id_transaction, refTransaction, numeroRecu, typeVersement, dateVersement);

                    liste.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement de l'historique des versements : " + e.getMessage());
        }

        return liste;
    }

    public static double calculerResteAPayerGlobal(long idCandidat) {
        double montantFormationTotal = 0;
        double montantVerseTotal = 0;

        String sql = "SELECT i.montant_formation, COALESCE(SUM(t.montant), 0) AS total_verse " +
                "FROM inscription i " +
                "LEFT JOIN recette_candidat rc ON i.id = rc.id_inscription " +
                "LEFT JOIN transactions t ON rc.id_transaction = t.id AND t.statut = 'validee' " +
                "WHERE i.id_candidat = ? AND rc.type_versement LIKE 'Paiement Tranche%'" +
                "GROUP BY i.id, i.montant_formation";

        try (java.sql.Connection connexion = java.sql.DriverManager.getConnection(App.url, App.utilisateur,
                App.motDePasse);
                java.sql.PreparedStatement pstmt = connexion.prepareStatement(sql)) {

            pstmt.setLong(1, idCandidat);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    montantFormationTotal = rs.getDouble("montant_formation");
                    montantVerseTotal = rs.getDouble("total_verse");
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return montantFormationTotal - montantVerseTotal;
    }

    public static String obtenirNumeroRecuParReference(Long IdTransaction) {
        String numeroRecu = null;

        // On joint la table transactions et recette_candidat via l'ID
        String sql = "SELECT rc.numero_recu " +
                "FROM recette_candidat rc " +
                "JOIN transactions t ON rc.id_transaction = t.id " +
                "WHERE t.id = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setLong(1, IdTransaction);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    numeroRecu = rs.getString("numero_recu");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du numéro de reçu : " + e.getMessage());
        }

        return numeroRecu;
    }

    public static String[] InfoServTier(Long IdServTier) {
        String numeroRecu = null;
        String reference = null;

        // Correction du nom de la table (autres_recettes et non autres_recette) et de
        // l'alias (ar)
        String sql = "SELECT t.reference, ar.numero_recu " +
                "FROM autres_recettes ar " +
                "JOIN transactions t ON ar.id_transaction = t.id " +
                "WHERE ar.id = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setLong(1, IdServTier);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    reference = rs.getString("reference");
                    numeroRecu = rs.getString("numero_recu");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des informations : " + e.getMessage());
        }

        return new String[] { reference, numeroRecu };
    }

    public static boolean mettreAJourNumeroRecu(long idTransaction, String numeroRecu) {
        String sql = "UPDATE recette_candidat SET numero_recu = ? WHERE id_transaction = ?";
        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, numeroRecu);
            pstmt.setLong(2, idTransaction);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean annulerTransaction(long idTransaction, String motifAnnulation) {
        String queryUpdate = "UPDATE transactions SET statut = 'annulee', motif_annulation = ? WHERE id = ? AND statut = 'validee'";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement stmt = connexion.prepareStatement(queryUpdate)) {

            stmt.setString(1, motifAnnulation);
            stmt.setLong(2, idTransaction);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // creation reçu versement
    public static DonneesVersementCandidat getDetailsVersement(long idRecetteCandidat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.systemDefault());
        String sql = "SELECT " +
                "c.id AS id_candidat, " +
                "i.id AS id_inscription, " +
                "t.id AS id_transaction, " +
                "rc.id AS id_recette_candidat, " +
                "i.id_agence, " +
                "c.nom, " +
                "c.nom_de_jeune_fille, " +
                "c.prenom, " +
                "i.date_inscription, " +
                "i.forfait, " +
                "i.categorie_de_permis, " +
                "t.montant AS montant_verse, " +
                "rc.reste_a_payer AS somme_restante, " +
                "t.created_at AS date_versement, " + // ou heure_transaction selon votre besoin
                "rc.type_versement, " +
                "rc.numero_recu " +
                "FROM recette_candidat rc " +
                "JOIN transactions t ON rc.id_transaction = t.id " +
                "JOIN candidat c ON rc.id_candidat = c.id " +
                "JOIN inscription i ON rc.id_inscription = i.id " +
                "WHERE rc.id = ?";

        try (Connection connexion = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setLong(1, idRecetteCandidat);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DonneesVersementCandidat(
                            rs.getLong("id_candidat"),
                            rs.getLong("id_inscription"),
                            rs.getLong("id_transaction"),
                            rs.getLong("id_recette_candidat"),
                            rs.getInt("id_agence"),
                            rs.getString("nom"),
                            rs.getString("nom_de_jeune_fille"),
                            rs.getString("prenom"),
                            rs.getString("date_inscription"),
                            rs.getString("forfait"),
                            rs.getString("categorie_de_permis"),
                            rs.getDouble("montant_verse"),
                            rs.getDouble("somme_restante"),
                            rs.getString("date_versement"),
                            rs.getString("type_versement"),
                            rs.getString("numero_recu"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<TableauServiceTier> recupererTousLesServicesTiers() {
        ObservableList<TableauServiceTier> liste = FXCollections.observableArrayList();

        String sql = "SELECT ar.id, ar.nom_client, ar.prenom_client, ar.contact_client, ar.description, " +
                "ar.nombre_seances, t.montant, t.created_at, YEAR(t.created_at) as annee " +
                "FROM autres_recettes ar " +
                "JOIN transactions t ON ar.id_transaction = t.id " +
                "WHERE ar.source = 'Service Tiers' " +
                "ORDER BY t.created_at DESC";

        try (Connection cx = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement ps = cx.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idServTiers = rs.getInt("id");
                String nom = rs.getString("nom_client");
                String prenom = rs.getString("prenom_client");
                String contact = rs.getString("contact_client");
                String description = rs.getString("description");
                int nombreSeances = rs.getInt("nombre_seances");
                BigDecimal montant = rs.getBigDecimal("montant");
                int annee = rs.getInt("annee"); // Récupéré dynamiquement depuis la date

                // Instanciation de votre objet (Adaptez selon le constructeur de votre classe
                // TableauServiceTier)
                TableauServiceTier item = new TableauServiceTier(idServTiers, nom, prenom, contact, description,
                        nombreSeances, montant, annee);
                liste.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    public static InfoRecuServiceTier recupererInfoRecuParId(long idAutreRecette) {
        String sql = "SELECT ar.nom_client, ar.prenom_client, ar.description, ar.numero_recu, " +
                "t.montant, t.created_at " +
                "FROM autres_recettes ar " +
                "JOIN transactions t ON ar.id_transaction = t.id " +
                "WHERE ar.id = ?";

        try (Connection cx = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setLong(1, idAutreRecette);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("nom_client");
                    String prenom = rs.getString("prenom_client");

                    // Formatage de la date de paiement (ex: dd/MM/yyyy HH:mm)
                    String datePaiement = "";
                    java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        datePaiement = timestamp.toLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    }

                    String description = rs.getString("description");
                    int montant = rs.getBigDecimal("montant").intValue(); // Conversion BigDecimal vers int pour votre
                                                                          // PDF
                    String numeroRecu = rs.getString("numero_recu");
                    if (numeroRecu == null || numeroRecu.isEmpty()) {
                        numeroRecu = "S/N"; // Valeur par défaut si aucun numéro n'est défini
                    }

                    return new InfoRecuServiceTier(nom, prenom, datePaiement, description, montant, numeroRecu);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retourne null si l'enregistrement n'est pas trouvé
    }

    public static ObservableList<Integer> recupererAnneesServicesTiers() {
        ObservableList<Integer> annees = FXCollections.observableArrayList();

        // Requête pour récupérer les années distinctes des transactions liées aux
        // services tiers
        String sql = "SELECT DISTINCT YEAR(t.created_at) AS annee " +
                "FROM autres_recettes ar " +
                "JOIN transactions t ON ar.id_transaction = t.id " +
                "WHERE ar.source = 'Service Tiers' " +
                "ORDER BY annee DESC";

        try (Connection cx = DriverManager.getConnection(App.url, App.utilisateur, App.motDePasse);
                PreparedStatement ps = cx.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                annees.add(rs.getInt("annee"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Sécurité : Si la base est vide, on ajoute au moins l'année courante pour
        // éviter une ChoiceBox vide
        if (annees.isEmpty()) {
            annees.add(LocalDate.now().getYear());
        }

        return annees;
    }
}
