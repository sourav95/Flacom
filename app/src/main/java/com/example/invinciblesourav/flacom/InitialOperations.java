package com.example.invinciblesourav.flacom;

import java.util.ArrayList;

public class InitialOperations {
    private int bitPatternLength;
    public static boolean utfFlag=false;
    public  InitialOperations(){

    }

   /* public String asciitobinary(int asciibit,int position){//Method to convert the given input String into binary pattern
        int number=asciibit;
        int t_rem;
        String output="";               //Stores the output pattern as a String adding leading 0s id necessary
        int sortLength=0;               //Stores the number of bits that fall sort from 8
        while(number>0){                //Loop to form the binary pattern from asciibit
            t_rem=number%2;
            output=output+Integer.toString(t_rem);
            number=number/2;
        }
        bitPatternLength=output.length();

        if(bitPatternLength<8){sortLength=8-bitPatternLength;}
        else if(bitPatternLength%8!=0){

            utfFlag=true;
            Test.utfCharacterBits.add(asciibit);
            Test.utfLength.add(output.length());
            Test.utfPosition.add(position);
            output="";
        }
        for(int j=0;j<sortLength;j++){
            output=output+"0";          //Add extra padding bits at the end to make bitpatternlength equal to 8
        }
        StringBuffer buffer = new StringBuffer(output);
        buffer.reverse();               //Reverse the bit pattern to obtain the actual bit string
        return buffer.toString();
    }*/
   public String asciitobinary(int ascii, int position){
       String utfHex,decimalHex,bitPattern="";
       int unpaddedLength;
       utfHex = Integer.toHexString(ascii).toString();

       for (int i = 0; i < utfHex.length(); i++) {
           char temp = utfHex.charAt(i);
           decimalHex = String.valueOf(temp);
           switch (temp) {
               case 'A':
                   decimalHex = "10";
                   break;
               case 'B':
                   decimalHex = "11";
                   break;
               case 'C':
                   decimalHex = "12";
                   break;
               case 'D':
                   decimalHex = "13";
                   break;
               case 'E':
                   decimalHex = "14";
                   break;
               case 'F':
                   decimalHex = "15";
                   break;
               case 'a':
                   decimalHex = "10";
                   break;
               case 'b':
                   decimalHex = "11";
                   break;
               case 'c':
                   decimalHex = "12";
                   break;
               case 'd':
                   decimalHex = "13";
                   break;
               case 'e':
                   decimalHex = "14";
                   break;
               case 'f':
                   decimalHex = "15";
                   break;
           }
           String temp_pattern = Integer.toBinaryString(Integer.parseInt(decimalHex));

           if (temp_pattern.length() < 4) {
               while (temp_pattern.length() < 4) {
                   temp_pattern = "0" + temp_pattern;
               }


           }
           //System.out.println(temp_pattern);
           bitPattern = bitPattern + temp_pattern;

       }
       unpaddedLength = bitPattern.length();
       //System.out.println(bitPattern);
       while (bitPattern.length() % 16 != 0) {
           bitPattern = "0" + bitPattern;
       }
        return bitPattern;
   }
    public String decAsciitobinary(int ascii){
        String utfHex,decimalHex,bitPattern="";
        int unpaddedLength;
        System.out.println("AScii "+ascii);
        utfHex = Integer.toHexString(ascii).toString();

        for (int i = 0; i < utfHex.length(); i++) {
            char temp = utfHex.charAt(i);
            decimalHex = String.valueOf(temp);
            switch (temp) {
                case 'A':
                    decimalHex = "10";
                    break;
                case 'B':
                    decimalHex = "11";
                    break;
                case 'C':
                    decimalHex = "12";
                    break;
                case 'D':
                    decimalHex = "13";
                    break;
                case 'E':
                    decimalHex = "14";
                    break;
                case 'F':
                    decimalHex = "15";
                    break;
                case 'a':
                    decimalHex = "10";
                    break;
                case 'b':
                    decimalHex = "11";
                    break;
                case 'c':
                    decimalHex = "12";
                    break;
                case 'd':
                    decimalHex = "13";
                    break;
                case 'e':
                    decimalHex = "14";
                    break;
                case 'f':
                    decimalHex = "15";
                    break;
            }
            String temp_pattern = Integer.toBinaryString(Integer.parseInt(decimalHex));

            if (temp_pattern.length() < 4) {
                while (temp_pattern.length() < 4) {
                    temp_pattern = "0" + temp_pattern;
                }


            }
            //System.out.println(temp_pattern);
            bitPattern = bitPattern + temp_pattern;

        }
        unpaddedLength = bitPattern.length();
        //System.out.println(bitPattern);
        while (bitPattern.length() % 8 != 0) {
            bitPattern = "0" + bitPattern;
        }
        return bitPattern;
    }
    public  int getBinaryPattLen(String s){//Method to find out the length of the obtained binary pattern
        return s.length();
    }
    public ArrayList getPrimeNosWithinRange(long length){
        ArrayList<Integer> primeList=new ArrayList<>();
        int t_divisibleFlag;
        for(int i=2;i<=length;i++){
            t_divisibleFlag=0;
            for(int j=2;j<i;j++){
                if(i%j==0)
                    t_divisibleFlag++;
            }
            if(t_divisibleFlag==0)
                primeList.add(i);
        }
        return primeList;
    }
    public ArrayList findposOfOne(String input){
        ArrayList<Integer> onePositions=new ArrayList<Integer>();
        for(int i=0;i<input.length();i++){
            if(input.charAt(i)=='1'){
                onePositions.add(i+1);
            }
        }
        return  onePositions;
    }
    public String getExponent(long a, long b){
       long t_product=1,t_leftOffbit=-1;
       long longIntsize=2147482648;
        for(long i=b;i>=1;i--){
           t_product=t_product*a;
           if(longIntsize-t_product<1000){
               t_product=t_product/10;
               t_leftOffbit=t_product%10;
               t_leftOffbit=t_leftOffbit*a;
           }
       }
       if(t_leftOffbit==-1)
           return Long.toString(t_product);
       else {
            String s= Long.toString(t_product);
            s=s+"0";
            String sub1=s.substring(s.length()/2,s.length()-1);
            String sub2=s.substring(0,(s.length()/2)-1);
            long sum1= Long.parseLong(sub1);
            sum1=sum1+t_leftOffbit;
            if(Long.toString(sum1).length()>sub1.length()){
                sub1= Long.toString(sum1).substring(1,sub1.length()-1);
            }
            sub2= Long.toString(Long.parseLong(sub2)+1);
            sub2=sub2+sub1;
            return sub2;
       }
    }

