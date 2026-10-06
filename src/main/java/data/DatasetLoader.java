package data;

import model.Anime;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import model.Genre;
import model.Studio;

public class DatasetLoader {
    private Genre[] genres = new Genre[0];
    private Studio[] studios = new Studio[0];

    /**
     * Reads an anime CSV dataset from the specified path, parses each record,
     * and maps all variations of the titles into the provided Trie.
     *
     * @param path The file path to the anime CSV dataset.
     * @param trie The search Trie where anime records will be indexed.
     * @return The anime records loaded from the dataset.
     * @throws IOException If the file cannot be read or parsed.
     */
    public Anime[] load(Path path, Trie trie) throws IOException {
        AnimeHashSet animeSet = new AnimeHashSet();

        try (Reader reader = Files.newBufferedReader(path);
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .get()
                        .parse(reader)) {
            for (CSVRecord record : parser) {
                Anime anime = parseAnime(record);
                insertTitles(trie, anime);
                animeSet.add(anime);
            }
        }

        return animeSet.toArray();
    }

    /**
     * Loads the genre and studio lookup tables before loading anime records.
     *
     * @param animePath path to the anime CSV
     * @param genresPath path to the genres CSV
     * @param studiosPath path to the studios CSV
     * @param trie search trie where anime records are indexed
     * @return anime records loaded from the dataset
     * @throws IOException if any CSV cannot be read or parsed
     */
    public Anime[] load(Path animePath, Path genresPath, Path studiosPath, Trie trie) throws IOException {
        genres = loadGenres(genresPath);
        studios = loadStudios(studiosPath);
        return load(animePath, trie);
    }

    public Genre[] getGenres() {
        return genres;
    }

    public Studio[] getStudios() {
        return studios;
    }

    private Genre[] loadGenres(Path path) throws IOException {
        Genre[] result = new Genre[16];
        int count = 0;

        try (Reader reader = Files.newBufferedReader(path);
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .get()
                        .parse(reader)) {
            for (CSVRecord record : parser) {
                if (count == result.length) {
                    result = growGenres(result);
                }
                result[count++] = new Genre(
                        Integer.parseInt(record.get("id").trim()),
                        record.get("name"));
            }
        }

        return copyGenres(result, count);
    }

    private Studio[] loadStudios(Path path) throws IOException {
        Studio[] result = new Studio[16];
        int count = 0;

        try (Reader reader = Files.newBufferedReader(path);
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .get()
                        .parse(reader)) {
            for (CSVRecord record : parser) {
                if (count == result.length) {
                    result = growStudios(result);
                }
                result[count++] = new Studio(
                        Integer.parseInt(record.get("id").trim()),
                        record.get("name"));
            }
        }

        return copyStudios(result, count);
    }

    private Genre[] growGenres(Genre[] genres) {
        Genre[] grown = new Genre[genres.length * 2];
        System.arraycopy(genres, 0, grown, 0, genres.length);
        return grown;
    }

    private Studio[] growStudios(Studio[] studios) {
        Studio[] grown = new Studio[studios.length * 2];
        System.arraycopy(studios, 0, grown, 0, studios.length);
        return grown;
    }

    private Genre[] copyGenres(Genre[] genres, int length) {
        Genre[] copy = new Genre[length];
        System.arraycopy(genres, 0, copy, 0, length);
        return copy;
    }

    private Studio[] copyStudios(Studio[] studios, int length) {
        Studio[] copy = new Studio[length];
        System.arraycopy(studios, 0, copy, 0, length);
        return copy;
    }

    private Anime parseAnime(CSVRecord record) {
        Anime anime = new Anime();
        anime.setId(parseInt(record, "id"));
        anime.setTitle(record.get("title"));
        anime.setTitleJa(record.get("titleJa"));
        anime.setTitleEn(record.get("titleEn"));
        anime.setMean(parseDouble(record, "mean"));
        anime.setRank(parseInt(record, "rank"));
        anime.setNumListUsers(parseInt(record, "num_list_users"));
        anime.setNumScoringUsers(parseInt(record, "num_scoring_users"));
        anime.setNumEpisodes(parseInt(record, "num_episodes"));
        anime.setStartDate(parseDate(record, "start_date"));
        anime.setEndDate(parseDate(record, "end_date"));
        anime.setMediaType(record.get("media_type"));
        anime.setStatus(record.get("status"));
        anime.setRating(record.get("rating"));
        anime.setAverageEpisodeDuration(parseInt(record, "average_episode_duration"));
        anime.setGenreIds(parseIds(record.get("genres")));
        anime.setStudioIds(parseIds(record.get("studios")));

        return anime;
    }

    private void insertTitles(Trie trie, Anime anime) {
        trie.insert(anime.getTitle(), anime);
        trie.insert(anime.getTitleEn(), anime);
        trie.insert(anime.getTitleJa(), anime);
    }

    private Integer parseInt(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        return Integer.parseInt(value.trim());
    }

    private Double parseDouble(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        return Double.parseDouble(value.trim());
    }

    private LocalDate parseDate(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        if (value.length() == 7) { // Handle YYYY-MM format by appending "-01" for the first day of the month
            value += "-01";
        }

        if (value.length() == 4) { // Handle YYYY format by appending "-01-01" for the first day of the year
            value += "-01-01";
        }

        return LocalDate.parse(value.trim());
    }

    /**
     * Parses a list of integer IDs from a semicolon-separated string.
     * If the value is null or blank, it returns an empty list. Otherwise,
     * it splits the string by semicolons, trims each ID, and parses them as
     * integers, adding them to a list.
     * 
     * Example: The input string is "1;14;17;23", the function will return a
     * list containing the integers [1, 14, 17, 23].
     * 
     * @param value
     * @return
     */
    private int[] parseIds(String value) {
        if (value == null || value.isBlank()) {
            return new int[0];
        }

        String[] values = value.split(";");
        int[] ids = new int[values.length];
        int count = 0;

        for (String id : values) {
            if (!id.isBlank()) {
                ids[count++] = Integer.parseInt(id.trim());
            }
        }

        int[] result = new int[count];
        System.arraycopy(ids, 0, result, 0, count);
        return result;
    }
}
