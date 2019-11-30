package util;

public class StringUtil {
    public static int parseBinaryToDecimal(int binary) {
        int dec = 0;
        int heavy = 1;
        do {
            int i = binary % 10;
            binary /= 10;
            dec += heavy * i;
            heavy *= 2;
        } while (binary > 0);
        return dec;
    }

    public static String parseDeviceID(int id) {
        String num = null;
        switch (id) {
            case 1:
                num = "A";
                break;
            case 2:
                num = "B";
                break;
            case 3:
                num = "C";
                break;
        }
        return num;
    }

    public static boolean checkInstruction(String ins){
        boolean res = true;
        if(ins.length() != 8) {
            res = false;
        }else if(ins.startsWith("111")) {
            res = false;
        }else {
            for(int i=0; i<8; ++i){
                if(ins.charAt(i) != '0' && ins.charAt(i) != '1'){
                    res = false;
                    break;
                }
            }
        }
        return res;
    }
}
