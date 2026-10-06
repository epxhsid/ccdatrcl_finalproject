package ui;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;

import data.Sorter;
import model.Anime;

public class App implements Model {
    private final AppState state;
    private final Anime[] rankedAnime;

    public App(Anime[] animeList) {
        this.rankedAnime = Sorter.sortByRank(animeList);
        this.state = new AppState();
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<App> update(Message message) {
        if (message instanceof KeyPressMessage key) {
            state.handleKeyPress(key.key(), rankedAnime.length);
        }

        return new UpdateResult<>(this, null);
    }

    @Override
    public String view() {
        return AppView.render(state, rankedAnime);
    }
}
