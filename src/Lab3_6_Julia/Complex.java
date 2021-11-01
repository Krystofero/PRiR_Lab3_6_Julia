package Lab3_6_Julia;

public class Complex {
    private double real;
    private double imagine;

    public Complex(double r, double i){
        this.real = r;
        this.imagine = i;
    }

//wartość bezwzględna liczby zespolonej (moduł)
    public double abs(){
        return Math.sqrt(real * real + imagine * imagine);
    }

    public Complex add(Complex in){
        return new Complex(this.real + in.real, this.imagine + in.imagine);
    }

    public Complex pow2(){
        double r = (this.real * this.real) - (this.imagine * this.imagine);
        double i = (this.real * this.imagine) + (this.imagine * this.real);

        return new Complex(r, i);
    }

}
