package towerdefense;

import java.util.Scanner;
import java.util.Vector;

/**
 * Classe principale del gioco TowerDefense
 * Gestisce la logica di gioco, il ciclo dei round, la stampa della mappa
 * e l'interazione con il giocatore tramite console.
 */
public class TowerDefense {

    // Dimensioni dell’area di gioco a caratteri
    public static int Altezza = 7;
    public static int Larghezza = 80;

    // Flag e variabili di stato generali
    public static boolean vinto = false, vittoriaGiocatore;
    public static int contRound = 0, personaggiSchierati = 0, personaggiNemiciSchierati = 0;
    public static double manaPosseduta = 10, manaAvversario = 10;
    public static boolean condRound = true;

    /**
     * Archivio dei personaggi disponibili al giocatore.
     * Ogni entry contiene:
     * 0 = simbolo multi-riga “formale” (a video nel menu personaggi)
     * 1 = simbolo in-linea usato nel rendering della mappa
     * 2 = costo mana
     * 3 = attacco
     * 4 = vita
     * 5 = velocità
     * 6 = nome/classe del personaggio
     */
    static String[][] logPersonaggi = {
        {"o\n/|>\n/ \\", "o[/|>[/ \\", "5", "6", "7", "3", "cavaliere"},
        {"O\n/|}_\n/ \\", "O[/|}_[/ \\", "3", "4", "8", "4", "barbaro"},
        {"^\n/|\\\n/ \\", "^[/|\\[/ \\", "3", "3", "5", "5", "ninja"},
        {"o\n/|)->\n/ \\", "o[/|)->[/ \\", "2", "4", "4", "3", "lanciere"},
        {"o\n<|>\n/ \\", "o[<|>[/ \\", "4", "4", "5", "4", "assassino"},
        {"@\n/|\\\n/ \\>", "@[/|\\[/ \\>", "10", "13", "10", "7", "mago"},
    };

    // Versione dei personaggi per il team nemico
    static String[][] logPersonaggiNemici = {
        {"o\n/|>\n/ \\", "o[/|<[\\ /", "5", "6", "7", "3", "cavaliere"},
        {"O\n/|}_\n/ \\", "O[/|{_[\\ /", "3", "4", "8", "4", "barbaro"},
        {"^\n/|\\\n/ \\", "^[\\|/[\\ /", "3", "3", "5", "5", "ninja"},
        {"o\n/|)->\n/ \\", "o[/|(-<[\\ /", "2", "4", "4", "3", "lanciere"},
        {"o\n<|>\n/ \\", "o[>|<[\\ /", "4", "4", "5", "4", "assassino"},
        {"@\n/|\\\n/ \\>", "@[/|\\[\\ /<", "10", "13", "10", "7", "mago"},
    };

    static String[][] personaggiNemici;

    public static void main(String[] args) {

        // Allocazione della mappa e dei vettori degli oggetti dinamici
        char[][] Mappa = new char[Altezza][Larghezza];
        Vector<Torre> T = new Vector<>();
        Vector<Personaggio> P = new Vector<>();
        Vector<Personaggio> Nemici = new Vector<>();

        schieraNemici(Nemici);  // Genera la prima ondata di nemici

        // Aggiunta delle due torri principali ai lati della mappa
        T.add(new Torre(3, 2));   // Torre giocatore
        T.add(new Torre(70, 2));  // Torre nemica

        Scanner in = new Scanner(System.in);

        // Menu iniziale
        System.out.println("Benvenuto a Tower Defense!");
        System.out.println("_______________");
        System.out.println("__ 1 - Gioca __");
        System.out.println("__ 2 - Esci  __");
        System.out.println("_______________");

        String scelta = in.next();

        switch (scelta) {
            case "1":
                // Ciclo di gioco: continua finché qualcuno non vince
                while (!vinto) {
                    attivaRound(P, Mappa, T, Nemici);
                }

                // Esito finale
                if (vittoriaGiocatore) System.out.println("Hai vinto!");
                else System.out.println("L'avversario ha vinto!");
                System.out.println("Vuoi rigiocare? (si o no)");
                scelta = in.next().toLowerCase();
                if(scelta.equals("si")) {
                    T.get(0).setVita(50);
                    T.get(1).setVita(50);
                    vinto = false;
                    vittoriaGiocatore = false;
                    manaPosseduta = 10;
                    contRound = 0;
                    condRound = true;
                    personaggiNemiciSchierati = 0;
                    personaggiSchierati = 0;
                    main(args);
                } else System.exit(0);
                break;

            case "2":
                return;
        }
    }

