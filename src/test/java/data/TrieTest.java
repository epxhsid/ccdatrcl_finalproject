package data;

import model.Anime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TrieTest {
    private int nextAnimeId = 1;

    @Test
    void insertShouldAcceptValidKey() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        assertDoesNotThrow(() -> trie.insert("Gintama", anime));
        assertDoesNotThrow(() -> trie.insert("Re:Zero", anime));
        assertDoesNotThrow(() -> trie.insert("Mushoku Tensei", anime));
    }

    @Test
    void insertShouldIgnoreNullKey() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        assertDoesNotThrow(() -> trie.insert(null, anime));
    }

    @Test
    void insertShouldIgnoreBlankKey() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        assertDoesNotThrow(() -> trie.insert("", anime));
        assertDoesNotThrow(() -> trie.insert("   ", anime));
    }
    
    @Test 
    void insertShouldNotAcceptEmptyKey() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        assertDoesNotThrow(() -> trie.insert("", anime));
    }

    @Test
    void insertShouldIgnoreNullAnime() {
        Trie trie = new Trie();

        assertDoesNotThrow(() -> trie.insert("Gintama", null));
    }

    @Test
    void insertShouldNormalizeKey() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("  GINTAMA  ", anime);

        Anime[] results = trie.exactSearch("gintama");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void insertShouldSupportJapaneseCharacters() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("銀魂", anime);

        Anime[] results = trie.exactSearch("銀魂");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void insertShouldSupportMultipleAnimeForSameKey() {
        Trie trie = new Trie();

        Anime first = createAnime();
        Anime second = createAnime();

        trie.insert("Gintama", first);
        trie.insert("Gintama", second);

        Anime[] results = trie.exactSearch("Gintama");

        assertEquals(2, results.length);
        assertSame(first, results[0]);
        assertSame(second, results[1]);
    }

    @Test
    void exactSearchShouldFindExactTitle() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("Gintama", anime);

        Anime[] results = trie.exactSearch("Gintama");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void exactSearchShouldBeCaseInsensitive() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("Gintama", anime);

        Anime[] results = trie.exactSearch("gInTaMa");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void exactSearchShouldNotMatchPartialTitle() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("Gintama", anime);

        Anime[] results = trie.exactSearch("Gin");

        assertEquals(0, results.length);
    }

    @Test
    void exactSearchShouldReturnEmptyArrayWhenTitleDoesNotExist() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("Jojo Kimyou na Bouken", anime);

        Anime[] results = trie.exactSearch("The Eminence in Shadow");

        assertEquals(0, results.length);
    }

    @Test
    void exactSearchShouldSupportJapaneseTitle() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("無職転生 〜異世界行ったら本気だす〜", anime);

        Anime[] results = trie.exactSearch(
                "無職転生 〜異世界行ったら本気だす〜"
        );

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void prefixSearchShouldFindMatchingTitles() {
        Trie trie = new Trie();

        Anime gintama = createAnime();
        Anime gintamaMovie = createAnime();
        Anime naruto = createAnime();

        trie.insert("Gintama", gintama);
        trie.insert("Gintama Movie", gintamaMovie);
        trie.insert("Frieren", naruto);

        Anime[] results = trie.prefixSearch("Gin");

        assertEquals(2, results.length);
        assertSame(gintama, results[0]);
        assertSame(gintamaMovie, results[1]);
    }

    @Test
    void startsWithShouldBeCaseInsensitive() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("Gintama", anime);

        Anime[] results = trie.prefixSearch("gIn");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void startsWithShouldSupportJapaneseCharacters() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("銀魂", anime);

        Anime[] results = trie.prefixSearch("銀");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void startsWithShouldReturnEmptyArrayWhenPrefixDoesNotExist() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("Gintama", anime);

        Anime[] results = trie.prefixSearch("Nar");

        assertEquals(0, results.length);
    }

    @Test
    void startsWithShouldFindTitleWhenPrefixIsTheWholeTitle() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        trie.insert("Gintama", anime);

        Anime[] results = trie.prefixSearch("Gintama");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    private Anime createAnime() {
        Anime anime = new Anime();
        anime.setId(nextAnimeId++);
        return anime;
    }
}
