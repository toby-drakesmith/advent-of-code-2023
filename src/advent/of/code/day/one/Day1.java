package advent.of.code.day.one;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

public class Day1 {

    private static final HashMap<String, String> numberMappings = new HashMap<>() {{
        put("one", "1");
        put("two", "2");
        put("three", "3");
        put("four", "4");
        put("five", "5");
        put("six", "6");
        put("seven", "7");
        put("eight", "8");
        put("nine", "9");
    }};

    public static void main(final String[] args) throws IOException {

        final BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/drakesmitht/IdeaProjects/AdventOfCode/src/advent/of/code/day/one/input.txt"));
        int total;
        String line = bufferedReader.readLine();

        total = Integer.parseInt(getFirstDigit(line) + String.valueOf(getLastDigit(line)));
        while (line != null) {
            line = bufferedReader.readLine();

            if (line == null)
            {
                break;
            }

            total += Integer.parseInt((getFirstDigit(line) + String.valueOf(getLastDigit(line))));

        }
        bufferedReader.close();
        System.out.println(total);
    }

    private static char getFirstDigit(final String line)
    {
        final String newString = replaceStringNumbersStart(line);
        for (int i = 0; i < newString.length(); i++)
        {
            if (Character.isDigit(newString.charAt(i)))
            {
                return newString.charAt(i);
            }
        }
        throw new RuntimeException();
    }

    private static char getLastDigit(final String line)
    {
        final String newString = replaceStringNumbersEnd(line);
        for (int i = newString.length() - 1 ; i >= 0; i--)
        {
            if (Character.isDigit(newString.charAt(i)))
            {
                return newString.charAt(i);
            }
        }
        throw new RuntimeException();
    }


    private static String replaceStringNumbersEnd(final String line)
    {
        final Optional<String> latestStringNumber = getLatestStringNumber(line);

        if (latestStringNumber.isPresent())
        {
            final String earliestString = latestStringNumber.get();
            return line.replaceAll(earliestString, numberMappings.get(earliestString));
        }
        return line;
    }

    private static String replaceStringNumbersStart(final String line)
    {
        final Optional<String> earliestStringNumber = getEarliestStringNumber(line);

        if (earliestStringNumber.isPresent())
        {
            final String earliestString = earliestStringNumber.get();
            return line.replaceFirst(earliestString, numberMappings.get(earliestString));
        }
        return line;
    }

    private static Optional<String> getEarliestStringNumber(final String line)
    {
        return numberMappings
                .keySet()
                .stream()
                .filter(line::contains)
                .min(Comparator.comparingInt(line::indexOf));
    }

    private static Optional<String> getLatestStringNumber(final String line)
    {
        return numberMappings
                .keySet()
                .stream()
                .filter(line::contains)
                .max(Comparator.comparingInt(line::lastIndexOf));
    }
}
