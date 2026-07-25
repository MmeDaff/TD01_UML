import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Représente un agent de la scolarité chargé de valider ou rejeter
 * les dossiers d'inscription.
 * Relation : AgentScolarite "1" -- "0..*" DossierInscription (valide / vérifie)
 */
public class AgentScolarite {

    private final String idAgent;
    private String nom;
    private String prenom;
    private String email;

    /** Dossiers déjà traités (validés ou rejetés) par cet agent. */
    private final List<DossierInscription> dossiersTraites = new ArrayList<>();

    public AgentScolarite(String nom, String prenom, String email) {
        this.idAgent = UUID.randomUUID().toString();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // ===== Méthodes métier =====

    public void validerDossier(DossierInscription dossier) {
        dossier.valider(this);
        if (!dossiersTraites.contains(dossier)) {
            dossiersTraites.add(dossier);
        }
    }

    public void rejeterDossier(DossierInscription dossier, String motif) {
        dossier.rejeter(this, motif);
        if (!dossiersTraites.contains(dossier)) {
            dossiersTraites.add(dossier);
        }
    }

    /**
     * Filtre, parmi une liste de dossiers fournie par le système,
     * ceux qui sont en attente de validation.
     */
    public List<DossierInscription> consulterDossiersEnAttente(List<DossierInscription> tousLesDossiers) {
        return tousLesDossiers.stream()
                .filter(d -> d.getStatut() == StatutDossier.EN_ATTENTE_VALIDATION)
                .collect(Collectors.toList());
    }

    // ===== Getters / Setters =====

    public String getIdAgent() { return idAgent; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<DossierInscription> getDossiersTraites() { return dossiersTraites; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgentScolarite)) return false;
        AgentScolarite that = (AgentScolarite) o;
        return Objects.equals(idAgent, that.idAgent);
    }

    @Override
    public int hashCode() { return Objects.hash(idAgent); }

    @Override
    public String toString() {
        return "AgentScolarite{" + prenom + " " + nom + "}";
    }
}
