import java.nio.file.Path;
import java.util.Scanner;

import data.DatasetLoader;
import data.Trie;
import model.Anime;

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        DatasetLoader loader = new DatasetLoader();

        Path dataset = Path.of("data", "anime.csv");  

        try {
            System.out.println("Loading anime dataset...");
            loader.load(dataset, trie);
            System.out.println("Dataset loaded.");
        } catch (Exception e) {
            System.err.println("Error loading dataset: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nSearch anime (or 'exit'): ");
            String query = scanner.nextLine();

            if (query.equalsIgnoreCase("exit")) {
                break;
            }

            Anime[] exactResults = trie.exactSearch(query);

            if (exactResults.length > 0) {
                System.out.println("\nExact matches:");

                for (Anime anime : exactResults) {
                    printAnime(anime);
                }

                continue;
            }

            Anime[] prefixResults = trie.prefixSearch(query);

            if (prefixResults.length == 0) {
                System.out.println("\nNo anime found.");
                continue;
            }

            System.out.println("\nPrefix matches:");

            for (Anime anime : prefixResults) {
                printAnime(anime);
            }
        }

        scanner.close();
        System.out.println("Goodbye.");
    }

    private static void printAnime(Anime anime) {
        System.out.println(
                anime.getId() + " - " +
                anime.getTitle()
                
        );
    }
}
