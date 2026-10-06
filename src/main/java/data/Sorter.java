package data;

import model.Anime;

public class Sorter {

    private Sorter() {
    }

    /**
     * Uses an in-place selection sort to order anime based on rank.
     * If the rank is null, it is considered lower than any non-null rank.
     * Time complexity is O(n^2), which is acceptable for the dataset size.
     * 
     * @param animeList : The list of anime to sort and select from.
     * @return All anime ordered by rank.
     */
    public static Anime[] sortByRank(Anime[] animeList) {
        Anime[] sorted = new Anime[animeList.length];
        System.arraycopy(animeList, 0, sorted, 0, animeList.length);

        for (int i = 0; i < sorted.length - 1; i++) {
            int bestIndex = i;
            for (int j = i + 1; j < sorted.length; j++) {
                if (compareRank(sorted[j], sorted[bestIndex]) < 0) {
                    bestIndex = j;
                }
            }

            swap(sorted, i, bestIndex);
        }

        return sorted;
    }

    /**
     * Compares the rank of two anime objects.
     * If the rank is null, it is considered lower than any non-null rank.
     * 
     * @param first  : The first anime object to compare. (example Frieren at 1)
     * @param second : The second anime object to compare. (example FMAB at 2)
     * @return
     *         Frieren is ranked higher than FMAB, so it returns -1.
     *         If both ranks are null, it returns 0.
     *         If the first rank is null and the second is not, it returns 1.
     */
    public static int compareRank(Anime first, Anime second) {
        if (first.getRank() == null) {
            return second.getRank() == null ? 0 : 1;
        }
        if (second.getRank() == null) {
            return -1;
        }
        return Integer.compare(first.getRank(), second.getRank());
    }

    private static void swap(Anime[] array, int first, int second) {
        Anime temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }
}
