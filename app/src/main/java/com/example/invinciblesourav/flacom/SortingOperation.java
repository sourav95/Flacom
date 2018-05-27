package com.example.invinciblesourav.flacom;

import static java.util.Arrays.sort;

public class SortingOperation {
    private String uid[];
    private String fees[];
    private String rating[];

    public SortingOperation(String[] uid, String[] fees, String[] rating) {
        this.uid = uid;
        this.fees = fees;
        this.rating = rating;


    }


    public String[] sortFeesAsc() {
        int[] feesChanged = new int[fees.length];
        String newUid[] = new String[uid.length];
        for (int i = 0; i < feesChanged.length; i++) {
            feesChanged[i] = Integer.parseInt(fees[i]);
        }
        sort(feesChanged);
        for (int i = 0; i < fees.length; i++)
            for (int j = 0; j < fees.length; j++) {
                if (Integer.parseInt(fees[i]) == feesChanged[j]) {
                    newUid[j] = uid[i];
                }
            }
        return newUid;


    }

    public String[] sortFeesDesc() {
        int[] feesChanged = new int[fees.length];
        String newUid[] = new String[uid.length];
        for (int i = 0; i < feesChanged.length; i++) {
            feesChanged[i] = Integer.parseInt(fees[i]);
        }
        int temp;
        sort(feesChanged);
        for (int i = 0; i < feesChanged.length / 2; i++) {
            temp = feesChanged[i];
            feesChanged[i] = feesChanged[feesChanged.length - i - 1];
            feesChanged[feesChanged.length - i - 1] = temp;
        }
        for (int i = 0; i < fees.length; i++)
            for (int j = 0; j < fees.length; j++) {
                if (Integer.parseInt(fees[i]) == feesChanged[j]) {
                    newUid[j] = uid[i];
                }
            }
        return newUid;


    }

    public String[] sortRatingAsc() {
        int[] ratingChanged = new int[rating.length];
        String newUid[] = new String[uid.length];
        for (int i = 0; i < ratingChanged.length; i++) {
            ratingChanged[i] = Integer.parseInt(rating[i]);
        }
        sort(ratingChanged);
        for (int i = 0; i < rating.length; i++)
            for (int j = 0; j < ratingChanged.length; j++) {
                if (Integer.parseInt(rating[i]) == ratingChanged[j]) {
                    newUid[j] = uid[i];
                }
            }
        return newUid;


    }

    public String[] sortRatingDesc() {
        int[] ratingChanged = new int[rating.length];
        String newUid[] = new String[uid.length];
        for (int i = 0; i < ratingChanged.length; i++) {
            ratingChanged[i] = Integer.parseInt(rating[i]);
        }
        int temp;
        sort(ratingChanged);
        for (int i = 0; i < ratingChanged.length / 2; i++) {
            temp = ratingChanged[i];
            ratingChanged[i] = ratingChanged[ratingChanged.length - i - 1];
            ratingChanged[ratingChanged.length - i - 1] = temp;
        }
        for (int i = 0; i < rating.length; i++)
            for (int j = 0; j < rating.length; j++) {
                if (Integer.parseInt(rating[i]) == ratingChanged[j]) {
                    newUid[j] = uid[i];
                }
            }
        return newUid;

    }
}
