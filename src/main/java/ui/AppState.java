package ui;

import lombok.Getter;

@Getter
public class AppState {
    private static final int TOP_ANIME_COUNT = 10;

    private int activeTab;
    private int menuSelection;
    private int topAnimePage;
    private String searchQuery = "";
    private boolean searchSubmitted;

    protected void handleKeyPress(String key, int animeCount) {
        switch (key) {
            case "up" -> menuSelection = 0;
            case "down" -> menuSelection = 1;

            case "left" -> previousPage();
            case "right" -> nextPage(animeCount);

            case "tab" -> menuSelection = 1;
            case "shift+tab" -> menuSelection = 0;
            case "enter" -> {
                if (activeTab == menuSelection && activeTab == 1) {
                    searchSubmitted = true;
                } else {
                    activeTab = menuSelection;
                }
            }
        }
    }

    protected void appendSearchCharacter(char character) {
        searchQuery += character;
        searchSubmitted = false;
    }

    protected void removeSearchCharacter() {
        if (!searchQuery.isEmpty()) {
            searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
            searchSubmitted = false;
        }
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public boolean isSearchSubmitted() {
        return searchSubmitted;
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
