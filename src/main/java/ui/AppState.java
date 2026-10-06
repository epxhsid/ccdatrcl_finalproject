package ui;

import lombok.Getter;

@Getter
public class AppState {
    private static final int TOP_ANIME_COUNT = 10;

    private int activeTab;
    private int menuSelection;
    private int topAnimePage;

    protected void handleKeyPress(String key, int animeCount) {
        switch (key) {
            case "up" -> menuSelection = 0;
            case "down" -> menuSelection = 1;

            case "left" -> previousPage();
            case "right" -> nextPage(animeCount);

            case "enter" -> activeTab = menuSelection;
        }
    }

    private void previousPage() {
        if (topAnimePage > 0) {
            topAnimePage--;
        }
    }

    private void nextPage(int animeCount) {
        if ((topAnimePage + 1) * TOP_ANIME_COUNT < animeCount) {
            topAnimePage++;
        }
    }
}
