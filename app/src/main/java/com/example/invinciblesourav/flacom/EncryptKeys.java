package com.example.invinciblesourav.flacom;

import android.util.Log;

import java.util.ArrayList;

public class EncryptKeys {
    public long calculateN(long num1,long num2){
        long result,result1,result2;
        long rand;
        result=~num1^~num2;
        result1=result^(num1*num2);
        Log.d("RESULT",result1+"");
        result2=result1%(findMSB(result1)*1000);
        return result2;
    }
    private long findMSB(long num){
       long quotient=0;
       while(num!=0){
           quotient=num;
           num=num/10;
       }
       return quotient;
    }
    public ArrayList<Long> findMultiplicative(long modulo){
        ArrayList<Long> inverseList=new ArrayList<>();
        for(long i=1;i<modulo;i++){
            if(checkInverse(i,modulo)) {
                if (!checkDuplicate(i,inverseList)) {
                    long inverse = findInverse(i, modulo);
                    if (inverse < 0) {
                        inverse = inverse + modulo;
                    }
                    //System.out.println(i+" "+inverse);
                    inverseList.add(i);
                    inverseList.add(inverse);
                }
            }
        }
        return inverseList;
    }
    private boolean checkInverse(long i,long n){
        long r1,r2,r;
        r1=i;
        r2=n;
        while (r2>0){
            r=r1%r2;
            r1=r2;
            r2=r;

        }

        if(r1!=1){
            return false;
        }else {
            return true;
        }
    }
    public long findInverse(long i,long n){
        long r1,r2,r,t1,t2,t,q;
        if(i>n) {
            r1 = i;
            r2 = n;
        }else{
            r1 = n;
            r2 = i;
        }
        t1=0;
        t2=1;
        while (r2>0){
            q=r1/r2;
            r=r1-(q*r2);r1=r2;r2=r;
            t=t1-(q*t2);t1=t2;t2=t;
            //System.out.println(q+" "+r1+" "+r2+" "+r+" "+t1+" "+t2+" "+t);
        }

        return t1;
    }
    private boolean checkDuplicate(long key,ArrayList<Long> array){
        for(int i=0;i<array.size();i++){
            if(key==array.get(i)){
                return true;
            }
        }
        return false;
    }


}
