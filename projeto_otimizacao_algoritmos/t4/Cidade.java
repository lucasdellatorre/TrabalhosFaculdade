public class Cidade {
   private String id;
   private String codCidade;
   private double latitude;
   private double longitude;
   
   Cidade(double latitude, double longitude, String codCidade) {
        this.latitude = longitude;
        this.longitude= longitude;
        this.codCidade = codCidade;
        String aux = "";
        for (int i = 0; i < codCidade.length(); i++) {
            char aChar = codCidade.charAt(i);
            if (Character.isDigit(aChar)) {
                aux += aChar;
            }
        }
        this.id = String.valueOf(aux);
   }



   public double getLatitude() {
       return latitude;
   } 

   public double getLongitude() {
       return longitude;
   }

   public String getCod() {
       return cod;
   }
}
