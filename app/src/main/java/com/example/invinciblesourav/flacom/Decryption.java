package com.example.invinciblesourav.flacom;

import java.util.ArrayList;

public class Decryption {
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
    public String decrypt(String cipherText, ArrayList<String> rowKey, ArrayList<String> depthKey, int sequenceNo){
        System.out.println("Decrtption starting.........."+cipherText);
        Received.logDisplay.addLine("Decryption of: "+cipherText,Received.thiscontext);

        bitPattern="";
        for(int i=0;i<cipherText.length();i++){
            t_bitOfInput=cipherText.charAt(i);
            if (t_bitOfInput=='!'){
                i++;
                t_bitOfInput=(char)((int)cipherText.charAt(i)-34);
            }
            bitAscii=t_bitOfInput;
            bitPattern+=initialOperations.decAsciitobinary(bitAscii);
            System.out.print("Got it: "+initialOperations.decAsciitobinary(bitAscii));

        }
       //bitPattern=bitPattern.substring(0,bitPattern.length()-16);
        bitPatternLength=initialOperations.getBinaryPattLen(bitPattern);
        //bitPattern=cipherText;
        System.out.println();
        System.out.println(bitPattern);
        Received.logDisplay.addLine("Bit pattern: "+bitPattern,Received.thiscontext);
        System.out.println(bitPatternLength);
        Received.logDisplay.addLine("Bit pattern length: "+bitPatternLength,Received.thiscontext);
        DNAProcesses DecdnaProcesses=new DNAProcesses();
        dnaSeq=DecdnaProcesses.dncal(bitPattern);
        System.out.println();
        System.out.println(dnaSeq);
        Received.logDisplay.addLine("DNA sequence: "+dnaSeq,Received.thiscontext);
        DecdnaProcesses.calculateDimens(dnaSeq);
        //DecdnaProcesses.calculateDimens(dnaSeq);
        System.out.println();
        System.out.println(dnaSeq.length());
        Received.logDisplay.addLine("Length of DNA sequence: "+dnaSeq.length(),Received.thiscontext);
        System.out.println("Rows="+DecdnaProcesses.row+"Columns="+DecdnaProcesses.column+"Depth="+DecdnaProcesses.depth);
        Received.logDisplay.addLine("Rows="+DecdnaProcesses.row+"Columns="+DecdnaProcesses.column+"Depth="+DecdnaProcesses.depth,Received.thiscontext);
        ArrayList<String> seqRowKey=new ArrayList<>();
        ArrayList<String> seqDepthKey=new ArrayList<>();
        seqDepthKey.clear();
        seqRowKey.clear();

        if(sequenceNo==0) {
            int startIndex=0;
            int endIndex = startIndex + (int) DecdnaProcesses.row;
            for (int i = startIndex; i < endIndex; i++) {
                seqRowKey.add(rowKey.get(i));
            }
            startIndex = 0;
            endIndex = startIndex + (int) DecdnaProcesses.depth;
            for (int i = startIndex; i < endIndex; i++) {
                seqDepthKey.add(depthKey.get(i));
            }
        }else{
            int sum=0;
            for(int k=0;k<sequenceNo;k++){
                sum+=StoreKeys.seqStartRow.get(k);
            }
            int startIndex=sum;
            int endIndex=startIndex+(int) DecdnaProcesses.row;
            System.out.println("Start "+startIndex+" end "+endIndex);
            for (int i = startIndex; i < endIndex; i++) {
                seqRowKey.add(rowKey.get(i));
            }
            sum=0;
            for(int k=0;k<sequenceNo;k++){
                sum+=StoreKeys.seqStartDepth.get(k);
            }
            startIndex=sum;
            endIndex=startIndex+(int) DecdnaProcesses.depth;
            for (int i = startIndex; i < endIndex; i++) {
                seqDepthKey.add(depthKey.get(i));
            }
        }
        for (int i=0;i<seqRowKey.size();i++){
            if(i==0){
                System.out.println("Row key for "+sequenceNo);
                System.out.println();
            }
            System.out.print(seqRowKey.get(i)+" ");
        }
        System.out.println();

        for (int i=0;i<seqDepthKey.size();i++){
            if(i==0){
                System.out.println("Depth key for "+sequenceNo);
                System.out.println();
            }
            System.out.print(seqDepthKey.get(i)+" ");
        }
        System.out.println();
        Transpositions Dectranspositions=new Transpositions(DecdnaProcesses.row,DecdnaProcesses.depth,seqRowKey,seqDepthKey);
        System.out.println();

        for (int i=0;i<Dectranspositions.rowColTrKey.size();i++){
            if(i==0){
                System.out.println("Row key for "+sequenceNo);
                System.out.println();
            }
            System.out.print(Dectranspositions.rowColTrKey.get(i)+" ");
        }
        System.out.println();

        for (int i=0;i<Dectranspositions.depthTrKey.size();i++){
            if(i==0){
                System.out.println("Depth key for "+sequenceNo);
                System.out.println();
            }
            System.out.print(Dectranspositions.depthTrKey.get(i)+" ");
        }
        System.out.println();
        String displayDna="";
        dnaArray=DecdnaProcesses.generateArray(dnaSeq, Integer.parseInt(String.valueOf(DecdnaProcesses.row)), Integer.parseInt(String.valueOf(DecdnaProcesses.depth)));
        for(int i=0;i<DecdnaProcesses.depth;i++){
            for (int j=0;j<DecdnaProcesses.row;j++){
                for(int k=0;k<DecdnaProcesses.column;k++){
                    System.out.print(dnaArray[i][j][k]);
                    displayDna+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayDna+="\n";
            }
            System.out.println();
            displayDna+="\n";
        }
        Received.logDisplay.addLine("DNA array: ",Received.thiscontext);
        Received.logDisplay.addLine(displayDna,Received.thiscontext);
        dnaArray=DecdnaProcesses.mutate(dnaArray, Integer.parseInt(String.valueOf(DecdnaProcesses.row)), Integer.parseInt(String.valueOf(DecdnaProcesses.depth)));
        System.out.println("After mutation:");
        displayDna="";
        for(int i=0;i<DecdnaProcesses.depth;i++){
            for (int j=0;j<DecdnaProcesses.row;j++){
                for(int k=0;k<DecdnaProcesses.column;k++){
                    System.out.print(dnaArray[i][j][k]);
                    displayDna+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayDna+="\n";
            }
            System.out.println();
            displayDna+="\n";
        }
        Received.logDisplay.addLine("DNA array after mutation: ",Received.thiscontext);
        Received.logDisplay.addLine(displayDna,Received.thiscontext);
        dnaArray=DecdnaProcesses.doRevCrossOver(dnaArray, Integer.parseInt(String.valueOf(DecdnaProcesses.row)), Integer.parseInt(String.valueOf(DecdnaProcesses.depth)),sequenceNo);
        displayDna="";
        System.out.println("After crossover:");
        for(int i=0;i<DecdnaProcesses.depth;i++){
            for (int j=0;j<DecdnaProcesses.row;j++){
                for(int k=0;k<DecdnaProcesses.column;k++){
                    System.out.print(dnaArray[i][j][k]);
                    displayDna+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayDna+="\n";
            }
            System.out.println();
            displayDna+="\n";
        }
        Received.logDisplay.addLine("DNA array after crossover: ",Received.thiscontext);
        Received.logDisplay.addLine(displayDna,Received.thiscontext);
        displayDna="";
        System.out.println("After depth transposition");
        dnaArray=Dectranspositions.revTransposeDepth(dnaArray);
        for(int i=0;i<DecdnaProcesses.depth;i++){
            for (int j=0;j<DecdnaProcesses.row;j++){
                for(int k=0;k<DecdnaProcesses.column;k++){
                    System.out.print(dnaArray[i][j][k]);
                    displayDna+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayDna+="\n";
            }
            System.out.println();
            displayDna+="\n";
        }
        Received.logDisplay.addLine("DNA array after depth transposition: ",Received.thiscontext);
        Received.logDisplay.addLine(displayDna,Received.thiscontext);
        displayDna="";
        System.out.println("After row transposition");
        dnaArray=Dectranspositions.revTransposeRow(dnaArray);
        for(int i=0;i<DecdnaProcesses.depth;i++){
            for (int j=0;j<DecdnaProcesses.row;j++){
                for(int k=0;k<DecdnaProcesses.column;k++){
                    System.out.print(dnaArray[i][j][k]);
                    displayDna+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayDna+="\n";
            }
            System.out.println();
            displayDna+="\n";
        }
        Received.logDisplay.addLine("DNA array after row transposition: ",Received.thiscontext);
        Received.logDisplay.addLine(displayDna,Received.thiscontext);
        System.out.println("After column transposition");
        displayDna="";
        dnaArray=Dectranspositions.revTransposeColumn(dnaArray);
        for(int i=0;i<DecdnaProcesses.depth;i++){
            for (int j=0;j<DecdnaProcesses.row;j++){
                for(int k=0;k<DecdnaProcesses.column;k++){
                    System.out.print(dnaArray[i][j][k]);
                    displayDna+=dnaArray[i][j][k]+" ";
                }
                System.out.println();
                displayDna+=" ";
            }
            System.out.println();
            displayDna+=" ";
        }
        Received.logDisplay.addLine("DNA array after column transposition: ",Received.thiscontext);
        Received.logDisplay.addLine(displayDna,Received.thiscontext);
        decDNAseq=DecdnaProcesses.RevFormString(dnaArray, Integer.parseInt(String.valueOf(DecdnaProcesses.row)), Integer.parseInt(String.valueOf(DecdnaProcesses.depth)));
        System.out.println(decDNAseq);
        Received.logDisplay.addLine("DNA sequence after transpositions: "+decDNAseq,Received.thiscontext);
        FinalOperations DecFinaloperations=new FinalOperations();
        decBitpattern=DecFinaloperations.RevconvertBit(decDNAseq);
        System.out.println(decBitpattern);
        Received.logDisplay.addLine("Resulting bit pattern: "+decBitpattern,Received.thiscontext);
        ArrayOperations DecarrayOperations=new ArrayOperations(decBitpattern.length(),decBitpattern);
        System.out.println();
        DecarrayOperations.revShift1D2D();
        System.out.println("After reverse shift");
        System.out.println();
        DecarrayOperations.displayArray();
        System.out.println();
        DecarrayOperations.shiftUp();
        System.out.println("After up shift");
        System.out.println();
        DecarrayOperations.displayArray();
        System.out.println();
        DecarrayOperations.shiftLeft();
        System.out.print("After left shift");
        System.out.println();
        DecarrayOperations.displayArray();
        System.out.println();
        DecarrayOperations.diagonalInterchange();
        System.out.println("After diagonal interchange");
        DecarrayOperations.displayArray();
        System.out.println();
        DecarrayOperations.shiftDown();
        System.out.println("After down shift");
        System.out.println();
        DecarrayOperations.displayArray();
        DecarrayOperations.shiftRight();
        System.out.println("After right shift");
        System.out.println();
        DecarrayOperations.displayArray();
        permutedPattern=DecarrayOperations.getBitPattern();
        System.out.println(permutedPattern);
        Received.logDisplay.addLine("Permuted bit pattern: "+permutedPattern,Received.thiscontext);
        //System.out.println(permutedPattern.length());
        InitialOperations DecinitialOperations=new InitialOperations();
        decXoredString=DecinitialOperations.xrcal(permutedPattern);
        System.out.println(decXoredString);
        Received.logDisplay.addLine("Resulting XORed string: "+decXoredString,Received.thiscontext);
        primenoList=DecinitialOperations.getPrimeNosWithinRange(decXoredString.length());
        for(int i=0;i<primenoList.size();i++){
            System.out.print(primenoList.get(i)+" ");
        }


        compString=DecinitialOperations.complementPrimePos(decXoredString,primenoList);
        System.out.println();
        System.out.println(compString);
        Received.logDisplay.addLine("Complemented bit pattern: "+compString,Received.thiscontext);
        reveString=DecinitialOperations.reversePatt(compString);
        System.out.println();
        System.out.println(reveString);
        Received.logDisplay.addLine("Reversing bit pattern: "+reveString,Received.thiscontext);
        compString=DecinitialOperations.complementPrimePos(reveString,primenoList);
        System.out.println(compString);
        Received.logDisplay.addLine("Complemented bit pattern: "+compString,Received.thiscontext);
        onePositions=new ArrayList<>();
        onePositions.clear();
        onePositions=DecinitialOperations.findposOfOne(compString);
        for(int i=0;i<onePositions.size();i++){
            System.out.print(onePositions.get(i)+" ");
        }
        holdExponentials=new ArrayList<>();
        holdExponentials.clear();
        for(int i=0,j=0;i<primenoList.size();i++){
            holdExponentials.add(DecinitialOperations.calculate_power(primenoList.get(i).toString(),onePositions.get(j).toString()));
            j++;
            if(j==onePositions.size()){
                j=0;
            }
        }
        for(int i=0;i<holdExponentials.size();i++){
            System.out.print(holdExponentials.get(i)+" ");
        }
        System.out.println();
        holdSum="0";
        for(int i=0;i<holdExponentials.size();i++){
            holdSum=DecinitialOperations.add_large(holdSum,holdExponentials.get(i));
        }
        System.out.println(holdSum);
        MessageDigest DecmessageDigest=new MessageDigest(holdSum);
        System.out.println("Message digest:"+DecmessageDigest.generateDigest());
        InvokeDecryption.decMsgDigest.add(DecmessageDigest.generateDigest());
        plainText=DecFinaloperations.decfinalConversion(compString);

        if(plainText.length()-StoreKeys.length.get(sequenceNo)!=0){
            plainText=plainText.substring(0,plainText.length()-(plainText.length()-StoreKeys.length.get(sequenceNo)));
        }
        System.out.println("Plain:"+plainText);
        Received.logDisplay.addLine("Plain text for "+cipherText+": "+decBitpattern,Received.thiscontext);
        return plainText;
    }
}
