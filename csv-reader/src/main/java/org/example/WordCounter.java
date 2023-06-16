package org.example;

import java.io.*;
import java.util.*;

public class WordCounter {

    private final StringBuilder word = new StringBuilder();
    private final Map<String, Long> wordFrequencyMap = new HashMap<>();
    private long totalCounter = 0;

    public long getTotalCounter() {
        return totalCounter;
    }

    void countWord() {
        String completedWord = word.toString();
        if (completedWord.isEmpty()) {
            return;
        }
        word.delete(0, word.length());
        Long counter = wordFrequencyMap.get(completedWord);
        if (counter == null) {
            wordFrequencyMap.put(completedWord, 1L);
        } else {
            wordFrequencyMap.replace(completedWord, counter + 1);
        }
        totalCounter++;
    }

    Map<String, Long> countWordsInFile(String fileName) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(fileName))) {
            int character;
            while ((character = reader.read()) != -1) {
                if (Character.isLetterOrDigit(character)) {
                    word.append((char) character);
                } else {
                    countWord();
                }
            }
            countWord();
        }
        return wordFrequencyMap;
    }


    public static void main(String[] args) {

        WordCounter wordCounter = new WordCounter();
        Map<String, Long> wordFrequencyMap;
        try {
            wordFrequencyMap = wordCounter.countWordsInFile(args[0]);
        } catch (IOException e) {
            System.out.println("Unable to read file");
            return;
        }

        List<Map.Entry<String, Long>> entryList = new ArrayList<>(wordFrequencyMap.entrySet());
        entryList.sort(Comparator.comparingLong(Map.Entry::getValue));

        try (Writer writer = new BufferedWriter(new FileWriter("output.csv"))) {
            for (var entry : entryList) {
                writer.write(entry.getKey());
                writer.write(',');
                writer.write(entry.getValue().toString());
                writer.write(',');
                writer.write(((Double) ((double) entry.getValue() * 100 / wordCounter.getTotalCounter())).toString());
                writer.write("%\n");
            }
        } catch (IOException e) {
            System.err.println("Error while writing into file: " + e.getLocalizedMessage());
        }
    }
}