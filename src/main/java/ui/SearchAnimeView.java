package ui;

import model.Anime;

public final class SearchAnimeView {
    public static final int PAGE_SIZE = 5;

    private SearchAnimeView() {
    }

    public static String render(
            AppState state,
            Anime[] searchResults) {
        StringBuilder content = new StringBuilder()
                .append("Search Anime\n\n")
                .append("Query: ")
                .append(state.getSearchQuery())
                .append("_\n\n");

        if (!state.isSearchSubmitted()) {
            return content
                    .append("Type a title prefix, then press Enter.")
                    .toString();
        }

        if (searchResults.length == 0) {
            return content
                    .append("No anime found.")
                    .toString();
        }

        int start = state.getSearchPage() * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, searchResults.length);
        content.append(String.format(
                "Results (%d-%d)%n%n",
                start + 1,
                end));

        for (int i = start; i < end; i++) {
            appendAnime(content, searchResults[i]);
        }

        return content.toString().stripTrailing();
    }

    private static void appendAnime(StringBuilder content, Anime anime) {
        content.append(String.format(
                "#%s  %s%n",
                displayValue(anime.getRank()),
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
