package com.innovx.gestionrh.Service;

import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Repository.CollaborateursRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CollaborateursServiceImpl implements CollaborateursService{
    @Autowired
    private CollaborateursRepository collaborateursRepository;
    @Override
    public List<Collaborateurs> getAllCollaborateurs() {
        List<Collaborateurs> allCollaborateurss = collaborateursRepository.findAll();
        return allCollaborateurss.stream()
                .filter(Collaborateurs -> !Collaborateurs.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Collaborateurs> getCollaborateursById(Long id) {
        Optional<Collaborateurs> CollaborateursOpt = collaborateursRepository.findById(id);
        if (CollaborateursOpt.isPresent() && !CollaborateursOpt.get().isDeleted()) {
            return CollaborateursOpt;
        }
        return Optional.empty();
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
}
