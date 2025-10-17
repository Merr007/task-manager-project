package org.tasker.projectservice.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для исключения ProjectMemberAlreadyExists
 */
class ProjectMemberAlreadyExistsTest {

    @Test
    void testExceptionCreationWithMessage() {
        // Given
        String message = "Project member already exists";

        // When
        ProjectMemberAlreadyExists exception = new ProjectMemberAlreadyExists(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionInheritance() {
        // Given
        ProjectMemberAlreadyExists exception = new ProjectMemberAlreadyExists("Test message");

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithNullMessage() {
        // When
        ProjectMemberAlreadyExists exception = new ProjectMemberAlreadyExists(null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    void testExceptionWithEmptyMessage() {
        // When
        ProjectMemberAlreadyExists exception = new ProjectMemberAlreadyExists("");

        // Then
        assertEquals("", exception.getMessage());
    }
}
