package data;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import model.Anime;

/**
 * Parses anime records and indexes their title variants.
 */
final class AnimeDatasetLoader {
    Anime[] load(Path path, Trie trie) throws IOException {
        AnimeHashSet animeSet = new AnimeHashSet();

        try (Reader reader = Files.newBufferedReader(path);
                CSVParser parser = CsvSupport.parser(reader)) {
            for (CSVRecord record : parser) {
                Anime anime = parse(record);
                insertTitles(trie, anime);
                animeSet.add(anime);
            }
        }

        return animeSet.toArray();
    }

    private Anime parse(CSVRecord record) {
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
        return value == null || value.isBlank() ? null : Integer.parseInt(value.trim());
    }

    private Double parseDouble(CSVRecord record, String column) {
        String value = record.get(column);
        return value == null || value.isBlank() ? null : Double.parseDouble(value.trim());
    }

    private LocalDate parseDate(CSVRecord record, String column) {
        String value = record.get(column);
        if (value == null || value.isBlank()) {
            return null;
        }
        value = value.trim();
        if (value.length() == 7) {
            value += "-01";
        } else if (value.length() == 4) {
            value += "-01-01";
        }
        return LocalDate.parse(value);
    }

    private int[] parseIds(String value) {
        if (value == null || value.isBlank()) {
            return new int[0];
        }

        String[] values = value.split(";");
        int[] ids = new int[values.length];
        int count = 0;
        for (String item : values) {
            if (!item.isBlank()) {
                ids[count++] = Integer.parseInt(item.trim());
            }
        }

        int[] result = new int[count];
        System.arraycopy(ids, 0, result, 0, count);
        return result;
    }
}
