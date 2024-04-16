package com.innovx.gestionrh.Repository;

import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Entity.Stagiaires;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaborateursRepository extends JpaRepository<Collaborateurs, Long> {
    List<Collaborateurs> findAllByOrderByMatriculeAsc();
}
