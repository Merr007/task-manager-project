package org.tasker.common.exception;

import lombok.Getter;

@Getter
public class StackTraceElem {

    private final String className;
    private final String methodName;
    private final String fileName;
    private final int lineNumber;

    public StackTraceElem(StackTraceElement element) {
        this.className = element.getClassName();
        this.methodName = element.getMethodName();
        this.fileName = element.getFileName();
        this.lineNumber = element.getLineNumber();
    }
}
