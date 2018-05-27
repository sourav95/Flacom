package com.example.invinciblesourav.flacom;

public class ArrayOperations {
    public int arraySize;
    int rows, cols;
    String bitPattern;
    private String residualString;
    private String residualArray[];
    public String holderArray[][];

    public ArrayOperations(long bitpatternLength, String bitPattern) {
        this.bitPattern = bitPattern;
        arraySize = calculateSize(bitpatternLength);
        holderArray = new String[arraySize][arraySize];
        rows = (int) Math.sqrt(Double.parseDouble(String.valueOf(arraySize)));
        cols = rows;
        makeArray();
        displayArray();
    }

    private int calculateSize(long patternLength) {
        int size = 0;
        int sampleInt = 0;
        for (int i = 1; i * i <= patternLength; i++) {
            sampleInt = i;
        }
        size = sampleInt * sampleInt;
        return size;
    }

    private void makeArray() {
        int k = 0;
        // System.out.println(rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                holderArray[i][j] = String.valueOf(bitPattern.charAt(k));
                k++;
            }
        }
        residualString = bitPattern.substring(rows * cols, bitPattern.length());
        //residualArray=new String[residualString.length()];
        residualArray = residualString.split("");
        System.out.println();
        System.out.println("Printing reisdual");
        for(int i=0;i<residualArray.length;i++){
            System.out.print(residualArray[i]+" "+"["+i+"] ");
        }
        System.out.println();
        //System.out.println("Residual array");
        //for (int i = 0; i < residualArray.length; i++) {
          //  System.out.print(residualArray[i] + "  ");
        //}
        //System.out.println();
        //System.out.println("2D array for transpositions : ");
    }

    public void displayArray() {
        String displayresult="";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(holderArray[i][j] + "   ");
                displayresult+=holderArray[i][j]+" ";
            }
            System.out.println();
            displayresult+="\n";
        }
        //HomePage.logDisplay.addLine(displayresult,Received.thiscontext);
    }

    public void shiftLeft() {
        String temp = "";
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                if (j == 0) {
                    temp = holderArray[i][j];
                    holderArray[i][j] = holderArray[i][j + 1];
                } else if (j == cols - 1) {
                    holderArray[i][j] = temp;
                } else {

                    holderArray[i][j] = holderArray[i][j + 1];
                }
            }
    }

    public void shiftRight() {
        String temp = " ";
        for (int i = 0; i < rows; i++)
            for (int j = cols - 1; j >= 0; j--) {
                if (j == 0) {

                    holderArray[i][j] = temp;
                } else if (j == cols - 1) {
                    temp = holderArray[i][j];
                    holderArray[i][j] = holderArray[i][j - 1];
                } else {

                    holderArray[i][j] = holderArray[i][j - 1];
                }
            }

    }

    public void shiftUp() {
        String temp[] = new String[cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                if (i == 0) {
                    temp[j] = holderArray[i][j];
                    holderArray[i][j] = holderArray[i + 1][j];
                } else if (i == rows - 1) {
                    holderArray[i][j] = temp[j];
                } else
                    holderArray[i][j] = holderArray[i + 1][j];
            }

    }

    public void shiftDown() {
        String temp[] = new String[cols];
        for (int i = rows - 1; i >= 0; i--)
            for (int j = 0; j < cols; j++) {
                if (i == rows - 1) {
                    temp[j] = holderArray[i][j];
                    holderArray[i][j] = holderArray[i - 1][j];
                } else if (i == 0) {
                    holderArray[i][j] = temp[j];
                } else {

                    holderArray[i][j] = holderArray[i - 1][j];
                }
            }

    }

    public void diagonalInterchange() {
        String temp = "";
        for (int i = 0; i < rows; i = i + 2)
            for (int j = 0; j < cols; j = j + 2) {
                if (i == j && i <= rows - 2 && j <= cols - 2) {
                    temp = holderArray[i][j];
                    holderArray[i][j] = holderArray[i + 1][j + 1];
                    holderArray[i + 1][j + 1] = temp;
                }
            }
    }

    public void shift1D2D() {
        String temp;

        if (rows != 0 && cols != 0) {
            for (int j = 0; j < residualArray.length-1; j++) {
                temp = holderArray[rows - 1][cols - 1];
                for (int x = rows - 1; x >= 0; x--) {
                    for (int y = cols - 1; y >= 0; y--) {
                        if (y == 0) {
                            if (x != 0)
                                holderArray[x][y] = holderArray[x - 1][cols - 1];
                        } else
                            holderArray[x][y] = holderArray[x][y - 1];
                    }

                }
                holderArray[0][0] = residualArray[residualArray.length - 1];
                for (int z = residualArray.length - 1; z > 0; z--) {
                    residualArray[z] = residualArray[z - 1];
                }
                residualArray[0] = temp;
            }

            //System.out.println();
            //System.out.println("Residual array:");
            //for (int i = 0; i < residualArray.length; i++) {
              //  System.out.print(" " + residualArray[i] + " ");
            //}
            //System.out.println("Residual ends:");
        }

    }
    public String getBitPattern(){
        String pattern="";
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                pattern+=holderArray[i][j];
            }
        }
        for(int i=0;i<residualArray.length-1;i++){
            pattern+=residualArray[i];
        }
        return pattern;
    }
    public void revShift1D2D() {
        String temp;
        for(int k=0;k<residualArray.length-1;k++){
            residualArray[k]=residualArray[k+1];
        }
        System.out.println();
        System.out.println("Printing modified reisdual");
        for(int i=0;i<residualArray.length-1;i++){
            System.out.print(residualArray[i]+" "+"["+i+"] ");
        }
        System.out.println();
        if(rows!=0 && cols!=0) {
            for (int j = 0; j < residualArray.length-1; j++) {
                temp = holderArray[0][0];
                for (int x = 0; x < rows; x++) {
                    for (int y = 0; y < cols; y++) {
                        if (y == cols - 1) {
                            if (x != rows - 1)
                                holderArray[x][y] = holderArray[x + 1][0];
                        } else
                            holderArray[x][y] = holderArray[x][y + 1];
                    }

                }
                holderArray[rows - 1][cols - 1] = residualArray[0];
                for (int z = 0; z < residualArray.length - 2; z++) {
                    residualArray[z] = residualArray[z + 1];
                }
                residualArray[residualArray.length - 2] = temp;
                System.out.println("Printing modified reisdual");
                for(int i=0;i<residualArray.length-1;i++){
                    System.out.print(residualArray[i]+" "+"["+i+"] ");
                }
            }
        }
        //System.out.println();
        //System.out.println("Residual array:");
        //for(int i=0;i<residualArray.length;i++){
          //  System.out.print(" "+residualArray[i]+" ");
        //}
        //System.out.println("Residual ends:");
    }


}

