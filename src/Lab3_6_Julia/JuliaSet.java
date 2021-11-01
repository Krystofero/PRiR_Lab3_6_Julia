package Lab3_6_Julia;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*Przykłady:
*                     x   y
*                 0.10259 -0.641
*                 -1.25 0.0
*                 -0.75 0.10
*                 -1.1 0.2345
*                 -0.023 0.745
*                 0.10259 -0.641
*/

public class JuliaSet extends Thread{
    final static int N = 4096;
    //stała okreslająca czy szereg w aktualnym punkcje będzie nieskończony
    final static int CUTOFF = 100;

    static int[][] tab = new int[N][N];
    int me;
    double X, Y;

    public static void main(String[] args) throws IOException {
        JuliaSet[] julia = new JuliaSet[4];
        for (int i = 0; i < 4; i++) {
            julia[i] = new JuliaSet(i);
            julia[i].start();
        }

        try {
            for (int i = 0; i < 4; i++) {
                julia[i].join();
            }
        } catch (Exception e) {

        }

        BufferedImage img = new BufferedImage(N, N, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int k = tab[i][j];
                Color c;

                if (k >= CUTOFF)
                    c = new Color(0, 0, (float) k / CUTOFF);
                else
                    c = new Color(0, 0, (float) k / CUTOFF);

                img.setRGB(i, j, c.getRGB());
            }
        }

        ImageIO.write(img, "PNG", new File("JuliaFractal.png"));

    }


    /**
     * Konstruktor
     * int me = numer wątku
     */
    public JuliaSet(int me){
        this.me = me;
        this.X = 2.5 / N;
        this.Y = 2.5 / N;
    }

    public void run() {

        int begin = 0, end = 0;
        if (me == 0) {
            begin = 0;
            end = (N / 4) * 1;
        }
        else if (me == 1) {
            begin = (N / 4) * 1;
            end = (N / 4) * 2;
        }
        else if (me == 2) {
            begin = (N / 4) * 2;
            end = (N / 4) * 3;
        }
        else if (me == 3) {
            begin = (N / 4) * 3;
            end = N;
        }


        for (int i = begin; i < end; i++) {
            for (int j = 0; j < this.N; j++) {
                double real = (i * this.Y) - 1.25;
                double imagine = (j * this.X) - 1.25;

                int k = 0;

                Complex z0 = new Complex(real, imagine);
                Complex z1 = new Complex(-0.023, 0.745);


                while(z0.abs() < 2.0 && k < 100) {
                    k++;
                    z0 = z1.add(z0.pow2());
                }
                this.tab[i][j] = k;
            }
        }
    }

}
