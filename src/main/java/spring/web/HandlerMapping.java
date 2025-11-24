package spring.web;

import spring.annotation.Controller;
import spring.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private final Map<String, HandlerMethod> handlerMap = new HashMap<>();

    public HandlerMapping(Map<String, Object> beanMap) {
        initialize(beanMap);
    }

    private void initialize(Map<String, Object> beanMap) {
        for (Object bean : beanMap.values()) {
            Class<?> clazz = bean.getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) continue;

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    String path = mapping.value();
                    handlerMap.put(path, new HandlerMethod(bean, method));
                }
            }
        }
    }

    public HandlerMethod getHandler(String path) {
        return handlerMap.get(path);
    }
}