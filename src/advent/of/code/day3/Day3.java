package advent.of.code.day3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3
{

    public static void main(String[] args) throws IOException
    {
        final List<Integer> currentRowSymbols = new ArrayList<>();
        List<Integer> previousRowSymbols = new ArrayList<>();

        final Map<List<Integer>, Integer> currentRowDigits = new HashMap<>();
        Map<List<Integer>, Integer> previousRowDigits = new HashMap<>();

        final List<Gear> gears = new ArrayList<>();
        final List<Gear> currentRowGears = new ArrayList<>();
        List<Gear> previousRowGears = new ArrayList<>();

        int sum = 0;
        final BufferedReader bufferedReader =
                new BufferedReader(new FileReader("/home/drakesmitht/IdeaProjects/AdventOfCode/src/advent/of/code/day/three/input.txt"));

        while (true)
        {
            final String line = bufferedReader.readLine();

            if (line == null)
                break;

            findNumbers(line, currentRowDigits);
            findSymbols(line, currentRowSymbols);
            findGears(line, currentRowGears);

            findGearMatches(currentRowDigits, currentRowGears);
            findGearMatches(previousRowDigits, currentRowGears);
            findGearMatches(currentRowDigits, previousRowGears);

            sum += findMatches(currentRowDigits, currentRowSymbols);
            sum += findMatches(currentRowDigits, previousRowSymbols);
            sum += findMatches(previousRowDigits, currentRowSymbols);

            gears.addAll(previousRowGears);
            previousRowDigits = new HashMap<>(currentRowDigits);
            previousRowSymbols = new ArrayList<>(currentRowSymbols);
            previousRowGears = new ArrayList<>(currentRowGears);

            currentRowSymbols.clear();
            currentRowDigits.clear();
            currentRowGears.clear();
        }

        System.out.println(sum);
        System.out.println(sumGears(gears));
    }

    private static void findGears(final String line, final List<Gear> currentRowGears)
    {
        final Pattern gearPattern = Pattern.compile("[*]");
        final Matcher gearMatcher = gearPattern.matcher(line);
        while (gearMatcher.find())
        {
            currentRowGears.add(new Gear(gearMatcher.start()));
        }
    }

    private static void findSymbols(final String line, final List<Integer> currentRowSymbols)
    {
        final Pattern symbolPattern = Pattern.compile("[^a-zA-Z\\d\\s.]");
        final Matcher symbolMatcher = symbolPattern.matcher(line);

        while (symbolMatcher.find())
        {
            currentRowSymbols.add(symbolMatcher.start());
        }
    }

    private static void findNumbers(final String line, final Map<List<Integer>, Integer> currentRowDigits)
    {
        final Pattern numberPattern = Pattern.compile("\\d+");
        final Matcher numberMatcher = numberPattern.matcher(line);
        while (numberMatcher.find())
        {
            final List<Integer> positions = new ArrayList<>();

            for (int i = numberMatcher.start(); i < numberMatcher.end(); i++)
            {
                positions.add(i);
            }
            currentRowDigits.put(positions, Integer.valueOf(numberMatcher.group()));
        }
    }

    private static int sumGears(final List<Gear> gears)
    {
        return gears.stream().mapToInt(Gear::getSum).sum();
    }

    private static int findMatches(final Map<List<Integer>, Integer> digits, final List<Integer> symbols)
    {
        final List<Integer> toReturn = new ArrayList<>();
        final List<List<Integer>> toRemove = new ArrayList<>();
        symbols.forEach(currentRowSymbolPosition -> {
            digits.keySet()
                    .stream()
                    .filter(digitPositions -> digitPositions
                            .stream()
                            .anyMatch(digitPosition -> matches(currentRowSymbolPosition, digitPosition)))
                    .forEach(digitPositions -> {
                        toReturn.add(digits.get(digitPositions));
                        toRemove.add(digitPositions);
                    });
        });

        toRemove.forEach(digits::remove);

        return toReturn.stream().mapToInt(Integer::intValue).sum();
    }

    private static void findGearMatches(final Map<List<Integer>, Integer> digits, final List<Gear> gears)
    {
        final List<Gear> toRemove = new ArrayList<>();
        gears.forEach(gear -> {
            digits.keySet()
                    .stream()
                    .filter(digitPositions -> digitPositions
                            .stream()
                            .anyMatch(digitPosition -> matches(gear.position, digitPosition)))
                    .forEach(digitPositions -> {
                        if (!gear.addNumber(digits.get(digitPositions)))
                            toRemove.add(gear);
                    });
        });
        toRemove.forEach(gears::remove);
    }

    private static boolean matches(final Integer symbolPosition, final Integer digitPosition)
    {
        return symbolPosition.equals(digitPosition - 1)
                || symbolPosition.equals(digitPosition + 1)
                || symbolPosition.equals(digitPosition);
    }
}

class Gear {
    int position;
    List<Integer> numbers;

    Gear(int position) {
        this.position = position;
        numbers = new ArrayList<>();
    }

    boolean addNumber(int number) {
        if (numbers.size() >= 2)
        {
            return false;
        }

        numbers.add(number);
        return true;
    }

    int getSum() {
        if (numbers.size() == 2)
            return numbers.get(0) * numbers.get(1);
        return 0;
    }
}
