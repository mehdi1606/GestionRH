package com.innovx.gestionrh.Service;

import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Repository.CollaborateursRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public
class CollaborateursServiceImpl implements CollaborateursService{
    @Autowired
    private CollaborateursRepository collaborateursRepository;
    private EntityManager entityManager;
    @Override
    public List<Collaborateurs> getAllCollaborateurs() {
        List<Collaborateurs> allCollaborateurss = collaborateursRepository.findAll();
        return allCollaborateurss.stream()
                .filter(collaborateurs -> !collaborateurs.isDeleted())
                .map(this::formatCollaborateurDates)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Collaborateurs> getCollaborateursById(Long id) {
        Optional<Collaborateurs> collaborateursOpt = collaborateursRepository.findById(id);
        return collaborateursOpt.map(this::formatCollaborateurDates);
    }
    @Override
    public List<String> getAllCollaborateursDateNaissance() {
        return collaborateursRepository.findAll().stream()
                .filter(collaborateurs -> !collaborateurs.isDeleted())
                .map(Collaborateurs::getDate_naissance) // Assuming date_naissance is a String
                .map(this::formatCollaborateurDates)
                .collect(Collectors.toList());
    }

    @Override
    public Collaborateurs createCollaborateurs(Collaborateurs Collaborateurs) {
        Collaborateurs savedCollaborateurs = collaborateursRepository.save(Collaborateurs);
        return savedCollaborateurs;
    }

    @Override
    public Collaborateurs updateCollaborateurs(Long id, Collaborateurs updatedCollaborateurs) {
        updatedCollaborateurs.setMatricule(id);
        Collaborateurs Collaborateurs = collaborateursRepository.findById(updatedCollaborateurs.getMatricule()).orElse(null);
        if (Collaborateurs != null) {
            BeanUtils.copyProperties(updatedCollaborateurs, Collaborateurs);
            return collaborateursRepository.save(Collaborateurs);

        }

        return updatedCollaborateurs;
    }

    @Override
    public String deleteCollaborateurs(Long id) {
        Collaborateurs Collaborateurs = collaborateursRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Collaborateurs not found with ID: " + id));
        collaborateursRepository.delete(Collaborateurs);
        return "Collaborateurs deleted successfully.";

    }
    @Override
    public List<String> getAllBirthdays() {
        return collaborateursRepository.findAll().stream()
                .map(Collaborateurs::getDate_naissance)
                .collect(Collectors.toList());
    }

    private String formatCollaborateurDates(String dateNaissance) {
        return formatDate(dateNaissance);
    }
    // Helper method to format date fields
    private Collaborateurs formatCollaborateurDates(Collaborateurs collaborateurs) {
        // Format date_entree
        String formattedDateEntree = formatDate(collaborateurs.getDate_entree());
        collaborateurs.setDate_entree(formattedDateEntree);

        // Format date_naissance
        String formattedDateNaissance = formatDate(collaborateurs.getDate_naissance());
        collaborateurs.setDate_naissance(formattedDateNaissance);

        return collaborateurs;
    }

    // Helper method to format a date string
    private String formatDate(String dateStr) {
        try {
            // Try parsing as LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            try {
                // If parsing as LocalDateTime fails, try parsing as LocalDate
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e2) {
                // If parsing as LocalDate fails, try parsing with French month names
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.FRENCH));
                return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        }
    }

}
