package ui;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;

import data.Sorter;
import data.Trie;
import model.Anime;

public class App implements Model {
    private final AppState state;
    private final Anime[] rankedAnime;
    private final Trie trie;
    private Anime[] searchResults = new Anime[0];

    public App(Anime[] animeList, Trie trie) {
        this.rankedAnime = Sorter.sortByRank(animeList);
        this.trie = trie;
        this.state = new AppState();
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<App> update(Message message) {
        if (!(message instanceof KeyPressMessage key)) {
            return new UpdateResult<>(this, null);
        }

        String keyName = key.key();

        if (state.getActiveTab() != 1 || isNavigationKey(keyName)) {
            state.handleKeyPress(keyName, rankedAnime.length);
            return new UpdateResult<>(this, null);
        }

        if (keyName.equals("backspace")) {
            state.removeSearchCharacter();
            return new UpdateResult<>(this, null);
        }

        if (keyName.equals("enter")) {
            if (state.getMenuSelection() == 1) {
                searchResults = trie.prefixSearch(state.getSearchQuery());
            }

            state.handleKeyPress(keyName, rankedAnime.length);
            return new UpdateResult<>(this, null);
        }

        for (char character : key.runes()) {
            state.appendSearchCharacter(character);
        }

        return new UpdateResult<>(this, null);
    }

    @Override
    public String view() {
        return AppView.render(state, rankedAnime, searchResults);
    }

    private boolean isNavigationKey(String key) {
        return key.equals("up")
                || key.equals("down")
                || key.equals("left")
                || key.equals("right")
                || key.equals("tab")
                || key.equals("shift+tab");
    }
}
