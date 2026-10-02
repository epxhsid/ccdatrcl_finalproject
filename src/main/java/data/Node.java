package data;

import model.Anime;

@SuppressWarnings("all")
public class Node {
    private final Node[] children;
    private boolean isEndOfWord;
    private Anime[] values;
    private int valueCount;

    private Node(int alphabetSize) {
        this.children = new Node[alphabetSize];
        this.isEndOfWord = false;
        this.valueCount = 0;
        this.values = new Anime[2];
    }
}
