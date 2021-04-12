package com.yyitsz.piggymetrics2.common.resilience4j;


import io.github.resilience4j.core.ContextPropagator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RequestAttributesPropagator implements ContextPropagator<RequestAttributes> {
    @Override
    public Supplier<Optional<RequestAttributes>> retrieve() {
        return () -> Optional.ofNullable(RequestContextHolder.getRequestAttributes());
    }

    @Override
    public Consumer<Optional<RequestAttributes>> copy() {
        return context -> {
            context.ifPresent(RequestContextHolder::setRequestAttributes);
        };
    }

    @Override
    public Consumer<Optional<RequestAttributes>> clear() {
        return context -> RequestContextHolder.resetRequestAttributes();
    }
}
