package com.example.invinciblesourav.flacom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Test{
    public static ArrayList<Integer> utfPosition=new ArrayList<>();
    public static ArrayList<Integer> utfLength=new ArrayList<>();
    public static ArrayList<Integer> utfCharacterBits=new ArrayList<>();
    public static StoreKeys storeKeys;



    public static void testify(String fileInput) throws IOException, InterruptedException {
       ArrayList<Integer> out;
       int actualBitLength;
       ArrayList<Integer> primenoList;
       ArrayList<Integer> onePositions;
       ArrayList<String> agendaForSum;
       ArrayList<String> holdExponentials;
       String holdSum="0";
       InitialOperations initialOperations=new InitialOperations();
       String inputText="ABC";
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
       String cipherText = "";
       String decDNAseq;
       String decBitpattern;
       String decXoredString;
       String plainText = "";
       ArrayList<String> rowKey=new ArrayList<>();
       ArrayList<String> colKey=new ArrayList<>();
       long keyToSend=0;
       long keyToEncrypt = 0;


       //FileOperations fileOperations=new FileOperations();
       //fileInput=fileOperations.readFile("C:\\Users\\Invincible Sourav\\Documents\\sample.txt");
       System.out.println(fileInput);
        /*Encryption encryption=new Encryption();
        cipherText=encryption.encrypt(fileInput);
        Log.d("cipher",cipherText);
        Decryption decryption=new Decryption();
        plainText=decryption.decrypt(cipherText,StoreKeys.rowTrKey,StoreKeys.depthTrKey,0);
        Log.d("plain",plainText);*/
       int length=fileInput.length();
       int parts=length/2;
       if(length>2) {
           int offset = length / parts;
           String partitions[] = new String[parts];
           int beg = 0;
           int end = offset;
           int i;
           for (i = 0; i < parts; i++) {
               partitions[i] = fileInput.substring(beg, end);
               beg = end;
               end = beg + offset;
               if (end >= length && i==parts-1) {
                   partitions[i] += fileInput.substring(beg, length);
               }
           }
           /*for(int k=0;k<partitions.length;k++){
               System.out.println(partitions[k]);
           }*/

           ParallelEncrypt parallelEncrypt[]=new ParallelEncrypt[parts];
           for (int j = 0; j < parts; j++) {
               parallelEncrypt[j]=new ParallelEncrypt(partitions[j]);
           }

           for(int j=0;j<parallelEncrypt.length;j++){
               cipherText+=parallelEncrypt[j].cipherText;
           }
           System.out.println("Output cipher text:"+cipherText);

       }else{
            ParallelEncrypt parallelEncrypt=new ParallelEncrypt(fileInput);
            cipherText=parallelEncrypt.cipherText;
           System.out.println("Output cipher text:"+parallelEncrypt.cipherText);
       }
        EncryptKeys encryptKeys=new EncryptKeys();
        Long modulo=encryptKeys.calculateN(983000690,943232454);
        ArrayList<Long> inverses=encryptKeys.findMultiplicative(modulo);
        int flag=0;
        do {
            flag=0;
            String maxLengthKey = String.valueOf(inverses.get(inverses.size() - 1));
            int maxLength = maxLengthKey.length();
            Random randomPartSelect = new Random();
            int sumArrayIndex = randomPartSelect.nextInt(StoreKeys.sumArray.size());
            Random randomIndexSelect = new Random();
            int begIndex = randomIndexSelect.nextInt(StoreKeys.sumArray.get(sumArrayIndex).length() - maxLength);
            int endIndex = begIndex + maxLength;
            String keyInput = StoreKeys.sumArray.get(sumArrayIndex).substring(begIndex, endIndex);

            for (int i = 0; i < inverses.size(); i++) {
                if (inverses.get(i) == Long.parseLong(keyInput)) {
                    if(i%2!=0){
                        keyToSend=inverses.get(i-1);
                        keyToEncrypt=inverses.get(i);
                    }else {
                        keyToSend=inverses.get(i);
                        keyToEncrypt=inverses.get(i+1);
                    }
                    flag=1;
                    break;
                }

            }

        }while (flag==0);
        //System.out.println(keyToSend);
        //System.out.println(keyToEncrypt);
        String temp[]=new String[StoreKeys.rowTrKey.size()];
        for(int i=0;i<temp.length;i++){
            temp[i]= String.valueOf(Long.parseLong(StoreKeys.rowTrKey.get(i))^keyToEncrypt);
        }
        StoreKeys.rowTrKey.clear();
        for(int i=0;i<temp.length;i++){
            StoreKeys.rowTrKey.add(temp[i]);
        }

       /*System.out.println("Keys for transposition");
       for(int i=0;i<StoreKeys.seqStartRow.size();i++){
           System.out.print(StoreKeys.seqStartRow.get(i)+" ");
       }
       System.out.println();
       for(int i=0;i<StoreKeys.seqStartDepth.size();i++){
           System.out.print(StoreKeys.seqStartDepth.get(i)+" ");
       }*/
        EncryptKeys encryptKeysDec=new EncryptKeys();
        Long moduloDec=encryptKeysDec.calculateN(983000690,943232454);
        Long key=encryptKeysDec.findInverse(keyToSend,moduloDec);
        if(key<0){
            key=key+moduloDec;
        }
        //System.out.println("deded"+key);
        String temp1[]=new String[StoreKeys.rowTrKey.size()];
        for(int i=0;i<temp.length;i++){
            temp1[i]= String.valueOf(Long.parseLong(StoreKeys.rowTrKey.get(i))^key);
        }
        StoreKeys.rowTrKey.clear();
        for(int i=0;i<temp1.length;i++){
            StoreKeys.rowTrKey.add(temp1[i]);
        }
        int cipherLength=cipherText.length();
        int cipherParts=length/2;
        if(cipherLength>2) {
            int offset = cipherLength / cipherParts;
            String partitions[] = new String[cipherParts];
            int beg = 0;
            int end = offset;
            int i;
            for (i = 0; i < cipherParts; i++) {
                partitions[i] = cipherText.substring(beg, end);
                beg = end;
                end = beg + offset;
                if (end >= cipherLength && i==cipherParts-1) {
                    partitions[i] += cipherText.substring(beg, cipherLength);
                }
            }
            //System.out.println(end + " " + length);

            ParallelDecrypt parallelDecrypt[]=new ParallelDecrypt[cipherParts];
            for (int j = 0; j < partitions.length; j++) {
                parallelDecrypt[j]=new ParallelDecrypt(partitions[j],j);
            }

            for(int j=0;j<parallelDecrypt.length;j++){
                plainText+=parallelDecrypt[j].plainText;
            }
            System.out.println("Output plain text:"+plainText);

        }else{
            ParallelDecrypt parallelDecrypt=new ParallelDecrypt(cipherText,0);
            System.out.println("Output plain text:"+parallelDecrypt.plainText);
        }









   }
    static class ParallelEncrypt implements Runnable {
        String inputText;
        String cipherText=null;
       @Override
       public void run() {
           Encryption encryption=new Encryption();
           cipherText=encryption.encrypt(inputText);

       }
       public ParallelEncrypt(String inputText) throws InterruptedException {
          this.inputText=inputText;
          Thread t=new Thread(this);
          t.start();
          t.join();
       }


   }
   static class ParallelDecrypt implements Runnable {
        String cipherText;
        String plainText;
        int seqNo;
       @Override
       public void run() {
           Decryption decryption=new Decryption();
           plainText=decryption.decrypt(cipherText,StoreKeys.rowTrKey,StoreKeys.depthTrKey,seqNo);
       }
       public ParallelDecrypt(String cipherText, int sequenceNo) throws InterruptedException {
           this.cipherText=cipherText;
           this.seqNo=sequenceNo;
           Thread t=new Thread(this);
           t.start();
           t.join();
       }

   }


}
