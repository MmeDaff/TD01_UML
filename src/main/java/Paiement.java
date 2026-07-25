
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Représente une transaction de paiement liée à un dossier d'inscription.
 * Relation : DossierInscription "1" -- "1" Paiement (effectue)
 */
public class Paiement {

    private final String idPaiement;
    private Double montant;
    private String referenceTransaction;
    private String modePaiement;
    private LocalDateTime datePaiement;
    private StatutPaiement statut;

    /** Référence inverse vers le dossier concerné (navigation bidirectionnelle). */
    private DossierInscription dossier;

    public Paiement(Double montant, String modePaiement) {
        this.idPaiement = UUID.randomUUID().toString();
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.statut = StatutPaiement.EN_ATTENTE;
    }

    // ===== Méthodes métier =====

    /**
     * Simule l'appel au système de paiement externe.
     * En cas de succès : statut = REUSSI et une référence de transaction est générée.
     * En cas d'échec : statut = ECHOUE.
     */
    public boolean effectuerTransaction() {
        boolean succes = simulerAppelSystemePaiement();
        if (succes) {
            this.referenceTransaction = "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.statut = StatutPaiement.REUSSI;
        } else {
            this.statut = StatutPaiement.ECHOUE;
        }
        this.datePaiement = LocalDateTime.now();
        return succes;
    }

    private boolean simulerAppelSystemePaiement() {
        // Point d'intégration réel avec le Système de Paiement (service comptable)
        return true;
    }

    public String genererRecu() {
        if (statut != StatutPaiement.REUSSI) {
            return "Aucun reçu disponible : paiement non abouti.";
        }
        return String.format("Reçu %s - Montant: %.2f - Mode: %s - Date: %s",
                referenceTransaction, montant, modePaiement, datePaiement);
    }

    // ===== Getters / Setters =====

    public String getIdPaiement() { return idPaiement; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public String getReferenceTransaction() { return referenceTransaction; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public LocalDateTime getDatePaiement() { return datePaiement; }

    public StatutPaiement getStatut() { return statut; }
    public void setStatut(StatutPaiement statut) { this.statut = statut; }

    public DossierInscription getDossier() { return dossier; }
    void setDossier(DossierInscription dossier) { this.dossier = dossier; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paiement)) return false;
        Paiement paiement = (Paiement) o;
        return Objects.equals(idPaiement, paiement.idPaiement);
    }

    @Override
    public int hashCode() { return Objects.hash(idPaiement); }

    @Override
    public String toString() {
        return "Paiement{" + montant + " via " + modePaiement + ", statut=" + statut + "}";
    }
}