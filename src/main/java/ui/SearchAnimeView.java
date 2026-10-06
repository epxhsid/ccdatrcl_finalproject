package ui;

import model.Anime;
import model.Genre;
import model.Studio;

public final class SearchAnimeView {
    public static final int PAGE_SIZE = 5;

    private SearchAnimeView() {
    }

    public static String render(
            AppState state,
            Anime[] searchResults,
            Genre[] genres,
            Studio[] studios) {
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

        if (state.isSearchDetailOpen()) {
            return renderDetails(
                    searchResults[state.getSearchSelection()],
                    genres,
                    studios);
        }

        int start = state.getSearchPage() * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, searchResults.length);
        content.append(String.format(
                "Results (%d-%d)%n%n",
                start + 1,
                end));

        for (int i = start; i < end; i++) {
            String marker = i == state.getSearchSelection() ? "> " : "  ";
            appendAnime(content, searchResults[i], marker);
        }

        return content.toString().stripTrailing();
    }

    private static void appendAnime(StringBuilder content, Anime anime, String marker) {
        content.append(String.format(
                "%s#%s  %s%n",
                marker,
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

    private static String renderDetails(
            Anime anime,
            Genre[] genres,
            Studio[] studios) {
        return String.format(
                "Anime Details%n%n#%s  %s%n"
                        + "    MAL score: %s%n"
                        + "    Members: %s%n"
                        + "    Type: %s%n"
                        + "    Aired: %s%n"
                        + "    Genres: %s%n"
                        + "    Studios: %s%n%n"
                        + "Press Escape to return.",
                displayValue(anime.getRank()),
                anime.getTitle(),
                displayValue(anime.getMean()),
                displayValue(anime.getNumListUsers()),
                displayValue(anime.getMediaType()),
                formatDateRange(anime),
                namesForIds(anime.getGenreIds(), genres),
                namesForIds(anime.getStudioIds(), studios));
    }

    private static String namesForIds(int[] ids, Genre[] genres) {
        StringBuilder names = new StringBuilder();
        for (int id : ids) {
            for (Genre genre : genres) {
                if (genre.getId() == id) {
                    appendName(names, genre.getName());
                    break;
                }
            }
        }
        return names.length() == 0 ? "-" : names.toString();
    }

    private static String namesForIds(int[] ids, Studio[] studios) {
        StringBuilder names = new StringBuilder();
        for (int id : ids) {
            for (Studio studio : studios) {
                if (studio.getId() == id) {
                    appendName(names, studio.getName());
                    break;
                }
            }
        }
        return names.length() == 0 ? "-" : names.toString();
    }

    private static void appendName(StringBuilder names, String name) {
        if (names.length() > 0) {
            names.append(", ");
        }
        names.append(name);
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
