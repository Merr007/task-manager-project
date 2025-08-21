package org.tasker.usermanagementservice.security.utils;

import org.tasker.usermanagementservice.model.UserRole;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class UserSecurityUtils {
    private static final ThreadLocal<SecureRandom> secureRandom;
    private static final char[] ALPHANUMERIC;

    static {
        ALPHANUMERIC = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        secureRandom = ThreadLocal.withInitial(() -> {
            try {
                return SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private UserSecurityUtils() {
    }

    public static String generateRandomString(int length) {
        char[] chars = new char[length];
        SecureRandom random = secureRandom.get();
        for (int i = 0; i < length; i++) {
            int randomInd = random.nextInt(ALPHANUMERIC.length);
            chars[i] = ALPHANUMERIC[randomInd];
        }
        return new String(chars);
    }

    public static String generateProviderRoles(UserRole role) {
        return "ROLE_" + role.name();
    }

    public static UserRole generateModelRoles(String role) {
        return UserRole.valueOf(role.substring("ROLE_".length()));
    }
}
