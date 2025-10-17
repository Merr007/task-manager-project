package org.tasker.projectservice.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для перечисления ProjectStatus
 */
class ProjectStatusTest {

    @Test
    void testProjectStatusValues() {
        // Проверяем что все статусы существуют
        ProjectStatus[] statuses = ProjectStatus.values();

        assertEquals(5, statuses.length);
        assertTrue(contains(statuses, ProjectStatus.CREATED));
        assertTrue(contains(statuses, ProjectStatus.IN_PROGRESS));
        assertTrue(contains(statuses, ProjectStatus.ON_HOLD));
        assertTrue(contains(statuses, ProjectStatus.COMPLETED));
        assertTrue(contains(statuses, ProjectStatus.ARCHIVED));
    }

    @Test
    void testProjectStatusOrdinals() {
        // Проверяем порядковые номера статусов
        assertEquals(0, ProjectStatus.CREATED.ordinal());
        assertEquals(1, ProjectStatus.IN_PROGRESS.ordinal());
        assertEquals(2, ProjectStatus.ON_HOLD.ordinal());
        assertEquals(3, ProjectStatus.COMPLETED.ordinal());
        assertEquals(4, ProjectStatus.ARCHIVED.ordinal());
    }

    @Test
    void testProjectStatusNames() {
        // Проверяем строковые представления статусов
        assertEquals("CREATED", ProjectStatus.CREATED.name());
        assertEquals("IN_PROGRESS", ProjectStatus.IN_PROGRESS.name());
        assertEquals("ON_HOLD", ProjectStatus.ON_HOLD.name());
        assertEquals("COMPLETED", ProjectStatus.COMPLETED.name());
        assertEquals("ARCHIVED", ProjectStatus.ARCHIVED.name());
    }

    private boolean contains(ProjectStatus[] statuses, ProjectStatus status) {
        for (ProjectStatus s : statuses) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}
