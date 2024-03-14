package a01a.task1LLMCodeGeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class TimetableFactoryImpl implements TimetableFactory {

    @Override
    public Timetable empty() {
        return new TimetableImpl(new HashMap<>());
    }

    @Override
    public Timetable single(String activity, String day) {
        Map<Pair<String, String>, Integer> data = new HashMap<>();
        data.put(new Pair<>(activity, day), 1);
        return new TimetableImpl(data);
    }

    @Override
    public Timetable join(Timetable table1, Timetable table2) {
        Map<Pair<String, String>, Integer> newData = new HashMap<>();

        // Combine data from table1
        for (Map.Entry<Pair<String, String>, Integer> entry : ((TimetableImpl) table1).getData().entrySet()) {
            newData.put(entry.getKey(), newData.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }

        // Combine data from table2
        for (Map.Entry<Pair<String, String>, Integer> entry : ((TimetableImpl) table2).getData().entrySet()) {
            newData.put(entry.getKey(), newData.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }

        return new TimetableImpl(newData);
    }


    @Override
    public Timetable cut(Timetable table, BiFunction<String, String, Integer> bounds) {
        Map<Pair<String, String>, Integer> newData = new HashMap<>();
        Map<Pair<String, String>, Integer> oldData = ((TimetableImpl) table).getData();

        for (Map.Entry<Pair<String, String>, Integer> entry : oldData.entrySet()) {
            String activity = entry.getKey().get1();
            String day = entry.getKey().get2();
            int hours = entry.getValue();
            int maxHours = bounds.apply(activity, day);
            int newHours = Math.min(hours, maxHours);
            newData.put(new Pair<>(activity, day), newHours);
        }

        return new TimetableImpl(newData);
    }

    private static class TimetableImpl implements Timetable {
        private final Map<Pair<String, String>, Integer> data;

        public TimetableImpl(Map<Pair<String, String>, Integer> data) {
            this.data = data;
        }

        public Map<Pair<String, String>, Integer> getData() {
            return data;
        }

        @Override
        public Timetable addHour(String activity, String day) {
            Map<Pair<String, String>, Integer> newData = new HashMap<>(data);
            Pair<String, String> key = new Pair<>(activity, day);
            newData.put(key, newData.getOrDefault(key, 0) + 1);
            return new TimetableImpl(newData);
        }

        @Override
        public Set<String> activities() {
            Set<String> activities = new HashSet<>();
            for (Pair<String, String> key : data.keySet()) {
                activities.add(key.get1());
            }
            return activities;
        }

        @Override
        public Set<String> days() {
            Set<String> days = new HashSet<>();
            for (Pair<String, String> key : data.keySet()) {
                days.add(key.get2());
            }
            return days;
        }

        @Override
        public int getSingleData(String activity, String day) {
            return data.getOrDefault(new Pair<>(activity, day), 0);
        }

        @Override
        public int sums(Set<String> activities, Set<String> days) {
            int sum = 0;
            for (Pair<String, String> key : data.keySet()) {
                if (activities.contains(key.get1()) && days.contains(key.get2())) {
                    sum += data.get(key);
                }
            }
            return sum;
        }
    }
}