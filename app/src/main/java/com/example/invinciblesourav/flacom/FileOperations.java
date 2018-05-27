package com.example.invinciblesourav.flacom;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileOperations {
    public static String readFile(String infilename)throws IOException
    {
        String line = null;

            infilename = infilename.substring(6, infilename.length());

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(infilename));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d("Error", ex.getMessage());
        }
        catch(IOException ex) {
            Log.d("Error", ex.getMessage());
        }
        //Toast.makeText(context, ""+line, Toast.LENGTH_SHORT).show();

        return line;
    }

    public static void  writeFile(String outfilename, String buf)throws IOException
    {
        try{
            FileWriter fw=new FileWriter(outfilename,true);
            //BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outfilename)), StandardCharsets.UTF_16));
            //bufferedWriter.write(buf);
            fw.write(buf);
            fw.close();
        }catch(Exception e){
            Log.d("ErrorWrite",e.toString());
        }
    }
}
