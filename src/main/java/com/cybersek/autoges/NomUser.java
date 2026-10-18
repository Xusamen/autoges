package com.cybersek.autoges;

public class NomUser {
    private static String logger = "";
    private static String logger_identifiant = "";
    private static String nom;
    private static String prenom;
    private static int id_utilisateur;
    private static String logger_fonction;

    public static String getlogger() {
        return logger;
    }

    public static String getfonction() {
        return logger_fonction;
    }

    public static String getlogger_identifiant() {
        return logger_identifiant;
    }

    public static void setlogger(int idUtilisateur, String value1, String value2) {
        logger = value1 + "  " + value2;
        nom = value1;
        prenom = value2;
        id_utilisateur = idUtilisateur;
    }

    public static void setlogger_info(int idUtilisateur, String value1, String value2, String value3) {
        logger = value1 + "  " + value2;
        nom = value1; prenom = value2;
        id_utilisateur = idUtilisateur;
        logger_fonction = value3;
    }

    public static void setlogger_identifiant(String value) {
        logger_identifiant = value;
    }

    public static void setlogger_fonction(String value) {
        logger_fonction = value;
    }

    public static String getNom() {
        return nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static int getIdUtilisateur() {
        return id_utilisateur;
    }
}
