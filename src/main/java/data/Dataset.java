package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Anime;
import model.Genre;
import model.Studio;

/**
 * Immutable container for the related datasets loaded by the application.
 */
@Getter 
@AllArgsConstructor 
public final class Dataset {
    private final Anime[] anime;
    private final Genre[] genres;
    private final Studio[] studios;

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
