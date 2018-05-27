package com.example.invinciblesourav.flacom;

import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Invincible Sourav on 30-03-2018.
 */

public class InvokeDecryption {
    public static boolean securelyReceived=true;
    public static ArrayList<Integer> decMsgDigest=new ArrayList<>();
    public static String decryptFeeder(String cipherText,String sender,String receiver,String keydec) throws IOException, InterruptedException {
        decMsgDigest.clear();
        String  plainText = "";
        int length;
        ArrayList<String> rowKey=new ArrayList<>();
        ArrayList<String> colKey=new ArrayList<>();
        String fileInput;
        //long keyToSend=0;
        //long keyToEncrypt = 0;
        String senderNo=sender;
        if(senderNo.startsWith("+91")){
            senderNo=senderNo.substring(3,senderNo.length());
        }
        String receiverNo=receiver;
        if(receiverNo.startsWith("+91")){
            receiverNo=receiverNo.substring(3,receiverNo.length());
        }


        EncryptKeys encryptKeysDec=new EncryptKeys();
        Log.d("sendedr",senderNo+"  "+receiverNo);
        Long moduloDec=encryptKeysDec.calculateN(Long.parseLong(senderNo),Long.parseLong(receiverNo));
        System.out.println("Modulo Dec"+moduloDec);
        Long key=encryptKeysDec.findInverse(Long.parseLong(keydec),moduloDec);
        if(key<0){
            key=key+moduloDec;
        }
        Received.logDisplay.addLine("Key to decrypt keyset: "+key,Received.thiscontext);
        System.out.println("key to decrypt:"+key);
        System.out.println("Row: "+StoreKeys.rowTrKey);
        System.out.println("Depth: "+StoreKeys.depthTrKey);
        System.out.println("SeqRow"+StoreKeys.seqStartRow);
        System.out.println("SeqDepth"+StoreKeys.seqStartDepth);
        System.out.println("SelectionPoint"+StoreKeys.selectionPoint);
        System.out.println("Selectionkey"+StoreKeys.selectionKeys);
        System.out.println("length"+StoreKeys.length);
        System.out.println("Sum"+StoreKeys.sumArray);
        System.out.println("Portions decrypt"+StoreKeys.decGroup);
        System.out.println("Msg"+StoreKeys.messageDigest);
        //System.out.println("deded"+key);
        for(int i=0;i<StoreKeys.rowTrKey.size();i++){
            StoreKeys.rowTrKey.set(i,String.valueOf(Long.parseLong(StoreKeys.rowTrKey.get(i))^key));
        }
        for(int i=0;i<StoreKeys.depthTrKey.size();i++){
            StoreKeys.depthTrKey.set(i,String.valueOf(Long.parseLong(StoreKeys.depthTrKey.get(i))^key));
        }
        for(int i=0;i<StoreKeys.seqStartRow.size();i++){
            StoreKeys.seqStartRow.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.seqStartRow.get(i)))^key)));
        }
        for(int i=0;i<StoreKeys.seqStartDepth.size();i++){
            StoreKeys.seqStartDepth.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.seqStartDepth.get(i)))^key)));
        }
        for(int i=0;i<StoreKeys.length.size();i++){
            StoreKeys.length.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.length.get(i)))^key)));
        }
        for(int i=0;i<StoreKeys.selectionKeys.size();i++){
            StoreKeys.selectionKeys.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.selectionKeys.get(i)))^key)));
        }
        for(int i=0;i<StoreKeys.selectionPoint.size();i++){
            StoreKeys.selectionPoint.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.selectionPoint.get(i)))^key)));
        }
        for(int i=0;i<StoreKeys.messageDigest.size();i++){
            StoreKeys.messageDigest.set(i,Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(StoreKeys.messageDigest.get(i)))^key)));
        }
        System.out.println("hfhfvh"+cipherText);

        length=cipherText.length();
        int cipherLength=cipherText.length();
        int cipherParts=length/2;
        Received.logDisplay.addLine("Partition lengths: "+StoreKeys.decGroup.toString(),Received.thiscontext);
        if(cipherLength>2) {
            int offset = cipherLength / cipherParts;
            String partitions[] = new String[StoreKeys.decGroup.size()];
            /*int beg = 0;
            int end = offset;
            int i;
            for (i = 0; i < cipherParts; i++) {
                partitions[i] = cipherText.substring(beg, end);
                beg = end;
                end = beg + offset;
                if (end >= cipherLength && i==cipherParts-1) {
                    partitions[i] += cipherText.substring(beg, cipherLength);
                }
            }*/
            int beg=0;
            for(int z=0;z<StoreKeys.decGroup.size();z++){
                partitions[z] = cipherText.substring(beg,beg+StoreKeys.decGroup.get(z));
                beg=beg+StoreKeys.decGroup.get(z);
            }

            System.out.println();
            System.out.println("Partitions: ");
            String displayPartitions="";
            for(int k=0;k<partitions.length;k++){
                System.out.print(partitions[k]+" ");
                displayPartitions+=partitions[k]+" ";
            }
            Received.logDisplay.addLine("Partitions: "+displayPartitions,Received.thiscontext);
            System.out.println();
            ParallelDecrypt parallelDecrypt[]=new ParallelDecrypt[StoreKeys.decGroup.size()];
            for (int j = 0; j < partitions.length; j++) {
                parallelDecrypt[j]=new ParallelDecrypt(partitions[j],j);
            }

            for(int j=0;j<parallelDecrypt.length;j++){
                plainText+=parallelDecrypt[j].plainText;
            }
            System.out.println("Output plain text:"+plainText);
            Received.logDisplay.addLine("Output plain text: "+plainText,Received.thiscontext);

        }else{
            ParallelDecrypt parallelDecrypt=new ParallelDecrypt(cipherText,0);
            plainText=parallelDecrypt.plainText;
            System.out.println("Output plain text:"+parallelDecrypt.plainText);
            Received.logDisplay.addLine("Output plain text: "+plainText,Received.thiscontext);
        }
        Log.d("Plain:",plainText);
        Received.logDisplay.addLine("Final plain text: "+plainText,Received.thiscontext);
        System.out.println("MsgFinal"+StoreKeys.messageDigest);
        Received.logDisplay.addLine("Message Digest receievd: "+StoreKeys.messageDigest,Received.thiscontext);
        System.out.println("MsgFinal"+decMsgDigest);
        Received.logDisplay.addLine("Deciphered Message Digest: "+decMsgDigest,Received.thiscontext);
        Received.logDisplay.progressBar.setVisibility(View.INVISIBLE);
        Received.logDisplay.logHeader.setText("Decryption Complete");
        for(int i=0;i<StoreKeys.messageDigest.size();i++){
            if(decMsgDigest.get(i)!=StoreKeys.messageDigest.get(i)){
                securelyReceived=false;
                break;
            }
        }
        if(securelyReceived==false){
            Log.d("Message","Message tampered");
        }
        StoreKeys.length.clear();
        StoreKeys.sumArray.clear();
        StoreKeys.selectionKeys.clear();
        StoreKeys.selectionPoint.clear();
        StoreKeys.seqStartDepth.clear();
        StoreKeys.seqStartRow.clear();
        StoreKeys.rowTrKey.clear();
        StoreKeys.depthTrKey.clear();
        StoreKeys.decGroup.clear();
        StoreKeys.messageDigest.clear();
        StoreKeys.picEncryptKey="";
        return plainText;
    }
    static class ParallelDecrypt implements Runnable{
        String cipherText;
        String plainText;
        int seqNo;
        @Override
        public void run() {
            Decryption decryption=new Decryption();
            plainText=decryption.decrypt(cipherText,StoreKeys.rowTrKey,StoreKeys.depthTrKey,seqNo);
        }
        public ParallelDecrypt(String cipherText,int sequenceNo) throws InterruptedException {
            this.cipherText=cipherText;
            this.seqNo=sequenceNo;
            Thread t=new Thread(this);
            t.start();
            t.join();
        }

    }
}
