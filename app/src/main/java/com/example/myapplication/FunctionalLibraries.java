package com.example.myapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FunctionalLibraries {

    public ArrayList<Restaurant> binarySort(ArrayList<Restaurant> array, String sortingElement) {
        switch (sortingElement) {
            case "restaurant":
                for (int i = 1; i < array.size(); i++) {
                    Restaurant x = array.get(i);

                    // Perform binary search based on restaurant name
                    int j = Math.abs(
                            Collections.binarySearch(array.subList(0, i), x, Comparator.comparing(Restaurant::getName, String::compareToIgnoreCase))
                    );

                    // Insert the element at the found position
                    array.add(j, x);
                    array.remove(i + 1);
                }
                break;

            case "medal":
                for (int i = 1; i < array.size(); i++) {
                    Restaurant x = array.get(i);

                    // Perform binary search based on gold, silver, and bronze medals
                    int j = Math.abs(
                            Collections.binarySearch(array.subList(0, i), x, (r1, r2) -> {
                                int compareGold = Integer.compare(r2.getGoldMedalNo(), r1.getGoldMedalNo());
                                if (compareGold != 0) return compareGold;

                                int compareSilver = Integer.compare(r2.getSilverMedalNo(), r1.getSilverMedalNo());
                                if (compareSilver != 0) return compareSilver;

                                return Integer.compare(r2.getBronzeMedalNo(), r1.getBronzeMedalNo());
                            })
                    );

                    // Insert the element at the found position
                    array.add(j, x);
                    array.remove(i + 1);
                }
                break;

            // Add more cases here for other sorting elements like "Rating" if needed
        }
        return array;
    }

    public void insertionSort(){
        // Implement the insertion sort logic here if needed
    }
}
