package ui;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Join;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;

import data.Trie;
import model.Anime;

@SuppressWarnings("all")
public class App implements Model {
    private static final int TOP_ANIME_COUNT = 10;


    private final Trie trie;
    private final Anime[] rankedAnime;
    private int topAnimePage;
    private int activeTab;
    private int menuSelection;

    private final Style boxStyle = Style.newStyle()
            .border(StandardBorder.RoundedBorder)
            .padding(1);

    /**
     * Creates a new App instance with the specified search trie and anime list.
     * @param trie The search trie to use for searching anime.
     * @param animeList The list of anime to sort and select from.
     */
    public App(Trie trie, Anime[] animeList) {
        this.trie = trie;
        this.rankedAnime = sortByRank(animeList);
    }

    /**
     * Uses an in-place selection sort to order anime based on rank.
     * If the rank is null, it is considered lower than any non-null rank.
     * Time complexity is O(n^2), which is acceptable for the dataset size.
     * @param animeList : The list of anime to sort and select from.
     * @return All anime ordered by rank.
     */
    private Anime[] sortByRank(Anime[] animeList) {
        Anime[] sorted = new Anime[animeList.length];
        System.arraycopy(animeList, 0, sorted, 0, animeList.length);

        for (int i = 0; i < sorted.length - 1; i++) {
            int bestIndex = i;
            for (int j = i + 1; j < sorted.length; j++) {
                if (compareRank(sorted[j], sorted[bestIndex]) < 0) {
                    bestIndex = j;
                }
            }

            Anime temporary = sorted[i];
            sorted[i] = sorted[bestIndex];
            sorted[bestIndex] = temporary;
        }

        return sorted;
    }

    
    private int compareRank(Anime first, Anime second) {
        if (first.getRank() == null) {
            return second.getRank() == null ? 0 : 1;
        }
        if (second.getRank() == null) {
            return -1;
        }
        return Integer.compare(first.getRank(), second.getRank());
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<App> update(Message message) {
        if (message instanceof KeyPressMessage keyMessage) {
            if (keyMessage.key().equals("left") && activeTab == 0 && topAnimePage > 0) {
                topAnimePage--;
            } else if (keyMessage.key().equals("right")
                    && activeTab == 0
                    && (topAnimePage + 1) * TOP_ANIME_COUNT < rankedAnime.length) {
                topAnimePage++;
            } else if (keyMessage.key().equals("up")) {
                menuSelection = 0;
            } else if (keyMessage.key().equals("down")) {
                menuSelection = 1;
            } else if (keyMessage.key().equals("enter")) {
                activeTab = menuSelection;
            }
        }

        return new UpdateResult<>(this, null); 
    }

    @Override
    public String view() {
        StringBuilder content = new StringBuilder();

        if (activeTab == 0) {
            int start = topAnimePage * TOP_ANIME_COUNT;
            int end = Math.min(start + TOP_ANIME_COUNT, rankedAnime.length);
            content.append(String.format("Top Anime (%d-%d)%n", start + 1, end));

            for (int i = start; i < end; i++) {
                Anime anime = rankedAnime[i];
                String rank = anime.getRank() == null ? "-" : anime.getRank().toString();
                content.append(String.format("#%s  %s%n", rank, anime.getTitle()));
            }
        }

        String topAnimeTab = menuSelection == 0 ? "> Top Anime" : "  Top Anime";
        String searchAnimeTab = menuSelection == 1 ? "> Search Anime" : "  Search Anime";
        String tabs = topAnimeTab + "\n" + searchAnimeTab;

        String contentBox = boxStyle.render(content.toString().stripTrailing());
        String tabsBox = boxStyle.render(tabs);

        return Join.joinHorizontal(Position.Top, contentBox, tabsBox);
    }
}