    public void setPaddingAddenda(ArrayList<String> addenda){
        long addendaSize=0;
        long diff;
        ArrayList<String> paddedAddenda=new ArrayList<>();
        for(int i=0;i<addenda.size();i++){
            if(i==0)
                addendaSize=addenda.get(i).length();
            else
                if(addenda.get(i).length()>addendaSize)
                    addendaSize=addenda.get(i).length();
        }
        for(int i=0;i<addenda.size();i++){
            if(addenda.get(i).length()<addendaSize){
                diff=addendaSize-addenda.get(i).length();
                String t_pad="";
                t_pad=addenda.get(i);
                for(long k=0;k<diff;k++)
                   t_pad="0"+t_pad;
                paddedAddenda.add(t_pad);
            }
            else
                paddedAddenda.add(addenda.get(i));
        }
        for(int i=0;i<paddedAddenda.size();i++){
            //System.out.println(" "+paddedAddenda.get(i));
        }
        addAll(paddedAddenda);
    }
    public void addAll(ArrayList<String> arrayList){
        String lower_nibble;
        long sum=0;
        for (int i=0;i<arrayList.size();i++){
         lower_nibble=arrayList.get(i).substring((arrayList.get(i).length())/2,arrayList.get(i).length());
            //lower_nibble=arrayList.get(i).substring(0,(arrayList.get(i).length()/2)-1);
            sum=sum+ Long.parseLong(lower_nibble);
        }
        //System.out.println();
        //System.out.println(sum);
    }
    public String calculate_power(String num1, String num2)
    {
        String sum=num1;
        String iterate=num1;
        for (int j = 0; j< Integer.parseInt(num2)-1; j++) {
           // System.out.println("j="+j);
            sum=iterate;
            for (int i = 0; i < Integer.parseInt(num1)-1; i++) {
               // System.out.println(iterate+" "+"+"+sum);
                iterate = add_large(iterate, sum);

            }

        }
        return iterate;

    }

