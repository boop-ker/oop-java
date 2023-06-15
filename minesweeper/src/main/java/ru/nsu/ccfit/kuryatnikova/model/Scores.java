package ru.nsu.ccfit.kuryatnikova.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Scores implements Serializable {
    public static class ScoreEntry implements Serializable {
        private String name;
        private Long score;

        public ScoreEntry(String name, Long score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public Long getScore() {
            return score;
        }
    }
    private final ArrayList<ScoreEntry> scores = new ArrayList<>();

    public ArrayList<ScoreEntry> getScores() {
        return scores;
    }

    public void addScore(String name, Long score) {
        scores.add(new ScoreEntry(name, score));
        scores.sort(Comparator.comparingLong(ScoreEntry::getScore));
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (var entry : scores) {
            str.append(entry.name ).append(": ").append(entry.score).append("\n");
        }

        return str.toString();
    }
}
