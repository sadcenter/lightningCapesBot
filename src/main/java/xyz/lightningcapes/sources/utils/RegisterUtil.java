package xyz.lightningcapes.sources.utils;

import org.apache.commons.lang3.StringUtils;

public final class RegisterUtil {

    public static boolean isAlphanumeric(CharSequence cs) {
        if (StringUtils.isEmpty(cs)) {
            return false;
        } else {
            for (int i = 0; i < cs.length(); ++i) {
                char ch = cs.charAt(i);
                if (ch == '_')
                    continue;

                if (!Character.isLetterOrDigit(ch)) {
                    return false;
                }
            }

            return true;
        }
    }


}
