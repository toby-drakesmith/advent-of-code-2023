package advent.of.code.day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;

public class Day2 {

    private static final int RED = 12;
    private static final int GREEN = 13;
    private static final int BLUE = 14;

    public static void main(String[] args) throws IOException {

            final BufferedReader bufferedReader =
                    new BufferedReader(new FileReader("/home/drakesmitht/IdeaProjects/AdventOfCode/src/advent/of/code/day/two/input.txt"));

        int total = 0;
        int powerTotal = 0;

        while (true)
        {
            final String line = bufferedReader.readLine();
            if (line == null)
                break;
            final SimpleEntry<Integer, List<CubeSet>> simpleEntry = parseLine(line);
            if (isLegal(simpleEntry))
            {
                total += simpleEntry.getKey();
            }
            powerTotal += calculateMinimumPowerSet(simpleEntry);
        }
        System.out.println(total);
        System.out.println(powerTotal);
    }

    private static int calculateMinimumPowerSet(SimpleEntry<Integer, List<CubeSet>> simpleEntry) {
       final List<CubeSet> sets = simpleEntry.getValue();
        int maxRed = 0, maxBlue = 0, maxGreen = 0;
        for (final CubeSet cubeSet : sets) {
            maxRed = max(maxRed, cubeSet.red());
            maxBlue = max(maxBlue, cubeSet.blue());
            maxGreen = max(maxGreen, cubeSet.green());
        }
        return maxRed*maxBlue*maxGreen;
    }

    private static boolean isLegal(SimpleEntry<Integer, List<CubeSet>> simpleEntry) {
        return simpleEntry.getValue()
                .stream()
                .noneMatch(cubeSet -> cubeSet.red() > RED || cubeSet.blue() > BLUE || cubeSet.green() > GREEN );
    }

    private static SimpleEntry<Integer, List<CubeSet>> parseLine(final String line)
    {
        final int id = parseInt(line.split(" ")[1].replaceAll(":", ""));
        final ArrayList<CubeSet> list = new ArrayList<>();
        final String[] split = line.split(": ")[1].split("; ");

        for (final String set : split)
        {
            list.add(parseCubeSet(set));
        }

        return new SimpleEntry<>(id, list);
    }

    private static CubeSet parseCubeSet(final String set) {

        int blue = 0, green = 0 , red = 0;
        for (final String s : set.split(", "))
        {
            switch (s.split(" ")[1])
            {
                case "blue" -> blue = parseInt(s.split(" ")[0]);
                case "red" -> red = parseInt(s.split(" ")[0]);
                case "green" -> green = parseInt(s.split(" ")[0]);
            }
        }
        return new CubeSet(blue, red, green);
    }
}

record CubeSet(int blue, int red, int green) {}
