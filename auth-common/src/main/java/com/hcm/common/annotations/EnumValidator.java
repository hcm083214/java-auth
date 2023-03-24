package com.hcm.common.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * enum验证器
 *
 * @author pc
 * @date 2023/03/23
 */
public class EnumValidator implements ConstraintValidator<EnumValue, String> {
    private Set<String> allowedValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        // 得到注解中枚举类型的Class对象
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        boolean ignoreCase = constraintAnnotation.ignoreCase();
        allowedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(value -> ignoreCase ? value.name().toLowerCase() : value.name())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return allowedValues.contains(s);
    }
}
