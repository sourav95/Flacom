package com.example.invinciblesourav.flacom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by Invincible Sourav on 28-04-2018.
 */

public class EncryptPic {
    public static int height;
    public static int cols;
    public static String randomKey;
    public static String[][] getBitStream(String filename) throws IOException, InterruptedException {
        File imageFile = new File(filename);

        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(imageFile));
        int imageheight = b.getHeight();
        int blockHeight = checkPower(imageheight);
        height = b.getHeight();
        cols = b.getWidth();
        int reducedCols=checkPower(cols);

        int bitmap[][] = new int[height][cols];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < cols; j++) {
                bitmap[i][j] = b.getPixel(j, i);
            }
        }
        String displayPixel="";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(bitmap[i][j] + " ");
                displayPixel+=bitmap[i][j]+" ";
            }
            System.out.println();
            displayPixel+="\n";
        }
        HomePage.logDisplay.addLine("Displaying pixel values: ",HomePage.homecontext);
        HomePage.logDisplay.addLine(displayPixel,HomePage.homecontext);
        String displayFFT="";
        String fft[][] = DFT.matrixDFT(bitmap, height, reducedCols);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < reducedCols; j++) {
                System.out.print(fft[i][j] + " ");
                displayFFT+=fft[i][j]+" ";
            }
            System.out.println();
            displayFFT+="\n";
        }
        HomePage.logDisplay.addLine("Displaying FFT values: ",HomePage.homecontext);
        HomePage.logDisplay.addLine(displayFFT,HomePage.homecontext);
        String finalOutput[][]=new String[height][cols];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < cols; j++) {
                finalOutput[i][j]=String.valueOf(bitmap[i][j]);
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < reducedCols; j++) {
                finalOutput[i][j]=String.valueOf(fft[i][j]);
            }

        }
        System.out.println();
        System.out.println();
        String displayFinal="";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < reducedCols; j++) {
                System.out.print(finalOutput[i][j]+" ");
                displayFinal+=finalOutput[i][j]+" ";
            }
            System.out.println();
            displayFinal+="\n";
        }
        HomePage.logDisplay.addLine("Final image matrix: ",HomePage.homecontext);
        HomePage.logDisplay.addLine(displayFinal,HomePage.homecontext);
        finalOutput=encodeWithRandom(finalOutput);


        return finalOutput;
    }

    private static int checkPower(int number) {

        int i = 1;
        int result = 0;
        while (result <= number) {
            result = (int) Math.pow(2.0, i);
            i++;
        }
        int finalresult = (int) Math.pow(2.0, i - 2);
        return finalresult;
    }
    private static String[][] encodeWithRandom(String [][]array){
        randomKey=generateRandomString();
        System.out.println("Key "+randomKey);
        String displayXor="";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j]=bitWiseXor(array[i][j],randomKey);
                displayXor+=array[i][j]+" ";
            }
            displayXor+="\n";
        }
        HomePage.logDisplay.addLine("XORED image array: ",HomePage.homecontext);
        HomePage.logDisplay.addLine(displayXor,HomePage.homecontext);
        return array;
    }
    public static String[][] decodeWithRandom(String [][]array,String key,int height,int cols){
        String a[][]=new String[height][cols];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < cols; j++) {
                a[i][j]=removeLeadingZeros(decbitWiseXor(array[i][j],key));
                if(a[i][j].startsWith(":")){
                    a[i][j]=a[i][j].substring(1,a[i][j].length());
                }
            }

        }

        return a;
    }

    private static String generateRandomString(){
        Random random=new Random();
        int integer;
        String randomString="";
        for(int i=0;i<8;i++) {
            do {
                integer = random.nextInt(122);
            } while (!(integer>33&&integer < 122));
            char randomChar=(char)integer;
            randomString+=randomChar;
        }
        HomePage.logDisplay.addLine("Random string to XOR: "+randomString,HomePage.homecontext);
        return randomString;
    }
    private static String bitWiseXor(String string1,String string2){

        String finalOutput="";
        //System.out.println(string1+"  "+string2);
        if(string1.length()<string2.length()){
            while (string1.length()<string2.length()){
                string1="0"+string1;
            }
        }else if(string2.length()<string1.length()){
            while (string2.length()<string1.length()){
                string2="0"+string2;
            }
        }
        //System.out.println(string1+"  "+string2);
        for(int i=0;i<string1.length();i++) {
            String binaryString1="";
            String binaryString2="";
            String resultString="";
            int charAscii1 = string1.charAt(i);
            int charAscii2 = string2.charAt(i);
            binaryString1 = Integer.toBinaryString(charAscii1);
            binaryString2 = Integer.toBinaryString(charAscii2);
            //System.out.println(binaryString1+"  "+binaryString2);
            while (binaryString1.length() < 8) {
                binaryString1 = "0"+binaryString1;
            }

            while (binaryString2.length() < 8) {
                binaryString2 = "0"+binaryString2;
            }
            //System.out.println("---------------------------------");
            //System.out.println(binaryString1+"  "+binaryString2);
            for (int k = 0; k < binaryString1.length(); k++) {
                if ((binaryString1.charAt(k) == '0' && binaryString2.charAt(k) == '1') || (binaryString1.charAt(k) == '1' && binaryString2.charAt(k) == '0')) {
                    resultString += "1";
                } else {
                    resultString += "0";
                }

            }
            //System.out.println(resultString);
            finalOutput += convertToChar(resultString);
        }
        return finalOutput;
    }
    private static String decbitWiseXor(String string1,String string2){
        int ignoreCount=0;
        for(int i=0;i<string1.length();i++){
            if(string1.charAt(i)=='!'){
                ignoreCount++;
            }
        }
        int string1length=string1.length()-ignoreCount;
        String finalOutput="";
        //System.out.println("hhhhh"+string1+"  "+string2);
        if(string1length<string2.length()){
            while (string1length<string2.length()){
                string1="0"+string1;
            }
        }else if(string2.length()<string1length){
            while (string2.length()<string1length){
                string2="0"+string2;
            }
        }
        //System.out.println(string1+"  "+string2);
        int l=0;
        //System.out.println("String"+string1length);
        for(int i=0;i<string1.length();i++) {

            String binaryString1="";
            String binaryString2="";
            String resultString="";
            int charAscii1 = string1.charAt(i);
            if((i+1)<string1.length()&&string1.charAt(i+1)=='!'){
                charAscii1=charAscii1-34;
                i++;
            }
            int charAscii2 = string2.charAt(l++);
            binaryString1 = Integer.toBinaryString(charAscii1);
            binaryString2 = Integer.toBinaryString(charAscii2);
            //System.out.println(binaryString1+"  "+binaryString2);
            while (binaryString1.length() < 8) {
                binaryString1 = "0"+binaryString1;
            }

            while (binaryString2.length() < 8) {
                binaryString2 = "0"+binaryString2;
            }
            //System.out.println("---------------------------------");
            //System.out.println(binaryString1+"  "+binaryString2);
            for (int k = 0; k < binaryString1.length(); k++) {
                if ((binaryString1.charAt(k) == '0' && binaryString2.charAt(k) == '1') || (binaryString1.charAt(k) == '1' && binaryString2.charAt(k) == '0')) {
                    resultString += "1";
                } else {
                    resultString += "0";
                }

            }
            //System.out.println(resultString);
            finalOutput += convertToChar(resultString);
        }
        return finalOutput;
    }
    public static String convertToChar(String s) {
        StringBuffer t_string = new StringBuffer(s);
        String revString = t_string.reverse().toString();
        int bitCount = 0;
        String cipher = "";
        int sum, k;

        for (int i = 0; i < revString.length(); ) {
            bitCount = 0;
            sum = 0;
            k = 0;
            while (bitCount < 8) {
                if (revString.charAt(i) == '1') {
                    sum = sum + power(2, k);

                }
                bitCount = bitCount + 1;
                i++;
                k++;
            }

            if((sum>=0 && sum<34)|| sum==127||sum==129||sum==141||sum==143||sum==144||sum==157||sum==160) {
                sum = sum + 34;
                cipher += String.valueOf((char) sum) + "!";
            }else {
                cipher += String.valueOf((char) sum);
            }
            //System.out.print(sum%255 + " ");

        }
        return cipher;
    }
    public static int power(int x,int y){
        int prod=1;
        if(y==0){
            return 1;
        }else if(y==1){
            return x;
        }else {
            for (int i = 0; i < y; i++) {
                prod = prod * x;
            }
            return prod;
        }
    }
    private static String removeLeadingZeros(String string){
        int i=0;
        while (i<string.length()&&string.charAt(i)=='0'){
            i++;
        }
        return string.substring(i,string.length());
    }


}