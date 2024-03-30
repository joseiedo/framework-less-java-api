package com.rest.app.utils;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {FIELD, CONSTRUCTOR, METHOD})
@Documented
public @interface Inject {
}
