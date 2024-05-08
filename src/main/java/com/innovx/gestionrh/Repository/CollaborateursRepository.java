package com.innovx.gestionrh.Repository;

import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Entity.Stagiaires;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface CollaborateursRepository extends JpaRepository<Collaborateurs, Long> {
    List<Collaborateurs> findAllByOrderByMatriculeAsc();

}

