Programowanie równolegle i rozproszone - projekt 6 labolatorium 3 Fraktal Julii

Projekt przedstawia generowanie fraktala Julii pracując na wielu wątkach, co znacząco skraca czas wykonywania się programu.

Wygenerowany fraktal: 
![image](https://user-images.githubusercontent.com/80296885/139745184-3485e645-edc0-4423-8738-7d2c52c02a7d.png)

Klasa Complex reprezentuje liczby rzeczywiste. konstruktor dwuparametrowy przyjmuje część rzeczywistą i urojoną:

    public Complex(double r, double i){
        this.real = r;
        this.imagine = i;
    }

,Operacja dodawania do liczby zespolonej:

    public Complex add(Complex in){
        return new Complex(this.real + in.real, this.imagine + in.imagine);
    }
    
,wartość bezwzględna liczby zespolonej( inaczej modal):

    public double abs(){
        return Math.sqrt(real * real + imagine * imagine);
    }
    
,podnoszenie do kwadratu liczby zespolonej:

    public Complex pow2(){
        double r = (this.real * this.real) - (this.imagine * this.imagine);
        double i = (this.real * this.imagine) + (this.imagine * this.real);

        return new Complex(r, i);
    }
    
    
Klasa JuliaSet rozszerza klasę Thread.
Przyjmowane sa dwie stale oraz tablica o stałych wymiarach:

    final static int N = 4096;
    //stała okreslająca czy szereg w aktualnym punkcje będzie nieskończony
    final static int CUTOFF = 100;
    static int[][] tab = new int[N][N];
    
Konstruktor przyjmuje numer wątku: 
 
  public JuliaSet(int me){
        this.me = me;
        this.X = 2.5 / N;
        this.Y = 2.5 / N;
    }
    
Metoda run() zależnie od wątku operuje na innych współrzędnych:

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
    
W main'ie tworzone są wątki klasy JuliaSet i wstawiane do tablicy wątków. Rozpoczyna się ich praca, a także program czeka aż wszystkie zakończą pracę.

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
        
Po zakończeniu pracy wszystkich wątków generowany jest obraz przedstawiający fraktal (zbiór Julii) o określonych kolorach(niebieskich). Następnie zapisywany jest do plikuJuliaFractal.png: 

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
