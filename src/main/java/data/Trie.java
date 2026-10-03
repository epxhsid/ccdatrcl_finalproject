package data;

import model.Anime;

    
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
    public static class Node {
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
     * Searches for an exact key in the trie.
     * 
     * <p>
     * The search key is normalized by trimming leading/trailing whitespace
     * and converting characters to lowercase.
     * <p>
     * Returns all Anime objects associated with the exact key.
     * If the key does not exist, an empty array is returned.
     * <p><b>Time Complexity:</b> O(n + k), where n is the length of the
     * search key and k is the number of Anime objects associated with
     * that key.
     * @param key : The string key to search for in the trie.
     * @return
     * An array of Anime objects associated with the key, 
     * or an empty array if the key does not exist.
     */
    public Anime[] exactSearch(String key) {
        if (key == null || key.isBlank()) {
            return new Anime[0];
        }

        String normalized = normalize(key);
        Node current = root;

        for (int i = 0; i < normalized.length(); i++) {
            char character = normalized.charAt(i);

            Node child = findChild(current, character);

            if (child == null) {
                return new Anime[0];
            }

            current = child;
        }

        if (!current.isEndOfWord || current.animeCount == 0) {
            return new Anime[0];
        }

        Anime[] results = new Anime[current.animeCount];
        System.arraycopy(current.anime, 0, results, 0, current.animeCount);

        return results;
    }

    /**
     * Searches for all Anime objects associated with keys that start with the given 
     * prefix.
     * <p>
     * The prefix is normalized by trimming leading/trailing whitespace and converting 
     * characters to lowercase.
     * <p>
     * Returns all Anime objects associated with keys that start with the prefix.
     * If no keys start with the prefix, an empty array is returned.
     * <p><b>Time Complexity:</b> O(n + k), where n is the length of the prefix and k 
     * is the number of Anime objects associated with keys that start with the prefix.
     * @param prefix : The string prefix to search for in the trie.
     * @return 
     * array of Anime objects related with keys that start with the prefix,
     * or an empty array if no keys start with the prefix.
     */
    public Anime[] prefixSearch(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return new Anime[0];
        }

        String normalized = normalize(prefix);
        Node current = root;

        // retrieves the node corresponding to the last character of the prefix.
        // e.g. if prefix input is "G", it will traverse the trie to find the node 
        // representing 'g'.
        for (int i = 0; i < normalized.length(); i++) {
            char character = normalized.charAt(i);

            Node child = findChild(current, character);

            if (child == null) {
                return new Anime[0];
            }

            current = child;
        }
        
        int[] count = new int[1];

        return collectAnime(current, count);
    }
    
    /**
     * Iterative depth-first traversal of the trie starting from the given node.
     * So it means it uses a LIFO (Last In First Out) stack to explore the nodes.
     * <p>
     * This method collects all Anime objects associated with the nodes in the
     * subtree rooted at the specified startNode. It uses a stack to perform an
     * iterative depth-first traversal, so that all child nodes are visited.
     * <p>
     * Example: If the startNode represents the character 'a' and has child nodes 
     * for 'b' and 'c', and those child nodes have their own children, the method 
     * will traverse through all of them and collect any Anime objects associated 
     * with the nodes in that subtree.
     * <p>
     * @param startNode 
     * : The node from which to start the depth-first traversal.
     * @param outCount 
     * : An array of size 1 to hold the count of Anime objects collected during the 
     * traversal.
     * @return 
     * An array of Anime objects collected from the subtree rooted at startNode. 
     * If no Anime objects are found, an empty array is returned.
     */
    private Anime[] collectAnime(Node startNode, int[] outCount) {
        if (startNode == null) {
            return new Anime[0];
        }

        Anime[] results = new Anime[10];
        int count = 0;

        Node[] stack = new Node[128];
        int top = 0;

        stack[top++] = startNode;

        while (top > 0) {
            Node current = stack[--top];

            // checks if the current node is the end of a word 
            // and has associated Anime objects. 
            // If so, it adds those Anime objects to the results array.
            if (current.isEndOfWord && current.animeCount > 0) {
                if (count + current.animeCount > results.length) {
                    Anime[] expanded = new Anime[(count + current.animeCount) * 2];
                    System.arraycopy(results, 0, expanded, 0, count);
                    results = expanded;
                }

                System.arraycopy(current.anime, 0, results, count, current.animeCount);
                count += current.animeCount;
            }

            // For instance, if the current node at `Node current = stack[--top];`
            // represents the character 'g', it will append ["i", "o", "u"] to the stack, 
            // because the current node has children nodes for 'i', 'o', and 'u'.
            // and this example assumes that the dataset has 
            // ["Gintama", "Gurren Lagann", "Goblin Slayer"] right now
            Node child = current.firstChild;

            while (child != null) {
                // similar to a dynamic resizing array this code block expands 
                // the stack if the number of nodes to be explored exceeds the 
                // current stack size. 
                if (top >= stack.length) {
                    Node[] expandedStack = new Node[stack.length * 2];
                    System.arraycopy(stack, 0, expandedStack, 0, stack.length);
                    stack = expandedStack;
                }

                stack[top++] = child; // assume that first will be appended is "i"
                child = child.nextSibling; // assume that next will be appended is "o"

                // then it would loop again, and the next child would be "o"
                // eventually will reach "u" until it reaches null,
            }   
        }

        outCount[0] = count;

        // creates a new array of Anime objects with the exact count of collected 
        // Anime objects, and copies the collected Anime objects from the results 
        // array to the trimmed array.
        Anime[] trimmed = new Anime[count];
        System.arraycopy(results, 0, trimmed, 0, count);

        return trimmed;
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
        if (key == null || key.isBlank() || anime == null) {
            return;
        }

        String normalized = normalize(key);
        Node current = root;

        // Iterate through each character in the normalized key and traverse or create nodes in the 
        // trie accordingly. Example: If the normalized key is "gintama", the loop will iterate 
        // through each character, for instance, ('g', 'i', 'n', 't', 'a', 'm', 'a') and either find 
        // existing child nodes or create new ones as needed. The final node corresponding to the 
        // last character will be marked as the end of a word and the anime object will be 
        // associated with it.
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
     * Example: If the parent node represents the character 'a' and we want to add a 
     * child node for the character 'b', this function will create a new node for 'b', 
     * set it as the first child of the parent node, and return the newly created child 
     * node. But if a already has a child node for 'b', this function will not be called, 
     * and the existing child node will be used instead.
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
