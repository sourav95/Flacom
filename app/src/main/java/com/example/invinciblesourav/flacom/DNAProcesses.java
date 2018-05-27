package com.example.invinciblesourav.flacom;

import java.util.ArrayList;
import java.util.Random;

public class DNAProcesses {
    long row;
    long column;
    long depth;
    public static String residualDNA[];
    String dncal(String pattern)
    {
        String dnaSequence="";
        for(int i=0;i<pattern.length();i=i+2) {
            String t_seqMatch="";
            t_seqMatch=t_seqMatch+ String.valueOf(pattern.charAt(i))+ String.valueOf(pattern.charAt(i+1));
            switch (t_seqMatch) {
                case "00":
                    dnaSequence += "A";
                    break;
                case "01":
                    dnaSequence += "C";
                    break;
                case "10":
                    dnaSequence += "G";
                    break;
                case "11":
                    dnaSequence += "T";
                    break;
            }
        }

        return dnaSequence;
    }
    private long calculatePrimeRange(long length){
        return length/2;
    }
    private ArrayList calculateSquares(long range){
        long sampleExp=2;
        ArrayList<Long> perfectSquares=new ArrayList<Long>();
        //System.out.println();
        while(sampleExp*sampleExp<=range){
            perfectSquares.add(sampleExp*sampleExp);
            //System.out.print(sampleExp*sampleExp+"-");
            sampleExp++;
        }
        return perfectSquares;
    }
    public void calculateDimens(String sequence){
        long range;
        long midPosition;
        double row,column,depth;
        ArrayList<Long> perfectSquares=new ArrayList<Long>();
        range=calculatePrimeRange(Long.parseLong( String.valueOf(sequence.length())));
        perfectSquares=calculateSquares(range);
        //System.out.println("Size="+perfectSquares.size());
        if(sequence.length()%16==0 && !sequence.equals("")) {
            if (perfectSquares.size() == 1)
                midPosition = 0;
            else
                midPosition = perfectSquares.size() / 2 - 1;

            row = Math.sqrt(perfectSquares.get(Integer.parseInt(String.valueOf(midPosition))));
            column = row;
            depth = sequence.length() / (row * column);
            this.row = Long.parseLong(String.valueOf(row).substring(0, String.valueOf(row).indexOf('.')));
            this.column = Long.parseLong(String.valueOf(column).substring(0, String.valueOf(row).indexOf('.')));
            this.depth = Long.parseLong(String.valueOf(depth).substring(0, String.valueOf(row).indexOf('.')));
        }
    }
    public String[][][] generateArray(String dnaseq, int rows, int height){
            String array3D[][][] = new String[height][rows][rows];
            int index = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < rows; j++) {
                    for (int k = 0; k < rows; k++) {
                        array3D[i][j][k] = String.valueOf(dnaseq.charAt(index++));

                    }
                }
            }
            residualDNA = dnaseq.substring(index, dnaseq.length()).split("");
            //System.out.println(dnaseq.substring(index, dnaseq.length()));

            //System.out.println();
            for (int i = 0; i < residualDNA.length; i++) {
              //  System.out.print(residualDNA[i]);
            }
            //System.out.println();

            return array3D;

    }

    public String crossOver(String string1, String string2){
        int length=string1.length()/2;
        String head1="",head2="",tail1="",tail2="";
        int i;

        for(i=0;i<length;i++){
            head1+= String.valueOf(string1.charAt(i));
            head2+= String.valueOf(string2.charAt(i));
        }
        while (i<string1.length()){
            tail1+= String.valueOf(string1.charAt(i));
            tail2+= String.valueOf(string2.charAt(i));
            i++;
        }
        return head1+tail2+"%"+head2+tail1;
    }
    public String[][][] doCrossOver(String array[][][], int rows, int height){
        String temp_holder1="",temp_holder2="",temp_crossRes,temp_crossRes1,temp_crossRes2;
        /*for(int i=0;i<height;i++){
            for(int j=0;j<rows-1;j++){
                temp_holder1="";temp_holder2="";
                for(int k=0;k<rows;k++){
                    temp_holder1+=array[i][j][k];
                    temp_holder2+=array[i][j+1][k];
                }
                temp_crossRes=crossOver(temp_holder1,temp_holder2);
                temp_crossRes1=temp_crossRes.substring(0,temp_crossRes.indexOf('%'));
                temp_crossRes2=temp_crossRes.substring(temp_crossRes.indexOf('%')+1,temp_crossRes.length());
                for(int k=0;k<rows;k++){
                    array[i][j][k]=String.valueOf(temp_crossRes1.charAt(k));
                    array[i][j+1][k]=String.valueOf(temp_crossRes2.charAt(k));
                }
            }
        }*/
        ArrayList<Integer> randomizedIndices=randomizeIndex(rows,height);
        int randomRow,randomHeight,randomRowNext,randomHeightNext;
        for(int i=0;i<randomizedIndices.size();i=i+2){
            String groupedIndex= String.valueOf(randomizedIndices.get(i));
            String groupedIndexNext= String.valueOf(randomizedIndices.get(i+1));
            if(groupedIndex.length()==1){
                randomRow= Integer.parseInt(String.valueOf(groupedIndex.charAt(0)));
                randomHeight=0;
            }else{
                randomRow= Integer.parseInt(String.valueOf(groupedIndex.charAt(1)));
                randomHeight= Integer.parseInt(String.valueOf(groupedIndex.charAt(0)));
            }
            if(groupedIndexNext.length()==1){
                randomRowNext= Integer.parseInt(String.valueOf(groupedIndexNext.charAt(0)));
                randomHeightNext=0;
            }else{
                randomRowNext= Integer.parseInt(String.valueOf(groupedIndexNext.charAt(1)));
                randomHeightNext= Integer.parseInt(String.valueOf(groupedIndexNext.charAt(0)));
            }
            temp_holder1="";temp_holder2="";
            for(int k=0;k<rows;k++){
                temp_holder1+=array[randomHeight][randomRow][k];
                temp_holder2+=array[randomHeightNext][randomRowNext][k];
            }
            temp_crossRes=crossOver(temp_holder1,temp_holder2);
            temp_crossRes1=temp_crossRes.substring(0,temp_crossRes.indexOf('%'));
            temp_crossRes2=temp_crossRes.substring(temp_crossRes.indexOf('%')+1,temp_crossRes.length());
            for(int k=0;k<rows;k++){
                array[randomHeight][randomRow][k]= String.valueOf(temp_crossRes1.charAt(k));
                array[randomHeightNext][randomRowNext][k]= String.valueOf(temp_crossRes2.charAt(k));
            }
        }

        return array;
    }
    public String[][][] mutate(String array[][][], int rows, int height ) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < rows; k++) {
                    switch (array[i][j][k]) {
                        case "A":
                            array[i][j][k] = "G";
                            break;
                        case "C":
                            array[i][j][k] = "T";
                            break;
                        case "G":
                            array[i][j][k] = "A";
                            break;
                        case "T":
                            array[i][j][k] = "C";
                            break;
                    }
                }
            }
        }
        return array;
    }
    public String[][][] doRevCrossOver(String array[][][], int rows, int height, int seqNo){
        String temp_holder1="",temp_holder2="",temp_crossRes,temp_crossRes1,temp_crossRes2;
        /*for(int i=height-1;i>=0;i--){
            for(int j=rows-1;j>=1;j--){
                temp_holder1="";temp_holder2="";
                for(int k=0;k<rows;k++){
                    temp_holder1+=array[i][j][k];
                    temp_holder2+=array[i][j-1][k];
                }
                temp_crossRes=crossOver(temp_holder1,temp_holder2);
                temp_crossRes1=temp_crossRes.substring(0,temp_crossRes.indexOf('%'));
                temp_crossRes2=temp_crossRes.substring(temp_crossRes.indexOf('%')+1,temp_crossRes.length());
                for(int k=0;k<rows;k++){
                    array[i][j][k]=String.valueOf(temp_crossRes1.charAt(k));
                    array[i][j-1][k]=String.valueOf(temp_crossRes2.charAt(k));
                }
            }
        }*/
        ArrayList<Integer> selectionList=new ArrayList<>();


        if(seqNo==0) {
            int startIndex=0;
            int endIndex = startIndex + rows*rows*height;
            for (int i = startIndex; i < endIndex; i++) {
                selectionList.add(StoreKeys.selectionKeys.get(i));
            }

        }else{
            int sum=0;
            for(int k=0;k<seqNo;k++){
                sum+=StoreKeys.selectionPoint.get(k);
            }
            int startIndex=sum;
            int endIndex=startIndex+(int) rows*rows*height;
            //System.out.println("Start "+startIndex+" end "+endIndex);
            for (int i = startIndex; i < endIndex; i++) {
                selectionList.add(StoreKeys.selectionKeys.get(i));
            }

        }
        int randomRow,randomHeight,randomRowNext,randomHeightNext;
        for(int i=selectionList.size()-1;i>=0;i=i-2){
            String groupedIndex= String.valueOf(selectionList.get(i));
            String groupedIndexNext= String.valueOf(selectionList.get(i-1));
            if(groupedIndex.length()==1){
                randomRow= Integer.parseInt(String.valueOf(groupedIndex.charAt(0)));
                randomHeight=0;
            }else{
                randomRow= Integer.parseInt(String.valueOf(groupedIndex.charAt(1)));
                randomHeight= Integer.parseInt(String.valueOf(groupedIndex.charAt(0)));
            }
            if(groupedIndexNext.length()==1){
                randomRowNext= Integer.parseInt(String.valueOf(groupedIndexNext.charAt(0)));
                randomHeightNext=0;
            }else{
                randomRowNext= Integer.parseInt(String.valueOf(groupedIndexNext.charAt(1)));
                randomHeightNext= Integer.parseInt(String.valueOf(groupedIndexNext.charAt(0)));
            }
            temp_holder1="";temp_holder2="";
            for(int k=0;k<rows;k++){
                temp_holder1+=array[randomHeight][randomRow][k];
                temp_holder2+=array[randomHeightNext][randomRowNext][k];
            }
            temp_crossRes=crossOver(temp_holder1,temp_holder2);
            temp_crossRes1=temp_crossRes.substring(0,temp_crossRes.indexOf('%'));
            temp_crossRes2=temp_crossRes.substring(temp_crossRes.indexOf('%')+1,temp_crossRes.length());
            for(int k=0;k<rows;k++){
                array[randomHeight][randomRow][k]= String.valueOf(temp_crossRes1.charAt(k));
                array[randomHeightNext][randomRowNext][k]= String.valueOf(temp_crossRes2.charAt(k));
            }
        }

        return array;
    }
    public String RevFormString(String array[][][], int rows, int height){
        String string;
        string="";
        for(int i=0;i<depth;i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < rows; k++) {
                    string += array[i][j][k];
                }
            }
        }
        for(int i=0;i<residualDNA.length;i++){
            string+=residualDNA[i];
        }
        return string;
    }
    public ArrayList<Integer> randomizeIndex(int rows, int height){
        ArrayList<Integer> randomIndices=new ArrayList<>();
        //System.out.println(rows+" "+height);
        int maxLimit=(height-1)*10+(rows-1);
        Random randomGenerator=new Random();
        int count=0;
        while (count<row*row*height){
            int randInteger=randomGenerator.nextInt(maxLimit);
            if(randInteger%10<rows && randInteger>=10 && randInteger<maxLimit){
                randomIndices.add(randInteger);
                StoreKeys.selectionKeys.add(randInteger);
                count++;
            }
        }
        /*for(int i=0;i<randomIndices.size();i++){
            System.out.println(randomIndices.get(i));
        }*/
        StoreKeys.selectionPoint.add(randomIndices.size());

        return randomIndices;
    }
}
