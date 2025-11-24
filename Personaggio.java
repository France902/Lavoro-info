package towerdefense;

public class Personaggio {
    
    // Posizione X del personaggio sulla linea di combattimento
    private int posX;

    // Simbolo ASCII che rappresenta graficamente il personaggio
    private String simbolo, nome;

    // Costo in mana per evocare questo personaggio
    private int manaCost;

    // Forza d'attacco base del personaggio
    private int attacco;

    // Quantità di vita attuale del personaggio
    private int vita;
    
    private int vitaMassima;

    // Velocità di movimento per turno
    private int velocita;
    
    
    // Costruttore che inizializza tutte le statistiche del personaggio
    Personaggio(int posX, String simbolo, int manaCost, int attacco, int vita, int velocita, String nome) {
        this.posX = posX;
        this.simbolo = simbolo;
        this.manaCost = manaCost;
        this.attacco = attacco;
        this.vita = vita;
        this.vitaMassima = vita;
        this.velocita = velocita;
        this.nome = nome;
    }
    
    // Movimento in avanti del personaggio di un certo numero di passi
    public void vaiAvanti(int passo) {
        posX += passo;
    }

    // Movimento indietro del personaggio
    public void vaiIndietro(int passo) {
        posX -= passo;
    }
    
    // Ritorna il simbolo ASCII del personaggio
    public String getSimbolo() {
        return simbolo;
    }
    
    // Imposta un nuovo simbolo ASCII
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    // Restituisce la posizione X attuale
    public int getPos() {
        return posX;
    }
    
    // Restituisce il costo in mana del personaggio
    public int getMana() {
        return manaCost;
    }
    
    // Restituisce la vita attuale
    public int getVita() {
        return vita;
    }
    
    //Restituisce la vita senza modifiche del danno degli avversari
    public int getVitaMassima() {
        return vitaMassima;
    }
    
    // Modifica la vita del personaggio
    public void setVita(int vita) {
        this.vita = vita;
    }
    
    // Ritorna la forza d'attacco
    public int getAttacco() {
        return attacco;
    }
    
    // Ritorna la velocità del personaggio
    public int getVelocita() {
        return velocita;
    }
    
    // Controlla se il personaggio sta toccando o superando una torre
    public boolean condMovimento(Torre T) {
        // Larghezza del simbolo, utile per il calcolo della collisione
        int larghezzaSimbolo = simbolo.length();

        // Verifica se il simbolo entra nella hitbox della torre
        if((posX + larghezzaSimbolo - 5) >= T.getX() && posX <= (T.getX() + T.getLarghezza()-1)) {
            simbolo = "";
            return true;
        }
        return false;
    }
    
    // Gestisce un combattimento corpo a corpo fra questo personaggio e uno nemico
    public String fight(Personaggio Nemico) {

        // Stat nemiche temporanee per il confronto
        int vitaNemico = Nemico.getVita();
        int attaccoNemico = Nemico.getAttacco();
        int i;

        // Scambio colpi finché uno dei due non va sotto zero
        while(this.vita >= 0 && vitaNemico >= 0) {
            vitaNemico -= this.attacco;
            this.vita -= attaccoNemico;
        }

        // Aggiorna la vita residua del nemico dopo lo scontro
        Nemico.setVita(vitaNemico);

        // Determina l’esito del duello
        if(this.vita <= 0 && vitaNemico <= 0) return "pareggio";
        else if(this.vita <= 0) return "sconfitta";
        else return "vittoria";
    }
}
