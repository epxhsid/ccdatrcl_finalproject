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

    /**
     * Loads the genre reference CSV file.
     *
     * @param path the path to the genres CSV
     * @return an array of Genre objects
     * @throws IOException if the CSV cannot be read
     */
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

    /**
     * Loads the studio reference CSV file.
     *
     * @param path the path to the studios CSV
     * @return an array of Studio objects
     * @throws IOException if the CSV cannot be read
     */
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

    /**
     * Vector/Dynamic array growth strategy for genres.
     * only used internally to grow the array when it is full.
     * 
     * @param genres the array of genres to grow
     * @return returns a new array with double the size of the input array,
     *         containing the same elements.
     */
    private Genre[] grow(Genre[] genres) {
        Genre[] grown = new Genre[genres.length * 2];
        System.arraycopy(genres, 0, grown, 0, genres.length);
        return grown;
    }

    /**
     * Vector/Dynamic array growth strategy for studios.
     * only used internally to grow the array when it is full.
     * 
     * @param studios the array of studios to grow
     * @return returns a new array with double the size of the input array,
     *         containing the same elements.
     */
    private Studio[] grow(Studio[] studios) {
        Studio[] grown = new Studio[studios.length * 2];
        System.arraycopy(studios, 0, grown, 0, studios.length);
        return grown;
    }

    /**
     * Copies the contents of the input array into a new array of the specified
     * length.
     * 
     * @param genres the array of genres to copy
     * @param length the number of elements to copy
     * @return a new array containing the copied elements
     */
    private Genre[] copy(Genre[] genres, int length) {
        Genre[] result = new Genre[length];
        System.arraycopy(genres, 0, result, 0, length);
        return result;
    }

    /**
     * Copies the contents of the input array into a new array of the specified
     * length.
     * 
     * @param studios the array of studios to copy
     * @param length  the number of elements to copy
     * @return a new array containing the copied elements
     */
    private Studio[] copy(Studio[] studios, int length) {
        Studio[] result = new Studio[length];
        System.arraycopy(studios, 0, result, 0, length);
        return result;
    }
}
