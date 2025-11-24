package spring.web;

import java.lang.reflect.InvocationTargetException;

public class HandlerAdapter {

    public Object handle(HandlerMethod handlerMethod) {
        try {
            return handlerMethod.getMethod().invoke(handlerMethod.getBean());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("핸들러 실행 실패", e);
        }
    }
}
