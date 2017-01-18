package org.profit.util;

public class StockUtils {

    /**
     * 修正股票代码
     *
     * @param code
     * @return
     */
    public static String code(int code) {
        String sCode = String.valueOf(code);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6 - sCode.length(); i++) {
            sb.append("0");
        }
        sb.append(sCode);
        return sb.toString();
    }
}
