import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Représente un étudiant pouvant créer un ou plusieurs dossiers d'inscription.
 * Relation : Etudiant "1" -- "0..*" DossierInscription (possède)
 */
public class Etudiant {

    private final String idEtudiant;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String email;
    private String telephone;
    private String motDePasse;

    /** Un étudiant peut posséder plusieurs dossiers d'inscription (0..*). */
    private final List<DossierInscription> dossiers = new ArrayList<>();

    public Etudiant(String nom, String prenom, LocalDate dateNaissance, String email,
                    String telephone, String motDePasse) {
        this.idEtudiant = UUID.randomUUID().toString();
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
    }

    // ===== Méthodes métier =====

    public boolean seConnecter(String email, String motDePasse) {
        return this.email.equals(email) && this.motDePasse.equals(motDePasse);
    }

    public DossierInscription creerDossier(String typeInscription) {
        DossierInscription dossier = new DossierInscription(typeInscription, this);
        this.dossiers.add(dossier);
        return dossier;
    }

    public String consulterStatut(String idDossier) {
        return dossiers.stream()
                .filter(d -> d.getIdDossier().equals(idDossier))
                .findFirst()
                .map(d -> d.getStatut().name())
                .orElse("DOSSIER_INTROUVABLE");
    }

    public boolean annulerInscription(String idDossier) {
        return dossiers.stream()
                .filter(d -> d.getIdDossier().equals(idDossier))
                .findFirst()
                .map(DossierInscription::annuler)
                .orElse(false);
    }

    // ===== Getters / Setters =====

    public String getIdEtudiant() { return idEtudiant; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public List<DossierInscription> getDossiers() { return dossiers; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Etudiant)) return false;
        Etudiant etudiant = (Etudiant) o;
        return Objects.equals(idEtudiant, etudiant.idEtudiant);
    }

    @Override
    public int hashCode() { return Objects.hash(idEtudiant); }

    @Override
    public String toString() {
        return "Etudiant{" + prenom + " " + nom + ", email=" + email + "}";
    }
}