package org.tasker.projectservice.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для перечисления ProjectRole
 */
class ProjectRoleTest {

    @Test
    void testProjectRoleValues() {
        // Проверяем что все роли существуют
        ProjectRole[] roles = ProjectRole.values();

        assertEquals(4, roles.length);
        assertTrue(contains(roles, ProjectRole.ADMIN));
        assertTrue(contains(roles, ProjectRole.MEMBER));
        assertTrue(contains(roles, ProjectRole.OWNER));
        assertTrue(contains(roles, ProjectRole.VIEWER));
    }

    @Test
    void testProjectRoleOrdinals() {
        // Проверяем порядковые номера ролей
        assertEquals(0, ProjectRole.ADMIN.ordinal());
        assertEquals(1, ProjectRole.MEMBER.ordinal());
        assertEquals(2, ProjectRole.OWNER.ordinal());
        assertEquals(3, ProjectRole.VIEWER.ordinal());
    }

    @Test
    void testProjectRoleNames() {
        // Проверяем строковые представления ролей
        assertEquals("ADMIN", ProjectRole.ADMIN.name());
        assertEquals("MEMBER", ProjectRole.MEMBER.name());
        assertEquals("OWNER", ProjectRole.OWNER.name());
        assertEquals("VIEWER", ProjectRole.VIEWER.name());
    }

    private boolean contains(ProjectRole[] roles, ProjectRole role) {
        for (ProjectRole r : roles) {
            if (r == role) {
                return true;
            }
        }
        return false;
    }
}
