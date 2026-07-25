import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Représente une notification par email envoyée à un étudiant suite
 * à un changement de statut de son dossier.
 * Relation : DossierInscription "1" -- "0..*" NotificationEmail (génère)
 */
public class NotificationEmail {

    private final String idNotification;
    private String destinataire;
    private String objet;
    private String contenu;
    private LocalDateTime dateEnvoi;

    /** Plusieurs notifications peuvent être générées par un même dossier. */
    private final DossierInscription dossier;

    public NotificationEmail(String destinataire, String objet, String contenu, DossierInscription dossier) {
        this.idNotification = UUID.randomUUID().toString();
        this.destinataire = destinataire;
        this.objet = objet;
        this.contenu = contenu;
        this.dossier = dossier;
    }

    // ===== Méthodes métier =====

    public boolean envoyer() {
        // Point d'intégration réel avec un service d'envoi d'emails (SMTP, API tierce...)
        this.dateEnvoi = LocalDateTime.now();
        return true;
    }

    // ===== Getters / Setters =====

    public String getIdNotification() { return idNotification; }

    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }

    public String getObjet() { return objet; }
    public void setObjet(String objet) { this.objet = objet; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }

    public DossierInscription getDossier() { return dossier; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationEmail)) return false;
        NotificationEmail that = (NotificationEmail) o;
        return Objects.equals(idNotification, that.idNotification);
    }

    @Override
    public int hashCode() { return Objects.hash(idNotification); }

    @Override
    public String toString() {
        return "NotificationEmail{" + objet + " -> " + destinataire + "}";
    }
}