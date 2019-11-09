package instruction.util;

/**
 * @author Rorke
 * @date 2019/11/9 17:12
 */
public class InstrUtils {
    /**
     * @param num   源十进制数字    2^(digit)-1=>num>=0
     * @param digit 转换到二进制的位数   31>=digit>=0
     * @return 二进制串
     */
    public static String decToBinary(int num, int digit) {
        int value = 1 << (digit) | num;                     //确保位数有digit位，1<<digit是做位运算，将1左移digit位这时候有digit+1位，
        //然后对后digit个0与num做或运算使得后digit位是num，此时value=2^(digit-1)+num
        String result = Integer.toBinaryString(value);  //使用java的api直接转换
        return result.substring(1);                     //去掉头的1
    }
}
