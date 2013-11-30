import java.math.*;


public class Test {

    public static void main(String[] args) {
        String value = "123456.0023 -3.04567E-8 -3.01967E-20";
        String[] tabOfFloatString = value.split(" ");
        int length = tabOfFloatString.length;

        for (int l = 0; l < length; l++) {
            String res = new BigDecimal(tabOfFloatString[l]).toPlainString();
            System.out.println("Float is " + res);
        }

    }

}