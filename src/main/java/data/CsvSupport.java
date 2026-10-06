package data;

import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import lombok.NoArgsConstructor;

@NoArgsConstructor 
final class CsvSupport {

    static CSVParser parser(Reader reader) throws java.io.IOException {
        return CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .get()
                .parse(reader);
    }
}
