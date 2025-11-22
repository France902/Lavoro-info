package towerdefense;

import java.util.Scanner;
import java.util.Vector;

public class TowerDefense {

    public static int Altezza = 7;
    public static int Larghezza = 80;
    public static boolean vinto = false, vittoriaGiocatore;
    public static int contRound = 0;
    
    static String[][] logPersonaggi = {
        {/*simbolo formale*/ "o\n/|>\n/ \\", /*simbolo*/ "o[/|>[/ \\", /*costo mana*/ "4", /*attacco*/ "6", /*vita*/ "7", /*velocità*/ "3", /*classe*/ "cavaliere"},

        {/*simbolo formale*/ "O\n/|}_\n/ \\", /*simbolo*/ "O[/|}_[/ \\", /*costo mana*/ "3", /*attacco*/ "4", /*vita*/ "8", /*velocità*/ "4", /*classe*/ "barbaro"},

        {/*simbolo formale*/ "^\n/|\\\n/ \\", /*simbolo*/ "^[/|\\[/ \\", /*costo mana*/ "3", /*attacco*/ "3", /*vita*/ "5", /*velocità*/ "5", /*classe*/ "ninja"},

        {/*simbolo formale*/ "o\n/|)->\n/ \\", /*simbolo*/ "o[/|)->[/ \\", /*costo mana*/ "2", /*attacco*/ "4", /*vita*/ "4", /*velocità*/ "3", /*classe*/ "arciere"},

        {/*simbolo formale*/ "o\n<|>\n/ \\", /*simbolo*/ "o[<|>[/ \\", /*costo mana*/ "4", /*attacco*/ "4", /*vita*/ "5", /*velocità*/ "4", /*classe*/ "assassino"},

        {/*simbolo formale*/ "@\n/|\\\n/ \\>", /*simbolo*/ "@[/|\\[/ \\>", /*costo mana*/ "10", /*attacco*/ "13", /*vita*/ "9", /*velocità*/ "13", /*classe*/ "mago"}, 
    };


static String[][] logPersonaggiNemici = {
    {/*simbolo formale*/ "o\n<|/\n/ \\", /*simbolo*/ "o[<|/[/ \\", /*costo mana*/ "4", /*attacco*/ "6", /*vita*/ "7", /*velocità*/ "3", /*classe*/ "cavaliere"},

    {/*simbolo formale*/ "O\n/|}_\n/ \\", /*simbolo*/ "O[/|}_[/ \\", /*costo mana*/ "3", /*attacco*/ "4", /*vita*/ "8", /*velocità*/ "4", /*classe*/ "barbaro"},

    {/*simbolo formale*/ "^\n/|\\\n/ \\", /*simbolo*/ "^[/|\\[/ \\", /*costo mana*/ "3", /*attacco*/ "3", /*vita*/ "5", /*velocità*/ "5", /*classe*/ "ninja"},

    {/*simbolo formale*/ "o\n/|)->\n/ \\", /*simbolo*/ "o[/|)->[/ \\", /*costo mana*/ "2", /*attacco*/ "4", /*vita*/ "4", /*velocità*/ "3", /*classe*/ "arciere"},

    {/*simbolo formale*/ "o\n<|>\n/ \\", /*simbolo*/ "o[<|>[/ \\", /*costo mana*/ "4", /*attacco*/ "4", /*vita*/ "5", /*velocità*/ "4", /*classe*/ "assassino"},

    {/*simbolo formale*/ "@\n/|\\\n/ \\>", /*simbolo*/ "@[/|\\[/ \\>", /*costo mana*/ "10", /*attacco*/ "13", /*vita*/ "9", /*velocità*/ "13", /*classe*/ "mago"}, 
};
    
    static String[][] personaggiSchierati;
    
    static String[][] personaggiNemici = {
        logPersonaggiNemici[5],
    };
    
    
    public static void main(String[] args) {
        char[][] Mappa = new char[Altezza][Larghezza];
        Vector<Torre> T = new Vector<>();
        Vector<Personaggio> P = new Vector<>();
        Vector<Personaggio> Nemici = new Vector<>();
        
        int i;
        String scelta;
        
        for(i=0;i<personaggiNemici.length;i++) {
            Nemici.add(new Personaggio((65-(5*i)), personaggiNemici[i][1], Integer.valueOf(personaggiNemici[i][2]), Integer.valueOf(personaggiNemici[i][3]), Integer.valueOf(personaggiNemici[i][4]), Integer.valueOf(personaggiNemici[i][5]), personaggiNemici[i][6]));
        }
        
        T.add(new Torre(3, 2));
        T.add(new Torre(70, 2));
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("Benvenuto a Tower Defense!");
        System.out.println("_______________");
        System.out.println("__ 1 - Gioca __");
        System.out.println("__ 2 - Esci  __");
        System.out.println("_______________");
        scelta = in.next();
        switch(scelta) {
            case "1":
                while(!vinto) {
                    attivaRound(P, Mappa, T, Nemici);
                }
                if(vittoriaGiocatore) System.out.println("Hai vinto!");
                else System.out.println("L'avversario ha vinto!");
                break;
            case "2":
                return;
        }
        
    }
    
