package com.innovx.gestionrh.Service;

import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Entity.Document;
import com.innovx.gestionrh.Entity.Stagiaires;
import com.innovx.gestionrh.Repository.CollaborateursRepository;
import com.innovx.gestionrh.Repository.DocumentRepository;
import com.innovx.gestionrh.Repository.StagiairesRepository;
import com.innovx.gestionrh.dto.DocumentValidityDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class StagiaireServiceImpl implements StagiareService{
    @Autowired
    private StagiairesRepository stagiairesRepository;

@Autowired
private CollaborateursRepository collaborateursRepository;



    @Override
    public List<Stagiaires> getAllStagiaires() {
        List<Stagiaires> allStagiaires = stagiairesRepository.findAll();
        return allStagiaires.stream()
                .filter(stagiaire -> !stagiaire.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Stagiaires> getStagiairesById(Long id) {
        Optional<Stagiaires> stagiairesOpt = stagiairesRepository.findById(id);
        if (stagiairesOpt.isPresent() && !stagiairesOpt.get().isDeleted()) {
            return stagiairesOpt;
        }
        return Optional.empty();
    }

    @Override
    public Stagiaires createStagiaires(Stagiaires stagiaires) {
        Collaborateurs existingCollaborateur = collaborateursRepository.findByFullName(stagiaires.getNomEncadrant());
        if (existingCollaborateur != null) {
            if ("Terminé".equalsIgnoreCase(stagiaires.getStatus())) {
                stagiaires.createMeetings();
            }
            return stagiairesRepository.save(stagiaires);
        } else {
            throw new IllegalStateException("No collaborateur exists with the full name as provided.");
        }
    }

    @Override
    public Stagiaires updateStagiaires(Long id, Stagiaires updatedStagiaires) {
        updatedStagiaires.setMatricule(id);
        Stagiaires stagiaires = stagiairesRepository.findById(updatedStagiaires.getMatricule()).orElse(null);
        if (stagiaires != null) {
            BeanUtils.copyProperties(updatedStagiaires, stagiaires);
            if ("Terminé".equalsIgnoreCase(stagiaires.getStatus())) {
                stagiaires.createMeetings();
            }
            return stagiairesRepository.save(stagiaires);
        }
        return updatedStagiaires;
    }

    @Override
    public String deleteStagiaires(Long id) {
        Stagiaires stagiaires = stagiairesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("stagiaires not found with ID: " + id));
        stagiairesRepository.delete(stagiaires);
        return "stagiaires deleted successfully.";
    }

    @Override
    public List<DocumentValidityDTO> getAllDocumentValidity() {
        return stagiairesRepository.findAll().stream().map(stagiaire -> {
            DocumentValidityDTO dto = new DocumentValidityDTO();
            dto.setMatricule(stagiaire.getMatricule());
            dto.setAttestationAssurance(stagiaire.isAttestationAssurance());
            dto.setCarteNationale(stagiaire.isCarteNationale());
            dto.setFicheAnthropométrique(stagiaire.isFicheAnthropométrique());
            dto.setCopieCertifiéeDiplômes(stagiaire.isCopieCertifiéeDiplômes());
            dto.setRelevéIdentitéBancaire(stagiaire.isRelevéIdentitéBancaire());
            dto.setCv(stagiaire.isCv());
            dto.setConventionStage(stagiaire.isConventionStage());
            dto.setFicheÉvaluation(stagiaire.isFicheÉvaluation());
            dto.setCharteEngagement(stagiaire.isCharteEngagement());
            dto.setAttestationStage(stagiaire.isAttestationStage());
            return dto;
        }).collect(Collectors.toList());
    }
}
