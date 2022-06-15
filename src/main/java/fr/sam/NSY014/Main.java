package fr.sam.NSY014;

import java.net.UnknownHostException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Vous avez donné les arguments suivant pour le démarrage : " + Arrays.toString(args));
        if (args.length < 4) {
            System.out.println("Erreur : Le nombre minimum d'arguments est 4. Dans l'ordre, il faut l'hôte, le port, l'utilisateur et le mot de passe");
        }
        else {
            final SquashTMRestApi squash = new SquashTMRestApi(args[0], args[1], args[2], args[3]);
            squash.getTestsByProjectName("Test Project-1","Test 1", "test 1");
            squash.modifyTestStatus("2","SUCCESS");
        }
    }
}
