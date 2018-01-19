package com.merlin.common;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author merlin
 */

public class AmountTextWatcher implements TextWatcher {

    private int maxLength = 6;
    private double maxAmount = 0.0;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() < 1) {

        } else if (s.length() == 1) {
            if (s.toString().startsWith(".")) {
                s.insert(0, "0");
            }
        } else {
            String temp = s.toString();
            int posDot = temp.indexOf(".");
            // 如果包含点
            if (temp.contains(".")) {
                String intAmount = temp.substring(0, posDot);
                // 控制整数位金额数位的输入，多出位数，删除个位，6位
                if (intAmount.length() >= maxLength) {
                    s.delete(intAmount.length() - 1, intAmount.length());
                }
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            } else {
                // 控制整数位金额数位的输入
                if (temp.length() >= maxLength) {
                    s.delete(maxLength - 1, maxLength);
                }
            }
        }
    }

}
