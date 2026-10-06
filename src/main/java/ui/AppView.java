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

    public static String render(AppState state, Anime[] rankedAnime) {
        String tabs = renderTabs(state.getMenuSelection());
        String content = renderContent(state, rankedAnime);

        String tabsBox = BOX_STYLE.render(tabs);
        String contentBox = BOX_STYLE.render(content);

        return Join.joinHorizontal(
                Position.Top,
                tabsBox,
                contentBox
        );
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

    private static String renderContent(AppState state, Anime[] rankedAnime) {
        return switch (state.getActiveTab()) {
            case 0 -> TopAnimeView.render(
                    rankedAnime,
                    state.getTopAnimePage()
            );

            case 1 -> "Search Anime";
            default -> "";
        };
    }
}
