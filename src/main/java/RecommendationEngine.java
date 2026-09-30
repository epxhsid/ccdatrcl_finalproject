import java.util.ArrayList;

public class RecommendationEngine {

    /*
     * ============================================================
     * LINEAR SEARCH
     * ============================================================
     *
     * Searches for anime whose title contains the user's keyword.
     *
     * Time Complexity:
     * O(n)
     *
     * where n = number of anime in the dataset.
     */

    public static ArrayList<Anime> searchByName(
            ArrayList<Anime> animeList,
            String keyword
    ) {

        ArrayList<Anime> results = new ArrayList<>();

        keyword = keyword.toLowerCase().trim();

        for (Anime anime : animeList) {

            String animeName = anime.getName().toLowerCase();

            if (animeName.contains(keyword)) {
                results.add(anime);
            }
        }

        return results;
    }


    /*
     * ============================================================
     * GENRE SEARCH
     * ============================================================
     *
     * Finds anime containing the selected genre.
     *
     * Time Complexity:
     * O(n)
     */

    public static ArrayList<Anime> searchByGenre(
            ArrayList<Anime> animeList,
            String genre
    ) {

        ArrayList<Anime> results = new ArrayList<>();

        genre = genre.toLowerCase().trim();

        for (Anime anime : animeList) {

            if (anime.getGenre().toLowerCase().contains(genre)) {
                results.add(anime);
            }
        }

        return results;
    }


    /*
     * ============================================================
     * FILTER BY RATING
     * ============================================================
     *
     * Finds anime with a rating equal to or higher than
     * the minimum rating.
     *
     * Time Complexity:
     * O(n)
     */

    public static ArrayList<Anime> filterByRating(
            ArrayList<Anime> animeList,
            double minimumRating
    ) {

        ArrayList<Anime> results = new ArrayList<>();

        for (Anime anime : animeList) {

            if (anime.getRating() >= minimumRating) {
                results.add(anime);
            }
        }

        return results;
    }


    /*
     * ============================================================
     * RECOMMENDATION ALGORITHM
     * ============================================================
     *
     * Gives each anime a score based on:
     *
     * 1. Genre match
     * 2. Minimum rating
     * 3. Preferred type
     *
     * The anime are then sorted according to their score.
     */

    public static ArrayList<Anime> getRecommendations(
            ArrayList<Anime> animeList,
            String preferredGenre,
            String preferredType,
            double minimumRating
    ) {

        ArrayList<AnimeScore> scoredAnime = new ArrayList<>();

        preferredGenre = preferredGenre.toLowerCase().trim();
        preferredType = preferredType.toLowerCase().trim();

        for (Anime anime : animeList) {

            double score = 0;

            // Genre match
            if (!preferredGenre.isEmpty()
                    && anime.getGenre().toLowerCase().contains(preferredGenre)) {

                score += 50;
            }

            // Rating
            if (anime.getRating() >= minimumRating) {

                score += 30;

                // Extra points for higher ratings
                score += anime.getRating();
            }

            // Type match
            if (!preferredType.equals("any")
                    && anime.getType().toLowerCase().equals(preferredType)) {

                score += 20;
            }

            /*
             * Only include anime that meet the minimum rating
             * and genre preference.
             */

            boolean genreMatch =
                    preferredGenre.isEmpty()
                            || anime.getGenre()
                            .toLowerCase()
                            .contains(preferredGenre);

            boolean ratingMatch =
                    anime.getRating() >= minimumRating;

            boolean typeMatch =
                    preferredType.equals("any")
                            || anime.getType()
                            .toLowerCase()
                            .equals(preferredType);

            if (genreMatch && ratingMatch && typeMatch) {

                scoredAnime.add(
                        new AnimeScore(anime, score)
                );
            }
        }

        /*
         * Sort recommendations by score.
         */
        bubbleSortByScore(scoredAnime);

        ArrayList<Anime> recommendations = new ArrayList<>();

        for (AnimeScore item : scoredAnime) {
            recommendations.add(item.getAnime());
        }

        return recommendations;
    }


    /*
     * ============================================================
     * BUBBLE SORT
     * ============================================================
     *
     * Sorts anime according to rating.
     *
     * Highest rating comes first.
     *
     * Time Complexity:
     * O(n²)
     */

    public static void bubbleSortByRating(
            ArrayList<Anime> animeList
    ) {

        int n = animeList.size();

        for (int i = 0; i < n - 1; i++) {

            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {

                if (animeList.get(j).getRating()
                        < animeList.get(j + 1).getRating()) {

                    Anime temporary = animeList.get(j);

                    animeList.set(
                            j,
                            animeList.get(j + 1)
                    );

                    animeList.set(
                            j + 1,
                            temporary
                    );

                    swapped = true;
                }
            }

            /*
             * If nothing was swapped, the list is already sorted.
             */
            if (!swapped) {
                break;
            }
        }
    }


    /*
     * ============================================================
     * BUBBLE SORT BY RECOMMENDATION SCORE
     * ============================================================
     */

    private static void bubbleSortByScore(
            ArrayList<AnimeScore> list
    ) {

        int n = list.size();

        for (int i = 0; i < n - 1; i++) {

            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {

                if (list.get(j).getScore()
                        < list.get(j + 1).getScore()) {

                    AnimeScore temporary = list.get(j);

                    list.set(
                            j,
                            list.get(j + 1)
                    );

                    list.set(
                            j + 1,
                            temporary
                    );

                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }


    /*
     * ============================================================
     * ANIME SCORE CLASS
     * ============================================================
     */

    private static class AnimeScore {

        private Anime anime;
        private double score;

        public AnimeScore(
                Anime anime,
                double score
        ) {

            this.anime = anime;
            this.score = score;
        }

        public Anime getAnime() {
            return anime;
        }

        public double getScore() {
            return score;
        }
    }
}