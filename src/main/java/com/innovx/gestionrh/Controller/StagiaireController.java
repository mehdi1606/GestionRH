package com.innovx.gestionrh.Controller;

import com.innovx.gestionrh.Entity.Document;
import com.innovx.gestionrh.Entity.Stagiaires;
import com.innovx.gestionrh.Repository.StagiairesRepository;
import com.innovx.gestionrh.Service.StagiareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class StagiaireController {
    @Autowired
    private StagiareService stagiaireService;
    @Autowired
    private StagiairesRepository stagiairesRepository;
    @GetMapping("/stagiares")
    public List<Stagiaires> allStagiares() throws IOException, GeneralSecurityException {
        return stagiaireService.getAllStagiaires();
    }

    @GetMapping("/stagiares/{id}")
    public Optional<Stagiaires> getStagiaireById(@PathVariable Long id) {

        return stagiaireService.getStagiairesById(id);
    }

    @PostMapping("/stagiares")
    public Stagiaires createStagiaire(@RequestBody Stagiaires stagiaire,@RequestBody Document document) {
        return stagiaireService.createStagiaires(stagiaire,document);
    }

    @PutMapping("/stagiares/{id}")
    public Stagiaires updateStagiaire(@PathVariable Long id, @RequestBody Stagiaires updatedStagiaire) {
        return stagiaireService.updateStagiaires(id, updatedStagiaire);
    }

    @DeleteMapping("/stagiares/{id}")
    public ResponseEntity<Object> deleteStagiaire(@PathVariable Long id) {
        Optional<Stagiaires> existingStagiaireOptional = stagiaireService.getStagiairesById(id);
        if (!existingStagiaireOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find stagiaire");
        }

        Stagiaires existingStagiaire = existingStagiaireOptional.get();
        existingStagiaire.setDeleted(true);
        stagiairesRepository.save(existingStagiaire);

        return ResponseEntity.ok().build();
    }

}
