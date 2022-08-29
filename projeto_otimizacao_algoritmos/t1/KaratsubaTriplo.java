/*
 * PUCRS
 * Disciplina: Projeto e Análise de Algoritmos
 * Prof Joao Batista  
 * O karatsuba mortal triplo carpado
 *
 * @author Lucas Dellatorre de Freitas
 *
 */

public class KaratsubaTriplo {
  public static void main(String args[]) {
    KaratsubaTriplo kt = new KaratsubaTriplo();
    String arg1 = args[0].trim();
    String arg2 = args[1].trim();

    System.out.println(kt.karatsuba(arg1, arg2));
  }

  public String karatsuba(String a, String b) {
    if (a == null || b == null)
      return null;

    if ("".equals(a) || "".equals(b))
      return "";

    if (a.length() == 1 || b.length() == 1) {
      return gradeSchoolMultiplication(a, b);
    }

    /* balancear a e b */

    StringBuilder sb1 = new StringBuilder(a);
    StringBuilder sb2 = new StringBuilder(b);

    while (sb1.length() < sb2.length() || sb1.length() % 3 != 0) {
      sb1.insert(0, '0');
    }

    while (sb2.length() < sb1.length() || sb2.length() % 3 != 0)
      sb2.insert(0, '0');

    a = sb1.toString();
    b = sb2.toString();

    int m =  Math.max(a.length(), b.length());
    
    /* partes que serao divididas */

    int n = m / 3;
    
    String a1 = a.substring(0, n);
    String a2 = a.substring(n, n * 2);
    String a3 = a.substring(n * 2, n * 3);

    String b1 = b.substring(0, n);
    String b2 = b.substring(n, n * 2);
    String b3 = b.substring(n * 2, n * 3);

    /* shifts */ 

    int s1 = 2 * m / 3; 
    int s2 = m / 3;

    String e1 = addZerosAtRight(s1);

    String e2 = addZerosAtRight(s2);


    String a1b1 = karatsuba(a1, b1) + e1 + e1;

    /* lugar que teria a otimizacao */

    String a1b3 = karatsuba(a1, b3) + e1;
    String a2b2 = karatsuba(a2, b2) + e2 + e2;
    String a3b1 = karatsuba(a3, b1) + e1;

    String a1b2 = karatsuba(a1, b2) + e1 + e2;
    String a2b1 = karatsuba(a2, b1) + e2 + e1;
    String a2b3 = karatsuba(a2, b3) + e2;
    String a3b2 = karatsuba(a3, b2) + e2;

    String a3b3 = karatsuba(a3, b3);

    String r = add(a1b3, add(a2b2, add(a3b1, add(a1b2, add(a2b1, add(a2b3, a3b2))))));

    return add(a1b1, add(a3b3, r));
  }

  private String gradeSchoolMultiplication(String input1, String input2) {
    if ("0".equals(input1) || "0".equals(input2))
      return "0";
    if ("1".equals(input1))
      return input2;
    if ("1".equals(input2))
      return input1;
      
    int a = Integer.parseInt(input1);
    int b = Integer.parseInt(input2);

    int result = a * b;

    return String.valueOf(result);
  }

  private String add(String num1, String num2) {
    int i = num1.length() - 1;
    int j = num2.length() - 1;
        
    StringBuilder sb = new StringBuilder();

    int sum = 0;
        
    while(i>=0 || j >= 0 || sum != 0){
      if (i >= 0) { 
        sum += Character.getNumericValue(num1.charAt(i));
        i--;
      }   
      if (j >= 0) {
        sum += Character.getNumericValue(num2.charAt(j));
        j--;
      }  
      sb.append(sum%10);
      sum /= 10;
    }
    return trim(sb.reverse());
  }

  private String trim(StringBuilder sb) {
    while (sb.length() > 1 && sb.charAt(0) == '0')
      sb.deleteCharAt(0);
    return sb.toString();
  }

  private String addZerosAtRight(int zeros) {
    StringBuilder sb = new StringBuilder();
    for (int a = 0; a < zeros; a++)
      sb.append('0');
    return sb.toString();
  }
}
