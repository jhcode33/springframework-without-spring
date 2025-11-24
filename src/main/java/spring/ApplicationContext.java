package spring;

import spring.annotation.Controller;
import spring.annotation.Service;
import spring.annotation.Repository;

import java.beans.Introspector;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationContext
 * - Bean 등록/조회 기능 제공
 * - 패키지 스캔을 통한 @Controller, @Service, @Repository 자동 등록
 */
public class ApplicationContext {

    /** Bean 이름과 Bean 인스턴스를 저장하는 Map */
    private final Map<String, Object> beanMap = new HashMap<>();

    public ApplicationContext() {
        registerPackage("app");
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
     *
     * @return Bean 이름과 객체 Map
     */
    public Map<String, Object> getAllBeans() {
        return new HashMap<>(beanMap);
    }

    /**
     * 특정 패키지를 스캔하여 @Controller, @Service, @Repository가 붙은 클래스 자동 등록
     *
     * @param basePackage 스캔할 패키지 이름
     */
    public void registerPackage(String basePackage) {
        File dir = getPackageDirectory(basePackage);
        if (dir == null) return;
        scanDirectory(dir, basePackage);
    }

    /**
     * 패키지 이름을 실제 디렉토리로 변환
     *
     * @param packageName 패키지 이름
     * @return 디렉토리 객체 또는 null
     */
    private File getPackageDirectory(String packageName) {
        String path = packageName.replace('.', '/');
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url == null) return null;
        File dir = new File(url.getFile());
        return dir.isDirectory() ? dir : null;
    }

    /**
     * 디렉토리 내 클래스 파일 및 하위 디렉토리 스캔
     *
     * @param dir         디렉토리 객체
     * @param packageName 현재 패키지 이름
     */
    private void scanDirectory(File dir, String packageName) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanSubDirectory(file, packageName);
            } else if (file.getName().endsWith(".class")) {
                registerClass(packageName, file);
            }
        }
    }

    /**
     * 하위 디렉토리 스캔
     *
     * @param dir         하위 디렉토리 객체
     * @param packageName 부모 패키지 이름
     */
    private void scanSubDirectory(File dir, String packageName) {
        String subPackage = packageName + "." + dir.getName();
        scanDirectory(dir, subPackage);
    }

    /**
     * 클래스 파일을 로드하고 Bean으로 등록
     *
     * @param packageName 패키지 이름
     * @param file        클래스 파일 객체
     */
    private void registerClass(String packageName, File file) {
        String className = packageName + "." + file.getName().replace(".class", "");
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Controller.class) ||
                    clazz.isAnnotationPresent(Service.class) ||
                    clazz.isAnnotationPresent(Repository.class)) {

                Object instance = clazz.getDeclaredConstructor().newInstance();
                String beanName = Introspector.decapitalize(clazz.getSimpleName());
                beanMap.put(beanName, instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
