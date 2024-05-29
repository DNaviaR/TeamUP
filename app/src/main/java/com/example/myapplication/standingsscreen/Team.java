package com.example.myapplication.standingsscreen;

public class Team {
    private String name;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private String goalDifference;
    private int points;

    public Team(String name, int played, int wins, int draws, int losses, String goalDifference, int points) {
        this.name = name;
        this.played = played;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalDifference = goalDifference;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPlayed() {
        return played;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public String getGoalDifference() {
        return goalDifference;
    }

    public int getPoints() {
        return points;
    }
}

