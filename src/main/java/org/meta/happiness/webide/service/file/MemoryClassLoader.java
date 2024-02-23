package org.meta.happiness.webide.service.file;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryClassLoader extends ClassLoader {
    private final Map<String, byte[]> compiledClasses = new ConcurrentHashMap<>();

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = compiledClasses.get(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}