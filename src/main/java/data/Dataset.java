package data;

import model.Anime;
import model.Genre;
import model.Studio;

/**
 * Immutable container for the related datasets loaded by the application.
 */
public final class Dataset {
    private final Anime[] anime;
    private final Genre[] genres;
    private final Studio[] studios;

    public Dataset(Anime[] anime, Genre[] genres, Studio[] studios) {
        this.anime = anime;
        this.genres = genres;
        this.studios = studios;
    }

    public Anime[] getAnime() {
        return anime;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public Studio[] getStudios() {
        return studios;
    }
}
