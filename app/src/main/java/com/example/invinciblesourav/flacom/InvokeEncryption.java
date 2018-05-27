package com.example.invinciblesourav.flacom;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Invincible Sourav on 29-03-2018.
 */

public class InvokeEncryption {
public static long keyToEncrypt;
public static long keyToSend;
    public static String encryptFeeder(String fileName,String sender,String receiver) throws IOException, InterruptedException {
        String fileInput;
        String cipherText="",plainText="";
        keyToSend=0;
        keyToEncrypt = 0;
        if(fileName.contains(".txt")||fileName.contains(".jpg")) {
            fileInput = FileOperations.readFile(fileName);
            //System.out.println(fileInput);
        }else{
            fileInput=fileName;
        }
        Log.d("fileInput",fileInput);
        HomePage.logDisplay.addLine("File contents: "+fileInput,HomePage.homecontext);
        int length=fileInput.length();
        HomePage.logDisplay.addLine("File Length: "+length,HomePage.homecontext);
        int parts=length/2;
        HomePage.logDisplay.addLine("File divided into: "+parts+" parts",HomePage.homecontext);
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
            String displayPartitions="";
           for(int k=0;k<partitions.length;k++){
              displayPartitions+=partitions[k];
           }
            HomePage.logDisplay.addLine("File Partitions: "+displayPartitions,HomePage.homecontext);
            HomePage.logDisplay.addLine("Parallel Encryption starting......",HomePage.homecontext);
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
            cipherText=cipherText+parallelEncrypt.cipherText;
            System.out.println("Output cipher text:"+parallelEncrypt.cipherText);
        }
        EncryptKeys encryptKeys=new EncryptKeys();
        sender=sender.substring(0,10);
        receiver=receiver.substring(0,10);
        Log.d("Beefore encryu",sender+" "+receiver);
        Long modulo=encryptKeys.calculateN(Long.parseLong(sender),Long.parseLong(receiver));
        System.out.println("Modulo="+modulo);
        HomePage.logDisplay.addLine("Modulo: "+modulo,HomePage.homecontext);
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
        System.out.println("key to send:"+keyToSend);
        System.out.println("key to encrypt:"+keyToEncrypt);
        HomePage.logDisplay.addLine("Key to send: "+keyToSend,HomePage.homecontext);
        HomePage.logDisplay.addLine("Key to encrypt: "+keyToEncrypt,HomePage.homecontext);
        System.out.println("Row: "+StoreKeys.rowTrKey);
        System.out.println("Depth: "+StoreKeys.depthTrKey);
        System.out.println("SeqRow"+StoreKeys.seqStartRow);
        System.out.println("SeqDepth"+StoreKeys.seqStartDepth);
        System.out.println("SelectionPoint"+StoreKeys.selectionPoint);
        System.out.println("Selectionkey"+StoreKeys.selectionKeys);
        System.out.println("length"+StoreKeys.length);
        System.out.println("Sum"+StoreKeys.sumArray);
        System.out.println("Msg"+StoreKeys.messageDigest);
        HomePage.logDisplay.addLine("Row Transposition Keys: "+StoreKeys.rowTrKey,HomePage.homecontext);
        HomePage.logDisplay.addLine("Depth Transpositions keys: "+StoreKeys.depthTrKey,HomePage.homecontext);
        HomePage.logDisplay.addLine("Row sequence Transpositions Key: "+StoreKeys.seqStartRow,HomePage.homecontext);
        HomePage.logDisplay.addLine("Depth sequence Transpositions Key: "+StoreKeys.seqStartDepth,HomePage.homecontext);
        HomePage.logDisplay.addLine("Selection points: "+StoreKeys.selectionPoint,HomePage.homecontext);
        HomePage.logDisplay.addLine("Selection Keys: "+StoreKeys.selectionKeys,HomePage.homecontext);
        HomePage.logDisplay.addLine("Length: "+StoreKeys.length,HomePage.homecontext);
        HomePage.logDisplay.addLine("Sum: "+StoreKeys.sumArray,HomePage.homecontext);
        HomePage.logDisplay.addLine("Message Digests: "+StoreKeys.messageDigest,HomePage.homecontext);
        Log.d("plainlen",String.valueOf(cipherText.length()));
        for(int i=0;i<StoreKeys.rowTrKey.size();i++){
            StoreKeys.rowTrKey.set(i,String.valueOf(Long.parseLong(StoreKeys.rowTrKey.get(i))^keyToEncrypt));
        }
        for(int i=0;i<StoreKeys.depthTrKey.size();i++){
            StoreKeys.depthTrKey.set(i,String.valueOf(Long.parseLong(StoreKeys.depthTrKey.get(i))^keyToEncrypt));
        }
        for(int i=0;i<StoreKeys.seqStartRow.size();i++){
            StoreKeys.seqStartRow.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.seqStartRow.get(i)))^keyToEncrypt)));
        }
        for(int i=0;i<StoreKeys.seqStartDepth.size();i++){
            StoreKeys.seqStartDepth.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.seqStartDepth.get(i)))^keyToEncrypt)));
        }
        for(int i=0;i<StoreKeys.length.size();i++){
            StoreKeys.length.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.length.get(i)))^keyToEncrypt)));
        }
        for(int i=0;i<StoreKeys.selectionKeys.size();i++){
            StoreKeys.selectionKeys.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.selectionKeys.get(i)))^keyToEncrypt)));
        }
        for(int i=0;i<StoreKeys.selectionPoint.size();i++){
            StoreKeys.selectionPoint.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.selectionPoint.get(i)))^keyToEncrypt)));
        }
        for(int i=0;i<StoreKeys.messageDigest.size();i++){
            StoreKeys.messageDigest.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.messageDigest.get(i)))^keyToEncrypt)));
        }
        /*for(int i=0;i<StoreKeys.sumArray.size();i++){
            StoreKeys.sumArray.set(i,String.valueOf(Long.parseLong(StoreKeys.sumArray.get(i))^keyToEncrypt));
        }*/


        return cipherText;
    }
    static class ParallelEncrypt implements Runnable{
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

}
