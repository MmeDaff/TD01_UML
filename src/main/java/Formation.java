import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente une formation proposée par l'établissement.
 * Relation : Formation "1" -- "1..*" DossierInscription (concerne)
 */
public class Formation {

    private final String codeFormation;
    private String libelle;
    private String niveau;
    private Double fraisInscription;
    private int placesDisponibles;

    /** Une formation peut concerner plusieurs dossiers d'inscription (1..*). */
    private final List<DossierInscription> dossiers = new ArrayList<>();

    public Formation(String codeFormation, String libelle, String niveau,
                     Double fraisInscription, int placesDisponibles) {
        this.codeFormation = codeFormation;
        this.libelle = libelle;
        this.niveau = niveau;
        this.fraisInscription = fraisInscription;
        this.placesDisponibles = placesDisponibles;
    }

    // ===== Méthodes métier =====

    public String consulterDetails() {
        return String.format("%s (%s) - Niveau %s - %.2f XOF",
                libelle, codeFormation, niveau, fraisInscription);
    }

    public boolean verifierDisponibilite() {
        return placesDisponibles > 0;
    }

    /** Appelée par DossierInscription lors de l'association à cette formation. */
    void ajouterDossier(DossierInscription dossier) {
        if (!dossiers.contains(dossier)) {
            dossiers.add(dossier);
        }
    }

    // ===== Getters / Setters =====

    public String getCodeFormation() { return codeFormation; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    public Double getFraisInscription() { return fraisInscription; }
    public void setFraisInscription(Double fraisInscription) { this.fraisInscription = fraisInscription; }

    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) { this.placesDisponibles = placesDisponibles; }

    public List<DossierInscription> getDossiers() { return dossiers; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Formation)) return false;
        Formation formation = (Formation) o;
        return Objects.equals(codeFormation, formation.codeFormation);
    }

    @Override
    public int hashCode() { return Objects.hash(codeFormation); }

    @Override
    public String toString() {
        return "Formation{" + libelle + " (" + codeFormation + ")}";
    }
}