    public static void attivaRound(Vector<Personaggio> P, char[][] Mappa, Vector<Torre> T, Vector<Personaggio>Nemici) {
        long ritardoMillisecondi = 500;
        int i, j;
        if(stampaAreaDiGioco(Mappa, P, T, Nemici) == "esci") return;
        for(i=0;i<P.size();i++) {
            P.get(i).vaiAvanti(3);
            for(j=0;j<T.size();j++) {
                if(P.get(i).condMovimento(T.get(j))) {
                    String esitoAttacco = T.get(j).rimuoviVita(P.get(i).getAttacco());
                    if(esitoAttacco == "sconfitta") {
                        vinto = true;
                        vittoriaGiocatore = true;
                    }
                    else {
                        P.remove(i);
                        break;
                    }
                }
             }
        }
        for(i=0;i<Nemici.size();i++) {
            Nemici.get(i).vaiIndietro(3);
            for(j=0;j<T.size();j++) {
                if(Nemici.get(i).condMovimento(T.get(j))) {
                    String esitoAttacco = T.get(j).rimuoviVita(Nemici.get(i).getAttacco());
                    if(esitoAttacco == "sconfitta") {
                        vinto = true;
                        vittoriaGiocatore = false;
                    }
                    else {
                        Nemici.remove(i);
                        break;
                    }
                   
                }
             }
        }
        try {
            Thread.sleep(ritardoMillisecondi);
            System.out.flush(); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public static String stampaAreaDiGioco(char[][] M, Vector<Personaggio> P, Vector<Torre> T, Vector<Personaggio>Nemici) {
        int i, j, posizionePartenza;
        int characterPosY = 4, indexSimbolo = -1;
        int scelta;
        Scanner in = new Scanner(System.in);
        
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
            int posAlleato = P.get(i).getPos() + 1;
            for(j=0;j<Nemici.size();j++) {
                int posNemico = Nemici.get(j).getPos() - 1;
                if(posAlleato >= posNemico) {
                    String esitoScontro = P.get(i).fight(Nemici.get(j));
                    if(esitoScontro.equals("vittoria")) {
                        Nemici.get(j).setSimbolo("");
                        Nemici.remove(j);
                    } else if(esitoScontro.equals("sconfitta")) {
                        P.get(i).setSimbolo("");
                        P.remove(i);
                    } else {
                        P.get(i).setSimbolo("");
                        P.remove(i);
                        Nemici.get(j).setSimbolo("");
                        Nemici.remove(j);
                    }
                }
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
        for(i=0;i<Nemici.size();i++) {
            characterPosY = 4;
            indexSimbolo = -1;
            simbolo = Nemici.get(i).getSimbolo();
            posizionePartenza = Nemici.get(i).getPos();
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
        if(contRound == 0) {
            while(true) {
            System.out.println("______________________________");
            System.out.println("__ 1 - Scegli il personagio __");
            System.out.println("__ 2 - Combatti             __");
            System.out.println("__ 3 - Esci                 __");
            System.out.println("______________________________");

            scelta = in.nextInt();

            switch(scelta) {
                case 1:
                    System.out.println("Quale personaggio vuoi scegliere?");
                    String nome = in.next().toLowerCase();
                    for(i=0;i<logPersonaggi.length;i++) {
                        if(logPersonaggi[i][6].equals(nome)) P.add(new Personaggio((9+(5*i)), logPersonaggi[i][1], Integer.valueOf(logPersonaggi[i][2]), Integer.valueOf(logPersonaggi[i][3]), Integer.valueOf(logPersonaggi[i][4]), Integer.valueOf(logPersonaggi[i][5]), logPersonaggi[i][6]));
                    }
                    break;
                case 2:
                    contRound++;
                    return "combatti";
                default:
                    contRound++;
                    return "esci";
                }
            }
        } else return "combatti";
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
            System.out.println(logPersonaggi[i][6] + "\nCosto: " + logPersonaggi[i][2] + "\nVita: " + logPersonaggi[i][4] + "  Attacco: " + logPersonaggi[i][3] + "  Velocita: " + logPersonaggi[i][5]);
            System.out.print(" ");
            System.out.println(logPersonaggi[i][0] + "\n");
        }
    }
    
}
