package com.example.invinciblesourav.flacom;

import java.util.ArrayList;

public class Encryption {
    ArrayList<Integer> out;
    int actualBitLength;
    ArrayList<Integer> primenoList;
    ArrayList<Integer> onePositions;
    ArrayList<String> agendaForSum;
    ArrayList<String> holdExponentials;
    String holdSum="0";
    InitialOperations initialOperations=new InitialOperations();
    //String inputText="HE IS GOON";
    //String inputText="T";
    char t_bitOfInput;
    int  bitAscii;
    String bitPattern="";
    long  bitPatternLength;
    String compString;
    String reveString;
    String xoredSt;
    String dnaSeq;
    String permutedPattern;
    String dnaArray[][][];
    String resultantPattern;
    String cipherText;
    String decDNAseq;
    String decBitpattern;
    String decXoredString;
    String plainText;
    int actualLength;

    public String encrypt(String inputText) {
        System.out.println("Encryption process starting.....................");
        actualLength=inputText.length();
        System.out.println("Actual length"+actualLength);

        if((inputText.length()*8)%16!=0){
            while ((inputText.length()*8)%16!=0){
                inputText=inputText+"@";

            }
        }
        System.out.println(inputText);
        HomePage.logDisplay.addLine("Input text: "+inputText,HomePage.homecontext);
        for (int i = 0; i < inputText.length(); i++) {
            t_bitOfInput = inputText.charAt(i);
            bitAscii = t_bitOfInput;
            bitPattern += initialOperations.asciitobinary(bitAscii, i);
            //System.out.print(initialOperations.asciitobinary(bitAscii));
        }
        bitPatternLength = initialOperations.getBinaryPattLen(bitPattern);
        System.out.println("Bit pattern of the plain text: " + bitPattern);
        HomePage.logDisplay.addLine("Bit pattern: "+bitPattern,HomePage.homecontext);
        System.out.println("Bit pattern length :" + bitPatternLength);
        HomePage.logDisplay.addLine("Bit pattern length: "+bitPatternLength,HomePage.homecontext);
        primenoList = initialOperations.getPrimeNosWithinRange(bitPatternLength);
        System.out.println("Prime numbers within the given range 2-" + bitPatternLength + ":");
        HomePage.logDisplay.addLine("Prime numbers within the given range 2-" + bitPatternLength + ":",HomePage.homecontext);
        for (int i = 0; i < primenoList.size(); i++) {
        //    System.out.print(primenoList.get(i) + " ");
        }
        System.out.println();
        onePositions = initialOperations.findposOfOne(bitPattern);
        System.out.println("Position of 1's in the bit pattern:");
        String displayOnePos="";
        for (int i = 0; i < onePositions.size(); i++) {
        //    System.out.print(onePositions.get(i) + " ");
                displayOnePos+=onePositions.get(i).toString()+" ";
        }
        HomePage.logDisplay.addLine("Position of 1's in the bit pattern: "+displayOnePos,HomePage.homecontext);
        System.out.println();
        holdExponentials = new ArrayList<String>();
        System.out.println("Primes raised to the order of primes:");
        for (int i = 0, j = 0; i < primenoList.size(); i++) {
            holdExponentials.add(initialOperations.calculate_power(primenoList.get(i).toString(), onePositions.get(j).toString()));
            j++;
            if (j == onePositions.size()) {
                j = 0;
            }
        }
        String displayHoldExp="";
        for (int i = 0; i < holdExponentials.size(); i++) {
            System.out.print(holdExponentials.get(i) + " ");
            displayHoldExp+=holdExponentials.get(i)+" ";
        }
        System.out.println();
        HomePage.logDisplay.addLine("Primes raised to the power of primes :"+displayHoldExp,HomePage.homecontext);
        for (int i = 0; i < holdExponentials.size(); i++) {
            holdSum = initialOperations.add_large(holdSum, holdExponentials.get(i));
        }
        StoreKeys.sumArray.add(holdSum);
        System.out.println("Sum :" + holdSum);
        HomePage.logDisplay.addLine("Sum :" + holdSum,HomePage.homecontext);
        MessageDigest messageDigest = new MessageDigest(holdSum);
        System.out.println("Message digest: " + messageDigest.generateDigest());
        HomePage.logDisplay.addLine("Message digest: " + messageDigest.generateDigest(),HomePage.homecontext);
        StoreKeys.messageDigest.add(messageDigest.generateDigest());
        compString = initialOperations.complementPrimePos(bitPattern, primenoList);
        System.out.println("Complementing prime positions : " + compString);
        HomePage.logDisplay.addLine("Complementing prime positions : " + compString,HomePage.homecontext);
        reveString = initialOperations.reversePatt(compString);
        System.out.println();
        System.out.println("Reversing the pattern : " + reveString);
        HomePage.logDisplay.addLine("Reversing the pattern : " + reveString,HomePage.homecontext);
        compString = initialOperations.complementPrimePos(reveString, primenoList);
        System.out.println();
        System.out.println("Again complementing the prime positions : " + compString);
        HomePage.logDisplay.addLine("Again complementing the prime positions : " + compString,HomePage.homecontext);
        System.out.println();
        xoredSt = initialOperations.xrcal(compString);
        System.out.println("Xored String : " + xoredSt);
        HomePage.logDisplay.addLine("Xored String : " + xoredSt,HomePage.homecontext);
        System.out.println();
        ArrayOperations arrayOperations = new ArrayOperations(bitPatternLength, xoredSt);
        System.out.println();
        System.out.println("2D array after left shift");
        HomePage.logDisplay.addLine("2D array after left shift",HomePage.homecontext);
        arrayOperations.shiftLeft();
        arrayOperations.displayArray();
        System.out.println("2D array after up shift");
        HomePage.logDisplay.addLine("2D array after up shift",HomePage.homecontext);
        System.out.println();
        arrayOperations.shiftUp();
        arrayOperations.displayArray();
        System.out.println("2D array after diagonal interchange");
        HomePage.logDisplay.addLine("2D array after diagonal interchange",HomePage.homecontext);
        System.out.println();
        arrayOperations.diagonalInterchange();
        arrayOperations.displayArray();
        System.out.println("2D array after right shift");
        HomePage.logDisplay.addLine("2D array after right shift",HomePage.homecontext);
        System.out.println();
        arrayOperations.shiftRight();
        arrayOperations.displayArray();
        System.out.println("2D array after down shift");
        HomePage.logDisplay.addLine("2D array after down shift",HomePage.homecontext);
        System.out.println();
        arrayOperations.shiftDown();
        arrayOperations.displayArray();
        System.out.println("2D array after 1D to 2D shift");
        HomePage.logDisplay.addLine("2D array after 1D to 2D shift",HomePage.homecontext);
        arrayOperations.shift1D2D();
        System.out.println();
        arrayOperations.displayArray();
        permutedPattern = arrayOperations.getBitPattern();
        System.out.println("Permuted bit pattern :" + permutedPattern);
        HomePage.logDisplay.addLine("Permuted bit pattern :" + permutedPattern,HomePage.homecontext);
        System.out.println("Length of permuted bit pattern : " + permutedPattern.length());

        DNAProcesses dnaProcesses = new DNAProcesses();
        dnaSeq = dnaProcesses.dncal(permutedPattern);
        System.out.println();
        System.out.println("DNA sequence : " + dnaSeq);
        HomePage.logDisplay.addLine("DNA sequence : " + dnaSeq,HomePage.homecontext);
        dnaProcesses.calculateDimens(dnaSeq);
        System.out.println();
        System.out.println(dnaSeq.length());
        System.out.println("Rows=" + dnaProcesses.row + "Columns=" + dnaProcesses.column + "Depth=" + dnaProcesses.depth);
        HomePage.logDisplay.addLine("Rows=" + dnaProcesses.row + "Columns=" + dnaProcesses.column + "Depth=" + dnaProcesses.depth,HomePage.homecontext);

        Transpositions transpositions = new Transpositions(holdSum, dnaProcesses.row, dnaProcesses.depth);
        System.out.println();

        for (int i = 0; i < transpositions.rowColTrKey.size(); i++) {

            System.out.print(transpositions.rowColTrKey.get(i) + " ");
            StoreKeys.rowTrKey.add(transpositions.rowColTrKey.get(i));
        }
        StoreKeys.seqStartRow.add(transpositions.rowColTrKey.size());
        System.out.println();


        for (int i = 0; i < transpositions.depthTrKey.size(); i++) {

            System.out.print(transpositions.depthTrKey.get(i) + " ");
            StoreKeys.depthTrKey.add(transpositions.depthTrKey.get(i));
        }
        StoreKeys.seqStartDepth.add(transpositions.depthTrKey.size());
        System.out.println();
        StoreKeys.length.add(actualLength);

        dnaArray = dnaProcesses.generateArray(dnaSeq, Integer.parseInt(String.valueOf(dnaProcesses.row)), Integer.parseInt(String.valueOf(dnaProcesses.depth)));
        for (int i = 0; i < dnaProcesses.depth; i++) {
            for (int j = 0; j < dnaProcesses.row; j++) {
                for (int k = 0; k < dnaProcesses.column; k++) {
                    System.out.print(dnaArray[i][j][k]);
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("After column transposition");
        HomePage.logDisplay.addLine("After column transposition",HomePage.homecontext);
        String displayCtr="";
        dnaArray = transpositions.transposeColumn(dnaArray);
        for (int i = 0; i < dnaProcesses.depth; i++) {
            for (int j = 0; j < dnaProcesses.row; j++) {
                for (int k = 0; k < dnaProcesses.column; k++) {
                    System.out.print(dnaArray[i][j][k]);
                    displayCtr+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayCtr+="\n";
            }
            System.out.println();
            displayCtr+="\n";
        }
        HomePage.logDisplay.addLine(displayCtr,HomePage.homecontext);
        System.out.println("After row transposition");
        HomePage.logDisplay.addLine("After row transposition",HomePage.homecontext);
        displayCtr="";
        dnaArray = transpositions.transposeRow(dnaArray);
        for (int i = 0; i < dnaProcesses.depth; i++) {
            for (int j = 0; j < dnaProcesses.row; j++) {
                for (int k = 0; k < dnaProcesses.column; k++) {
                    System.out.print(dnaArray[i][j][k]);
                    displayCtr+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayCtr+="\n";
            }
            System.out.println();
            displayCtr+="\n";
        }
        HomePage.logDisplay.addLine(displayCtr,HomePage.homecontext);
        System.out.println("After depth transposition");
        HomePage.logDisplay.addLine("After depth transposition",HomePage.homecontext);
        displayCtr="";
        dnaArray = transpositions.transposeDepth(dnaArray);
        for (int i = 0; i < dnaProcesses.depth; i++) {
            for (int j = 0; j < dnaProcesses.row; j++) {
                for (int k = 0; k < dnaProcesses.column; k++) {
                    System.out.print(dnaArray[i][j][k]);
                    displayCtr+=dnaArray[i][j][k]+" ";

                }
                System.out.println();
                displayCtr+="\n";
            }
            System.out.println();
            displayCtr+="\n";
        }
        HomePage.logDisplay.addLine(displayCtr,HomePage.homecontext);
        dnaArray = dnaProcesses.doCrossOver(dnaArray, Integer.parseInt(String.valueOf(dnaProcesses.row)), Integer.parseInt(String.valueOf(dnaProcesses.depth)));
        System.out.println("After crossover:");
        HomePage.logDisplay.addLine("After crossover:",HomePage.homecontext);
        displayCtr="";
        for (int i = 0; i < dnaProcesses.depth; i++) {
            for (int j = 0; j < dnaProcesses.row; j++) {
                for (int k = 0; k < dnaProcesses.column; k++) {
                    System.out.print(dnaArray[i][j][k]);
                    displayCtr+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayCtr+="\n";
            }
            System.out.println();
            displayCtr+="\n";
        }
        HomePage.logDisplay.addLine(displayCtr,HomePage.homecontext);
        dnaArray = dnaProcesses.mutate(dnaArray, Integer.parseInt(String.valueOf(dnaProcesses.row)), Integer.parseInt(String.valueOf(dnaProcesses.depth)));
        System.out.println("After mutation:");
        HomePage.logDisplay.addLine("After mutation:",HomePage.homecontext);
        displayCtr="";
        for (int i = 0; i < dnaProcesses.depth; i++) {
            for (int j = 0; j < dnaProcesses.row; j++) {
                for (int k = 0; k < dnaProcesses.column; k++) {
                    System.out.print(dnaArray[i][j][k]);
                    displayCtr+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayCtr+="\n";
            }
            System.out.println();
            displayCtr+="\n";
        }

        FinalOperations finalOperations = new FinalOperations();
        resultantPattern = finalOperations.convertBit(dnaArray, Integer.parseInt(String.valueOf(dnaProcesses.row)), Integer.parseInt(String.valueOf(dnaProcesses.depth)));
        System.out.println("Resultant bit pattern : " + resultantPattern);
        HomePage.logDisplay.addLine("Resultant bit pattern : " + resultantPattern,HomePage.homecontext);
        System.out.println("Resultant bit pattern : " + resultantPattern.length());
        cipherText = finalOperations.finalConversion(resultantPattern);
        //cipherText=resultantPattern;
        System.out.println("Cipher Text for "+inputText+": " + cipherText);
        HomePage.logDisplay.addLine("Cipher Text for "+inputText+": " + cipherText,HomePage.homecontext);
        StoreKeys.decGroup.add(cipherText.length());
        return cipherText;
    }
}
