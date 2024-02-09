package org.meta.happiness.webide.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "org.meta.happiness.controller")
public class ExceptionAdvice {

}
