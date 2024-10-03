package ya.market.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * класс обрабатывает строки содержащие кириллицу
 */
public class LetterTranslate {

    /**
     * переменная символов словаря с <k>-кирилица, <v>-латиница
     */
    private static final Map<Character, Character> cyrillicToLatin = new HashMap<>();


    /**
     * блок <k>,<v> значений
     */
    static {
        cyrillicToLatin.put('а', 'a');
        cyrillicToLatin.put('б', 'b');
        cyrillicToLatin.put('в', 'v');
        cyrillicToLatin.put('г', 'g');
        cyrillicToLatin.put('д', 'd');
        cyrillicToLatin.put('е', 'e');
        cyrillicToLatin.put('з', 'z');
        cyrillicToLatin.put('и', 'i');
        cyrillicToLatin.put('й', 'j');
        cyrillicToLatin.put('к', 'k');
        cyrillicToLatin.put('л', 'l');
        cyrillicToLatin.put('м', 'm');
        cyrillicToLatin.put('н', 'n');
        cyrillicToLatin.put('о', 'o');
        cyrillicToLatin.put('п', 'p');
        cyrillicToLatin.put('р', 'r');
        cyrillicToLatin.put('с', 's');
        cyrillicToLatin.put('т', 't');
        cyrillicToLatin.put('у', 'u');
        cyrillicToLatin.put('ф', 'f');
        cyrillicToLatin.put('х', 'h');
        cyrillicToLatin.put('э', 'e');
    }

    /**
     * метод перебирает и заменяет кириллицу на латиницу
     * @param text строка для обработки
     * @return строка
     */
    public static String convertCyrillicToLatin(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 0xC0 && c <= 0xD6 || c >= 0xD8 && c <= 0xF2 || c == 0xF5) {
                continue;
            }
            result.append(cyrillicToLatin.getOrDefault(c, c));
        }
        return result.toString();
    }
}