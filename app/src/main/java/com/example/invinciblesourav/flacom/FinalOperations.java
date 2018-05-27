package com.example.invinciblesourav.flacom;

public class FinalOperations {
    public String convertBit(String array[][][], int rows, int height) {
        String temp_string = "";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < rows; k++) {
                    switch (array[i][j][k]) {
                        case "A":
                            temp_string += "00";
                            break;
                        case "C":
                            temp_string += "01";
                            break;
                        case "G":
                            temp_string += "10";
                            break;
                        case "T":
                            temp_string += "11";
                            break;
                    }
                }
            }
        }
        for (int i = 0; i < DNAProcesses.residualDNA.length; i++) {
            switch (DNAProcesses.residualDNA[i]) {
                case "A":
                    temp_string += "00";
                    break;
                case "C":
                    temp_string += "01";
                    break;
                case "G":
                    temp_string += "10";
                    break;
                case "T":
                    temp_string += "11";
                    break;
            }
        }
        return temp_string;
    }

    public String finalConversion(String s) {
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
            System.out.print("prim "+sum + " ");

            if((sum>=0 && sum<34)|| sum==127||sum==129||sum==141||sum==143||sum==144||sum==157||sum==160 || sum==60){
                    sum=sum+34;
                    cipher += String.valueOf((char) sum)+"!";

            }else {
                cipher += String.valueOf((char) sum);
            }
            System.out.print(sum + " ");

        }



        //System.out.println();
        StringBuffer t_finalString=new StringBuffer(cipher);
        return t_finalString.reverse().toString();
    }
    public String decfinalConversion(String s) {
        StringBuffer t_string = new StringBuffer(s);
        String revString = t_string.reverse().toString();
        int bitCount = 0;
        String cipher = "";
        int sum, k;

        for (int i = 0; i < revString.length(); ) {
            bitCount = 0;
            sum = 0;
            k = 0;
            while (bitCount < 16) {
                if (revString.charAt(i) == '1') {
                    sum = sum + power(2, k);

                }
                bitCount = bitCount + 1;
                i++;
                k++;
            }


            cipher += String.valueOf((char) sum);

            System.out.print(sum + " ");

        }



        //System.out.println();
        StringBuffer t_finalString=new StringBuffer(cipher);
        return t_finalString.reverse().toString();
    }
    public int power(int x,int y){
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
    public String RevconvertBit(String string ) {
        String temp_string = "";
        for (int k = 0; k < string.length(); k++) {
            switch (string.charAt(k)) {
                case 'A':
                    temp_string += "00";
                    break;
                case 'C':
                    temp_string += "01";
                    break;
                case 'G':
                    temp_string += "10";
                    break;
                case 'T':
                    temp_string += "11";
                    break;
            }
        }
        return temp_string;
    }

}
