package data;

import model.Anime;

/**
 * A simple hash set implementation for Anime objects.
 * Primary implementation of this data structure is from
 * deduplication of anime records when loading the dataset.
 */
public class AnimeHashSet {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Anime[] table;
    private int size;

    public AnimeHashSet() {
        table = new Anime[DEFAULT_CAPACITY];
    }

    public boolean add(Anime anime) {
        if (anime == null) {
            return false;
        }

        if ((size + 1) > table.length * LOAD_FACTOR) {
            resize();
        }

        int index = indexFor(anime.getId(), table.length);
        while (table[index] != null) {
            if (table[index].getId() == anime.getId()) {
                return false;
            }
            index = (index + 1) % table.length;
        }

        table[index] = anime;
        size++;
        return true;
    }

    public int size() {
        return size;
    }

    public Anime[] toArray() {
        Anime[] result = new Anime[size];
        int resultIndex = 0;

        for (Anime anime : table) {
            if (anime != null) {
                result[resultIndex++] = anime;
            }
        }

        return result;
    }

    private void resize() {
        Anime[] oldTable = table;
        table = new Anime[oldTable.length * 2];
        size = 0;

        for (Anime anime : oldTable) {
            if (anime != null) {
                add(anime);
            }
        }
    }

    private int indexFor(int id, int capacity) {
        int hash = id ^ (id >>> 16);
        return (hash & 0x7fffffff) % capacity;
    }
}
