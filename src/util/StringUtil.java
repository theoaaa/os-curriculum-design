package util;

public class StringUtil {
    public static  int parseBinaryToDecimal(int binary){
        int dec = 0;
        int heavy = 1;
        do {
            int i = binary % 10;
            binary /= 10;
            dec += heavy * i;
            heavy *= 2;
        }while (binary > 0);
        return dec;
    }
}
