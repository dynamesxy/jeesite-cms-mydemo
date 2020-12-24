package com.jeesite.modules.cms.utils;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VisitLogAnnotation {
//    String visitSiteCode() default ""; //访问站点
//    String visitCategoryCode() default ""; //访问栏目
//    String visitContentId() default "";  //访问内容
}
