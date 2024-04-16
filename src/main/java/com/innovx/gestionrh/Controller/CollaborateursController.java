package com.innovx.gestionrh.Controller;

import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Repository.CollaborateursRepository;
import com.innovx.gestionrh.Service.CollaborateursService;
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
@RequestMapping("/api/v1")
public class CollaborateursController {
    @Autowired
    private CollaborateursService collaborateurservice;


    @Autowired
    private CollaborateursRepository collaborateursRepository;
    @GetMapping("/Collaborateurs")
    public List<Collaborateurs> allStagiares() throws IOException, GeneralSecurityException {
        return collaborateurservice.getAllCollaborateurs();
    }

    @GetMapping("/Collaborateurs/{id}")
    public Optional<Collaborateurs> getStagiaireById(@PathVariable Long id) {

        return collaborateurservice.getCollaborateursById(id);
    }

    @PostMapping("/Collaborateurs")
    public Collaborateurs createStagiaire(@RequestBody Collaborateurs stagiaire) {
        return collaborateurservice.createCollaborateurs(stagiaire);
    }

    @PutMapping("/Collaborateurs/{id}")
    public Collaborateurs updateStagiaire(@PathVariable Long id, @RequestBody Collaborateurs updatedStagiaire) {
        return collaborateurservice.updateCollaborateurs(id, updatedStagiaire);
    }

    @DeleteMapping("/Collaborateurs/{id}")
    public ResponseEntity<Object> deleteStagiaire(@PathVariable Long id) {
        Optional<Collaborateurs> existingStagiaireOptional = collaborateurservice.getCollaborateursById(id);
        if (!existingStagiaireOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find stagiaire");
        }

        Collaborateurs existingCollaborateurs = existingStagiaireOptional.get();
        existingCollaborateurs.setDeleted(true);
        collaborateursRepository.save(existingCollaborateurs);

        return ResponseEntity.ok().build();
    }

}
