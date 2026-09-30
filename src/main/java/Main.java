
import java.io.*;
import java.util.*;

public class Main {

    static ArrayList<Anime> animeList = new ArrayList<>();
    static ArrayList<Anime> watchlist = new ArrayList<>();
    static ArrayList<Anime> recentlyViewed = new ArrayList<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String file = new File("data/anime.csv").exists()
                ? "data/anime.csv"
                : "../../../data/anime.csv";

        loadAnime(file);

        System.out.println("========================================");
        System.out.println("       ANIME RECOMMENDATION ENGINE");
        System.out.println("========================================");

        if (animeList.isEmpty()) {
            System.out.println("No anime data was loaded.");
            System.out.println("Check that data/anime.csv exists.");
            return;
        }

        System.out.println(animeList.size() + " anime loaded.");
        System.out.println();

        while (true) {

            System.out.println("----------------------------------------");
            System.out.println("[1] Search Anime");
            System.out.println("[2] Get Recommendations");
            System.out.println("[3] Similar Anime");
            System.out.println("[4] Top Anime");
            System.out.println("[5] Surprise Me");
            System.out.println("[6] My Watchlist");
            System.out.println("[7] Anime Statistics");
            System.out.println("[8] Recently Viewed");
            System.out.println("[9] Exit");
            System.out.println("----------------------------------------");
            System.out.print("Choice: ");

            String choice = sc.nextLine();

            if (choice.equals("1")) {

                search(sc);

            } else if (choice.equals("2")) {

                recommend(sc);

            } else if (choice.equals("3")) {

                similarAnime(sc);

            } else if (choice.equals("4")) {

                topAnime();

            } else if (choice.equals("5")) {

                surpriseMe();

            } else if (choice.equals("6")) {

                watchlistMenu(sc);

            } else if (choice.equals("7")) {

                statistics();

            } else if (choice.equals("8")) {

                recentlyViewed();

            } else if (choice.equals("9")) {

                System.out.println();
                System.out.println("Thank you for using AnimeRec!");
                break;

            } else {

                System.out.println("Invalid choice.");
            }

            System.out.println();
        }

