package org.meta.happiness.webide.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Compiler {

    private final JavaCompiler compiler;
    private final MemoryJavaFile memoryFileManager;



    public Compiler() {
        log.trace("Initializing IdeCompiler.");
        compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler == null) {
            throw new IllegalStateException("System Java Compiler not available.");
        }

        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
        if (standardFileManager == null) {
            throw new IllegalStateException("StandardFileManager initialization failed.");
        }

        memoryFileManager = new MemoryJavaFile(standardFileManager);
        log.trace("IdeCompiler initialized successfully.");
    }

    public Map<String, Object> compile(String sourceCode) {
        StringWriter output = new StringWriter();
        boolean success = compileSource(sourceCode, output);

        Map<String, Object> result = new HashMap<>();
        if (!success) {
            result.put("error", output.toString());
        } else {
            String className = extractClassName(sourceCode);
            byte[] classBytes = memoryFileManager.getClassBytes(className);
            MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
            memoryClassLoader.addClass(className, classBytes);
            try {
                result.put("class", memoryClassLoader.loadClass(className));
            } catch (ClassNotFoundException e) {
                result.put("error", "Class loading failed: " + e.getMessage());
            }
        }

        return result;
    }

    private boolean compileSource(String sourceCode, StringWriter output) {
        JavaFileObject sourceFile = createSourceFile(sourceCode);
        return compiler.getTask(output, memoryFileManager, null, null, null, Collections.singletonList(sourceFile)).call();
    }

    private JavaFileObject createSourceFile(String sourceCode) {
        String className = extractClassName(sourceCode);
        String fileName = className + ".java";

        return new SimpleJavaFileObject(URI.create("string:///" + fileName), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return sourceCode;
            }
        };
    }

    public String extractClassName(String sourceCode) {
        Matcher matcher = Pattern.compile("public\\s+class\\s+([\\w]+)\\s*\\{?").matcher(sourceCode);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    static class MemoryClassLoader extends ClassLoader {
        private final Map<String, byte[]> classBytes = new HashMap<>();

        public void addClass(String name, byte[] bytes) {
            classBytes.put(name, bytes);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classBytes.get(name);
            if (bytes == null) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
