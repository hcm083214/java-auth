package com.hcm.common.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举校验注解
 *
 * @author pc
 * @date 2023/03/23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//标明由哪个类执行校验逻辑
@Constraint(validatedBy = {EnumValidator.class})
public @interface EnumValue {
    Class<? extends java.lang.Enum<?>> enumClass();

    // 用于指定验证约束属于哪个验证组
    Class<?>[] groups() default {};

    // 用于指定验证约束的有效负载类型
    Class<? extends Payload>[] payload() default {};

    boolean ignoreCase() default false;

    String message() default "Invalid value. Must be one of: {enumValues}";
}
