package com.example.invinciblesourav.flacom;

import android.graphics.Bitmap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;

/**
 * Created by Invincible Sourav on 16-05-2018.
 */

public class DecryptPic {
    public static void getDecPic(String bitmap[][],int height,int cols,String filename,String extension) throws FileNotFoundException {
        int reducedCols=checkPower(cols);
        int ifft[][] = DFT.matrixIDFT(bitmap, height, reducedCols);
        Bitmap b1 = Bitmap.createBitmap(cols, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < cols; j++) {
                if (j < reducedCols) {
                    System.out.print(ifft[i][j] + " ");
                    b1.setPixel(j, i, ifft[i][j]);
                } else {
                    System.out.print(bitmap[i][j] + " ");
                    b1.setPixel(j, i, Integer.parseInt(bitmap[i][j]));
                }
            }
            System.out.println();
        }
        String timestamp=String.valueOf(System.currentTimeMillis());
        FileOutputStream fileOutputStream = new FileOutputStream("/sdcard/SendSec/Downloads/"+timestamp+extension);
        b1.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        Received.downloadedFileUri.add(filename+"%"+"/sdcard/SendSec/Downloads/" + timestamp+extension.toLowerCase()+"%"+InvokeDecryption.securelyReceived);
        Received.tinyDB.putListString("fileStorageUri",Received.downloadedFileUri);
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
    public static String[][] generateArray(String matrix,int rows,int columns){
        String bitmap[][]=new String[rows][columns];
        String coefficients[]=matrix.split(" ");
        int k=0;
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                bitmap[i][j]=coefficients[k++];
            }
        }
        return bitmap;
    }
}
