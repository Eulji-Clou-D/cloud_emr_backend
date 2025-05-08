package com.cloud.emr.Main.Core.common.annotation;

import com.cloud.emr.Main.User.type.RoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthRole {
    RoleType[] roles() default { RoleType.WAIT }; // 여러 역할을 허용하도록 변경

}

