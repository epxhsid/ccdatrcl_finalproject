import java.io.IOException;
import java.nio.file.Path;

import com.williamcallahan.tui4j.compat.bubbletea.Program;

import data.Dataset;
import data.DatasetLoader;
import data.Trie;
import ui.App;

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        Path dataset = Path.of("data/anime.csv");
        Path genres = Path.of("data/genres.csv");
        Path studios = Path.of("data/studios.csv");
        DatasetLoader loader = new DatasetLoader();
        Dataset datasetData;

        try {
            datasetData = loader.load(dataset, genres, studios, trie);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load anime dataset: " + dataset, e);
        }

        App app = new App(datasetData.getAnime());

        new Program(app).run();
    }
}
