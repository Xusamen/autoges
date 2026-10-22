package com.cybersek.autoges;


import com.cybersek.autoges.StructureDonnee.DonneesVersementCandidat;
import com.cybersek.autoges.ModuleComptabilite.Recette;

public class test {

    public static void main(String[] args) {
        DonneesVersementCandidat data = Recette.getDetailsVersement(3);
        GenerateurPdf.CreationRecuVersement(data.getNom(), data.getNomDeJeuneFille(), data.getPrenom(),
                data.getDateInscription(), data.getForfait(), data.getCategoriePermis(),
                data.getMontantVerse(), data.getSommeRestante(), data.getDateVersement(), data.getTypeVersement(), data.getNumeroRecu());

    }

}
