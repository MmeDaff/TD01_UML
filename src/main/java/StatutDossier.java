
/**
 * Représente les différents états possibles d'un dossier d'inscription
 * tout au long du processus (création, paiement, validation).
 */
public enum StatutDossier {
    EN_COURS,
    EN_ATTENTE_VALIDATION,
    VALIDE,
    REJETE,
    ANNULE
}