package com.hcm.framework.config;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;
import com.hcm.common.utils.BaseUtils;

/**
 * KaptchaMathTextCreator
 *
 * @author pc
 * @date 2023/04/20
 */
public class KaptchaMathTextCreator extends DefaultTextCreator {

    private final String[] flags = {"+", "-", "*", "/"};

    @Override
    public String getText() {
        int num1 = BaseUtils.getRandomInt(0, 9);
        int num2 = BaseUtils.getRandomInt(0, 9);
        Integer operation = BaseUtils.getRandomInt(0, 4);
        return getCode(flags[operation], num1, num2);
    }

    private String getCode(String flag, Integer num1, Integer num2) {
        StringBuilder code = new StringBuilder();
        if (num1 < num2) {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }
        if (flag.equals(flags[3])) {
            if(num2 == 0){
                num2++;
            }
            num1 *=num2;
        }
        code.append(num1);
        code.append(flag);
        code.append(num2);
        int result = 0;
        switch (flag) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                result = num1 / num2;
                break;
            default:
                break;
        }
        code.append("=@");
        code.append(result);
        return code.toString();
    }
}
