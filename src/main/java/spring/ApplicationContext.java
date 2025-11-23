package spring;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    // Bean 이름과 객체를 저장하는 컨테이너
    private final Map<String, Object> beanMap = new HashMap<>();

    public ApplicationContext() {
        initializeBeans();
    }

    // 초기 Bean 등록 (여기서 서버, 서비스, 컨트롤러 등을 등록)
    private void initializeBeans() {
    }

    /**
     * Bean 등록
     * @param name Bean 이름
     * @param instance Bean 객체
     */
    public void registerBean(String name, Object instance) {
        beanMap.put(name, instance);
    }

    /**
     * Bean 조회
     * @param name Bean 이름
     * @param <T> 객체 타입
     * @return 등록된 Bean
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        return (T) beanMap.get(name);
    }

    /**
     * Bean 전체 조회
     */
    public Map<String, Object> getAllBeans() {
        return new HashMap<>(beanMap);
    }
}
