package data;

import model.Anime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AnimeHashSetTest {

    @Test
    void addShouldUseAnimeIdAsUniquenessKey() {
        AnimeHashSet set = new AnimeHashSet();
        Anime first = animeWithId(1, "First");
        Anime duplicateId = animeWithId(1, "Duplicate");

        assertTrue(set.add(first));
        assertFalse(set.add(duplicateId));

        assertEquals(1, set.size());
        assertSame(first, set.toArray()[0]);
    }

    @Test
    void addShouldPreserveAnimeWithDifferentIds() {
        AnimeHashSet set = new AnimeHashSet();

        assertTrue(set.add(animeWithId(1, "First")));
        assertTrue(set.add(animeWithId(2, "Second")));

        assertEquals(2, set.size());
    }

    @Test
    void addShouldIgnoreNullAnime() {
        AnimeHashSet set = new AnimeHashSet();

        assertFalse(set.add(null));
        assertEquals(0, set.size());
        assertEquals(0, set.toArray().length);
    }

    @Test
    void shouldResizeWithoutLosingAnime() {
        AnimeHashSet set = new AnimeHashSet();

        for (int id = 0; id < 20; id++) {
            assertTrue(set.add(animeWithId(id, "Anime " + id)));
        }

        assertEquals(20, set.size());
    }

    @Test
    void trieSearchShouldRemoveAnimeWithDuplicateIds() {
        Trie trie = new Trie();
        Anime first = animeWithId(1, "First");
        Anime duplicateId = animeWithId(1, "Duplicate");

        trie.insert("Same title", first);
        trie.insert("Same title extended", duplicateId);

        Anime[] results = trie.prefixSearch("Same");

        assertEquals(1, results.length);
        assertSame(first, results[0]);
    }

    private Anime animeWithId(int id, String title) {
        Anime anime = new Anime();
        anime.setId(id);
        anime.setTitle(title);
        return anime;
    }
}
