package org.tasker.projectservice.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для исключения NoSuchTagException
 */
class NoSuchTagExceptionTest {

    @Test
    void testExceptionCreationWithMessage() {
        // Given
        String message = "Tag not found";

        // When
        NoSuchTagException exception = new NoSuchTagException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionInheritance() {
        // Given
        NoSuchTagException exception = new NoSuchTagException("Test message");

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithNullMessage() {
        // When
        NoSuchTagException exception = new NoSuchTagException(null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    void testExceptionWithEmptyMessage() {
        // When
        NoSuchTagException exception = new NoSuchTagException("");

        // Then
        assertEquals("", exception.getMessage());
    }
}
