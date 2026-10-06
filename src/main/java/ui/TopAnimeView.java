package ui;

import com.williamcallahan.tui4j.compat.lipgloss.Join;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Style;

import model.Anime;

public class TopAnimeView {
    private static final int PAGE_SIZE = 10;

    private static final Style COLUMN_STYLE = Style.newStyle()
            .marginRight(4);

    private TopAnimeView() {
    }

    public static String render(Anime[] anime, int page) {
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, anime.length);

        StringBuilder firstColumn = new StringBuilder();
        StringBuilder secondColumn = new StringBuilder();

        int midpoint = start + (end - start + 1) / 2;

        for (int i = start; i < midpoint; i++) {
            appendAnime(firstColumn, anime[i]);
        }

        for (int i = midpoint; i < end; i++) {
            appendAnime(secondColumn, anime[i]);
        }

        String header = String.format(
                "Top Anime (%d-%d)%n%n",
                start + 1,
                end);

        return header + Join.joinHorizontal(
                Position.Top,
                COLUMN_STYLE.render(firstColumn.toString().stripTrailing()),
                secondColumn.toString().stripTrailing());
    }

    private static void appendAnime(StringBuilder content, Anime anime) {
        String rank = anime.getRank() == null
                ? "-"
                : anime.getRank().toString();

        content.append(String.format(
                "#%s  %s%n",
                rank,
                anime.getTitle()));

        content.append(String.format(
                "    MAL score: %s%n"
                        + "    Members: %s%n"
                        + "    Type: %s%n"
                        + "    Aired: %s%n%n",
                displayValue(anime.getMean()),
                displayValue(anime.getNumListUsers()),
                displayValue(anime.getMediaType()),
                formatDateRange(anime)));
    }

    private static String formatDateRange(Anime anime) {
        String startDate = displayValue(anime.getStartDate());
        String endDate = displayValue(anime.getEndDate());

        if (anime.getStartDate() == null && anime.getEndDate() == null) {
            return "-";
        }

        if (anime.getEndDate() == null) {
            return startDate + " to present";
        }

        return startDate + " to " + endDate;
    }

    private static String displayValue(Object value) {
        return value == null ? "-" : value.toString();
    }
}
