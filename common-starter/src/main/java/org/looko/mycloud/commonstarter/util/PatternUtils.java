package org.looko.mycloud.commonstarter.util;

import java.util.regex.Pattern;

public class PatternUtils {
    public static final Pattern lengthPattern1to128 = Pattern.compile("^.{1,128}$");
    public static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
}
