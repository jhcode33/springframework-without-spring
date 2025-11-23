package spring;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    // Bean 이름과 Bean 인스턴스를 저장하는 Map
    private final Map<String, Object> beanMap = new HashMap<>();

    public ApplicationContext() {
        // 초기 Bean 등록 가능 (필요 시)
    }

    /**
     * Reflection 기반 객체 생성 후 Bean 등록
     *
     * @param name  Bean 이름
     * @param clazz 생성할 클래스
     * @param <T>   객체 타입
     * @return 생성된 객체
     */
    public <T> T registerBean(String name, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            beanMap.put(name, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Bean 생성 실패: " + clazz.getName(), e);
        }
    }

    /**
     * 이미 생성된 객체를 Bean으로 등록
     *
     * @param name     Bean 이름
     * @param instance Bean 객체
     */
    public void registerBean(String name, Object instance) {
        beanMap.put(name, instance);
    }

    /**
     * Bean 조회
     *
     * @param name Bean 이름
     * @param <T>  반환 타입
     * @return Bean 객체
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        return (T) beanMap.get(name);
    }

    /**
     * 등록된 모든 Bean 확인
     */
    public Map<String, Object> getAllBeans() {
        return new HashMap<>(beanMap);
    }
}
