package fr.sam.NSY014;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Vous avez donné les arguments suivant pour le démarrage : " + Arrays.toString(args));
        if (args.length < 4) {
            System.out.println("Erreur : Le nombre minimum d'arguments est 4. Dans l'ordre, il faut l'hôte, le port, l'utilisateur et le mot de passe");
        }
        else {
            final SquashTMRestApi authSystem = new SquashTMRestApi(args[0], args[1], args[2], args[3]);
            System.out.println(authSystem.getProjectIdByName("Test Project-1"));
            System.out.println(authSystem.getProjectIdById("14"));
        }
    }
}
