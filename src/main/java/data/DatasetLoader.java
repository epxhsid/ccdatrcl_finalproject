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

public class DatasetLoader {

    /**
     * Reads an anime CSV dataset from the specified path, parses each record,
     * and maps all variations of the titles into the provided Trie.
     *
     * @param path  The file path to the CSV dataset.
     * @param trie  The search Trie where anime records will be indexed.
     * @param animeList  The list of anime to sort and select from.
     * @return The top anime based on their rank.
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
