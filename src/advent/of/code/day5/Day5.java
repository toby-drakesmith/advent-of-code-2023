package advent.of.code.day5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day5
{
    final static Map<Long, Long> seedToSoilMap = new HashMap<>();
    final static Map<Long, Long> soilToFertiliserMap = new HashMap<>();
    final static Map<Long, Long> fertiliserToWaterMap = new HashMap<>();
    final static Map<Long, Long> waterToLightMap = new HashMap<>();
    final static Map<Long, Long> lightToTemperatureMap = new HashMap<>();
    final static Map<Long, Long> temperatureToHumidtyMap = new HashMap<>();
    final static Map<Long, Long> humidityToLocationMap = new HashMap<>();

    public static void main(final String[] args) throws IOException
    {
        final BufferedReader bufferedReader =
                new BufferedReader(new FileReader("/home/drakesmitht/IdeaProjects/AdventOfCode/src/advent/of/code/day/five/test.txt"));

        final String seedsLine = bufferedReader.readLine();
        final List<Long> seeds = new ArrayList<>(getSeeds(seedsLine));
        final List<Long> seedsPart2 = new ArrayList<>(getSeedsPart2(seedsLine));

        final List<Long> soils = new ArrayList<>();
        final List<Long> fertilisers = new ArrayList<>();
        final List<Long> waters = new ArrayList<>();
        final List<Long> lights = new ArrayList<>();
        final List<Long> temperatures = new ArrayList<>();
        final List<Long> humidities = new ArrayList<>();
        final List<Long> locations = new ArrayList<>();

        while (true)
        {
            final String line = bufferedReader.readLine();

            if (line == null)
            {
                break;
            }


            switch (line)
            {
                case "seed-to-soil map:" ->
                {
                    extracted(bufferedReader, seedToSoilMap, seedsPart2);
                    for (Long seed : seedsPart2)
                    {
                        if (!seedToSoilMap.containsKey(seed))
                        {
                            seedToSoilMap.put(seed, seed);
                        }
                    }
                    soils.addAll(seedToSoilMap.values().stream().toList());
                }
                case "soil-to-fertilizer map:" ->
                {
                    extracted(bufferedReader, soilToFertiliserMap, soils);
                    for (Long soil : soils)
                    {
                         if (!soilToFertiliserMap.containsKey(soil))
                         {
                             soilToFertiliserMap.put(soil, soil);
                         }
                    }
                    fertilisers.addAll(soilToFertiliserMap.values().stream().toList());
                }
                case "fertilizer-to-water map:" ->
                {
                    extracted(bufferedReader, fertiliserToWaterMap, fertilisers);
                    for (Long fert : fertilisers)
                    {
                        if (!fertiliserToWaterMap.containsKey(fert))
                        {
                            fertiliserToWaterMap.put(fert, fert);
                        }
                    }
                    waters.addAll(fertiliserToWaterMap.values().stream().toList());
                }
                case "water-to-light map:" ->
                {
                    extracted(bufferedReader, waterToLightMap, waters);
                    for (Long water : waters)
                    {
                        if (!waterToLightMap.containsKey(water))
                        {
                            waterToLightMap.put(water, water);
                        }
                    }
                    lights.addAll(waterToLightMap.values().stream().toList());
                }
                case "light-to-temperature map:" ->
                {
                    extracted(bufferedReader, lightToTemperatureMap, lights);
                    for (Long light : lights)
                    {
                        if (!lightToTemperatureMap.containsKey(light))
                        {
                            lightToTemperatureMap.put(light, light);
                        }
                    }
                    temperatures.addAll(lightToTemperatureMap.values().stream().toList());
                }
                case "temperature-to-humidity map:" ->
                {
                    extracted(bufferedReader, temperatureToHumidtyMap, temperatures);
                    for (Long temperature : temperatures)
                    {
                        if (!temperatureToHumidtyMap.containsKey(temperature))
                        {
                            temperatureToHumidtyMap.put(temperature, temperature);
                        }
                    }
                    humidities.addAll(temperatureToHumidtyMap.values().stream().toList());
                }
                case "humidity-to-location map:" ->
                {
                    extracted(bufferedReader, humidityToLocationMap, humidities);
                }
            }
        }
        OptionalLong min = seedsPart2.stream().map(Day5::chain).mapToLong(Long::longValue).min();
        System.out.println(min.getAsLong());
    }

    private static List<Long> getSeeds(final String line)
    {
        return Arrays.stream(line.split("seeds: ")[1].split(" ")).map(Long::parseLong).collect(Collectors.toList());
    }

    private static List<Long> getSeedsPart2(final String line)
    {

        List<Long> toReturn = new ArrayList<>();
        String[] split = line.split("seeds: ")[1].split(" ");
        for (int i = 0; i < split.length; i+=2)
        {
            toReturn.addAll(LongStream.range(Long.parseLong(split[i]), Long.parseLong(split[i]) + Long.parseLong(split[i+1])).boxed().toList());
        }
        return toReturn;
    }

    private static long chain(final long seed)
    {
        final long soil = seedToSoilMap.getOrDefault(seed, seed);
        final long fertiliser = soilToFertiliserMap.getOrDefault(soil, soil);
        final long water = fertiliserToWaterMap.getOrDefault(fertiliser, fertiliser);
        final long light = waterToLightMap.getOrDefault(water, water);
        final long temperature = lightToTemperatureMap.getOrDefault(light, light);
        final long humidity = temperatureToHumidtyMap.getOrDefault(temperature, temperature);

        return humidityToLocationMap.getOrDefault(humidity, humidity);
    }

    private static void extracted(final BufferedReader bufferedReader, final Map<Long, Long> map, List<Long> seeds) throws IOException
    {
        while (true)
        {
            final String s = bufferedReader.readLine();

            if (s == null || s.isEmpty())
            {
                break;
            }

            String[] split = s.split(" ");
            populateMap(map, split, seeds);
        }
    }

    private static void populateMap(final Map<Long, Long> map, final String[] split, List<Long> seeds)
    {
        final long soil = Long.parseLong(split[0]);
        final long seed = Long.parseLong(split[1]);
        final long range = Long.parseLong(split[2]);

        for (int i = 0; i < range; i++)
        {
            if (seeds.contains(seed + i))
            {
                map.put(seed + i, soil + i);
            }
        }
    }
}