    /**
     * Genera automaticamente una squadra di nemici spendendo mana avversario.
     */
    public static void schieraNemici(Vector<Personaggio> Nemici) {

        manaAvversario = 10;
        
        int maxManaSpendibile = (int) (Math.random() * manaAvversario);
        
        manaAvversario -= maxManaSpendibile;
        
        manaAvversario += (manaAvversario / 100 * 70);
        manaAvversario = Math.round(manaAvversario);
        if(manaAvversario > 10) manaAvversario = 10;
        else if(manaAvversario < 2) manaAvversario += 4;

        // Finché rimane abbastanza mana, continua a generare personaggi random
        while (maxManaSpendibile > 1) {
            int numeroRandom = (int)(Math.random() * 6);
            int costoMana = Integer.parseInt(logPersonaggiNemici[numeroRandom][2]);

            if (costoMana <= maxManaSpendibile) {

                Nemici.add(new Personaggio(
                    65 - (5 * personaggiNemiciSchierati),            // Posizione di spawn
                    logPersonaggiNemici[numeroRandom][1],            // Simbolo
                    Integer.valueOf(logPersonaggiNemici[numeroRandom][2]),  // Costo mana
                    Integer.valueOf(logPersonaggiNemici[numeroRandom][3]),  // Attacco
                    Integer.valueOf(logPersonaggiNemici[numeroRandom][4]),  // Vita
                    Integer.valueOf(logPersonaggiNemici[numeroRandom][5]),  // Velocità
                    logPersonaggiNemici[numeroRandom][6]             // Classe
                ));

                maxManaSpendibile -= costoMana;
                personaggiNemiciSchierati++;
            }
        }
    }

