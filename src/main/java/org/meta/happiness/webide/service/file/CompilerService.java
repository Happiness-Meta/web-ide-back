package org.meta.happiness.webide.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilerService {

    private final Compiler compiler;
    private static final long TIMEOUT = 15000;

    @Transactional
    public String executeCode(String userCode){

        Map<String, Object> compileResult = compiler.compile(userCode);
        String userOutput;

        if (compileResult.containsKey("error")) {
            log.error("Compile Error: {}", compileResult.get("error"));
            userOutput = compileResult.get("error").toString();
        } else {
            log.info("<<>> {}", compileResult.get("class"));
            userOutput = execute(compileResult.get("class"));
        }

        return userOutput;
    }

    private String execute(Object compiledClassObj) {
        Class<?> compiledClass = (Class<?>) compiledClassObj;
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);

        try {
            System.setOut(newOut);
            Map<String, Object> executionResult = timeOutCall(
                    compiledClass,
                    new Object[]{new String[]{}},
                    new Class[]{String[].class}
            );

            System.out.flush();
            String userOutput = baos.toString().trim();
            log.info("User Output: " + userOutput);

            return userOutput;

        } catch (Exception e) {
            log.error("Error executing user code: ", e);
            return null;

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }


    private Map<String, Object> timeOutCall(Class<?> mainClass, Object[] params, Class<?>[] arguments) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Map<String, Object>> task = () -> {
            Map<String, Object> result = new HashMap<>();
            Method method = mainClass.getMethod("main", arguments);
            result.put("output", method.invoke(null, params));
            return result;
        };

        Future<Map<String, Object>> future = executor.submit(task);
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Method execution error: {}", e.getMessage());
            return Map.of("error", "Execution failed or timed out");
        } finally {
            executor.shutdown();
        }
    }

}