package com.example.invinciblesourav.flacom;

import java.math.BigDecimal;

/**
 * Created by Invincible Sourav on 28-04-2018.
 */

public class DFT {
    public final double re; // the real part
    public final double im; // the imaginary part

    // create a new object with the given real and imaginary parts
    public DFT(double real, double imag)
    {
        re = real;
        im = imag;
    }

    // return a string representation of the invoking DFT object
    public String toString()
    {
        if (im == 0)
        //long real= Math.round(re);
        return re + "";
        if (re == 0)
            return im +"";
        if (im < 0)
            return re + "&" + (-im) ;
        return re + "%" + im ;
    }

    // return abs/modulus/magnitude and angle/phase/argument
    public double abs()
    {
        return Math.hypot(re, im);
    } // Math.sqrt(re*re + im*im)

    public double phase()
    {
        return Math.atan2(im, re);
    } // between -pi and pi

    // return a new DFT object whose value is (this + b)
    public DFT plus(DFT b)
    {
        DFT a = this; // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new DFT(real, imag);
    }

    // return a new DFT object whose value is (this - b)
    public DFT minus(DFT b)
    {
        DFT a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new DFT(real, imag);

    }

    // return a new DFT object whose value is (this * b)
    public DFT times(DFT b)
    {
        DFT a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new DFT(real, imag);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public DFT times(double alpha)
    {
        return new DFT(alpha * re, alpha * im);
    }

    // return a new DFT object whose value is the conjugate of this
    public DFT conjugate() {
        return new DFT(re, -im);
    }

    // return a new DFT object whose value is the reciprocal of this
    public DFT reciprocal()
    {
        double scale = re * re + im * im;
        return new DFT(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re()
    {
        return re;
    }

    public double im()
    {
        return im;
    }

    // return a / b
    public DFT divides(DFT b)
    {
        DFT a = this;
        return a.times(b.reciprocal());
    }

    // return a new DFT object whose value is the complex exponential of
    // this
    public DFT exp()
    {
        return new DFT(Math.exp(re) * Math.cos(im), Math.exp(re)
                * Math.sin(im));
    }

    // return a new DFT object whose value is the complex sine of this
    public DFT sin()
    {
        return new DFT(Math.sin(re) * Math.cosh(im), Math.cos(re)
                * Math.sinh(im));
    }

    // return a new DFT object whose value is the complex cosine of this
    public DFT cos()
    {
        return new DFT(Math.cos(re) * Math.cosh(im), -Math.sin(re)
                * Math.sinh(im));
    }

    // return a new DFT object whose value is the complex tangent of
    // this
    public DFT tan()
    {
        return sin().divides(cos());
    }

    // a static version of plus
    public static DFT plus(DFT a, DFT b)
    {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        DFT sum = new DFT(real, imag);
        return sum;
    }

    // compute the FFT of x[], assuming its length is a power of 2
    public static DFT[] fft(DFT[] x)
    {
        int N = x.length;

        // base case
        if (N == 1)
            return new DFT[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0)
        {
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        // fft of even terms
        DFT[] even = new DFT[N / 2];
        for (int k = 0; k < N / 2; k++)
        {
            even[k] = x[2 * k];
        }

        DFT[] q = fft(even);

        // fft of odd terms
        DFT[] odd = even; // reuse the array
        for (int k = 0; k < N / 2; k++)
        {
            odd[k] = x[2 * k + 1];
        }

        DFT[] r = fft(odd);

        // combine
        DFT[] y = new DFT[N];
        for (int k = 0; k < N / 2; k++)
        {
            double kth = -2 * k * Math.PI / N;
            DFT wk = new DFT(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    // compute the inverse FFT of x[], assuming its length is a power of 2
    public static DFT[] ifft(DFT[] x)
    {
        int N = x.length;
        DFT[] y = new DFT[N];

        // take conjugate
        for (int i = 0; i < N; i++)
        {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again

        for (int i = 0; i < N; i++)
        {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++)
        {
            y[i] = y[i].times(1.0 / N);
        }

        return y;

    }

    // display an array of DFT numbers to standard output
    public static void show(DFT[] x, String title)
    {
        System.out.println(title);
        for (int i = 0; i < x.length; i++)
        {
            System.out.print(x[i].re+" ");
        }
        System.out.println();
    }
    public static String[] returnArray(DFT[] x)
    {
        String[] a=new String[x.length];
        for (int i = 0; i < x.length; i++)
        {
            a[i]=x[i].toString();
        }
        return a;

    }

    /*public static void main(String[] args)
    {
        int N = 8;//Integer.parseInt(args[0]);
        DFT[] x = new DFT[N];

        // original data
        for (int i = 0; i < N; i++)
        {
            x[i] = new DFT(i, 0);
            x[i] = new DFT(-2 * Math.random() + 1, 0);
        }
        show(x, "x");

        // FFT of original data
        DFT[] y = fft(x);
        show(y, "y = fft(x)");

        // take inverse FFT
        DFT[] z = ifft(y);
        show(z, "z = ifft(y)");

    }*/
    public static String[][] matrixDFT(int a[][],int rows,int cols){
        String b[][]=new String[rows][cols];
        for(int i=0;i<rows;i++){
            DFT[] y = new DFT[cols];
            for(int j=0;j<cols;j++){
                y[j]=new DFT(a[i][j],0.0);
            }
            DFT[] z=DFT.fft(y);
            for(int k=0;k<z.length;k++){
                b[i][k]=z[k].re+"&"+z[k].im;
            }
        }
        return b;
    }
    public static int[][] matrixIDFT(String a[][],int rows,int cols){
        int b[][]=new int[rows][cols];
        for(int i=0;i<rows;i++){
            DFT[] x = new DFT[cols];
            for(int j=0;j<cols;j++){
                String coef[]=a[i][j].split("&");
                x[j]=new DFT(Double.parseDouble(coef[0]),Double.parseDouble(coef[1]));
            }
            DFT[] z=DFT.ifft(x);
            for (int k=0;k<z.length;k++){
                b[i][k]=(int)Math.round(z[k].re);
            }
        }
        return b;
    }


}
