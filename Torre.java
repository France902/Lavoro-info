package towerdefense;

public class Torre {

    // Dimensioni fisse della torre sulla griglia di gioco.
    private int larghezza = 6, altezza = 5;

    // Coordinate della torre nella mappa.
    private int posX, posY;

    // Punti vita della torre.
    private int vita = 50;

    /**
     * Costruttore della torre.
     */
    Torre(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Restituisce la coordinata X della torre.
     */
    public int getX() {
        return posX;
    }

    /**
     * Restituisce la coordinata Y della torre.
     */
    public int getY() {
        return posY;
    }

    /**
     * Restituisce la larghezza totale (in caratteri/celle) della torre.
     */
    public int getLarghezza() {
        return larghezza;
    }

    /**
     * Restituisce l’altezza totale della torre.
     */
    public int getAltezza() {
        return altezza;
    }

    /**
     * Restituisce i punti vita attuali.
     */
    public int getVita() {
        return vita;
    }

    /**
     * Imposta i punti vita della torre.
     * @param vita nuovo valore dei punti vita
     */
    public void setVita(int vita) {
        this.vita = vita;
    }

    /**
     * Riduce i punti vita della torre in base all’attacco subito.
     * Restituisce un messaggio sullo stato della torre dopo il danno.
     *
     * @return "sconfitta" se la torre viene distrutta, altrimenti "vittoria"
     */
    public String rimuoviVita(int attacco) {
        this.vita -= attacco;

        // Se la vita scende a zero o sotto, la torre è distrutta.
        if (this.vita <= 0) {
            this.vita = 0;  // Evita valori negativi.
            return "sconfitta";
        }

        // Torre ancora in piedi.
        return "vittoria";
    }
}
