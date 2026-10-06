package data;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import model.Genre;
import model.Studio;

/**
 * Parses the genre and studio reference CSV files.
 */
final class ReferenceDataLoader {
    Genre[] loadGenres(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path);
                CSVParser parser = CsvSupport.parser(reader)) {
            Genre[] genres = new Genre[16];
            int count = 0;

            for (CSVRecord record : parser) {
                if (count == genres.length) {
                    genres = grow(genres);
                }
                genres[count++] = new Genre(
                        Integer.parseInt(record.get("id").trim()),
                        record.get("name"));
            }
            return copy(genres, count);
        }
    }

    Studio[] loadStudios(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path);
                CSVParser parser = CsvSupport.parser(reader)) {
            Studio[] studios = new Studio[16];
            int count = 0;

            for (CSVRecord record : parser) {
                if (count == studios.length) {
                    studios = grow(studios);
                }
                studios[count++] = new Studio(
                        Integer.parseInt(record.get("id").trim()),
                        record.get("name"));
            }
            return copy(studios, count);
        }
    }

    private Genre[] grow(Genre[] genres) {
        Genre[] grown = new Genre[genres.length * 2];
        System.arraycopy(genres, 0, grown, 0, genres.length);
        return grown;
    }

    private Studio[] grow(Studio[] studios) {
        Studio[] grown = new Studio[studios.length * 2];
        System.arraycopy(studios, 0, grown, 0, studios.length);
        return grown;
    }

    private Genre[] copy(Genre[] genres, int length) {
        Genre[] result = new Genre[length];
        System.arraycopy(genres, 0, result, 0, length);
        return result;
    }

    private Studio[] copy(Studio[] studios, int length) {
        Studio[] result = new Studio[length];
        System.arraycopy(studios, 0, result, 0, length);
        return result;
    }
}
