# Contributing

## Requirements

Before contributing, make sure you have:

* **Java 21 JDK**
* **Git or GitHub Desktop**
* No system-wide Maven installation is required. This project uses the **Maven Wrapper**.

Check your Java version:

```bash
java -version
```

It should report Java 21.

## Getting Started

Clone the repository:

```bash
git clone <repository-url> # ether SSH or HTTPS, your choice, or even easier clone it via GitHub Desktop
cd <repository-directory> 
```

Verify the project builds and tests pass:

### Linux / macOS

```bash
./mvnw clean test
```

### Windows

```powershell
.\mvnw.cmd clean test
```

If the tests pass, the project is ready for development.

## Development

Use the Maven Wrapper instead of installing Maven globally.

### Compile

```bash
./mvnw compile # Linux / macOS
.\mvnw.cmd compile # Windows
```

### Run tests

```bash
./mvnw test # Linux / macOS
.\mvnw.cmd test # Windows
```

### Clean and test

```bash
./mvnw clean test # Linux / macOS
.\mvnw.cmd clean test # Windows
```

## Git Workflow

Create a branch for your work.

```bash
git switch -c <type>/<short-description>
```

Examples:

```text
feature/anime-search
fix/csv-parser
refactor/trie
test/anime-repository
```

Do not commit directly to `master`, It won't work.

Before opening a pull request, make sure:

```bash
./mvnw clean test
```

passes locally.

## Pull Requests

Pull requests should:

* Have a clear title describing the change.
* Explain what was changed and why.
* Include tests for new or changed behavior where appropriate.
* Pass all required CI checks.
* Resolve all review conversations before merging.

Pull requests are merged using **squash merge**.

Do not force-push shared branches.

## Code Guidelines

* Target **Java 21**.
* Follow the existing project structure and naming conventions.
* Keep classes focused on one responsibility.
* Avoid adding dependencies unless they are actually needed.
* Do not commit generated build files or IDE-specific files.
* Implement required data structures within the project rather than replacing them with equivalent library implementations when the assignment requires a custom implementation.

## Before Submitting a PR

Run:

```bash
./mvnw clean test
```

Then check:

```bash
git status
git diff
```

Make sure there are no accidental files, credentials, generated artifacts, or unrelated changes.

## CI

GitHub Actions runs the project's automated checks on pull requests.
A pull request must pass the required CI checks before it can be merged.
