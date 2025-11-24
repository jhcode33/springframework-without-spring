package spring;

import spring.annotation.Controller;
import spring.annotation.Service;
import spring.annotation.Repository;

import java.beans.Introspector;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        String path = basePackage.replace('.', '/');
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url == null) return;

        if (url.getProtocol().equals("file")) {
            scanDirectory(new File(url.getFile()), basePackage);
        } else if (url.getProtocol().equals("jar")) {
            scanJar(url, path);
        }
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
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                registerClass(packageName + "." + file.getName().replace(".class", ""));
            }
        }
    }

    /**
     * JAR 파일 스캔
     *
     * @param url  JAR URL
     * @param path JAR 내부 경로
     */
    private void scanJar(URL url, String path) {
        try {
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            try (JarFile jar = conn.getJarFile()) {
                scanJarEntries(jar, path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * JAR 엔트리에서 클래스 추출 및 등록
     *
     * @param jar  JarFile 객체
     * @param path JAR 내부 경로
     */
    private void scanJarEntries(JarFile jar, String path) {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.isDirectory() && entry.getName().startsWith(path) && entry.getName().endsWith(".class")) {
                registerClass(entry.getName().replace('/', '.').replace(".class", ""));
            }
        }
    }

    /**
     * 클래스 로드 후 Bean 등록
     *
     * @param className 클래스 FQN
     */
    private void registerClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Controller.class)
                    || clazz.isAnnotationPresent(Service.class)
                    || clazz.isAnnotationPresent(Repository.class)) {

                Object instance = clazz.getDeclaredConstructor().newInstance();
                String beanName = Introspector.decapitalize(clazz.getSimpleName());
                beanMap.put(beanName, instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