        sc.close();
    }


    // ========================================
    // LOAD CSV
    // ========================================

    static void loadAnime(String file) {

        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {

            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split(
                        ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"
                );

                if (d.length < 17)
                    continue;

                try {

                    int id =
                            Integer.parseInt(d[0].trim());

                    String title =
                            d[1].trim();

                    double score =
                            d[5].trim().isEmpty()
                                    ? 0
                                    : Double.parseDouble(d[5].trim());

                    int episodes =
                            d[9].trim().isEmpty()
                                    ? 0
                                    : Integer.parseInt(d[9].trim());

                    String type =
                            d[12].trim();

                    String genres =
                            d[16].trim();

                    animeList.add(
                            new Anime(
                                    id,
                                    title,
                                    score,
                                    episodes,
                                    type,
                                    genres
                            )
                    );

                } catch (Exception e) {

                    // Skip invalid rows
                }
            }

        } catch (IOException e) {

            System.out.println("Error reading anime.csv.");
        }
    }


    // ========================================
    // SEARCH - LINEAR SEARCH
    // ========================================

    static void search(Scanner sc) {

        System.out.println();

        System.out.println(
                "============= SEARCH ANIME ============="
        );

        System.out.print("Enter anime title: ");

        String keyword =
                sc.nextLine()
                        .toLowerCase()
                        .trim();

        int count = 0;

        // Linear Search
        for (Anime anime : animeList) {

            if (anime.title
                    .toLowerCase()
                    .contains(keyword)) {

                show(anime);

                addRecent(anime);

                count++;

                if (count == 10)
                    break;
            }
        }

        if (count == 0) {

            System.out.println(
                    "No anime found."
            );
        }
    }


    // ========================================
    // RECOMMENDATIONS
    // ========================================

    static void recommend(Scanner sc) {

        String[] genreNames = {

                "Action",
                "Adventure",
                "Comedy",
                "Drama",
                "Fantasy",
                "Romance",
                "Sci-Fi",
                "Sports",
                "Supernatural",
                "Mystery",
                "Horror",
                "Thriller",
                "Music",
                "Slice of Life",
                "Mecha"
        };

        String[] genreIDs = {

                "1",
                "2",
                "4",
                "8",
                "10",
                "22",
                "24",
                "30",
                "37",
                "7",
                "14",
                "41",
                "19",
                "36",
                "18"
        };


        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "          GET RECOMMENDATIONS"
        );

        System.out.println(
                "========================================"
        );

        System.out.println();

        System.out.println(
                "Choose a genre:"
        );

        System.out.println();


        // Display genre options
        for (int i = 0; i < genreNames.length; i++) {

            System.out.println(
                    "[" + (i + 1) + "] "
                            + genreNames[i]
            );
        }

        System.out.println();

        System.out.println(
                "[0] Back to Main Menu"
        );

        System.out.println();

        System.out.print(
                "Enter your choice: "
        );

        int choice;

        try {

            choice =
                    Integer.parseInt(
                            sc.nextLine().trim()
                    );

        } catch (Exception e) {

            System.out.println(
                    "Invalid choice."
            );

            return;
        }


        // Back to menu
        if (choice == 0) {
            return;
        }


        // Invalid option
        if (
                choice < 1
                        || choice > genreNames.length
        ) {

            System.out.println(
                    "Invalid choice."
            );

            return;
        }


        // Get selected genre
        String selectedGenre =
                genreNames[choice - 1];

        String selectedGenreID =
                genreIDs[choice - 1];


        System.out.println();

        System.out.println(
                "Selected Genre: "
                        + selectedGenre
        );


        // Ask for rating
        System.out.println();

        System.out.print(
                "Minimum rating (0-10): "
        );

        double rating;

        try {

            rating =
                    Double.parseDouble(
                            sc.nextLine().trim()
                    );

        } catch (Exception e) {

            System.out.println(
                    "Invalid rating."
            );

            return;
        }


        if (rating < 0 || rating > 10) {

            System.out.println(
                    "Rating must be between 0 and 10."
            );

            return;
        }


        // Store matching anime
        ArrayList<Anime> results =
                new ArrayList<>();


        // Search through anime list
        // and filter by genre and rating.
        for (Anime anime : animeList) {

            if (
                    hasGenre(
                            anime.genres,
                            selectedGenreID
                    )
                    && anime.score >= rating
            ) {

                results.add(anime);
            }
        }


        // Sort recommendations
        // from highest rating to lowest.
        bubbleSort(results);


        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "          RECOMMENDED ANIME"
        );

        System.out.println(
                "========================================"
        );

        System.out.println();

        System.out.println(
                "Genre: "
                        + selectedGenre
        );

        System.out.println(
                "Minimum Rating: "
                        + rating
        );

        System.out.println();


        if (results.isEmpty()) {

            System.out.println(
                    "No anime matched your preferences."
            );

        } else {

            int limit =
                    Math.min(
                            10,
                            results.size()
                    );


            for (int i = 0; i < limit; i++) {

                System.out.println(
                        "#" + (i + 1)
                );

                show(
                        results.get(i)
                );

                System.out.println();
            }
        }
    }


    // ========================================
    // SIMILAR ANIME
    // ========================================

    static void similarAnime(Scanner sc) {

        System.out.println();

        System.out.println(
                "============ SIMILAR ANIME ============"
        );

        System.out.print(
                "Enter anime title: "
        );

        String keyword =
                sc.nextLine()
                        .toLowerCase()
                        .trim();

        Anime selected = null;


        // Linear Search
        for (Anime anime : animeList) {

            if (anime.title
                    .toLowerCase()
                    .contains(keyword)) {

                selected = anime;
                break;
            }
        }


        if (selected == null) {

            System.out.println(
                    "Anime not found."
            );

            return;
        }


        addRecent(selected);


        ArrayList<Anime> results =
                new ArrayList<>();


        // Find anime with similar genres
        for (Anime anime : animeList) {

            if (anime.id == selected.id)
                continue;

            if (
                    similarity(
                            selected,
                            anime
                    ) > 0
            ) {

                results.add(anime);
            }
        }


        // Sort by number of matching genres
        for (
                int i = 0;
                i < results.size() - 1;
                i++
        ) {

            for (
                    int j = 0;
                    j < results.size() - i - 1;
                    j++
            ) {

                if (
                        similarity(
                                selected,
                                results.get(j)
                        )
                        <
                        similarity(
                                selected,
                                results.get(j + 1)
                        )
                ) {

                    Anime temp =
                            results.get(j);

                    results.set(
                            j,
                            results.get(j + 1)
                    );

                    results.set(
                            j + 1,
                            temp
                    );
                }
            }
        }


        System.out.println();

        System.out.println(
                "Similar to: "
                        + selected.title
        );

        System.out.println();


        if (results.isEmpty()) {

            System.out.println(
                    "No similar anime found."
            );

        } else {

            for (
                    int i = 0;
                    i < Math.min(10, results.size());
                    i++
            ) {

                System.out.println(
                        "#" + (i + 1)
                );

                show(
                        results.get(i)
                );

                System.out.println();
            }
        }
    }


    // ========================================
    // CALCULATE SIMILARITY
    // ========================================

    static int similarity(
            Anime a,
            Anime b
    ) {

        int count = 0;

        String[] aGenres =
                a.genres.split(";");

        String[] bGenres =
                b.genres.split(";");


        for (String x : aGenres) {

            for (String y : bGenres) {

                if (
                        x.trim()
                                .equals(y.trim())
                ) {

                    count++;
                }
            }
        }

        return count;
    }


    // ========================================
    // CHECK GENRE
    // ========================================

    static boolean hasGenre(
            String genres,
            String genreID
    ) {

        String[] ids =
                genres.split(";");


        for (String id : ids) {

            if (
                    id.trim()
                            .equals(genreID)
            ) {

                return true;
            }
        }

        return false;
    }


    // ========================================
    // BUBBLE SORT
    // ========================================

    static void bubbleSort(
            ArrayList<Anime> list
    ) {

        // Bubble Sort
        // Highest rating first.

        for (
                int i = 0;
                i < list.size() - 1;
                i++
        ) {

            for (
                    int j = 0;
                    j < list.size() - i - 1;
                    j++
            ) {

                if (
                        list.get(j).score
                                < list.get(j + 1).score
                ) {

                    Anime temp =
                            list.get(j);

                    list.set(
                            j,
                            list.get(j + 1)
                    );

                    list.set(
                            j + 1,
                            temp
                    );
                }
            }
        }
    }


    // ========================================
    // TOP ANIME
    // ========================================

    static void topAnime() {

        ArrayList<Anime> list =
                new ArrayList<>(animeList);

        bubbleSort(list);

        System.out.println();

        System.out.println(
                "============= TOP ANIME ============="
        );


        for (
                int i = 0;
                i < Math.min(10, list.size());
                i++
        ) {

            System.out.println(
                    "\n#" + (i + 1)
            );

            show(
                    list.get(i)
            );
        }
    }


    // ========================================
    // SURPRISE ME
    // ========================================

    static void surpriseMe() {

        Random random =
                new Random();

        Anime anime =
                animeList.get(
                        random.nextInt(
                                animeList.size()
                        )
                );


        addRecent(anime);


        System.out.println();

        System.out.println(
                "============= SURPRISE ME ============="
        );

        System.out.println(
                "Your random anime is:"
        );

        show(anime);
    }


    // ========================================
    // WATCHLIST MENU
    // ========================================

    static void watchlistMenu(
            Scanner sc
    ) {

        System.out.println();

        System.out.println(
                "============= MY WATCHLIST ============="
        );

        System.out.println(
                "[1] View Watchlist"
        );

        System.out.println(
                "[2] Add Anime"
        );

        System.out.println(
                "[3] Remove Anime"
        );

        System.out.println(
                "[0] Back to Main Menu"
        );

        System.out.print(
                "\nChoice: "
        );

        String choice =
                sc.nextLine();


        if (choice.equals("1")) {

            viewWatchlist();

        } else if (choice.equals("2")) {

            addWatchlist(sc);

        } else if (choice.equals("3")) {

            removeWatchlist(sc);

        } else if (choice.equals("0")) {

            return;

        } else {

            System.out.println(
                    "Invalid choice."
            );
        }
    }


    // ========================================
    // VIEW WATCHLIST
    // ========================================

    static void viewWatchlist() {

        if (watchlist.isEmpty()) {

            System.out.println(
                    "Your watchlist is empty."
            );

            return;
        }


        System.out.println();


        for (
                int i = 0;
                i < watchlist.size();
                i++
        ) {

            System.out.println(
                    "[" + (i + 1) + "] "
                            + watchlist.get(i).title
            );
        }


        System.out.println(
                "\nTotal: "
                        + watchlist.size()
        );
    }


    // ========================================
    // ADD TO WATCHLIST
    // ========================================

    static void addWatchlist(
            Scanner sc
    ) {

        System.out.print(
                "Enter anime title: "
        );

        String keyword =
                sc.nextLine()
                        .toLowerCase()
                        .trim();


        // Linear Search
        for (Anime anime : animeList) {

            if (
                    anime.title
                            .toLowerCase()
                            .contains(keyword)
            ) {

                if (
                        watchlist.contains(anime)
                ) {

                    System.out.println(
                            "Already in your watchlist."
                    );

                } else {

                    watchlist.add(anime);

                    System.out.println(
                            anime.title
                                    + " added to watchlist!"
                    );
                }

                return;
            }
        }


        System.out.println(
                "Anime not found."
        );
    }


    // ========================================
    // REMOVE FROM WATCHLIST
    // ========================================

    static void removeWatchlist(
            Scanner sc
    ) {

        if (watchlist.isEmpty()) {

            System.out.println(
                    "Your watchlist is empty."
            );

            return;
        }


        viewWatchlist();


        System.out.print(
                "Enter number to remove: "
        );


        try {

            int choice =
                    Integer.parseInt(
                            sc.nextLine()
                    );


            if (
                    choice < 1
                            || choice > watchlist.size()
            ) {

                System.out.println(
                        "Invalid choice."
                );

                return;
            }


            Anime removed =
                    watchlist.remove(
                            choice - 1
                    );


            System.out.println(
                    removed.title
                            + " removed from watchlist."
            );


        } catch (Exception e) {

            System.out.println(
                    "Invalid choice."
            );
        }
    }


    // ========================================
    // ANIME STATISTICS
    // ========================================

    static void statistics() {

        double totalScore = 0;

        int ratedAnime = 0;

        Anime highestRated = null;

        HashMap<String, Integer> types =
                new HashMap<>();


        for (Anime anime : animeList) {

            // Calculate average rating
            if (anime.score > 0) {

                totalScore += anime.score;

                ratedAnime++;


                if (
                        highestRated == null
                                || anime.score
                                > highestRated.score
                ) {

                    highestRated = anime;
                }
            }


            // Count anime types
            types.put(
                    anime.type,
                    types.getOrDefault(
                            anime.type,
                            0
                    ) + 1
            );
        }


        System.out.println();

        System.out.println(
                "========== ANIME STATISTICS =========="
        );


        System.out.println(
                "Total Anime: "
                        + animeList.size()
        );


        System.out.printf(
                "Average Rating: %.2f%n",
                ratedAnime == 0
                        ? 0
                        : totalScore / ratedAnime
        );


        System.out.println(
                "Watchlist: "
                        + watchlist.size()
        );


        System.out.println(
                "Recently Viewed: "
                        + recentlyViewed.size()
        );


        if (highestRated != null) {

            System.out.println(
                    "Highest Rated: "
                            + highestRated.title
                            + " ("
                            + highestRated.score
                            + ")"
            );
        }


        System.out.println();

        System.out.println(
                "Anime by Type:"
        );


        for (String type : types.keySet()) {

            System.out.println(
                    "- "
                            + type
                            + ": "
                            + types.get(type)
            );
        }
    }


    // ========================================
    // RECENTLY VIEWED
    // ========================================

    static void recentlyViewed() {

        System.out.println();

        System.out.println(
                "========= RECENTLY VIEWED ========="
        );


        if (recentlyViewed.isEmpty()) {

            System.out.println(
                    "No anime viewed yet."
            );

            return;
        }


        for (
                int i = 0;
                i < recentlyViewed.size();
                i++
        ) {

            System.out.println(
                    "[" + (i + 1) + "] "
                            + recentlyViewed.get(i).title
            );
        }
    }


    // ========================================
    // ADD TO RECENTLY VIEWED
    // ========================================

    static void addRecent(
            Anime anime
    ) {

        recentlyViewed.remove(anime);

        recentlyViewed.add(
                0,
                anime
        );


        // Keep only the latest 10
        if (
                recentlyViewed.size() > 10
        ) {

            recentlyViewed.remove(
                    recentlyViewed.size() - 1
            );
        }
    }


    // ========================================
    // DISPLAY ANIME
    // ========================================

    static void show(
            Anime anime
    ) {

        System.out.println(
                "----------------------------------------"
        );


        System.out.println(
                "Title    : "
                        + anime.title
        );


        System.out.println(
                "Type     : "
                        + anime.type
        );


        System.out.println(
                "Episodes : "
                        + anime.episodes
        );


        System.out.printf(
                "Rating   : %.2f%n",
                anime.score
        );


        System.out.println(
                "Genres   : "
                        + anime.genres
        );


        System.out.println(
                "----------------------------------------"
        );
    }


    // ========================================
    // ANIME CLASS
    // ========================================

    static class Anime {

        int id;

        String title;

        double score;

        int episodes;

        String type;

        String genres;


        Anime(
                int id,
                String title,
                double score,
                int episodes,
                String type,
                String genres
        ) {

            this.id = id;

            this.title = title;

            this.score = score;

            this.episodes = episodes;

            this.type = type;

            this.genres = genres;
        }
    }
}

