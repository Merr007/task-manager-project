package org.tasker.projectservice.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для исключения NoSuchProjectException
 */
class NoSuchProjectExceptionTest {

    @Test
    void testExceptionCreationWithMessage() {
        // Given
        String message = "Project not found";

        // When
        NoSuchProjectException exception = new NoSuchProjectException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionInheritance() {
        // Given
        NoSuchProjectException exception = new NoSuchProjectException("Test message");

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithNullMessage() {
        // When
        NoSuchProjectException exception = new NoSuchProjectException(null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    void testExceptionWithEmptyMessage() {
        // When
        NoSuchProjectException exception = new NoSuchProjectException("");

        // Then
        assertEquals("", exception.getMessage());
    }
}
