package data;

import model.Anime;

@SuppressWarnings("unused")        
public class Trie {

    private final Node root = new Node();

    /**
     * Represents a node in a trie data structure.
     * <p>
     * Each node represents a character in a string and can have multiple 
     * children nodes representing the next characters in the string. 
     * The Node class also contains an array of Anime objects, which are 
     * associated with the string represented by the path from the root 
     * to this node.
     * <p>
     * It is essentially a linked-list like structure where each node 
     * can have a first child and a next sibling, allowing for efficient 
     * traversal of the trie.
     * <p>
     * <b>Time Complexity:</b> O(n) for insertion and search operations, 
     * where n is the length of the string being inserted or searched.
     */
    public class Node {
        private char character;
        private Node firstChild;
        private Node nextSibling;
        private Anime[] anime;
        private int animeCount;
        private boolean isEndOfWord;

        private Node(){}

        private Node(char character) {
            this.character = character;
        }
    }

    /**
     * <p>
     * Inserts a key-value pair into the trie. The key is a string and the value is an Anime object.
     * The key is normalized (trimmed and converted to lowercase) before insertion.
     * <p>
     * If the key is null, empty, or the anime object is null, the method will return without making 
     * any changes to the trie.
     * <p>
     * Example: If the key is "Gintama" and the anime object represents the anime "Gintama", the 
     * method will create nodes for each character in "gintama" (after normalization) and associate 
     * the anime object with the last node.
     * <p>
     * @param key - The string key to be inserted into the trie.
     * @param anime - The Anime object to be associated with the key in the trie.
     */
    public void insert(String key, Anime anime) {
        if (key == null || key.isEmpty() || anime == null) {
            return;
        }

        String normalized = normalize(key);
        Node current = root;

        // Iterate through each character in the normalized key and traverse or create nodes in the trie accordingly.
        // example: If the normalized key is "gintama", the loop will iterate through each character, for instance, 
        // ('g', 'i', 'n', 't', 'a', 'm', 'a') and either find existing child nodes or create new ones as needed.
        // The final node corresponding to the last character will be marked as the end of a word and the anime object 
        // will be associated with it.
        for (int i = 0; i < normalized.length(); i++) {
            char character = normalized.charAt(i);

            Node child = findChild(current, character);

            if (child == null) {
                child = addChild(current, character);
            }

            current = child;
        }

        // Mark the last node as the end of a word and associate the anime object with it.
        current.isEndOfWord = true;
        addAnimeToNode(current, anime);
    }

    /**
     * Finds a child node of the given parent node that matches the specified character.
     * <p>
     * This method traverses the linked list of child nodes starting from the first child
     * of the parent node. If a child node with the specified character is found, it is 
     * returned. If no such child node exists, the method returns null.
     * <p>
     * <p>
     * Example lifecycle: if the parent node has children 'a', 'b', and 'c', and the 
     * character to find is 'b', the method will return the child node representing 'b'. 
     * If the character to find is 'd', the method will return null.
     * <p>
     * @param parent : The parent node whose children or siblings are to be searched. 
     * @param character : The character to match against the child nodes of the parent.
     * @return The child node with the specified character, or null if no such node exists.
     */
    private Node findChild(Node parent, char character) {
        Node child = parent.firstChild;

        while (child != null) {
            if (child.character == character) {
                return child;
            }

            child = child.nextSibling;
        }

        return null;
    }

    /**
     * <p>
     * Adds an Anime object to the specified node in the trie. If the node's anime array is null, 
     * it initializes it with a size of 2. If the array is full, it doubles the size of the array 
     * to accommodate more Anime objects, similarly to a dynamic array or ArrayList in Java. 
     * The Anime object is then added to the node's anime array.
     * <p>
     * Example: If the node currently has an anime array of size 2 and already contains 2 Anime objects,
     * the method will create a new array of size 4, copy the existing Anime objects to the new array, 
     * and then add the new Anime object to the array. 
     * <p>
     * 
     * @param node : The node to which the Anime object will be added.
     * @param anime : The Anime object to be added to the node's anime array.
     */
    private void addAnimeToNode(Node node, Anime anime) {
        if (node.anime == null) {
            node.anime = new Anime[2];
        }

        if (node.animeCount == node.anime.length) {
            Anime[] expanded = new Anime[node.anime.length * 2];

            System.arraycopy(node.anime, 0, expanded, 0, node.anime.length);

            node.anime = expanded;
        }

        node.anime[node.animeCount++] = anime;
    }

    /**
     * <p>
     * This function executes when a new child node needs to be added to a parent node 
     * in the trie. Typically when the function findChild() returns null, indicating 
     * that the character does not exist as a child of the parent node.
     * <p>
     * <p>
     * Example: If the parent node represents the character 'a' and we want to add a 
     * child node for the character 'b', this function will create a new node for 'b', 
     * set it as the first child of the parent node, and return the newly created child 
     * node. But if a already has a child node for 'b', this function will not be called, 
     * and the existing child node will be used instead.
     * <p>
     * <p>
     * Example 2: If the parent node represents the character 'a' and it already has a 
     * child node for 'b', and we want to add a child node for the character 'c', this 
     * function will create a new node for 'c', which is called a sibling of 'b', and 
     * set it as the next sibling of the 'b' node. The 'c' node will then be returned 
     * as the newly created child node.
     * <p>
     * @param parent : The parent node to which the new child node will be added.
     * @param character : The character that the new child node will represent.
     * @return : The newly created child node that has been added to the parent node.
     */
    private Node addChild(Node parent, char character) {
        Node child = new Node(character);

        child.nextSibling = parent.firstChild;
        parent.firstChild = child;

        return child;
    }

    /**
     * <p>
     * This function normalizes a given string by trimming leading and trailing whitespace 
     * and converting all characters to lowercase. It's a guard clause to guarantee that 
     * the string is in a consistent format before being inserted into the trie.
     * <p>
     * <p>
     * e.g. if the input string is "  Hello World  ", the function will return "hello world".
     * <p>
     * @param value : The string to be normalized.
     * @return The normalized string
     * 
     */
    private String normalize(String value) {
        return value.trim().toLowerCase();
    }
}
