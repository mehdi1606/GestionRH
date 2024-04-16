package com.innovx.gestionrh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "matricule", referencedColumnName = "Matricule")
    private Stagiaires matriculestagiaire;

    @Lob
    @Column(name = "attestation_assurance")
    private byte[] attestationAssurance;

    @Lob
    @Column(name = "carte_nationale")
    private byte[] carteNationale;

    @Lob
    @Column(name = "fiche_anthropométrique")
    private byte[] ficheAnthropométrique;

    @Lob
    @Column(name = "copie_certifiée_diplômes")
    private byte[] copieCertifiéeDiplômes;

    @Lob
    @Column(name = "relevé_identité_bancaire")
    private byte[] relevéIdentitéBancaire;

    @Lob
    @Column(name = "CV")
    private byte[] cv;

    @Lob
    @Column(name = "convention_stage")
    private byte[] conventionStage;

    @Lob
    @Column(name = "fiche_évaluation")
    private byte[] ficheÉvaluation;

    @Lob
    @Column(name = "charte_engagement")
    private byte[] charteEngagement;

    @Lob
    @Column(name = "attestation_stage")
    private byte[] attestationStage;
}
