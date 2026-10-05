package ui;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;

import data.Trie;
import model.Anime;

public class App implements Model {
    private static final int TOP_ANIME_COUNT = 10;

    private final Trie trie;
    private final Anime[] topAnime;

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
        this.topAnime = selectTopAnime(animeList);
    }

    /**
     * Uses an in-place selection sort to find the top anime based on rank.
     * If the rank is null, it is considered lower than any non-null rank.
     * @param animeList : The list of anime to sort and select from.
     * @return The top anime based on their rank.
     */
    private Anime[] selectTopAnime(Anime[] animeList) {
        int count = Math.min(TOP_ANIME_COUNT, animeList.length);
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

        Anime[] top = new Anime[count];
        System.arraycopy(sorted, 0, top, 0, count);
        return top;
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

    @Override public UpdateResult<App> update(Message message) { 
        return new UpdateResult<>(this, null); 
    }

    @Override
    public String view() {
        StringBuilder view = new StringBuilder("Top Anime\n");

        for (Anime anime : topAnime) {
            String rank = anime.getRank() == null ? "-" : anime.getRank().toString();
            view.append(String.format("#%s  %s%n", rank, anime.getTitle()));
        }

        return boxStyle.render(view.toString().stripTrailing());
    }
}
