import java.util.HashMap;

public class Cidade implements Comparable<Cidade> {
    private double longitude;
    private double latitude;
    private String codCidade;
    private int id;

    public Cidade(double longitude, double latitude, String codCidade) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.codCidade = codCidade;
        String aux = "";

        for (int i = 0; i < codCidade.length(); i++) {
            char aChar = codCidade.charAt(i);
            if (Character.isDigit(aChar)) {
                aux += aChar;
            }
        }

        this.id = Integer.parseInt(aux);
    }    

    public String toString() {
        return "codCidade=" + codCidade + ";latidude=" + this.latitude + ";longitude=" + this.longitude;
    }

    public int getId() {
        return id;
    }

    public String getCodCidade() {
        return codCidade;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    @Override
    public int compareTo(Cidade outraCidade) {
        if (this.id < outraCidade.id)
            return -1;
        if (this.id > outraCidade.id)
            return 1;
        return 0;
    }
}
