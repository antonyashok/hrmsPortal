package com.tm.commonapi.web.rest.util;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import com.tm.commonapi.exception.ApplicationException;

public class ReflectionUtil {

	private ReflectionUtil() {

    }
	
    public static Method getMethodObjectFromJoinPoint(JoinPoint joinPoint) {
        Method method = null;
        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            method = methodSignature.getMethod();
            if (method.getDeclaringClass().isInterface()) {
                try {
                    method = joinPoint.getTarget().getClass().getDeclaredMethod(
                            joinPoint.getSignature().getName(), method.getParameterTypes());
                } catch (final SecurityException exception) {
                    throw new ApplicationException(
                            "Method information could not be inferred from provided JoinPoint reference due to SecurityException.",
                            exception);
                } catch (final NoSuchMethodException exception) {
                    throw new ApplicationException(
                            "Method information could not be inferred from provided JoinPoint reference due to NoSuchMethodException.",
                            exception);
                }
            }
        }
        if (method == null) {
            throw new ApplicationException(
                    "Method information could not be inferred from provided JoinPoint reference.");
        }
        return method;
    }

    public static Object invokeMethodByName(Class<?> methodClass, String methodName,
            Object object) {
        return ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(methodClass, methodName),
                object);
    }

}
