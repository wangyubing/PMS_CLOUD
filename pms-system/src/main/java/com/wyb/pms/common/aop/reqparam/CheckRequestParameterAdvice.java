package com.wyb.pms.common.aop.reqparam;


import com.wyb.pms.common.result.GlobalErrorInfoEnum;
import com.wyb.pms.common.result.GlobalErrorInfoException;
import com.wyb.pms.common.utils.RegexUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 *
 * Created by weiming on 2016/11/30.
 */
@Aspect
@Component
public class CheckRequestParameterAdvice {

    private Logger log = LoggerFactory.getLogger(CheckRequestParameterAdvice.class);

    @Before("execution(* com.wyb..*.*(..))" +
            " && @annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            " && @annotation(com.wyb.pms.common.aop.reqparam.CheckRequestParameter)")
    public void checkParameters(JoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        CheckResult result = checkParam(method, request);
        if (!result.isPass) {
            throw new GlobalErrorInfoException(GlobalErrorInfoEnum.ERROR_PARAMS);
        }
    }

    @SuppressWarnings({ "rawtypes" })
    private CheckResult checkParam(Method callerMethod, HttpServletRequest request) {
        try {
            callerMethod.setAccessible(true);

            CheckRequestParameter cdfs = callerMethod.getAnnotation(CheckRequestParameter.class);
            CheckRequestParameter.Ruler[] checks = cdfs.value();
            for (CheckRequestParameter.Ruler canno : checks) {
                String name = canno.name();
                String pvalue = request.getParameter(name);
                String failed = canno.failed();
                boolean isUsedFailed = !StringUtils.isEmpty(failed);

                boolean isNotEmpty = canno.isNotEmpty();
                if (!isNotEmpty && !checkNotEmpty(pvalue)) {
                    continue;
                }
                if (isNotEmpty && !checkNotEmpty(pvalue)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值不能为空"));
                }

                boolean isNumber = canno.isNumber();
                if (isNumber && !checkNumber(pvalue)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】非数字"));
                }

                boolean isInteger = canno.isInteger();
                if (isInteger && !checkInteger(pvalue)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】非正整数"));
                }

                String select = canno.select();
                if (!StringUtils.isEmpty(select) && !checkSelect(pvalue, select)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】不在要求范围【" + select + "】内"));
                }

                String regex = canno.regex();
                if (!StringUtils.isEmpty(regex) && !checkRegex(pvalue, regex)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】不符合正则表达式【" + regex + "】"));
                }

                String dateFmt = canno.dateFmt();
                if (!StringUtils.isEmpty(dateFmt) && !checkDateFmt(pvalue, dateFmt)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】不符合日期格式【" + dateFmt + "】"));
                }

                String min = canno.min();
                if (!StringUtils.isEmpty(min) && !checkMin(pvalue, min)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】必须大等于【" + min + "】"));
                }

                String max = canno.max();
                if (!StringUtils.isEmpty(max) && !checkMax(pvalue, max)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】必须小等于【" + max + "】"));
                }

                int strLenMin = canno.strLenMin();
                if (strLenMin != - 1 && !checkStrLenMin(pvalue, strLenMin)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】文本长度必须大等于【" + strLenMin + "】"));
                }

                int strLenMax = canno.strLenMax();
                if (strLenMax != - 1 && !checkStrLenMax(pvalue, strLenMax)) {
                    return new CheckResult(false, isUsedFailed ? failed :
                            ("参数【" + name + "】的值【" + pvalue + "】文本长度必须小等于【" + strLenMax + "】"));
                }

                String equalWhen = canno.equalWhen();
                String equalRequired = canno.equalRequired();
                if (!StringUtils.isEmpty(equalRequired) && pvalue.equals(equalWhen)) {
                    String[] requires = equalRequired.split(",");
                    if (requires.length > 0) {
                        for (String require : requires) {
                            if (StringUtils.isEmpty(request.getParameter(require))) {
                                return new CheckResult(false, isUsedFailed ? failed :
                                        ("参数【" + name + "】的值为【" + pvalue + "】时，参数【" + require + "】不能为空。"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new CheckResult(true, "参数验证成功");
    }

    private static boolean checkNotEmpty(String value) {
        return !StringUtils.isEmpty(value);
    }

    private static boolean checkNumber(String value) {
        return RegexUtils.decimal(10).matcher(value).find();
    }

    private static boolean checkInteger(String value) {
        return RegexUtils.integer(20).matcher(value).find();
    }

    private static boolean checkSelect(String value, String selects) {
        String[] strs = selects.split(",");
        for (int i = 0 ; i < strs.length; i++) {
            if (value.equals(strs[i])) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkRegex(String value, String regex) {
        return RegexUtils.pattern(regex).matcher(value).matches();
    }

    private static boolean checkDateFmt(String value, String dateFmt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
            sdf.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkMin(String value, String min) {
        try {
            BigDecimal v = new BigDecimal(value);
            BigDecimal m = new BigDecimal(min);
            return v.compareTo(m) != -1;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkMax(String value, String max) {
        try {
            BigDecimal v = new BigDecimal(value);
            BigDecimal m = new BigDecimal(max);
            return v.compareTo(m) != 1;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkStrLenMin(String value, int strLenMin) {
        return value.length() >= strLenMin;
    }

    private static boolean checkStrLenMax(String value, int strLenMax) {
        return value.length() <= strLenMax;
    }

    public class CheckResult {
        boolean isPass;
        String errMsg;

        public CheckResult() {
        }

        public CheckResult(boolean isPass, String errMsg) {
            this.isPass = isPass;
            this.errMsg = errMsg;
        }
    }
}
