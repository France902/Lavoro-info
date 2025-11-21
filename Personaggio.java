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
    
    public String getSimbolo() {
        return simbolo;
    }
    
    public int getPos() {
        return posX;
    }
    
    public int getMana() {
        return manaCost;
    }
    
    public boolean condMovimento(Torre T) {
        int larghezzaSimbolo = simbolo.length();
        if((posX + larghezzaSimbolo - 5) >= T.getX() && posX <= (T.getX() + T.getLarghezza()-1)) {
            simbolo = "";
            return true;
        }
        return false;
    }
}