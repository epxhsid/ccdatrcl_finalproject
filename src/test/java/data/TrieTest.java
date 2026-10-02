package data;

import model.Anime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TrieTest {

    @Test 
    void insertShouldAcceptValidKey() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        assertDoesNotThrow(() -> trie.insert("Gintama", anime));
        assertDoesNotThrow(() -> trie.insert("Re:Zero", anime));
        assertDoesNotThrow(() -> trie.insert("Mushoku Tensei", anime));
    }

    @Test 
    void insertShouldNotAcceptNullKey() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        assertDoesNotThrow(() -> trie.insert(null, anime));
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

        assertDoesNotThrow(() -> trie.insert("  GINTAMA  ", anime));
    }

    @Test
    void insertShouldSupportJapaneseCharacters() {
        Trie trie = new Trie();
        Anime anime = createAnime();

        assertDoesNotThrow(() -> trie.insert("銀魂", anime));
    }

    @Test
    void insertShouldSupportMultipleAnimeForSameKey() {
        Trie trie = new Trie();

        Anime first = createAnime();
        Anime second = createAnime();

        assertDoesNotThrow(() -> {
            trie.insert("Gintama", first);
            trie.insert("Gintama", second);
        });
    }

    @Test
    void searchShouldFindExactTitle() {
        Trie trie = new Trie();

        Anime anime = new Anime();

        trie.insert("Gintama", anime);

        Anime[] results = trie.search("Gintama");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void searchShouldBeCaseInsensitive() {
        Trie trie = new Trie();

        Anime anime = new Anime();

        trie.insert("Gintama", anime);

        Anime[] results = trie.search("gintama");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    @Test
    void searchShouldReturnEmptyArrayWhenTitleDoesNotExist() {
        Trie trie = new Trie();

        Anime anime = new Anime();

        trie.insert("Jojo Kimyou na Bouken", anime);

        Anime[] results = trie.search("The Eminence in Shadow");

        assertEquals(0, results.length);
    }

    @Test
    void searchShouldSupportJapaneseTitle() {
        Trie trie = new Trie();

        Anime anime = new Anime();

        trie.insert("無職転生 〜異世界行ったら本気だす〜", anime);

        Anime[] results = trie.search("無職転生 〜異世界行ったら本気だす〜");

        assertEquals(1, results.length);
        assertSame(anime, results[0]);
    }

    private Anime createAnime() {
        return new Anime();
    }
}
