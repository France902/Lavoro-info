
package towerdefense;

public class Torre {
    private int larghezza = 6, altezza = 5;
    private int posX, posY;
    private int vita = 50;
    
    Torre(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
    
    public int getX() {
        return posX;
    }
    
    public int getY() {
        return posY;
    }
    
    public int getLarghezza() {
        return larghezza;
    }
    
    public int getAltezza() {
        return altezza;
    }
    
    public String rimuoviVita(int attacco) {
        this.vita -= attacco;
        if(this.vita <= 0) return "sconfitta";
        else return "vittoria";
    }
}
