package towerdefense;

public class Personaggio {
    
    private int posX;
    private String simbolo, nome;
    private int manaCost;
    private int attacco;
    private int vita;
    private int velocita;
    
    
    Personaggio(int posX, String simbolo, int manaCost, int attacco, int vita, int velocita, String nome) {
        this.posX = posX;
        this.simbolo = simbolo;
        this.manaCost = manaCost;
        this.attacco = attacco;
        this.vita = vita;
        this.velocita = velocita;
        this.nome = nome;
    }
    
    public void vaiAvanti(int passo) {
        posX += passo;
    }
    public void vaiIndietro(int passo) {
        posX -= passo;
    }
    
    public String getSimbolo() {
        return simbolo;
    }
    
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    public int getPos() {
        return posX;
    }
    
    public int getMana() {
        return manaCost;
    }
    
    public int getVita() {
        return vita;
    }
    
    public void setVita(int vita) {
        this.vita = vita;
    }
    
    public int getAttacco() {
        return attacco;
    }
    
    public boolean condMovimento(Torre T) {
        int larghezzaSimbolo = simbolo.length();
        if((posX + larghezzaSimbolo - 5) >= T.getX() && posX <= (T.getX() + T.getLarghezza()-1)) {
            simbolo = "";
            return true;
        }
        return false;
    }
    
    public String fight(Personaggio Nemico) {
        int vitaNemico = Nemico.getVita();
        int attaccoNemico = Nemico.getAttacco();
        int i;
        while(this.vita >= 0 && vitaNemico >= 0) {
            vitaNemico -= this.attacco;
            this.vita -= attaccoNemico;
        }
        Nemico.setVita(vitaNemico);
        if(this.vita <= 0 && vitaNemico <= 0) return "pareggio";
        else if(this.vita <= 0) return "sconfitta";
        else return "vittoria";
    }
}
