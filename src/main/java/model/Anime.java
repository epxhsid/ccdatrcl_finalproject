package model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class Anime {
    private int id;
    private String title;
    private String titleJa;
    private String titleEn;
    private Double mean;
    private Integer rank;
    private Integer numListUsers;
    private Integer numScoringUsers;
    private Integer numEpisodes;
    private LocalDate startDate;
    private LocalDate endDate;
    private String mediaType;
    private String status;
    private String rating;
    private Integer averageEpisodeDuration;
    private int[] genreIds;
    private int[] studioIds;
}