package ui;

import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.Join;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;

import model.Anime;

public class AppView {
    private static final Style BOX_STYLE = Style.newStyle()
            .border(StandardBorder.RoundedBorder)
            .padding(1);

    private AppView() {
    }

    public static String render(AppState state, Anime[] rankedAnime, Anime[] searchResults) {
        String tabs = renderTabs(state.getMenuSelection());
        String content = renderContent(state, rankedAnime, searchResults);

        String tabsBox = BOX_STYLE.render(tabs);
        String contentBox = BOX_STYLE.render(content);

        return Join.joinHorizontal(
                Position.Top,
                tabsBox,
                contentBox);
    }

    private static String renderTabs(int selection) {
        String topAnime = selection == 0
                ? "> Top Anime"
                : "  Top Anime";

        String searchAnime = selection == 1
                ? "> Search Anime"
                : "  Search Anime";

        return topAnime + "\n" + searchAnime;
    }

    private static String renderContent(
            AppState state,
            Anime[] rankedAnime,
            Anime[] searchResults) {
        return switch (state.getActiveTab()) {
            case 0 -> TopAnimeView.render(
                    rankedAnime,
                    state.getTopAnimePage());

            case 1 -> renderSearch(state, searchResults);
            default -> "";
        };
    }

    private static String renderSearch(AppState state, Anime[] searchResults) {
        StringBuilder content = new StringBuilder();
        content.append("Search Anime\n\n");
        content.append("Query: ").append(state.getSearchQuery()).append("_\n\n");

        if (!state.isSearchSubmitted()) {
            content.append("Type a title prefix, then press Enter.");
        } else if (searchResults.length == 0) {
            content.append("No anime found.");
        } else {
            for (Anime anime : searchResults) {
                content.append(String.format(
                        "#%s  %s%n",
                        anime.getRank() == null ? "-" : anime.getRank(),
                        anime.getTitle()));
            }
        }

        return content.toString().stripTrailing();
    }
}
