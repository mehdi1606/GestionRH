package com.innovx.gestionrh.Service;

import com.innovx.gestionrh.Entity.Collaborateurs;
import com.innovx.gestionrh.Entity.Stagiaires;
import com.innovx.gestionrh.Entity.User;
import com.innovx.gestionrh.Entity.UserRole;
import com.innovx.gestionrh.Repository.CollaborateursRepository;
import com.innovx.gestionrh.Repository.StagiairesRepository;
import com.innovx.gestionrh.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private CollaborateursRepository collaborateurRepository;

    @Autowired
    private StagiairesRepository stagiaireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 60000) // Check every minute
    @Scheduled(cron = "0 0 8 * * ?") // This will run every day at 8 AM
    public void sendDailyNotifications() {
        logger.info("Starting daily notifications");

        List<Collaborateurs> collaborateurs = collaborateurRepository.findAll();
        List<Stagiaires> stagiaires = stagiaireRepository.findAll();

        LocalDate today = LocalDate.now();

        // Define supported date formats
        List<DateTimeFormatter> dateFormats = new ArrayList<>();
        dateFormats.add(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        dateFormats.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateFormats.add(DateTimeFormatter.ofPattern("dd-MMM-yyyy")); // for '30-juil.-2002'

        // Fetch users with specific roles
        List<User> adminUsers = userRepository.findByUserRole(UserRole.ADMIN);
        List<User> collaborateurRhUsers = userRepository.findByUserRole(UserRole.COLLABORATEUR_RH);
        List<User> stagiaireRhUsers = userRepository.findByUserRole(UserRole.STAGIAIRE_RH);

        // Combine roles for sending birthday notifications
        List<User> birthdayNotificationUsers = new ArrayList<>(adminUsers);
        birthdayNotificationUsers.addAll(collaborateurRhUsers);

        // Combine roles for sending meeting notifications
        List<User> meetingNotificationUsers = new ArrayList<>(adminUsers);
        meetingNotificationUsers.addAll(stagiaireRhUsers);

        // Check for birthdays and send emails
        collaborateurs.forEach(collaborateur -> {
            LocalDate birthDate = parseDate(collaborateur.getDate_naissance(), dateFormats);
            if (birthDate != null && birthDate.getDayOfMonth() == today.getDayOfMonth() && birthDate.getMonth() == today.getMonth()) {
                String message = getBirthdayMessage(collaborateur);
                birthdayNotificationUsers.forEach(user -> {
                    emailService.sendEmail(user.getEmail(), "Joyeux Anniversaire", message);
                    logger.info("Birthday email sent to: {}", user.getEmail());
                });
            } else if (birthDate == null) {
                logger.error("Invalid date format for collaborateur: {}", collaborateur.getDate_naissance());
            }
        });

        // Check for meetings and send emails
        stagiaires.forEach(stagiaire -> {
            if (checkMeetingDate(String.valueOf(stagiaire.getAccueilRhDate()), dateFormats, today) ||
                    checkMeetingDate(String.valueOf(stagiaire.getPointStagiaires7DaysDate()), dateFormats, today) ||
                    checkMeetingDate(String.valueOf(stagiaire.getPointStagiaires1MonthDate()), dateFormats, today) ||
                    checkMeetingDate(String.valueOf(stagiaire.getPointStagiaires3MonthsDate()), dateFormats, today)) {
                String message = getMeetingMessage(stagiaire);
                meetingNotificationUsers.forEach(user -> {
                    emailService.sendEmail(user.getEmail(), "Rappel de Réunion", message);
                    logger.info("Meeting email sent to: {}", user.getEmail());
                });
            }
        });

        logger.info("Daily notifications completed");
    }

    private LocalDate parseDate(String dateStr, List<DateTimeFormatter> dateFormats) {
        for (DateTimeFormatter formatter : dateFormats) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // Continue to the next format
            }
        }
        return null; // Return null if no format matches
    }

    private boolean checkMeetingDate(String meetingDateStr, List<DateTimeFormatter> dateFormats, LocalDate today) {
        LocalDate meetingDate = parseDate(meetingDateStr, dateFormats);
        return meetingDate != null && meetingDate.equals(today);
    }

    private String getBirthdayMessage(Collaborateurs collaborateur) {
        return "Anniversaire de " + collaborateur.getNom() + " " + collaborateur.getPrenom() + ",\n\n" +
                "À l'occasion de votre anniversaire, toute l'équipe se joint à moi pour vous souhaiter une journée pleine de joie et de succès. " +
                "Que cette nouvelle année vous apporte bonheur, santé et réussite dans tous vos projets.\n\n" +
                "Joyeux anniversaire !\n\n" +
                "Cordialement,";
    }

    private String getMeetingMessage(Stagiaires stagiaire) {
        return "Meet aujourd'hui " + stagiaire.getNom() + " " + stagiaire.getPrenom() + ",\n\n" +
                "N'oubliez pas votre réunion prévue aujourd'hui.\n\n" +
                "Cordialement,";
    }
}
