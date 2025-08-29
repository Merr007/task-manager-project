package org.tasker.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionContainer {
    private final String message;
    private final List<StackTraceElem> stackTrace;

    public ExceptionContainer(Exception e) {
        this.message = e.getMessage();
        this.stackTrace = createStackTrace(e);
    }

    private List<StackTraceElem> createStackTrace(Exception e) {
        return Arrays.stream(e.getStackTrace()).map(StackTraceElem::new).toList();
    }
}
