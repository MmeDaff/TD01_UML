import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Classe centrale du modèle : représente le dossier d'inscription
 * d'un étudiant à une formation, incluant son suivi de paiement,
 * de validation et ses notifications.
 *
 * Relations portées par cette classe (références directes) :
 *  - Etudiant           (1 étudiant  -- 0..* dossiers)
 *  - AgentScolarite      (1 agent     -- 0..* dossiers traités)
 *  - Formation           (1 formation -- 1..* dossiers)
 *  - Paiement            (1 dossier   -- 1 paiement)
 *  - List<NotificationEmail> (1 dossier -- 0..* notifications)
 */
public class DossierInscription {

    private final String idDossier;
    private final LocalDate dateCreation;
    private StatutDossier statut;
    private final List<String> piecesJustificatives = new ArrayList<>();
    private String typeInscription;
    private String motifRejet;

    private final Etudiant etudiant;
    private AgentScolarite agentValidateur;
    private Formation formation;
    private Paiement paiement;
    private final List<NotificationEmail> notifications = new ArrayList<>();

    public DossierInscription(String typeInscription, Etudiant etudiant) {
        this.idDossier = UUID.randomUUID().toString();
        this.typeInscription = typeInscription;
        this.etudiant = etudiant;
        this.dateCreation = LocalDate.now();
        this.statut = StatutDossier.EN_COURS;
    }

    // ===== Méthodes métier =====

    public void ajouterPiece(String piece) {
        this.piecesJustificatives.add(piece);
    }

    public void associerFormation(Formation formation) {
        this.formation = formation;
        formation.ajouterDossier(this);
    }

    /**
     * Initie le paiement des frais d'inscription liés à la formation choisie.
     */
    public boolean initierPaiement(String modePaiement) {
        if (formation == null) {
            throw new IllegalStateException("Impossible de payer : aucune formation associée.");
        }
        this.paiement = new Paiement(formation.getFraisInscription(), modePaiement);
        this.paiement.setDossier(this);
        boolean succes = this.paiement.effectuerTransaction();
        if (succes) {
            mettreAJourStatut(StatutDossier.EN_ATTENTE_VALIDATION);
            genererNotification("Paiement reçu",
                    "Votre paiement a été reçu, votre dossier est en attente de validation par la scolarité.");
        } else {
            genererNotification("Échec du paiement",
                    "Votre paiement n'a pas abouti. Veuillez réessayer.");
        }
        return succes;
    }

    /** Appelée par AgentScolarite.validerDossier(...) */
    void valider(AgentScolarite agent) {
        this.agentValidateur = agent;
        mettreAJourStatut(StatutDossier.VALIDE);
        genererNotification("Inscription validée",
                "Félicitations, votre inscription a été validée par la scolarité.");
    }

    /** Appelée par AgentScolarite.rejeterDossier(...) */
    void rejeter(AgentScolarite agent, String motif) {
        this.agentValidateur = agent;
        this.motifRejet = motif;
        mettreAJourStatut(StatutDossier.REJETE);
        genererNotification("Dossier rejeté",
                "Votre dossier a été rejeté. Motif : " + motif);
    }

    boolean annuler() {
        if (statut == StatutDossier.VALIDE) {
            return false; // impossible d'annuler une inscription déjà validée
        }
        mettreAJourStatut(StatutDossier.ANNULE);
        genererNotification("Inscription annulée", "Votre inscription a été annulée à votre demande.");
        return true;
    }

    public String consulterStatut() {
        return statut.name();
    }

    public void mettreAJourStatut(StatutDossier statut) {
        this.statut = statut;
    }

    private void genererNotification(String objet, String contenu) {
        NotificationEmail notification = new NotificationEmail(
                etudiant.getEmail(), objet, contenu, this);
        this.notifications.add(notification);
        notification.envoyer();
    }

    // ===== Getters / Setters =====

    public String getIdDossier() { return idDossier; }

    public LocalDate getDateCreation() { return dateCreation; }

    public StatutDossier getStatut() { return statut; }

    public List<String> getPiecesJustificatives() { return piecesJustificatives; }

    public String getTypeInscription() { return typeInscription; }
    public void setTypeInscription(String typeInscription) { this.typeInscription = typeInscription; }

    public String getMotifRejet() { return motifRejet; }

    public Etudiant getEtudiant() { return etudiant; }

    public AgentScolarite getAgentValidateur() { return agentValidateur; }

    public Formation getFormation() { return formation; }

    public Paiement getPaiement() { return paiement; }

    public List<NotificationEmail> getNotifications() { return notifications; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DossierInscription)) return false;
        DossierInscription that = (DossierInscription) o;
        return Objects.equals(idDossier, that.idDossier);
    }

    @Override
    public int hashCode() { return Objects.hash(idDossier); }

    @Override
    public String toString() {
        return "DossierInscription{" + idDossier + ", statut=" + statut + "}";
    }
}