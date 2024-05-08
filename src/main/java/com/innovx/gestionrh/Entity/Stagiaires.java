package com.innovx.gestionrh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @OneToOne(mappedBy = "matriculestagiaire")
    private Collaborateurs collaborateurs;
    @OneToOne(mappedBy = "matriculestagiaire")
    private Document document;
    @Builder.Default
    private boolean isDeleted = false;
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
}
