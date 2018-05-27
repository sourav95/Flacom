package com.example.invinciblesourav.flacom;

import java.util.ArrayList;

public class Transpositions {
    protected String sum;
    protected long rowCols,depth;
    public ArrayList<String> rowColTrKey=new ArrayList<String>();
    public ArrayList<String> depthTrKey=new ArrayList<String>();

    public Transpositions(String key, long rowCols, long depth){
        long rowColMaximum,depthMaximum;
        long t_size;
        long extraInt;
        this.sum=key;
        this.rowCols=rowCols;
        this.depth=depth;
        getColRowKey();
        getDepthKey();
        rowColMaximum=getRowColMaximum();
        depthMaximum=getDepthMaximum();
        if(rowCols>rowColTrKey.size()&& rowCols<=9) {
            t_size=rowCols-rowColMaximum;
            extraInt=rowColMaximum;
            for (long i = 1; i<t_size; i++)
                rowColTrKey.add(String.valueOf(extraInt + i));
            if(rowCols>rowColTrKey.size()){
                for(int i=0;i<rowCols;i++){
                    int flag=0;
                    for(int j=0;j<rowColTrKey.size();j++){
                        if(i== Integer.parseInt(rowColTrKey.get(j))){
                            flag=1;
                            break;
                        }
                    }
                    if(flag==0) {
                        rowColTrKey.add(String.valueOf(i));
                    }
                }
            }


        }
        if(depth>depthTrKey.size()&& depth<=9) {
            t_size=depth-depthMaximum;
            extraInt = depthMaximum;
            for (long i = 1; i<t_size; i++)
                depthTrKey.add(String.valueOf(extraInt + i));
            if(depth>depthTrKey.size()){
                for(int i=0;i<depth;i++){
                    int flag=0;
                    for(int j=0;j<depthTrKey.size();j++){
                        if(i== Integer.parseInt(depthTrKey.get(j))){
                            flag=1;
                            break;
                        }
                    }
                    if(flag==0) {
                        depthTrKey.add(String.valueOf(i));
                    }
                }
            }

        }

        if(rowCols>9) {
            t_size=rowCols-9;
            extraInt=9;
            for (long i = 1; i<t_size; i++)
                depthTrKey.add(String.valueOf(extraInt + i));

        }

        if(depth>9) {
            t_size=depth-9;
            extraInt = 9;
            for (long i = 1; i<t_size; i++)
                depthTrKey.add(String.valueOf(extraInt + i));

        }

    }
    public Transpositions(long rows, long depth, ArrayList<String> rowKey, ArrayList<String> depthkey){
        this.rowCols=rows;
        this.depth=depth;
        this.rowColTrKey=rowKey;
        this.depthTrKey=depthkey;
    }
    private void getColRowKey(){

        for(int i=0;i<sum.length();i++){
            if(Long.parseLong(String.valueOf(sum.charAt(i)))<rowCols){
                int flag=0;
                for(int j=0;j<rowColTrKey.size();j++){
                    if(rowColTrKey.get(j).equals(Character.toString(sum.charAt(i)))){
                        flag++;
                    }
                }
                if (flag==0) {
                    rowColTrKey.add(Character.toString(sum.charAt(i)));
                }
            }
        }

    }
    private void getDepthKey(){

        for(int i=0;i<sum.length();i++){
            if(Long.parseLong(String.valueOf(sum.charAt(i)))<depth){
                int flag=0;
                for(int j=0;j<depthTrKey.size();j++){
                    if(depthTrKey.get(j).equals(Character.toString(sum.charAt(i)))){
                        flag++;
                    }
                }

                if (flag==0) {
                    depthTrKey.add(Character.toString(sum.charAt(i)));
                }
            }
        }

    }
    private long getDepthMaximum(){
        long max=0;
        for (int i=0;i<depthTrKey.size();i++){
            if(i==0){
                max= Long.parseLong(depthTrKey.get(i));
            }
            if(Double.parseDouble(depthTrKey.get(i))>max)
                max= Long.parseLong(depthTrKey.get(i));
        }

        return max;
    }
    private long getRowColMaximum(){
        long max=0;
        for (int i=0;i<rowColTrKey.size();i++){
            if(i==0){
                max= Long.parseLong(rowColTrKey.get(i));
            }
            if(Double.parseDouble(rowColTrKey.get(i))>max)
                max= Long.parseLong(rowColTrKey.get(i));
        }

        return max;
    }
    public String[][][] transposeColumn(String array3D[][][]){
        int t_key=0;
        String changedArray[][][]=new String[Integer.parseInt(Long.toString(depth))][Integer.parseInt(Long.toString(rowCols))][Integer.parseInt(Long.toString(rowCols))];

            for(int x=0;x<depth;x++) {
                for (int y = 0; y < rowCols; y++) {
                    for (int z = 0; z < rowCols; z++) {
                        t_key= Integer.parseInt(rowColTrKey.get(z));
                        changedArray[x][y][t_key] = array3D[x][y][z];
                    }
                }
            }

        return changedArray;
    }
    public String[][][] transposeRow(String array3D[][][]){
        int t_key=0;
        String changedArray[][][]=new String[Integer.parseInt(Long.toString(depth))][Integer.parseInt(Long.toString(rowCols))][Integer.parseInt(Long.toString(rowCols))];
            for(int x=0;x<depth;x++) {
                for (int y = 0; y < rowCols; y++) {
                    t_key= Integer.parseInt(rowColTrKey.get(y));
                    for (int z = 0; z < rowCols; z++) {
                        changedArray[x][t_key][z] = array3D[x][y][z];

                    }
                }
            }

        return changedArray;
    }
    public String[][][] transposeDepth(String array3D[][][]){
        int t_key=0;
        String changedArray[][][]=new String[Integer.parseInt(Long.toString(depth))][Integer.parseInt(Long.toString(rowCols))][Integer.parseInt(Long.toString(rowCols))];


            for(int x=0;x<depth;x++) {
                t_key= Integer.parseInt(depthTrKey.get(x));
                //System.out.println("Transposing"+t_key);
                for (int y = 0; y < rowCols; y++) {
                    for (int z = 0; z < rowCols; z++) {
                        changedArray[t_key][y][z] = array3D[x][y][z];
                    }
                }
            }

        return changedArray;
    }
    public String[][][] revTransposeColumn(String array3D[][][]){
        int t_key=0;
        String changedArray[][][]=new String[Integer.parseInt(Long.toString(depth))][Integer.parseInt(Long.toString(rowCols))][Integer.parseInt(Long.toString(rowCols))];

        for(int x=0;x<depth;x++) {
            for (int y = 0; y < rowCols; y++) {
                for (int z = 0; z < rowCols; z++) {
                    t_key= Integer.parseInt(rowColTrKey.get(z));
                    changedArray[x][y][z] = array3D[x][y][t_key];
                }
            }
        }

        return changedArray;
    }
    public String[][][] revTransposeRow(String array3D[][][]){
        int t_key=0;
        String changedArray[][][]=new String[Integer.parseInt(Long.toString(depth))][Integer.parseInt(Long.toString(rowCols))][Integer.parseInt(Long.toString(rowCols))];
        for(int x=0;x<depth;x++) {
            for (int y = 0; y < rowCols; y++) {
                t_key= Integer.parseInt(rowColTrKey.get(y));
                for (int z = 0; z < rowCols; z++) {
                    changedArray[x][y][z] = array3D[x][t_key][z];

                }
            }
        }

        return changedArray;
    }
    public String[][][] revTransposeDepth(String array3D[][][]){
        int t_key=0;
        String changedArray[][][]=new String[Integer.parseInt(Long.toString(depth))][Integer.parseInt(Long.toString(rowCols))][Integer.parseInt(Long.toString(rowCols))];


        for(int x=0;x<depth;x++) {
            t_key= Integer.parseInt(depthTrKey.get(x));
            //System.out.println("Transposing"+t_key);
            for (int y = 0; y < rowCols; y++) {
                for (int z = 0; z < rowCols; z++) {
                    changedArray[x][y][z] = array3D[t_key][y][z];
                }
            }
        }

        return changedArray;
    }




}
