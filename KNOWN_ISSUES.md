# Known Issues

This file lists known bugs, limitations, and incomplete features in the project.

## Current Issues

```
1. Implement anime result deduplication

Search results can currently contain the same Anime multiple times when
different indexed titles resolve to the same Anime.

Example:
Searching "Gintama" currently returns:
918 - Gintama
918 - Gintama

Implement a custom HashSet to prevent duplicate Anime objects from
appearing in search results.

Requirements:
- Implement the hash set from scratch.
- Use the Anime ID as the uniqueness key.
- Do not use java.util.HashSet.
- Preserve the Anime object associated with the ID.
- Integrate deduplication into the search result pipeline.
- Write tests using JUnit5, and add it to src/test/java/data/AnimeHashSetTest.java test class
- Make sure to test using ./mvnw clean test (.\mvnw.cmd for Windows) and manually test it aswell.
```

## Notes

This file is intended to keep important issues visible to the team without requiring everyone to check the GitHub Issues tab.
