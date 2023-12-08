package advent.of.code.day4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day4
{
    public static void main(String[] args) throws IOException
    {

        final List<Integer> winningNumbers = new ArrayList<>();
        final List<Integer> rowNumbers = new ArrayList<>();
        final Map<Integer, Integer> scratchCards = new HashMap<>();

        final BufferedReader bufferedReader =
                new BufferedReader(new FileReader("/home/drakesmitht/IdeaProjects/AdventOfCode/src/advent/of/code/day/four/input.txt"));

        int total = 0;
        while (true)
        {
            final String line = bufferedReader.readLine();

            if (line == null)
            {
                break;
            }

            final int cardNumber = parseCardNumber(line);

            winningNumbers.addAll(parseWinningNumbers(line));
            rowNumbers.addAll(parseRowNumbers(line));

            final List<Integer> matches = rowNumbers.stream().filter(winningNumbers::contains).toList();

            calculateTotalNumberScratchCards(matches, scratchCards, cardNumber);


            total += calculateRowPoints(matches);

            winningNumbers.clear();
            rowNumbers.clear();
        }

        System.out.println(total);
        System.out.println(totalScratchCards(scratchCards));
    }

    private static int totalScratchCards(final Map<Integer, Integer> scratchCards) {
        return scratchCards.values().stream().mapToInt(Integer::intValue).sum();
    }

    private static void calculateTotalNumberScratchCards(final List<Integer> matches, final Map<Integer, Integer> scratchCards, int cardNumber)
    {
        /*
            for each copy:
                for each match:
                    add copy to card number below
         */

        final int numberOfCopies = scratchCards.getOrDefault(cardNumber, 0);

        if (numberOfCopies == 0)
        {
            scratchCards.put(cardNumber, 1);
            for (int i = 0; i < matches.size(); i++)
            {
                if (scratchCards.containsKey(++cardNumber))
                {
                    scratchCards.put(cardNumber, scratchCards.get(cardNumber) + 1);
                }
                else
                {
                    scratchCards.put(cardNumber, 1);
                }
            }
        }
        else
        {
            scratchCards.put(cardNumber, scratchCards.get(cardNumber) + 1);
            for (int i = 0; i < numberOfCopies+1; i++)
            {
                int cn = cardNumber;
                for (int j = 0; j < matches.size(); j++)
                {
                    if (scratchCards.containsKey(++cn))
                    {
                        scratchCards.put(cn, scratchCards.get(cn) + 1);
                    }
                    else
                    {
                        scratchCards.put(cn, 1);
                    }
                }
            }
        }
    }

    private static int parseCardNumber(final String line)
    {
        final String[] splitSpace = line.split(":")[0].split(" ");
        return Integer.parseInt(Arrays.stream(splitSpace)
                .filter(s -> !s.equals("Card"))
                .filter(string -> !string.isEmpty())
                .findFirst()
                .orElseThrow());
    }

    private static int calculateRowPoints(final List<Integer> matches)
    {
        int toReturn = 0;

        for (int i = 0; i < matches.size(); i++)
        {
            if (i == 0)
                toReturn += 1;
            else
                toReturn = toReturn * 2;
        }
        return toReturn;
    }

    private static List<Integer> parseRowNumbers(final String line)
    {
        final String[] rowNumbers = line.split("\\|")[1].split(" ");

        return Arrays.stream(rowNumbers)
                .filter(s -> !s.isEmpty())
                .map(rowNumber -> Integer.parseInt(rowNumber.strip()))
                .collect(Collectors.toList());
    }

    private static List<Integer> parseWinningNumbers(final String line)
    {
        final String[] winningNumbers = line.split("\\|")[0].split(": ")[1].split(" ");

        return Arrays.stream(winningNumbers)
                .filter(s -> !s.isEmpty())
                .map(winningNumber -> Integer.parseInt(winningNumber.strip()))
                .collect(Collectors.toList());
    }
}
