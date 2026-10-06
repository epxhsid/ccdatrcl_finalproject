package data;

import java.io.IOException;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import model.Anime;
import model.Genre;
import model.Studio;

/**
 * Coordinates loading the related anime datasets.
 */
@AllArgsConstructor 
public class DatasetLoader {
    private final AnimeDatasetLoader animeLoader;
    private final ReferenceDataLoader referenceLoader;

    /**
     * Loads only anime records and indexes their titles.
     *
     * @param animePath path to the anime CSV
     * @param trie      trie used for title search
     * @return loaded anime records
     * @throws IOException if the CSV cannot be read
     */
    public Anime[] load(Path animePath, Trie trie) throws IOException {
        return animeLoader.load(animePath, trie);
    }

    /**
     * Loads anime records and their genre and studio reference data.
     *
     * @param animePath   path to the anime CSV
     * @param genresPath  path to the genres CSV
     * @param studiosPath path to the studios CSV
     * @param trie        trie used for title search
     * @return all loaded datasets
     * @throws IOException if any CSV cannot be read
     */
    public Dataset load(Path animePath, Path genresPath, Path studiosPath, Trie trie) throws IOException {
        Anime[] anime = animeLoader.load(animePath, trie);

        Genre[] genres = referenceLoader.loadGenres(genresPath);

        Studio[] studios = referenceLoader.loadStudios(studiosPath);

        return new Dataset(anime, genres, studios);
    }
}
