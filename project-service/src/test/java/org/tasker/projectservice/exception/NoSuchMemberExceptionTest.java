package org.tasker.projectservice.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для исключения NoSuchMemberException
 */
class NoSuchMemberExceptionTest {

    @Test
    void testExceptionCreationWithMessage() {
        // Given
        String message = "Member not found";

        // When
        NoSuchMemberException exception = new NoSuchMemberException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionInheritance() {
        // Given
        NoSuchMemberException exception = new NoSuchMemberException("Test message");

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithNullMessage() {
        // When
        NoSuchMemberException exception = new NoSuchMemberException(null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    void testExceptionWithEmptyMessage() {
        // When
        NoSuchMemberException exception = new NoSuchMemberException("");

        // Then
        assertEquals("", exception.getMessage());
    }
}
