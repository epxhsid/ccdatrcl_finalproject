package ui;

import lombok.Getter;

@Getter
public class AppState {
    private static final int TOP_ANIME_COUNT = 10;

    private int activeTab;
    private int menuSelection;
    private int topAnimePage;
    private int searchPage;
    private int searchSelection;
    private String searchQuery = "";
    private boolean searchSubmitted;
    private boolean searchDetailOpen;

    protected void handleKeyPress(String key, int animeCount, int searchCount) {
        switch (key) {
            case "up" -> {
                if (activeTab == 1 && searchSubmitted && !searchDetailOpen && searchSelection > 0) {
                    searchSelection--;
                    searchPage = searchSelection / SearchAnimeView.PAGE_SIZE;
                } else {
                    menuSelection = 0;
                }
            }
            case "down" -> {
                if (activeTab == 1 && searchSubmitted && !searchDetailOpen
                        && searchSelection + 1 < searchCount) {
                    searchSelection++;
                    searchPage = searchSelection / SearchAnimeView.PAGE_SIZE;
                } else {
                    menuSelection = 1;
                }
            }

            case "left" -> previousPage();
            case "right" -> nextPage(animeCount, searchCount);

            case "tab" -> menuSelection = 1;
            case "shift+tab" -> menuSelection = 0;
            case "enter" -> {
                if (activeTab == 1 && searchSubmitted && searchCount > 0 && !searchDetailOpen) {
                    searchDetailOpen = true;
                } else if (activeTab == menuSelection && activeTab == 1) {
                    searchSubmitted = true;
                    searchSelection = 0;
                    searchPage = 0;
                } else {
                    activeTab = menuSelection;
                }
            }
            case "escape" -> searchDetailOpen = false;
        }
    }

    protected void appendSearchCharacter(char character) {
        searchQuery += character;
        searchSubmitted = false;
        searchPage = 0;
        searchSelection = 0;
        searchDetailOpen = false;
    }

    protected void removeSearchCharacter() {
        if (!searchQuery.isEmpty()) {
            searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
            searchSubmitted = false;
            searchPage = 0;
            searchSelection = 0;
            searchDetailOpen = false;
        }
    }

    private void previousPage() {
        if (activeTab == 0 && topAnimePage > 0) {
            topAnimePage--;
        } 
        
        if (activeTab == 1 && searchPage > 0) {
            searchPage--;
            searchSelection = searchPage * SearchAnimeView.PAGE_SIZE;
        }
    }

    private void nextPage(int animeCount, int searchCount) {
        if (activeTab == 0 && (topAnimePage + 1) * TOP_ANIME_COUNT < animeCount) {
            topAnimePage++;
        } 
        
        if (activeTab == 1 && (searchPage + 1) * SearchAnimeView.PAGE_SIZE < searchCount) {
            searchPage++;
            searchSelection = searchPage * SearchAnimeView.PAGE_SIZE;
        }
    }

    public int getSearchSelection() {
        return searchSelection;
    }

    public boolean isSearchDetailOpen() {
        return searchDetailOpen;
    }
}
