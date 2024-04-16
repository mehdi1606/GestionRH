package com.innovx.gestionrh.Service;

import com.innovx.gestionrh.Entity.Document;
import com.innovx.gestionrh.Entity.Stagiaires;
import com.innovx.gestionrh.Repository.DocumentRepository;
import com.innovx.gestionrh.Repository.StagiairesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class StagiaireServiceImpl implements StagiareService{
    @Autowired
    private StagiairesRepository stagiairesRepository;


    @Autowired
    private DocumentRepository documentRepository;
    @Override
    public List<Stagiaires> getAllStagiaires() {
        List<Stagiaires> allStagiairess = stagiairesRepository.findAll();
        return allStagiairess.stream()
                .filter(Stagiaires -> !Stagiaires.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Stagiaires> getStagiairesById(Long id) {
        Optional<Stagiaires> StagiairesOpt = stagiairesRepository.findById(id);
        if (StagiairesOpt.isPresent() && !StagiairesOpt.get().isDeleted()) {
            return StagiairesOpt;
        }
        return Optional.empty();
    }

    @Override
    public Stagiaires createStagiaires(Stagiaires stagiaires, Document document) {
        // Save the Stagiaires entity
        Stagiaires savedStagiaires = stagiairesRepository.save(stagiaires);

        // Set the Stagiaires reference for the Document entity
        document.setMatriculestagiaire(savedStagiaires);

        // Save the Document entity
        Document savedDocument = documentRepository.save(document);

        // Set the Document reference for the Stagiaires entity
        savedStagiaires.setDocument(savedDocument);

        // Update and save the Stagiaires entity with the associated Document
        return stagiairesRepository.save(savedStagiaires);
    }

    @Override
    public Stagiaires updateStagiaires(Long id, Stagiaires updatedStagiaires) {
        updatedStagiaires.setMatricule(id);
        Stagiaires stagiaires = stagiairesRepository.findById(updatedStagiaires.getMatricule()).orElse(null);
        if (stagiaires != null) {
            BeanUtils.copyProperties(updatedStagiaires, stagiaires);
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
}
