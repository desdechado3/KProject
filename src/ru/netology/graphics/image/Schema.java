package ru.netology.graphics.image;

public class Schema implements TextColorSchema {

    @Override
    public String convert(int color) {
        final String base = "#$@%*+-";
        //final String base = "MNHQ&OC?7>!;:-.";
        String result;
        int index = Math.round(color * (base.length() + 1) / 255);
        result = index >= base.length() ? "'" : String.valueOf(base.charAt(index));
        return result;
    }
}
