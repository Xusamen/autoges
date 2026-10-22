package com.cybersek.autoges;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class GenerateurPdf {

    public static String chemin_image_entete = "/com/cybersek/autoges/icone/entete.jpg";
    private static final float MARGE = 50;
    private static final float ROW_HEIGHT = 20;
    private static final float FONT_SIZE = 11;
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private static final float Y_MIN = MARGE + 100;
    public static final float Y_MIN_NEW = 60;
    public static final float PAGE_LARGEUR = 595;
    public static final float PAGE_HAUTEUR = 420;
    public static DateTimeFormatter formatterFichier = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm");

    public static LocalDateTime maintenant = LocalDateTime.now();

    public static String convertirFormatDate(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, inputFormatter);
        return dateTime.format(outputFormatter);
    }

    public static byte[] ajouterImageAuPDF(String imagePath) throws IOException {

        // Charger l'image à partir des ressources du JAR
        InputStream imageStream = GenerateurPdf.class.getResourceAsStream(imagePath);

        if (imageStream == null) {
            throw new IOException("Le fichier image " + imagePath + " n'a pas été trouvé dans le JAR.");
        }

        // Convertir le flux d'entrée en tableau d'octets
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = imageStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        imageStream.close();
        return imageBytes;

    }

    public static void CreationRecuVersement(String nom, String nom_de_jeune_fille, String prenom,
            String dateInscription, String forfait, String categoriePermis, Double montantVerse, Double sommeRestante,
            String dateVersement, String typeVersement, String numero_recu) {
        try (PDDocument document = new PDDocument()) {
            PDRectangle formatA5Paysage = new PDRectangle(PDRectangle.A5.getHeight(), PDRectangle.A5.getWidth());
            PDPage page = new PDPage(formatA5Paysage);
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float pageLargeur = formatA5Paysage.getWidth();
                float pageHauteur = formatA5Paysage.getHeight();

                dessinerRecuVersement(contentStream, document, nom, prenom, dateInscription, forfait, categoriePermis,
                        montantVerse, sommeRestante, dateVersement, typeVersement, numero_recu, 0, pageLargeur);
            }
            String cheminRacine = System.getProperty("user.home") + "\\Documents\\NOURAT-AUTO-DOCS\\reçus\\";
            String nomduFichier = "reçu_versement_" + maintenant.format(formatterFichier) + ".pdf";
            String cheminFichier = cheminRacine + nomduFichier;

            java.io.File dossier = new java.io.File(cheminRacine);
            if (!dossier.exists()) {
                dossier.mkdirs();
            }

            document.save(cheminFichier);
            System.out.println("PDF généré avec succès : " + cheminFichier);
            document.save(cheminFichier);
            document.close();
            Functions.printPdfLePlusRecent(cheminRacine);

        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
    }

    private static void dessinerRecuVersement(PDPageContentStream contentStream, PDDocument document, String nom,
            String prenom, String dateInscription, String forfait, String categoriePermis, Double montantVerse,
            Double sommeRestante, String dateVersement, String typeVersement, String numero_recu, float decalageY,
            float pageLargeur)
            throws IOException {
        float imageWidth = pageLargeur - 2 * 10;
        float imageHeight = 90;
        float imageX = 10;
        float imageY = decalageY + (PDRectangle.A4.getHeight() / 2) - 10 - imageHeight;
        float endX = pageLargeur - 10;

        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document,
                ajouterImageAuPDF(GenerateurPdf.chemin_image_entete), GenerateurPdf.chemin_image_entete);
        contentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);

        // Ensuite dessiner les textes à partir de decalageY
        float y = imageY - 20;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(200, y);
        contentStream.showText("REÇU N° : ");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(275, y);
        contentStream.showText(numero_recu);
        contentStream.endText();

        y -= 20;
        contentStream.setStrokingColor(0, 0, 0);
        contentStream.setLineWidth(1);
        contentStream.moveTo(10, y);
        contentStream.lineTo(endX, y);
        contentStream.stroke();

        y -= 20;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, y);
        contentStream.showText("Reçu de : ");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(75, y);
        contentStream.showText(nom + "   " + prenom);
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(300, y);
        contentStream.showText("Motif :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(450, y);
        contentStream.showText(typeVersement);
        contentStream.endText();

        y -= 20;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, y);
        contentStream.showText("Date d'inscription :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(175, y);
        contentStream.showText(convertirFormatDate(dateInscription));
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(300, y);
        contentStream.showText("Categorie du permis :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(450, y);
        contentStream.showText(categoriePermis);
        contentStream.endText();

        y -= 20;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, y);
        contentStream.showText("Somme versé :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(175, y);
        contentStream.showText(montantVerse + " Francs CFA");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(300, y);
        contentStream.showText("Date du versement :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(450, y);
        contentStream.showText(convertirFormatDate(dateVersement));
        contentStream.endText();

        if (typeVersement != null && (typeVersement.equals("Frais Dossier") || typeVersement.equals("Frais Examen"))) {
            y -= 20;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(10, y);
            contentStream.showText("Reçu généré par :");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(175, y);
            contentStream.showText(NomUser.getlogger());
            contentStream.endText();

            y -= 40;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(420, y);
            contentStream.showText("Signature et cachet");
            contentStream.endText();

            y -= 80;
            contentStream.setStrokingColor(0, 0, 0);
            contentStream.setLineWidth(1); // Épaisseur du trait
            contentStream.moveTo(10, y);
            contentStream.lineTo(endX, y);
            contentStream.stroke();

            y -= 10;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(10, y);
            contentStream.showText("Note :");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Merci de conserver ce reçu.");
            contentStream.endText();

            y -= 10;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, y);
            contentStream.showText(
                    "Si vous avez des questions, veuillez contacter le responsable aux coordonnées fournies ci-dessus");
            contentStream.endText();

        }

        if (typeVersement != null && typeVersement.contains("Paiement Tranche")) {
            y -= 20;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(10, y);
            contentStream.showText("Reste à payer :");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(175, y);
            contentStream.showText(sommeRestante + " Francs CFA");
            contentStream.endText();

            y -= 20;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(10, y);
            contentStream.showText("Tarif :");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(175, y);
            contentStream.showText(forfait);
            contentStream.endText();

            y -= 20;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(10, y);
            contentStream.showText("Reçu généré par :");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(175, y);
            contentStream.showText(NomUser.getlogger());
            contentStream.endText();

            y -= 10;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(420, y);
            contentStream.showText("Signature et cachet");
            contentStream.endText();

            y -= 80;
            contentStream.setStrokingColor(0, 0, 0);
            contentStream.setLineWidth(1); // Épaisseur du trait
            contentStream.moveTo(10, y);
            contentStream.lineTo(endX, y);
            contentStream.stroke();

            y -= 10;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(10, y);
            contentStream.showText("Note :");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Merci de conserver ce reçu.");
            contentStream.endText();

            y -= 10;
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, y);
            contentStream.showText(
                    "Si vous avez des questions, veuillez contacter le responsable aux coordonnées fournies ci-dessus");
            contentStream.endText();
        }
    }

    // reçu paiement services tiers
    public static void CreationRecuServiceTier(String nom, String prenom, String datePaiement, String description,
            int montant, String numeroRecu) {
        try (PDDocument document = new PDDocument()) {
            PDRectangle formatA5Paysage = new PDRectangle(PDRectangle.A5.getHeight(), PDRectangle.A5.getWidth());
            PDPage page = new PDPage(formatA5Paysage);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float pageLargeur = formatA5Paysage.getWidth();
                float pageHauteur = formatA5Paysage.getHeight();

                dessinerRecuServiceTier(contentStream, document, nom, prenom, datePaiement, description, montant,
                        numeroRecu, 0, pageLargeur);
            }
            String cheminRacine = System.getProperty("user.home") + "\\Documents\\NOURAT-AUTO-DOCS\\reçus\\";
            String nomduFichier = "reçu_versement_" + maintenant.format(formatterFichier) + ".pdf";
            String cheminFichier = cheminRacine + nomduFichier;

            java.io.File dossier = new java.io.File(cheminRacine);
            if (!dossier.exists()) {
                dossier.mkdirs();
            }

            document.save(cheminFichier);
            System.out.println("PDF généré avec succès : " + cheminFichier);
            document.save(cheminFichier);
            document.close();
            Functions.printPdfLePlusRecent(cheminRacine);
        } catch (IOException erreur) {
            System.err.println(erreur.getMessage());
        }
    }

    private static void dessinerRecuServiceTier(PDPageContentStream contentStream, PDDocument document, String nom,
            String prenom, String datePaiement, String description, int montant, String numero_recu, float decalageY,
            float pageLargeur) throws IOException {
        float imageWidth = pageLargeur - 2 * 10;
        float imageHeight = 90;
        float imageX = 10;
        float imageY = decalageY + (PDRectangle.A4.getHeight() / 2) - 10 - imageHeight;
        float endX = pageLargeur - 10;
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document,
                GenerateurPdf.ajouterImageAuPDF(GenerateurPdf.chemin_image_entete), GenerateurPdf.chemin_image_entete);
        contentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);

        float y = imageY - 20;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(200, y);
        contentStream.showText("Reçu N° : ");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(275, y);
        contentStream.showText(numero_recu);
        contentStream.endText();

        y -= 20;
        contentStream.setStrokingColor(0, 0, 0);
        contentStream.setLineWidth(1);
        contentStream.moveTo(10, y);
        contentStream.lineTo(endX, y);
        contentStream.stroke();

        y -= 20;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, y);
        contentStream.showText("Reçu de :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(125, y);
        contentStream.showText(nom + "   " + prenom);
        contentStream.endText();

        y -= 20;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, y);
        contentStream.showText("Description :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(125, y);
        contentStream.showText(description);
        contentStream.endText();

        y -= 20;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, y);
        contentStream.showText("Montant : ");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(125, y);
        contentStream.showText(montant + " Francs CFA ");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(300, y);
        contentStream.showText("Date de paiement : ");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(450, y);
        contentStream.showText(datePaiement);
        contentStream.endText();

        y -= 30;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, y);
        contentStream.showText("Reçu généré par :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(175, y);
        contentStream.showText(NomUser.getlogger());
        contentStream.endText();

        y -= 50;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(420, y);
        contentStream.showText("Signature et cachet");
        contentStream.endText();

        y -= 80;
        contentStream.setStrokingColor(0, 0, 0);
        contentStream.setLineWidth(1);
        contentStream.moveTo(10, y);
        contentStream.lineTo(endX, y);
        contentStream.stroke();

        y -= 10;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, y);
        contentStream.showText("Note :");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText("Merci de conserver ce reçu.");
        contentStream.endText();

        y -= 10;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText(
                "Si vous avez des questions, veuillez contacter le responsable aux coordonnées fournies ci-dessus");
        contentStream.endText();

    }

    /// GENERATION DES LISTES
    ///
    public static void creationPDFListeExamen(List<StructureListeExamen> liste, String titre, String date) {
        String jour_semaine = Function.jour_semaine(date);
        String exception = "Format de date invalide. Veuillez utiliser le format JJ/MM/AAAA.";
        if (jour_semaine == exception) {
            Function.Alerte_erreur("Erreur inatendue (format Date requis JJ/MM/AAAA)");
            return;
        }
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            float fontSize = 9f;
            float y = PAGE_HEIGHT - 100;

            PDPageContentStream content = new PDPageContentStream(document, page);

            // --- En-tête
            PDImageXObject image = PDImageXObject.createFromByteArray(document,
                    ajouterImageAuPDF(Function.chemin_image_entete), Function.chemin_image_entete);
            content.drawImage(image, MARGE, PAGE_HEIGHT - MARGE - 70, PDRectangle.A4.getWidth() - 2 * MARGE, 90);
            y -= 40;

            // Titre centré
            content.beginText();
            content.setFont(fontBold, 12);
            float titreWidth = fontBold.getStringWidth("LISTE DES CANDIDATS A L'EXAMEN DU PERMIS DE CONDUIRE") / 1000
                    * 12;
            float titreX = (PDRectangle.A4.getWidth() - titreWidth) / 2;
            content.newLineAtOffset(titreX, y);
            content.showText("LISTE DES CANDIDATS A L'EXAMEN DU PERMIS DE CONDUIRE");
            content.endText();
            float underlineY = y - 2; // Soulignement juste en dessous du texte
            content.moveTo(titreX, underlineY);
            content.lineTo(titreX + titreWidth, underlineY);
            content.stroke();
            y -= 30;

            content.setFont(fontBold, 10);
            float subtitleX = MARGE;
            content.beginText();
            content.newLineAtOffset(subtitleX, y);
            content.showText("EPREUVE DE : " + titre);
            content.endText();
            content.beginText();
            content.newLineAtOffset(subtitleX + 4 * 100, y);
            content.showText("AE 56");
            content.endText();
            y -= 20;

            // Info date et catégorie
            content.beginText();
            content.setFont(fontBold, 10);
            content.newLineAtOffset(MARGE, y);
            content.showText("Date : " + date);
            content.endText();
            if (titre.equalsIgnoreCase("CONDUITE POIDS LOURD")) {
                content.beginText();
                content.newLineAtOffset(170, y);
                content.showText("VEHICULE : POIDS-LOURD N°");
                content.endText();
            }
            if (titre.equalsIgnoreCase("CONDUITE POIDS LEGER")) {
                content.beginText();
                content.newLineAtOffset(170, y);
                content.showText("VEHICULE : POIDS-LEGER N°");
                content.endText();
            }
            content.beginText();
            content.newLineAtOffset(MARGE + 4 * 100, y);
            content.showText("JOUR : " + jour_semaine);
            content.endText();
            y -= 25;

            // --- Colonnes
            String[] enTetes = { "N°", "Nom & Prénoms", "Date & Lieu de naissance", "Catégorie", "DRTTM" };
            float[] colWidths = { 20, 190, 170, 60, 50 };
            y = drawTableHeader(content, enTetes, colWidths, y, fontBold);

            int num = 1;
            for (StructureListeExamen s : liste) {
                String[] data = { String.valueOf(num++), s.getNom() + " " + s.getPrenom(),
                        s.getDate_N() + " à " + s.getLieu_N(), s.getCategorie(), "" };

                float ligneHauteur = calculerHauteur(data, colWidths, font, fontSize);

                // Saut de page si besoin
                if (y - ligneHauteur < Y_MIN_NEW) {
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = PAGE_HEIGHT - MARGE;

                    y = drawTableHeader(content, enTetes, colWidths, y, fontBold);
                }

                y = drawTableRow(content, data, colWidths, y, font, fontSize);
            }
            y -= 50;
            float bas_de_page = fontBold.getStringWidth("Examinateur du Jour") / 1000 * 10;
            float bas_de_page_other = fontBold.getStringWidth("Le Directeur") / 1000 * 10;
            drawText(content, "Examinateur du Jour", fontBold, 10, MARGE, y);
            drawText(content, "Le Directeur", fontBold, 10, MARGE + 4 * 100, y);
            content.moveTo(MARGE, y - 2);
            content.lineTo(MARGE + bas_de_page, y - 2);
            content.stroke();
            content.moveTo(MARGE + 4 * 100, y - 2);
            content.lineTo(MARGE + 4 * 100 + bas_de_page_other, y - 2);
            content.stroke();
            y -= 20;
            drawText(content, " Alain Prosper BATIONO ", fontBold, 10, MARGE + 370, y);
            content.close();
            if (titre.equalsIgnoreCase("CODE")) {
                // Sauvegarder le document PDF
                String cheminRacine = System.getProperty("user.home")
                        + "\\Documents\\DAF-AUTO-DOCS\\liste_examen\\code\\";
                File dossierCandidat = new File(cheminRacine);
                // Création du dossier s'il n'existe pas
                if (!dossierCandidat.exists()) {
                    dossierCandidat.mkdirs();
                }
                String dateFormattee = date.replace("/", "_");
                String nomFichier = "Liste_examen-" + dateFormattee + ".pdf";
                String cheminFichier = cheminRacine + nomFichier;
                document.save(cheminFichier);
                System.out.println("PDF créé avec succès !");
            }
            if (titre.equalsIgnoreCase("CONDUITE POIDS LEGER")) {
                // Sauvegarder le document PDF
                String cheminRacine = System.getProperty("user.home")
                        + "\\Documents\\DAF-AUTO-DOCS\\liste_examen\\conduite_PLEGER\\";
                File dossierCandidat = new File(cheminRacine);
                // Création du dossier s'il n'existe pas
                if (!dossierCandidat.exists()) {
                    dossierCandidat.mkdirs();
                }
                String dateFormattee = date.replace("/", "_");
                String nomFichier = "Liste_examen-" + dateFormattee + ".pdf";
                String cheminFichier = cheminRacine + nomFichier;
                document.save(cheminFichier);
                System.out.println("PDF créé avec succès !");
            }
            if (titre.equalsIgnoreCase("CONDUITE POIDS LOURD")) {
                // Sauvegarder le document PDF
                String cheminRacine = System.getProperty("user.home")
                        + "\\Documents\\DAF-AUTO-DOCS\\liste_examen\\conduite_PLOURD\\";
                File dossierCandidat = new File(cheminRacine);
                // Création du dossier s'il n'existe pas
                if (!dossierCandidat.exists()) {
                    dossierCandidat.mkdirs();
                }
                String dateFormattee = date.replace("/", "_");
                String nomFichier = "Liste_examen-" + dateFormattee + ".pdf";
                String cheminFichier = cheminRacine + nomFichier;
                document.save(cheminFichier);
                System.out.println("PDF créé avec succès !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float drawTableHeader(PDPageContentStream content, String[] entetes, float[] widths, float y,
            PDFont font) throws IOException {
        float x = MARGE;
        float rowHeight = 20;
        for (int i = 0; i < entetes.length; i++) {
            content.addRect(x, y - rowHeight, widths[i], rowHeight);
            content.stroke();
            drawText(content, entetes[i], font, 9, x + 3, y - 14);
            x += widths[i];
        }
        return y - rowHeight;
    }

    public static float drawTableRow(PDPageContentStream content, String[] data, float[] widths, float y, PDFont font,
            float fontSize) throws IOException {
        float height = calculerHauteur(data, widths, font, fontSize);
        float x = MARGE;

        for (int i = 0; i < data.length; i++) {
            content.addRect(x, y - height, widths[i], height);
            content.stroke();
            drawMultilineCellule(content, data[i], font, fontSize, x, y - height, widths[i], height);
            x += widths[i];
        }
        return y - height;
    }

    public static void drawText(PDPageContentStream content, String text, PDFont font, float size, float x, float y)
            throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }

    public static void drawMultilineCellule(PDPageContentStream content, String text, PDFont font, float fontSize,
            float x, float y, float width, float height) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        for (String word : text.split(" ")) {
            String tentative = line + (line.length() == 0 ? "" : " ") + word;
            float w = font.getStringWidth(tentative) / 1000 * fontSize;
            if (w > width - 6) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(tentative);
            }
        }
        if (line.length() > 0)
            lines.add(line.toString());

        float textY = y + height - fontSize - 4;
        for (String l : lines) {
            content.beginText();
            content.setFont(font, fontSize);
            content.newLineAtOffset(x + 3, textY);
            content.showText(l);
            content.endText();
            textY -= fontSize;
        }
    }

    public static float calculerHauteur(String[] data, float[] widths, PDFont font, float fontSize) throws IOException {
        float max = 0;
        for (int i = 0; i < data.length; i++) {
            int lignes = 1;
            StringBuilder currentLine = new StringBuilder();
            for (String word : data[i].split(" ")) {
                String tentative = currentLine + (currentLine.length() == 0 ? "" : " ") + word;
                float w = font.getStringWidth(tentative) / 1000 * fontSize;
                if (w > widths[i] - 6) {
                    lignes++;
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine = new StringBuilder(tentative);
                }
            }
            float h = lignes * fontSize + 6;
            if (h > max)
                max = h;
        }
        return max;
    }

    //// Generateur Bordereau
    ///
    ///
    public static void Creation_liste_Bordereau(List<Info_bordereau> parametre, String date, String categorie) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            float fontSize = 9f;
            float y = PAGE_HEIGHT - 100;
            int ordre = 1;

            PDPageContentStream content = new PDPageContentStream(document, page);

            // --- Image en-tête
            PDImageXObject image = PDImageXObject.createFromByteArray(document,
                    ajouterImageAuPDF(Function.chemin_image_entete), Function.chemin_image_entete);

            content.drawImage(image, MARGE, PAGE_HEIGHT - MARGE - 70, PDRectangle.A4.getWidth() - 2 * MARGE, 90);
            y -= 40;

            // Titre
            String titre = "BORDEREAU D’ENVOI";
            content.beginText();
            content.setFont(fontBold, 12);
            float titreWidth = fontBold.getStringWidth(titre) / 1000 * 12;
            content.newLineAtOffset((PDRectangle.A4.getWidth() - titreWidth) / 2, y);
            content.showText(titre);
            content.endText();
            y -= 25;

            // Sous-titre
            String sousTitre = "Du " + Function.jour_semaine(date) + " " + date + " - Catégorie : " + categorie;
            content.beginText();
            content.setFont(fontBold, 10);
            float stWidth = fontBold.getStringWidth(sousTitre) / 1000 * 10;
            content.newLineAtOffset((PDRectangle.A4.getWidth() - stWidth) / 2, y);
            content.showText(sousTitre);
            content.endText();
            y -= 20;

            // AGENCE
            String agenceText = "AE 56";
            drawText(content, agenceText, fontBold, 10, MARGE, y);
            y -= 15;

            // Entête du tableau
            String[] enTetes = { "N°", "Nom & Prénoms", "Date & lieu de naissance", "Catégorie" };
            float[] colWidths = { 25, 210, 180, 90 };
            y = drawTableHeader(content, enTetes, colWidths, y, fontBold);

            for (Info_bordereau candidat : parametre) {
                String[] data = { String.valueOf(ordre++), candidat.getNom() + " " + candidat.getPrenom(),
                        new SimpleDateFormat("dd/MM/yyyy").format(candidat.getDate_de_naissance()) + " à "
                                + candidat.getLieu_de_naissance(),
                        candidat.getCategorie() };

                float hauteur = calculerHauteur(data, colWidths, font, fontSize);

                if (y - hauteur < Y_MIN_NEW) {
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = PAGE_HEIGHT - MARGE;
                    y = drawTableHeader(content, enTetes, colWidths, y, fontBold);
                }

                y = drawTableRow(content, data, colWidths, y, font, fontSize);
            }

            // Arrêt de liste
            y -= 20;
            String ligneArret;
            String nombre_de_nom_en_lettre = Function.convertion_en_lettre(parametre.size());
            int nombre_de_nom_int = parametre.size();
            if (nombre_de_nom_int == 0 || nombre_de_nom_int == 1) {
                ligneArret = "Arrêter la présente liste à " + nombre_de_nom_en_lettre + " (" + nombre_de_nom_int
                        + ") nom.";
            } else {
                ligneArret = "Arrêter la présente liste à " + nombre_de_nom_en_lettre + " (" + nombre_de_nom_int
                        + ") noms.";
            }
            drawText(content, ligneArret, font, 10, MARGE, y);

            // Signature
            y -= 50;
            drawText(content, "Le Directeur", fontBold, 11, MARGE + 350, y);
            y -= 25;
            drawText(content, "Alain Prosper BATIONO", font, 10, MARGE + 335, y);

            content.close();

            String chemin = System.getProperty("user.home") + "\\Documents\\DAF-AUTO-DOCS\\bordereaux\\Bordereau-"
                    + date.replace("/", "_") + ".pdf";
            new File(chemin).getParentFile().mkdirs();
            document.save(chemin);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // fonction generatrice du pdf du bilan journalier
    public static void generationPDFBilanJournalier(ClotureJour cloture, String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            String titre_document = "BILAN JOURNALIER DU " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String dateFichier = date.format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));

            String cheminImage = chemin_image_entete;
            String cheminSortie = System.getProperty("user.home")
                    + "\\Documents\\DAF-AUTO-DOCS\\bilan_journalier\\bilan_journalier-" + dateFichier + ".pdf";
            new File(cheminSortie).getParentFile().mkdirs();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                PDPageContentStream content = new PDPageContentStream(document, page);
                PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fontNormal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                float y = PAGE_HEIGHT - 120;

                // --- En-tête : image + titre ---
                y = drawHeader(content, document, cheminImage, titre_document, fontBold, y);

                // --- Entrées ---
                int totalEntree = 0;
                y = drawTableHeader(content, "Liste des entrées du jour",
                        new String[] { "N°", "DESCRIPTION", "MONTANT", "COMPTE", "MODE" },
                        new float[] { 30, 255, 70, 70, 70 }, y, fontBold);
                int ligne = 1;

                for (Transaction t : cloture.entrees) {
                    if (y < Y_MIN) {
                        content.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        y = PAGE_HEIGHT - MARGE;
                    }
                    String[] data = { String.valueOf(ligne++), t.description, String.valueOf(t.montant), t.compte,
                            t.modeTransaction };
                    y = drawTableRow(content, data, new float[] { 30, 255, 70, 70, 70 }, y, fontNormal);
                    totalEntree += t.montant;
                }

                if (cloture.entrees.isEmpty()) {
                    y -= ROW_HEIGHT;
                    writeLine(content, "Aucune entrée", fontNormal, 9, MARGE, y + 6);
                }

                y -= ROW_HEIGHT;
                writeLine(content, "Total Entrées : " + totalEntree, fontBold, 10, MARGE, y);

                // --- Sorties ---
                y -= 40;
                int totalSortie = 0;
                y = drawTableHeader(content, "Liste des sorties du jour",
                        new String[] { "N°", "DESCRIPTION", "MONTANT", "MODE" }, new float[] { 30, 255, 70, 70 }, y,
                        fontBold);
                ligne = 1;

                for (Transaction t : cloture.sorties) {
                    if (y < Y_MIN) {
                        content.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        y = PAGE_HEIGHT - MARGE;
                    }
                    String[] data = { String.valueOf(ligne++), t.description, String.valueOf(t.montant),
                            t.modeTransaction };
                    y = drawTableRow(content, data, new float[] { 30, 255, 70, 70 }, y, fontNormal);
                    totalSortie += t.montant;
                }

                if (cloture.sorties.isEmpty()) {
                    y -= ROW_HEIGHT;
                    writeLine(content, "Aucune sortie", fontNormal, 9, MARGE, y + 6);
                }

                y -= ROW_HEIGHT;
                writeLine(content, "Total Sorties : " + totalSortie, fontBold, 10, MARGE, y);

                // --- Bilan ---
                y -= 40;
                writeLine(content, "BILAN DU JOUR : ENTREE = " + totalEntree + " | SORTIE = " + totalSortie
                        + " | TOTAL = " + (totalEntree - totalSortie), fontBold, 12, MARGE, y);

                content.close();
                document.save(cheminSortie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generationPDFBilanMensuel(ClotureMois cloture, String mois) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String titre_document = "BILAN MENSUEL DU : " + mois;
            String moisFichier = mois.replace("/", "_");
            String cheminImage = Function.chemin_image_entete;
            String cheminSortie = System.getProperty("user.home")
                    + "\\Documents\\DAF-AUTO-DOCS\\bilan_mensuel\\bilan_mensuel-" + moisFichier + ".pdf";
            new File(cheminSortie).getParentFile().mkdirs();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                PDPageContentStream content = new PDPageContentStream(document, page);
                PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fontNormal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                float y = PAGE_HEIGHT - 120;

                // --- En-tête : image + titre ---
                y = drawHeader(content, document, cheminImage, titre_document, fontBold, y);

                // --- Entrées ---
                int totalEntree = 0;
                y = drawTableHeader(content, "Liste des entrées du mois",
                        new String[] { "N°", "DESCRIPTION", "MONTANT", "COMPTE", "MODE", "DATE" },
                        new float[] { 30, 220, 60, 60, 60, 60 }, y, fontBold);
                int ligne = 1;

                for (Transaction_Other t : cloture.entrees) {
                    if (y < Y_MIN) {
                        content.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        y = PAGE_HEIGHT - MARGE;
                    }
                    String[] data = { String.valueOf(ligne++), t.description, String.valueOf(t.montant), t.compte,
                            t.modeTransaction, t.date.format(format) // ou formaté
                    };
                    y = drawTableRow(content, data, new float[] { 30, 220, 60, 60, 60, 60 }, y, fontNormal);
                    totalEntree += t.montant;
                }

                if (cloture.entrees.isEmpty()) {
                    y -= ROW_HEIGHT;
                    writeLine(content, "Aucune entrée", fontNormal, 9, MARGE, y + 6);
                }

                y -= ROW_HEIGHT;
                writeLine(content, "Total Entrées : " + totalEntree, fontBold, 10, MARGE, y);

                // --- Sorties ---
                y -= 40;
                int totalSortie = 0;
                y = drawTableHeader(content, "Liste des sorties du mois",
                        new String[] { "N°", "DESCRIPTION", "MONTANT", "MODE", "DATE" },
                        new float[] { 30, 220, 60, 60, 60 }, y, fontBold);
                ligne = 1;

                for (Transaction_Other t : cloture.sorties) {
                    if (y < Y_MIN) {
                        content.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        y = PAGE_HEIGHT - MARGE;
                    }
                    String[] data = { String.valueOf(ligne++), t.description, String.valueOf(t.montant),
                            t.modeTransaction, t.date.format(format) };
                    y = drawTableRow(content, data, new float[] { 30, 220, 60, 60, 60 }, y, fontNormal);
                    totalSortie += t.montant;
                }

                if (cloture.sorties.isEmpty()) {
                    y -= ROW_HEIGHT;
                    writeLine(content, "Aucune sortie", fontNormal, 9, MARGE, y + 6);
                }

                y -= ROW_HEIGHT;
                writeLine(content, "Total Sorties : " + totalSortie, fontBold, 10, MARGE, y);

                // --- Bilan ---
                y -= 40;
                writeLine(content, "BILAN DU MOIS : ENTREE = " + totalEntree + " | SORTIE = " + totalSortie
                        + " | TOTAL = " + (totalEntree - totalSortie), fontBold, 12, MARGE, y);
                content.close();
                document.save(cheminSortie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generationPDFBilanAnnuel(ClotureAnnee cloture, String annee) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String titre_document = "BILAN ANNUEL DE : " + annee;
            String cheminImage = Function.chemin_image_entete;
            String cheminSortie = System.getProperty("user.home")
                    + "\\Documents\\DAF-AUTO-DOCS\\bilan_annuel\\bilan_annuel-" + annee + ".pdf";
            new File(cheminSortie).getParentFile().mkdirs();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                PDPageContentStream content = new PDPageContentStream(document, page);
                PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fontNormal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                float y = PAGE_HEIGHT - 120;

                // --- En-tête : image + titre ---
                y = drawHeader(content, document, cheminImage, titre_document, fontBold, y);

                // --- Entrées ---
                int totalEntree = 0;
                y = drawTableHeader(content, "Liste des entrées de l'année",
                        new String[] { "N°", "DESCRIPTION", "MONTANT", "COMPTE", "MODE", "DATE" },
                        new float[] { 30, 220, 60, 60, 60, 60 }, y, fontBold);
                int ligne = 1;

                for (Transaction_Other t : cloture.entrees) {
                    if (y < Y_MIN) {
                        content.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        y = PAGE_HEIGHT - MARGE;
                    }
                    String[] data = { String.valueOf(ligne++), t.description, String.valueOf(t.montant), t.compte,
                            t.modeTransaction, t.date.format(format) // ou formaté
                    };
                    y = drawTableRow(content, data, new float[] { 30, 220, 60, 60, 60, 60 }, y, fontNormal);
                    totalEntree += t.montant;
                }

                if (cloture.entrees.isEmpty()) {
                    y -= ROW_HEIGHT;
                    writeLine(content, "Aucune entrée", fontNormal, 9, MARGE, y + 6);
                }

                y -= ROW_HEIGHT;
                writeLine(content, "Total Entrées : " + totalEntree, fontBold, 10, MARGE, y);

                // --- Sorties ---
                y -= 40;
                int totalSortie = 0;
                y = drawTableHeader(content, "Liste des sorties de l'année",
                        new String[] { "N°", "DESCRIPTION", "MONTANT", "MODE", "DATE" },
                        new float[] { 30, 220, 60, 60, 60 }, y, fontBold);
                ligne = 1;

                for (Transaction_Other t : cloture.sorties) {
                    if (y < Y_MIN) {
                        content.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        y = PAGE_HEIGHT - MARGE;
                    }
                    String[] data = { String.valueOf(ligne++), t.description, String.valueOf(t.montant),
                            t.modeTransaction, t.date.format(format) };
                    y = drawTableRow(content, data, new float[] { 30, 220, 60, 60, 60 }, y, fontNormal);
                    totalSortie += t.montant;
                }

                if (cloture.sorties.isEmpty()) {
                    y -= ROW_HEIGHT;
                    writeLine(content, "Aucune sortie", fontNormal, 9, MARGE, y + 6);
                }

                y -= ROW_HEIGHT;
                writeLine(content, "Total Sorties : " + totalSortie, fontBold, 10, MARGE, y);

                // --- Bilan ---
                y -= 40;
                writeLine(content, "BILAN DE L'ANNEE : ENTREE = " + totalEntree + " | SORTIE = " + totalSortie
                        + " | TOTAL = " + (totalEntree - totalSortie), fontBold, 12, MARGE, y);

                content.close();
                document.save(cheminSortie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float drawHeader(PDPageContentStream content, PDDocument doc, String imagePath, String titre,
            PDFont font, float y) throws IOException {
        float imageWidth = PAGE_WIDTH - 2 * MARGE;
        float imageHeight = 90;
        float imageX = MARGE;
        float imageY = PAGE_HEIGHT - 20 - imageHeight;

        PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, ajouterImageAuPDF(chemin_image_entete),
                chemin_image_entete);
        content.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);

        float titreWidth = font.getStringWidth(titre) / 1000 * FONT_SIZE;
        float titreX = (PAGE_WIDTH - titreWidth) / 2;
        float titreY = imageY - 20;
        content.setFont(font, FONT_SIZE);
        content.beginText();
        content.newLineAtOffset(titreX, titreY);
        content.showText(titre);
        content.endText();

        content.moveTo(titreX, titreY - 2);
        content.lineTo(titreX + titreWidth, titreY - 2);
        content.stroke();

        return titreY - 40;
    }

    private static float drawTableHeader(PDPageContentStream content, String titre, String[] entetes, float[] widths,
            float y, PDFont font) throws IOException {
        writeLine(content, titre, font, 11, MARGE, y);
        y -= ROW_HEIGHT + 5;
        float x = MARGE;
        for (int i = 0; i < entetes.length; i++) {
            content.addRect(x, y, widths[i], ROW_HEIGHT);
            content.stroke();
            content.beginText();
            content.setFont(font, 10);
            content.newLineAtOffset(x + 5, y + 6);
            content.showText(entetes[i]);
            content.endText();
            x += widths[i];
        }
        return y;
    }

    private static void writeLine(PDPageContentStream content, String text, PDFont font, float size, float x, float y)
            throws IOException {
        content.setFont(font, size);
        content.beginText();
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }

    public static void drawMultilineCell(PDPageContentStream content, String text, PDFont font, float fontSize, float x,
            float y, float width, float cellHeight) throws IOException {
        List<String> lignes = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            String tentative = currentLine + (currentLine.length() == 0 ? "" : " ") + word;
            float textWidth = font.getStringWidth(tentative) / 1000 * fontSize;
            if (textWidth > width - 6) { // marge interne
                lignes.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(tentative);
            }
        }
        if (currentLine.length() > 0) {
            lignes.add(currentLine.toString());
        }

        float startY = y + cellHeight - fontSize - 4;

        for (String ligne : lignes) {
            content.beginText();
            content.setFont(font, fontSize);
            content.newLineAtOffset(x + 3, startY);
            content.showText(ligne);
            content.endText();
            startY -= fontSize;
        }
    }

    public static float drawTableRow(PDPageContentStream content, String[] data, float[] widths, float y, PDFont font)
            throws IOException {
        float fontSize = 9f;
        float maxHeight = 0f;

        // 1. Calculer la hauteur nécessaire pour chaque cellule
        int[] lignesParCellule = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            lignesParCellule[i] = calculerLignes(data[i], font, fontSize, widths[i]);
            float h = lignesParCellule[i] * fontSize + 6;
            if (h > maxHeight) {
                maxHeight = h;
            }
        }

        // 2. Dessiner les rectangles
        float x = MARGE;
        for (int i = 0; i < data.length; i++) {
            content.addRect(x, y - maxHeight, widths[i], maxHeight);
            content.stroke();
            x += widths[i];
        }

        // 3. Afficher le texte dans chaque cellule
        x = MARGE;
        for (int i = 0; i < data.length; i++) {
            drawMultilineCell(content, data[i], font, fontSize, x, y - maxHeight, widths[i], maxHeight);
            x += widths[i];
        }

        // 4. Retourner la nouvelle position y (ligne suivante)
        return y - maxHeight;
    }

    public static int calculerLignes(String text, PDFont font, float fontSize, float width) throws IOException {
        int lignes = 0;
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            String tentative = currentLine + (currentLine.length() == 0 ? "" : " ") + word;
            float textWidth = font.getStringWidth(tentative) / 1000 * fontSize;
            if (textWidth > width - 6) {
                lignes++;
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(tentative);
            }
        }

        if (currentLine.length() > 0)
            lignes++;
        return Math.max(lignes, 1);
    }

}
