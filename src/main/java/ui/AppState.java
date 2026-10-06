package ui;

import lombok.Getter;

@Getter
public class AppState {
    private static final int TOP_ANIME_COUNT = 10;

    private int activeTab;
    private int menuSelection;
    private int topAnimePage;
    private int searchPage;
    private String searchQuery = "";
    private boolean searchSubmitted;

    protected void handleKeyPress(String key, int animeCount, int searchCount) {
        switch (key) {
            case "up" -> menuSelection = 0;
            case "down" -> menuSelection = 1;

            case "left" -> previousPage();
            case "right" -> nextPage(animeCount, searchCount);

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
        searchPage = 0;
    }

    protected void removeSearchCharacter() {
        if (!searchQuery.isEmpty()) {
            searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
            searchSubmitted = false;
            searchPage = 0;
        }
    }

    private void previousPage() {
        if (activeTab == 0 && topAnimePage > 0) {
            topAnimePage--;
        } 
        
        if (activeTab == 1 && searchPage > 0) {
            searchPage--;
        }
    }

    private void nextPage(int animeCount, int searchCount) {
        if (activeTab == 0 && (topAnimePage + 1) * TOP_ANIME_COUNT < animeCount) {
            topAnimePage++;
        } 
        
        if (activeTab == 1 && (searchPage + 1) * SearchAnimeView.PAGE_SIZE < searchCount) {
            searchPage++;
        }
    }
}
