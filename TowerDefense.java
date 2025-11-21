package towerdefense;

import java.util.Vector;

public class TowerDefense {

    public static int Altezza = 7;
    public static int Larghezza = 80;
    
    static String[][] logPersonaggi = {
        {/*simbolo*/ "o\n/|>\n/ \\", /*costo mana*/ "4", /*attacco*/ "6", /*vita*/ "7", /*velocità*/ "3", /*classe*/ "Cavaliere"},

        {/*simbolo*/ "O\n/|}_\n/ \\", /*costo mana*/ "3", /*attacco*/ "4", /*vita*/ "8", /*velocità*/ "4", /*classe*/ "Barbaro"},

        {/*simbolo*/ "^\n/|\\\n/ \\", /*costo mana*/ "3", /*attacco*/ "3", /*vita*/ "5", /*velocità*/ "5", /*classe*/ "Ninja"},

        {/*simbolo*/ "o\n/|)->\n/ \\", /*costo mana*/ "2", /*attacco*/ "4", /*vita*/ "4", /*velocità*/ "3", /*classe*/ "Arciere"},

        {/*simbolo*/ "o\n<|>\n/ \\", /*costo mana*/ "4", /*attacco*/ "4", /*vita*/ "5", /*velocità*/ "4", /*classe*/ "Assassino"},

        {/*simbolo*/ "@\n/|\\\n/ \\>", /*costo mana*/ "10", /*attacco*/ "13", /*vita*/ "9", /*velocità*/ "13", /*classe*/ "Mago"}, 
    };
    
    static String[][] personaggiSchierati = { 
        {/*simbolo*/ "o[/|>[/\\", /*costo mana*/ "4", /*attacco*/ "6", /*vita*/ "7", /*velocità*/ "3", /*classe*/ "cavaliere"}, 
        {/*simbolo*/ "o[/|>[/\\", /*costo mana*/ "4", /*attacco*/ "6", /*vita*/ "7", /*velocità*/ "3", /*classe*/ "cavaliere"}, 
    };
    
    
    public static void main(String[] args) {
        char[][] Mappa = new char[Altezza][Larghezza];
        Vector<Torre> T = new Vector<>();
        Vector<Personaggio> P = new Vector<>();
        
        int i;
        
        for(i=0;i<personaggiSchierati.length;i++) {
            P.add(new Personaggio((9+(4*i)), personaggiSchierati[i][0], Integer.valueOf(personaggiSchierati[i][1]), Integer.valueOf(personaggiSchierati[i][2]), Integer.valueOf(personaggiSchierati[i][3]), Integer.valueOf(personaggiSchierati[i][4]), personaggiSchierati[i][5]));
        }
        
        T.add(new Torre(3, 2));
        T.add(new Torre(70, 2));
        while(true) {
            attivaRound(P, Mappa, T);
        }
    }
    
    public static void attivaRound(Vector<Personaggio> P, char[][] Mappa, Vector<Torre> T) {
        long ritardoMillisecondi = 500;
        int i, j;
        for(i=0;i<P.size();i++) {
            P.get(i).vaiAvanti(3);
            for(j=0;j<T.size();j++) {
                if(P.get(i).condMovimento(T.get(j))) {
                    P.remove(i);
                }
             }
        }
        stampaAreaDiGioco(Mappa, P, T);
        try {
            Thread.sleep(ritardoMillisecondi);
            System.out.flush(); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public static void stampaAreaDiGioco(char[][] M, Vector<Personaggio> P, Vector<Torre> T) {
        int i, j, posizionePartenza;
        int characterPosY = 4, indexSimbolo = -1;
        
        String simbolo;
        clearScreen();
        stampaMenu();
        for(i=0;i<Altezza;i++) {
            for(j=0;j<Larghezza;j++) {
                if(condTorre(i, j, T) == "verticale") M[i][j] = '|';
                else if(condTorre(i, j, T) == "orizzontale") M[i][j] = '_';
                else M[i][j] = ' ';
            }
        }
        for(i=0;i<P.size();i++) {
            characterPosY = 4;
            indexSimbolo = -1;
            simbolo = P.get(i).getSimbolo();
            posizionePartenza = P.get(i).getPos();
            for(j=0;j<simbolo.length();j++) {
                indexSimbolo++;
                if(indexSimbolo == simbolo.length()) break;
                if(simbolo.charAt(indexSimbolo) == '[') {
                    characterPosY++;
                    indexSimbolo++;
                    j=0;
                    M[characterPosY][posizionePartenza] = simbolo.charAt(indexSimbolo);
                }
                else {
                    if(characterPosY == 4) {
                        j++;
                        M[characterPosY][(posizionePartenza + j)] = simbolo.charAt(indexSimbolo);
                        j--;
                    }
                    else {
                        M[characterPosY][(posizionePartenza + j)] = simbolo.charAt(indexSimbolo);
                    }
                    
                }
            }
        }
        for(i=0;i<Altezza;i++) {
            for(j=0;j<Larghezza;j++) {
                System.out.print(M[i][j]);
            }
            System.out.print("\n");
        }
    }
    
    public static String condTorre(int y, int x, Vector<Torre> T) {
        int i, posX, posY;
        if(y > 1) {
            for(i=0;i<T.size();i++) {
               posX = T.get(i).getX();
               posY = T.get(i).getY();
               if(x >= posX && x <= (posX + T.get(i).getLarghezza() - 1)) {
                   if(x >= (posX + 1) && x <= (posX + T.get(i).getLarghezza() - 2)) {
                       if(y == posY) return "orizzontale";
                       else if(y == (posY + T.get(i).getAltezza() - 1)) return "orizzontale";
                   }
                   else if(y != posY) return "verticale";
               } 
            }  
        }
        return "";
    }
    
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) { 
            System.out.println();
        }
    }
    
    public static void stampaMenu() {
        int i;
        for(i=0;i<(logPersonaggi.length-1);i++) {
            System.out.println(logPersonaggi[i][5] + "\nCosto: " + logPersonaggi[i][1] + "\nVita: " + logPersonaggi[i][3] + "  Attacco: " + logPersonaggi[i][2] + "  Velocita: " + logPersonaggi[i][4]);
            System.out.print(" ");
            System.out.println(logPersonaggi[i][0] + "\n");
        }
    }
    
}