    public String add_large(String num1, String num2)
    {

        String result="",minnum="",maxnum="";
        int minlength=0,maxlength=0,index=0,minlengthcopy=0,maxlengthcopy=0,carry=0,digitadd=0,digitaddcopy=0;
        char dig1,dig2;

        if(num1.length()<=num2.length())
        {
            minlength=num1.length();
            maxlength=num2.length();
            minnum=num1;
            maxnum=num2;
        }
        else
        {
            minlength=num2.length();
            maxlength=num1.length();
            minnum=num2;
            maxnum=num1;
        }

        minlengthcopy=minlength;
        maxlengthcopy=maxlength;
        minlength--;
        maxlength--;

        for(int i=minlengthcopy-1;i>=0;i--)
        {
            dig1=maxnum.charAt(maxlength--);
            dig2=minnum.charAt(minlength--);

            digitadd=carry+ Character.getNumericValue(dig1)+ Character.getNumericValue(dig2);
            digitaddcopy=digitadd;
            if(digitadd>9)
            {
                if(digitadd%10==0)
                {
                    carry=digitadd/10;
                    digitadd=0;
                }
                else
                {
                    carry=digitadd/10;
                    digitadd%=10;
                }
            }
            else
                carry=0;

            result= Integer.toString(digitadd)+result;

        }

        index=maxlength-minlength;

        for(int i=index-1;i>=0;i--)
        {
            dig1=maxnum.charAt(i);

            digitadd=carry+ Character.getNumericValue(dig1);
            digitaddcopy=digitadd;
            if(digitadd>9)
            {
                if(digitadd%10==0)
                {
                    carry=digitadd/10;
                    digitadd=0;
                }
                else
                {
                    carry=digitadd/10;
                    digitadd%=10;
                }
            }
            else
                carry=0;

            result= Integer.toString(digitadd)+result;
        }


        if(digitaddcopy>9 )
            result= Integer.toString(carry)+result;

        return result;
    }

    public String complementPrimePos(String s, ArrayList<Integer> primeList){
        String t_st[]=s.split("");
        String compSt="";
        for(int i=0;i<primeList.size();i++){
            if(t_st[primeList.get(i)].equals("1")){
                t_st[primeList.get(i)]="0";
            }
            else if(t_st[primeList.get(i)].equals("0"))
                t_st[primeList.get(i)]="1";
        }
       for (int i=0;i<t_st.length;i++){
            compSt+=t_st[i];
       }
       return compSt;
    }
    public String reversePatt(String s){
        StringBuffer stringBuffer=new StringBuffer(s);
        stringBuffer.reverse();
        return stringBuffer.toString();
    }
    public String xrcal(String s)
    {
        String pattern1,pattern2,xoredPattern="";
        char t_bit1,t_bit2,resBit;
        /*long l=s.length();
        int i=0,j=i+1;
        String sf="";
        sf=sf+s.charAt(0);
        while(j<l)
        {
            x1=s.charAt(i);
            x2=s.charAt(j);
            j++;
            if((x1=='0' && x2=='0')||(x1=='1' && x2=='1'))
                x='0';
            else
                x='1';
            sf=sf+x;
        }*/
        pattern1=s.substring(0,s.length()/2);
        pattern2=s.substring(s.length()/2,s.length());
        StringBuffer revPattern2=new StringBuffer(pattern2);
        pattern2=revPattern2.reverse().toString();
        //System.out.println(pattern1+"\n"+pattern2);
        for(int i=0;i<pattern1.length();i++){
            t_bit1=pattern1.charAt(i);
            t_bit2=pattern2.charAt(i);
            if((t_bit1=='0' && t_bit2=='0')||(t_bit1=='1' && t_bit2=='1'))
                resBit='0';
            else
                resBit='1';
            xoredPattern=xoredPattern+resBit;

        }
        StringBuffer t_holdRes=new StringBuffer(xoredPattern);
        xoredPattern=t_holdRes.reverse().toString();
        xoredPattern=pattern1+xoredPattern;
        return xoredPattern;
    }
}
