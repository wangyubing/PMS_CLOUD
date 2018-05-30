package com.wyb.pms.common.aop.reqparam;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解： 验证HttpServletRequest参数
 * @author lihw
 * @created 2016年6月20日 下午2:34:59
 *
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRequestParameter {
    Ruler[] value();

    /**
     * 注解： 验证规则
     * @author lihw
     * @created 2016年6月20日 下午2:34:43
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Ruler {
        /** 参数名。大小写敏感 */
        String name();
        /** 非空验证 */
        boolean isNotEmpty() default true;
        /** 数字验证 */
        boolean isNumber() default false;
        /** 正整数验证 */
        boolean isInteger() default false;
        /** 验证参数值是否在几个选项中。多个选项间以英文逗号隔开 */
        String select() default "";
        /** 正则表达式验证 */
        String regex() default "";
        /** 日期格式验证 */
        String dateFmt() default "";
        /** 最小值验证 */
        String min() default "";
        /** 最大值验证 */
        String max() default "";
        /** 文本长度最小值 */
        int strLenMin() default -1;
        /** 文本长度最大值 */
        int strLenMax() default -1;
        /** 当参数值为n时，其他参数不能为空。 与equalRequired配合使用  */
        String equalWhen() default "";
        /** 当参数值为n时，其他参数不能为空。 与equalWhen配合使用。 多个参数名间用英文逗号隔开 */
        String equalRequired() default "";
        /** 验证失败时描述。此值为空时则使用默认的描述。  */
        String failed() default "";
    }

}