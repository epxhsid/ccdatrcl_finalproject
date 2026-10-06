package data;

import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

final class CsvSupport {
    private CsvSupport() {
    }

    static CSVParser parser(Reader reader) throws java.io.IOException {
        return CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .get()
                .parse(reader);
    }
}
