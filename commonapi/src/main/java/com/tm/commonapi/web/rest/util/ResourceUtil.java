package com.tm.commonapi.web.rest.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.core.DummyInvocationUtils.LastInvocationAware;
import org.springframework.hateoas.core.DummyInvocationUtils.MethodInvocation;
import org.springframework.security.util.SimpleMethodInvocation;

public class ResourceUtil {
	
	private static final Logger log = LoggerFactory.getLogger(ResourceUtil.class);

    private ResourceUtil() {}

    public static SimpleMethodInvocation methodInvocation(final @NotNull Object invocationValue) {
        final LastInvocationAware invocations = (LastInvocationAware) invocationValue;
        final MethodInvocation invocation = invocations.getLastInvocation();
        return new SimpleMethodInvocation(invocationValue,
                invocation.getMethod(), invocation.getArguments());
    }

    public synchronized static UUID generateUUID() {
//        Long ms = System.currentTimeMillis();
//        Long nano = System.nanoTime();
//        String nanoString = String.valueOf(nano);
//        String msString = String.valueOf(ms);
//        nanoString = StringUtils.right(nanoString, 4);
//        msString = msString + nanoString;
//        return new UUID(424223232329474L, Long.parseLong(msString));
    	return UUID.randomUUID();
    }
    
	public static String getRegex(String searchString) {
		return searchString.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
	}
    
	public static List<Object> prepareSearchParam(String searchParam) {
		List<Object> searchParams = new ArrayList<>();
		if (StringUtils.contains(searchParam, "$")) {
			searchParams.add(Pattern.compile(".*"+"\\$"+".*", Pattern.CASE_INSENSITIVE));
			searchParams.add(getRegex(searchParam));
		}
		else {
			searchParams.add(Pattern.compile(getRegex(searchParam), Pattern.CASE_INSENSITIVE));
			searchParams.add(getRegex(searchParam));
		}
		return searchParams;
	}
	

	public static String escapeSpecialCharacter(String searchParam){
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(searchParam)){
			try {
				String updatedSearchParam = URLDecoder.decode(searchParam, "UTF-8").replaceAll(".", "");
				for (char c : updatedSearchParam.toCharArray())
				{
				    switch(c)
				    {
				        case '~':
				        case '`':
				        case '!':
				        case '{':
				        case '}':
				        case '<':
				        case '>':
				        case '[':
				        case ']':
				        case '"':
				        case '*':
				        case '(':
				        case ')':
				        case '_':
				        case '+':
				        case '=':
				        case '/':
				        case '|':
				        case '#':
				        case '%':
				        case '&':
				        case '\\':
				            sb.append('\\');
				        default:
				            sb.append(c);
				    }
				}
			} catch (UnsupportedEncodingException e) {
				log.error(" Error while escapeSpecialCharacter() :: "+e);
			}
		}
		return sb.toString();
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public static String firstLetterUpperCaseOtherLowerCase(String givenString) {
		if (StringUtils.isNotBlank(givenString)) {
			return givenString.substring(0, 1).toUpperCase() + givenString.substring(1, 3).toLowerCase();
		}
		return null;
	}
	
	public static List<UUID> convertListStringToListUUID(List<String> stringData) {
		if (CollectionUtils.isNotEmpty(stringData)) {
			try {
				return stringData.stream().map(UUID::fromString).collect(Collectors.toList());
			} catch (Exception e) {
				log.error(" Error while convertListStringToListUUID() :: "+e);
			}
		}
		return Collections.<UUID> emptyList();
	}

}
