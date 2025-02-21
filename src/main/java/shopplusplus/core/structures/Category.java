package shopplusplus.core.structures;

import shopplusplus.Plugin;

public class Category {
    public static final long BUILDING_BLOCKS = 1;
    public static final long TOOLS = 1L << 1;
    public static final long FOOD = 1L << 2;
    public static final long MINERALS = 1L << 3;
    public static final long NATURAL = 1L << 4;
    public static final long REDSTONE = 1L << 5;
    public static final long MISCELLANEOUS = 1L << 6;

    public static String getFromLong(long category) {
        return Plugin.language.get("identification.categories." + String.valueOf(category));
    }
}