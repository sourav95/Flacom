package com.example.invinciblesourav.flacom;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Invincible Sourav on 18-05-2018.
 */

public class OtherFileHandler {
    public static String randomString = "";

    public static String getXoredByteString(String fname)//gives ByteString of file
    {
        StringBuffer sb = new StringBuffer();
        StringBuffer sb1 = new StringBuffer();
        String xoredString = "";
        try {
            int buffLength = 0;
            String buff[];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(fname));
            for (int b; (b = bufferedInputStream.read()) != -1; ) {
                String s = "0000000" + Integer.toBinaryString(b);
                s = s.substring(s.length() - 8);
                sb.append(s).append(" ");
                sb1.append(s).append(" ");
                buffLength++;
                if (buffLength == 100) {
                    String binaryString[] = sb.toString().split(" ");
                    xoredString += doXor(binaryString);
                    buffLength = 0;
                    sb.delete(0, sb.length());
                }
            }
            String binaryString[] = sb.toString().split(" ");
            xoredString += doXor(binaryString);
            buffLength = 0;
            sb.delete(0, sb.length());
            System.out.println("Orijinal String: " + sb1);
            System.out.println(sb1.length());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return xoredString;
    }

    public static String doXor(String array[]) {
        String result = "";
        for (int i = 0; i < array.length; i++) {
            result += convertToChar(binaryXor(array[i], randomString)) + " ";
        }
        System.out.println(result);
        return result;
    }

    public static String binaryXor(String string1, String string2) {
        String resultString = "";
        if (string1.length() < string2.length()) {
            while (string1.length() < string2.length()) {
                string1 = "0" + string1;
            }
        } else if (string2.length() < string1.length()) {
            while (string2.length() < string1.length()) {
                string2 = "0" + string2;
            }
        }
        for (int k = 0; k < string1.length(); k++) {
            if ((string1.charAt(k) == '0' && string2.charAt(k) == '1') || (string1.charAt(k) == '1' && string2.charAt(k) == '0')) {
                resultString += "1";
            } else {
                resultString += "0";
            }

        }
        return resultString;

    }

    public static String decBinaryXor(String s1, String s2) {

        int integerEqv;
        if (s1.endsWith("!")) {
            integerEqv = s1.charAt(0);
            integerEqv = integerEqv - 34;
        } else {
            integerEqv = s1.charAt(0);
        }
        String string1 = Integer.toBinaryString(integerEqv);
        String string2 = s2;
        String resultString = "";
        if (string1.length() < string2.length()) {
            while (string1.length() < string2.length()) {
                string1 = "0" + string1;
            }
        } else if (string2.length() < string1.length()) {
            while (string2.length() < string1.length()) {
                string2 = "0" + string2;
            }
        }
        for (int k = 0; k < string1.length(); k++) {
            if ((string1.charAt(k) == '0' && string2.charAt(k) == '1') || (string1.charAt(k) == '1' && string2.charAt(k) == '0')) {
                resultString += "1";
            } else {
                resultString += "0";
            }

        }
        if (resultString.length() < 8) {
            while (resultString.length() < 8) {
                resultString = "0" + resultString;
            }
        }
        //System.out.println(resultString);
        return resultString;

    }

    public static void getFileByte(String ofname, String info)//outputs byteStream to file
    {
        System.out.println(info.length());
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(ofname));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ofname)));
            Scanner scanner = new Scanner(info);
            //System.out.println(info);
            while (scanner.hasNext()) {
                String b = scanner.next();
                //System.out.println(b);

                bufferedOutputStream.write(convertToCharFinal(b));

                }
                bufferedOutputStream.close();

                StringBuffer sb = new StringBuffer();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(ofname));
                for (int b; (b = bufferedInputStream.read()) != -1; ) {
                    String s = "0000000" + Integer.toBinaryString(b);
                    s = s.substring(s.length() - 8);
                    sb.append(s).append(" ");


                }
                System.out.println("Orijinal string"+sb.toString());
                System.out.println(sb.toString().length());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String generateRandomString(){
        Random random=new Random();
        int integer;
        String randomString="";
        for(int i=0;i<8;i++) {
            do {
                integer = random.nextInt(122);
            } while (!(integer>33&&integer < 122));
            char randomBit=Integer.toBinaryString(integer).charAt(Integer.toBinaryString(integer).length()-1);
            randomString+=randomBit;
        }
        OtherFileHandler.randomString=randomString;
        return randomString;
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
    public static int convertToCharFinal(String s) {
        StringBuffer t_string = new StringBuffer(s);
        String revString = t_string.reverse().toString();
        int bitCount = 0;
        String cipher = "";
        int sum=0, k;

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




            //System.out.print(sum%255 + " ");

        }
        return sum;
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
}
