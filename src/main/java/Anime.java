public class Anime {

    private int id;
    private String name;
    private String genre;
    private String type;
    private int episodes;
    private double rating;
    private int members;

    public Anime(
            int id,
            String name,
            String genre,
            String type,
            int episodes,
            double rating,
            int members
    ) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.type = type;
        this.episodes = episodes;
        this.rating = rating;
        this.members = members;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getType() {
        return type;
    }

    public int getEpisodes() {
        return episodes;
    }

    public double getRating() {
        return rating;
    }

    public int getMembers() {
        return members;
    }

    // Display anime information

    public void display() {

        System.out.println("----------------------------------------");
        System.out.println("Title    : " + name);
        System.out.println("Genre    : " + genre);
        System.out.println("Type     : " + type);
        System.out.println("Episodes : " + episodes);
        System.out.println("Rating   : " + String.format("%.2f", rating));
        System.out.println("Members  : " + members);
        System.out.println("----------------------------------------");
    }
}