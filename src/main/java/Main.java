import java.io.IOException;
import java.nio.file.Path;

import com.williamcallahan.tui4j.compat.bubbletea.Program;

import data.DatasetLoader;
import data.Trie;
import model.Anime;
import ui.App;

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        Path dataset = Path.of("data/anime.csv");
        DatasetLoader loader = new DatasetLoader();
        Anime[] animeList;

        try {
            animeList = loader.load(dataset, trie);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load anime dataset: " + dataset, e);
        }

        App app = new App(trie, animeList);

        new Program(app).run();
    }
}
