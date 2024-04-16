package com.innovx.gestionrh.Repository;

import com.innovx.gestionrh.Entity.Stagiaires;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StagiairesRepository extends JpaRepository<Stagiaires, Long> {

    List<Stagiaires> findAllByOrderByMatriculeAsc();

}
