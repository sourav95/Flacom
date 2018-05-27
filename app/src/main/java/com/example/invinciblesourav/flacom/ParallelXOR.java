package com.example.invinciblesourav.flacom;

/**
 * Created by Invincible Sourav on 22-05-2018.
 */

public class ParallelXOR {
    public static String getData(String array[],String plainText) throws InterruptedException {
        String concarenatedString="";
        int arrayLength=array.length;
        int parts=arrayLength/1000;
        int remainingPart=arrayLength%1000;
        System.out.println("Parts= "+parts+" "+"rem "+remainingPart);
        int start=0;
        int end;
        if(arrayLength>1000) {
            end = start + 1000;
        }else{
            end=0;
        }
        ParallelXOROperation parallelXOROperation[]=new ParallelXOROperation[parts+1];
        for(int i=0;i<parts;i++){
            parallelXOROperation[i]=new ParallelXOROperation(array,plainText,start,end);
            concarenatedString+=parallelXOROperation[i].bitString;
            start=end;
            end=end+1000;
        }
        if(arrayLength>1000) {
            end = end - 1000;
        }
        parallelXOROperation[parts]=new ParallelXOROperation(array,plainText,end,arrayLength-1);
        concarenatedString+=parallelXOROperation[parts].bitString;
        System.out.println("bitString "+concarenatedString);
        return concarenatedString;
    }
    static class ParallelXOROperation implements Runnable{
        int startLimit,endLimit;
        String encryptedBits[];
        String plainText;
        String bitString="";
        @Override
        public void run() {
            System.out.println(startLimit+" "+endLimit);
            for(int i=startLimit;i<endLimit;i++){

                bitString+=OtherFileHandler.decBinaryXor(encryptedBits[i],plainText)+" ";
            }

        }
        public ParallelXOROperation(String bitString[],String plainText,int startLimit,int endlimit) throws InterruptedException {
            this.encryptedBits=bitString;
            this.plainText=plainText;
            this.startLimit=startLimit;
            this.endLimit=endlimit;
            Thread t=new Thread(this);
            t.start();
            t.join();
        }


    }
}