    /**
     * Metodo che gestisce l'intero svolgimento di un round:
     *  - stampa la mappa
     *  - muove i personaggi
     *  - gestisce collisioni e combattimenti
     *  - applica un ritardo per animare la console
     */
    public static void attivaRound(Vector<Personaggio> P, char[][] Mappa,
                                   Vector<Torre> T, Vector<Personaggio> Nemici) {

        long ritardoMillisecondi = 500;  // Ritardo tra gli aggiornamenti della schermata

        // Se entrambi i campi sono vuoti, inizia un nuovo round
        
        if (P.size() == 0 && Nemici.size() == 0) {
            schieraNemici(Nemici);
            manaPosseduta += (manaPosseduta / 100 * 70);
            manaPosseduta = Math.round(manaPosseduta);
            if(manaPosseduta > 10) manaPosseduta = 10;
            else if(manaPosseduta < 3) manaPosseduta += 4;
            
            condRound = true;
        }

        // Stampa l’area di gioco
        stampaAreaDiGioco(Mappa, P, T, Nemici);

        // Movimento dei personaggi alleati e interazioni con le torri
        for (int i = 0; i < P.size(); i++) {

            P.get(i).vaiAvanti(P.get(i).getVelocita()); // Avanza di tot unità

            for (int j = 0; j < T.size(); j++) {

                if (P.get(i).condMovimento(T.get(j))) {
                    int attacco = P.get(i).getAttacco();
                    if((P.get(i).getVitaMassima() - P.get(i).getVita()) <= (P.get(i).getVitaMassima() / 2)) attacco /= 2;
                    String esitoAttacco = T.get(j).rimuoviVita(attacco); // Attacca la torre
                    
                    if (esitoAttacco.equals("sconfitta")) {  // Se la vita della torre è scesa a 0 si proclama la vittoria
                        vinto = true;
                        vittoriaGiocatore = true;
                    } else {
                        P.remove(i);
                        personaggiSchierati--;
                        break;
                    }
                }
            }
        }

        // Movimento dei nemici e interazione con le torri
        for (int i = 0; i < Nemici.size(); i++) {

            Nemici.get(i).vaiIndietro(Nemici.get(i).getVelocita());

            for (int j = 0; j < T.size(); j++) {

                if (Nemici.get(i).condMovimento(T.get(j))) {
                    int attacco = Nemici.get(i).getAttacco();
                    if((Nemici.get(i).getVitaMassima() - Nemici.get(i).getVita()) <= (Nemici.get(i).getVitaMassima() / 2)) attacco /= 2;
                    String esitoAttacco = T.get(j).rimuoviVita(attacco);

                    if (esitoAttacco.equals("sconfitta")) {
                        vinto = true;
                        vittoriaGiocatore = false;
                        Nemici.clear();
                        P.clear();
                        stampaAreaDiGioco(Mappa, P, T, Nemici);
                        break;
                    } else {
                        Nemici.remove(i);
                        personaggiNemiciSchierati--;
                        break;
                    }
                }
            }
        }

        /**
         * Il metodo sleep qui introduce un ritardo visivo nell’esecuzione,
         * permettendo alla console di funzionare come una sorta di “schermo animato”.
         *
         * Perché serve il try/catch?
         *  - Thread.sleep può lanciare InterruptedException se il thread viene interrotto
         *  - Si ripristina lo stato di interruzione con interrupt()
         *
         * Perché System.out.flush() ?
         *  - Forza la stampa immediata su console, evitando accumuli nel buffer.
         */
        try {
            Thread.sleep(ritardoMillisecondi); // Pausa di mezzo secondo
            System.out.flush();               // Pulisce il buffer di output
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stampa l'area di gioco, aggiorna grafica ASCII, gestisce scelta del personaggio e il menu del round.
     */
    public static String stampaAreaDiGioco(char[][] M, Vector<Personaggio> P, Vector<Torre> T, Vector<Personaggio>Nemici) {
        // Variabili di lavoro
        int i, j, posizionePartenza;
        // `characterPosY` indica la riga verticale (Y) della "testa" del simbolo quando si disegna il personaggio.
        // Viene inizializzato a 4 perché la riga principale del personaggio è posizionata qui.
        int characterPosY = 4, indexSimbolo = -1;
        int scelta;
        Scanner in = new Scanner(System.in);

        String simbolo;

        // Pulizia schermo (simulazione): stampa linee vuote per "spostare" contenuto precedente scorrendo.
        clearScreen();

        // Se siamo nella fase di setup del round, mostra il menu con le informazioni sui personaggi.
        if(condRound) stampaMenu(T);

        // Inizializza la matrice di gioco M riempiendo con elementi che rappresentano le torri o spazi vuoti.
        for(i=0;i<Altezza;i++) {
            for(j=0;j<Larghezza;j++) {
                // Per ogni cella chiede a condTorre se in quella posizione dev'essere disegnato un simbolo di torre.
                if(condTorre(i, j, T).equals("verticale")) M[i][j] = '|';
                else if(condTorre(i, j, T).equals("orizzontale")) M[i][j] = '_';
                else M[i][j] = ' ';
            }
        }

        // Risoluzione degli scontri: per ogni alleato controlla se collide con un nemico.
        // Se le posizioni si sovrappongono, viene chiamato il metodo fight per determinare l'esito.
        for(i=0;i<P.size();i++) {
            int posAlleato = P.get(i).getPos() + 1; // posizione di test per contatto (leggero offset)
            for(j=0;j<Nemici.size();j++) {
                int posNemico = Nemici.get(j).getPos() - 1; // posizione di test per il nemico (offset opposto)
                if((posAlleato - posNemico) > -2) {
                    // Se le posizioni si sovrappongono o sono vicino almeno di due caselle, avviene il combattimento
                    String esitoScontro = P.get(i).fight(Nemici.get(j));

                    // esitoScontro: "vittoria", "sconfitta", o altro (pare pareggio)
                    if(esitoScontro.equals("vittoria")) {
                        // Se l'alleato vince, rimuove il nemico: prima svuota il simbolo per evitare disegno residuo
                        Nemici.get(j).setSimbolo("");
                        Nemici.remove(j);
                        personaggiNemiciSchierati--;
                    } else if(esitoScontro.equals("sconfitta")) {
                        // Se l'alleato perde, lo rimuove dalla lista e decrementa il contatore
                        P.get(i).setSimbolo("");
                        P.remove(i);
                        personaggiSchierati--;
                    } else {
                        // Caso di pareggio / entrambi muoiono: rimuove entrambi e aggiorna i contatori
                        P.get(i).setSimbolo("");
                        P.remove(i);
                        Nemici.get(j).setSimbolo("");
                        Nemici.remove(j);
                        personaggiSchierati--;
                        personaggiNemiciSchierati--;
                    }
                }
            }
        }

        // Disegno dei personaggi alleati nella matrice M
        for(i=0;i<P.size();i++) {
            characterPosY = 4;      // reset della riga di partenza per il personaggio corrente
            indexSimbolo = -1;      // indice del carattere nel token simbolo del personaggio
            simbolo = P.get(i).getSimbolo(); // stringa simbolo in-linea (con marcatori '[' per newline)
            posizionePartenza = P.get(i).getPos(); // posizione X di partenza nella mappa

            // Scorre la stringa simbolo carattere per carattere per trasferirli nella matrice M
            for(j=0;j<simbolo.length();j++) {
                indexSimbolo++;
                // Protezione: se l'indice raggiunge la lunghezza, interrompe (sicurezza contro overflow)
                if(indexSimbolo == simbolo.length()) break;

                // Il carattere '[' viene usato come marcatore per spostare il disegno sulla riga successiva.
                if(simbolo.charAt(indexSimbolo) == '[') {
                    characterPosY++;   // sposta la riga verso il basso per disegnare la riga successiva del simbolo
                    indexSimbolo++;    // salta il carattere '[' e passa al primo carattere della riga successiva
                    j=0;               // reset dell'indice orizzontale per la nuova riga del simbolo
                    M[characterPosY][posizionePartenza] = simbolo.charAt(indexSimbolo);
                }
                else {
                    // Caso normale: posiziona il carattere nella riga corrente
                    if(characterPosY == 4) {
                        // Se siamo alla riga principale (4), il codice aumenta j temporalmente per disegnare
                        // spostando da sinistra a destra e poi lo riporta indietro: serve a gestire l'offset iniziale.
                        j++;
                        M[characterPosY][(posizionePartenza + j)] = simbolo.charAt(indexSimbolo);
                        j--;
                    }
                    else {
                        // Per le righe inferiori usa l'indice j direttamente
                        M[characterPosY][(posizionePartenza + j)] = simbolo.charAt(indexSimbolo);
                    }
                }
            }
        }

for(i=0;i<Nemici.size();i++) {  
    // Reset della Y del personaggio nemico, sempre da riga 4 si parte come un mob che respawna
    characterPosY = 4;

    // Indice per scandire i caratteri del simbolo ASCII del personaggio
    indexSimbolo = -1;

    // Il simbolo che rappresenta graficamente il nemico
    simbolo = Nemici.get(i).getSimbolo();

    // Posizione iniziale del nemico sulla mappa, con +2 per spostarlo più a destra
    posizionePartenza = Nemici.get(i).getPos() + 2;

    // Si scansiona ogni carattere del simbolo per disegnarlo nella matrice
    for(j=0;j<simbolo.length();j++) {
        indexSimbolo++;

        // Se finisce il simbolo esce dal loop
        if(indexSimbolo == simbolo.length()) break;

        // Se trova '[', significa passare alla riga sotto del personaggio ASCII
        if(simbolo.charAt(indexSimbolo) == '[') {
            // Scende di una riga nella rappresentazione grafica
            characterPosY++;

            // Avanza al carattere dopo '['
            indexSimbolo++;

            // Reset di j per ripartire dall’inizio della riga nel simbolo
            j=0;

            // Inserisce il carattere del simbolo nella nuova riga
            M[characterPosY][posizionePartenza] = simbolo.charAt(indexSimbolo);
        }
        else {
            // Se siamo nella prima riga del nemico
            if(characterPosY == 4) {
                // j viene incrementato per disegnare verso sinistra
                j++;

                // Inserisce il carattere all’indietro rispetto alla posizione base
                M[characterPosY][(posizionePartenza - j)] = simbolo.charAt(indexSimbolo);

                // Ripristina j alla sua iterazione naturale
                j--;
            }
            else {
                // Se non siamo nella prima riga, si disegna normalmente verso sinistra
                M[characterPosY][(posizionePartenza - j)] = simbolo.charAt(indexSimbolo);
            }
        }
    }
}

        // Stampa della matrice M riga per riga sulla console
        for(i=0;i<Altezza;i++) {
            for(j=0;j<Larghezza;j++) {
                System.out.print(M[i][j]);
            }
            System.out.print("\n");
        }

        // Stampa lo stato di vita delle due torri (sinistra e destra) con formattazione fissa
        System.out.println("    " + T.get(0).getVita() + " hp                                                              " + T.get(1).getVita() + " hp");

        // Se siamo nella fase di scelta del round, mostra il mana del giocatore come una sequenza di '0'
        if(condRound) {
            System.out.print("Mana: ");
            for(i=0;i<manaPosseduta;i++) {
                System.out.print("0");
            }
        }

        // Se è il primo round o siamo nella fase di setup, mostra il menu di scelta iterativamente
        if(contRound == 0 || condRound) {
            while(true) {
                System.out.println("\n________________________");
                System.out.println("__ 1 - Schiera truppe __");
                System.out.println("__ 2 - Combatti       __");
                System.out.println("__ 3 - Esci           __");
                System.out.println("________________________");

                scelta = in.nextInt();

                switch(scelta) {
                    case 1:
                        // Se l'utente sceglie di schierare un personaggio:
                        System.out.println("Quale personaggio vuoi scegliere?");
                        String nome = in.next().toLowerCase();

                        // Cerca il personaggio nel logPersonaggi e, se ha mana sufficiente, lo aggiunge a P
                        for(i=0;i<logPersonaggi.length;i++) {
                            if(logPersonaggi[i][6].equals(nome)) {
                                if(manaPosseduta - Integer.valueOf(logPersonaggi[i][2]) >= 0) {
                                    // Deduce il costo dal mana e crea il nuovo personaggio alla posizione di spawn alleata
                                    manaPosseduta -= Integer.valueOf(logPersonaggi[i][2]);
                                    P.add(new Personaggio(
                                        (9 + (5 * personaggiSchierati)), // posizione X calcolata in base ai già schierati
                                        logPersonaggi[i][1],             // simbolo inline
                                        Integer.valueOf(logPersonaggi[i][2]), // costo mana
                                        Integer.valueOf(logPersonaggi[i][3]), // attacco
                                        Integer.valueOf(logPersonaggi[i][4]), // vita
                                        Integer.valueOf(logPersonaggi[i][5]), // velocità
                                        logPersonaggi[i][6]                   // classe/nome
                                    ));
                                    personaggiSchierati++;

                                    // Richiama ricorsivamente stampaAreaDiGioco per aggiornare la visuale
                                    stampaAreaDiGioco(M, P, T, Nemici);

                                    // Dopo aver piazzato un personaggio, ritorna "esci" al chiamante per non continuare il menu
                                    return "esci";
                                } else {
                                    // Messaggio se mana insufficiente
                                    System.out.println("Non possiedi abbastanza mana");
                                }
                            }
                        }
                        break;

                    case 2:
                        // L'utente sceglie di iniziare la fase di combattimento: aggiorna i flag e ritorna "combatti"
                        contRound++;
                        condRound = false;
                        return "combatti";

                    default:
                        // Per qualsiasi altra scelta termina l'applicazione
                        System.exit(0);
                }
            }
        } else return "combatti";
    }
    
    public static String condTorre(int y, int x, Vector<Torre> T) {
        // Determina se la cella (y,x) deve contenere una porzione di torre e quale tipo ("verticale"/"orizzontale")
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
        // "Pulizia" console elementare: stampa 50 righe vuote per spostare il contenuto precedente fuori dallo schermo
        for (int i = 0; i < 50; i++) { 
            System.out.println();
        }
    }
    
    public static void stampaMenu(Vector<Torre> T) {
        // Stampa il menu informativo che mostra i personaggi giocabili (tranne l'ultimo) con le loro statistiche
        int i;
        for(i=0;i<(logPersonaggi.length-1);i++) {
            System.out.println(logPersonaggi[i][6] + "\nCosto: " + logPersonaggi[i][2] + "\nVita: " + logPersonaggi[i][4] + "  Attacco: " + logPersonaggi[i][3] + "  Velocita: " + logPersonaggi[i][5]);
            System.out.print(" ");
            System.out.println(logPersonaggi[i][0] + "\n");
        }
    }
}
