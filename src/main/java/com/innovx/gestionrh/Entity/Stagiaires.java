package com.innovx.gestionrh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stagiaires {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Matricule")
    private Long matricule;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "CIN")
    private String CIN;

    @Column(name = "Département")
    private String département;

    @Column(name = "sujet_de_stage")
    private String sujetDeStage;

    @Column(name = "date_de_naissance")
    private Date dateDeNaissance;

    @Column(name = "nom_encadrant")
    private String nomEncadrant;

    @Column(name = "Ecole_Université")
    private String ecoleUniversité;

    @Column(name = "type_de_stage")
    private String typeDeStage;

    @Column(name = "date_début_stage")
    private Date dateDébutStage;

    @Column(name = "date_fin_stage")
    private Date dateFinStage;

    @Column(name = "durée")
    private int durée;

    @Column(name = "status")
    private String status;
    @Builder.Default
    private boolean isDeleted = false;
    @Column(name = "accueil_rh_date")
    private LocalDate accueilRhDate;

    @Column(name = "point_stagiaires_7_days_date")
    private LocalDate pointStagiaires7DaysDate;

    @Column(name = "point_stagiaires_1_month_date")
    private LocalDate pointStagiaires1MonthDate;

    @Column(name = "point_stagiaires_3_months_date")
    private LocalDate pointStagiaires3MonthsDate;
    @Lob
    @Column(name = "photo")
    private String photo; // Field to store the image

    @Column(name = "attestation_assurance")
    private boolean attestationAssurance;

    @Column(name = "carte_nationale")
    private boolean carteNationale;

    @Column(name = "fiche_anthropométrique")
    private boolean ficheAnthropométrique;

    @Column(name = "copie_certifiée_diplômes")
    private boolean copieCertifiéeDiplômes;

    @Column(name = "relevé_identité_bancaire")
    private boolean relevéIdentitéBancaire;

    @Column(name = "CV")
    private boolean cv;

    @Column(name = "convention_stage")
    private boolean conventionStage;

    @Column(name = "fiche_évaluation")
    private boolean ficheÉvaluation;

    @Column(name = "charte_engagement")
    private boolean charteEngagement;
    @Column(name = "attestation_stage")
    private boolean attestationStage;
    @PrePersist
    @PreUpdate
    private void calculateDuration() {
        if (dateDébutStage != null && dateFinStage != null) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(dateDébutStage);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(dateFinStage);

            int diffYears = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonths = diffYears * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

            durée = diffMonths;
        } else {
            durée = 0;
        }
    }
    public int getTotalValidDocuments() {
        return (attestationAssurance ? 1 : 0) +
                (carteNationale ? 1 : 0) +
                (ficheAnthropométrique ? 1 : 0) +
                (copieCertifiéeDiplômes ? 1 : 0) +
                (relevéIdentitéBancaire ? 1 : 0) +
                (cv ? 1 : 0) +
                (conventionStage ? 1 : 0) +
                (ficheÉvaluation ? 1 : 0) +
                (charteEngagement ? 1 : 0) +
                (attestationStage ? 1 : 0);
    }

    public int getTotalNotValidDocuments() {
        return 10 - getTotalValidDocuments(); // Assuming 10 as the total number of documents
    }
    public void createMeetings() {
        LocalDate now = LocalDate.now();
        this.accueilRhDate = now.plusDays(3);
        this.pointStagiaires7DaysDate = now.plusDays(10);
        this.pointStagiaires1MonthDate = now.plusMonths(1);
        this.pointStagiaires3MonthsDate = now.plusMonths(3);
    }
}
