import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.*;

public class RainbowRoads {
    public static void main(String[] args) throws Exception {
        HashMap<Integer, HashMap<Integer, Integer>> allStations = new HashMap<>();
        HashMap<Integer, Boolean> rejectedStations = new HashMap<>();
        Queue<Point> stationsQueue = new LinkedList<>();

        File file = new File("src/main/resources/rainbowroads-0001.in");
        Kattio scan = new Kattio(new FileInputStream(file));

//        Kattio scan = new Kattio(System.in);

        int stationsAmount = scan.getInt();

        for (int i = 1; i <= stationsAmount; i++) {
            allStations.put(i, new HashMap<>());
        }

        while (scan.hasMoreTokens()) {
            int stationFrom = scan.getInt();
            int stationDestination = scan.getInt();
            int edgeColor = scan.getInt();

            allStations.get(stationFrom).put(stationDestination, edgeColor);
            allStations.get(stationDestination).put(stationFrom, edgeColor);
        }

        allStations.forEach((station, stationDescription) -> {
            if (!rejectedStations.containsKey(station)) {
                HashMap<Integer, Boolean> checkedStations = new HashMap<>();
                Set<Integer> duplicatedColors = new HashSet<>();
                Set<Integer> readColors = new HashSet<>();

                stationDescription.values().forEach(value ->
                {
                    if (readColors.contains(value)) {
                        duplicatedColors.add(value);
                    } else {
                        readColors.add(value);
                    }
                });

                stationDescription.keySet().stream()
                        .filter(o -> duplicatedColors.contains(stationDescription.get(o)))
                        .forEach(o -> stationsQueue.offer(new Point(o, stationDescription.get(o))));

                checkedStations.put(station, true);

                while (!stationsQueue.isEmpty()) {
                    Point onHandle = stationsQueue.poll();
                    rejectedStations.put(onHandle.x, true);

                    for (int key : allStations.get(onHandle.x).keySet()) {
                        if (allStations.get(onHandle.x).get(key) == onHandle.y && !checkedStations.containsKey(key)) {
                            System.out.println(0);
                            System.exit(0);
                        }

                        if (key != station && !rejectedStations.containsKey(key)) {
                            stationsQueue.offer(new Point(key, allStations.get(onHandle.x).get(key)));
                            checkedStations.put(onHandle.x, true);
                        }
                    }
                }
            }
        });

        TreeSet<Integer> result = new TreeSet<>();
        allStations.keySet().stream()
                .filter(key -> !rejectedStations.containsKey(key))
                .forEach(result::add);

        System.out.println(result.size());
        result.forEach(System.out::println);
    }
}
