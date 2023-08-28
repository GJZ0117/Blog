package com.gjz.Aspect;

import java.lang.annotation.*;

// method表示可以放在方法上面, type表示可以放在类上面
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operation() default "";
}
