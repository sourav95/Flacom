package com.example.invinciblesourav.flacom;

public class MessageDigest {
   String sum;

    public MessageDigest(String sum) {
        this.sum = sum;
    }
    public int generateDigest(){
        InitialOperations initialOperations=new InitialOperations();
        String total="0";
        int msgDigest=0;
        for(int i=0;i<sum.length();i=i+1){
            total=initialOperations.add_large(String.valueOf(sum.charAt(i)),total);
        }
        System.out.println("Message digest total: "+total);
        if(!total.equals("0")) {
            msgDigest = Integer.parseInt(total) % (sum.length() / 2);
        }
        return msgDigest;
    }
}
