package a01a.task1LLMCodeGeneration;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


class CopilotFromZeroTimetableFactoryImpl implements TimetableFactory {
    @Override
    public Timetable empty() {
        return new CopilotTimetableImpl();
    }

    @Override
    public Timetable single(String activity, String day) {
        return new CopilotTimetableImpl().addHour(activity, day);
    }

    @Override
    public Timetable join(Timetable t1, Timetable t2) {
        Timetable joined = new CopilotTimetableImpl();
        for (String activity : t1.activities()) {
            for (String day : t1.days()) {
                for (int i = 0; i < t1.getSingleData(activity, day); i++) {
                    joined.addHour(activity, day);
                }
            }
        }
        for (String activity : t2.activities()) {
            for (String day : t2.days()) {
                for (int i = 0; i < t2.getSingleData(activity, day); i++) {
                    joined.addHour(activity, day);
                }
            }
        }
        return joined;
    }

    @Override
    public Timetable cut(Timetable t, BiFunction<String, String, Integer> f) {
        Timetable cut = new CopilotTimetableImpl();
        for (String activity : t.activities()) {
            for (String day : t.days()) {
                int hoursToKeep = Math.min(t.getSingleData(activity, day), f.apply(activity, day));
                for (int i = 0; i < hoursToKeep; i++) {
                    cut.addHour(activity, day);
                }
            }
        }
        return cut;
    }


    private static class CopilotTimetableImpl implements Timetable {
        private Map<String, Map<String, Integer>> timetable;

        public CopilotTimetableImpl() {
            this.timetable = new HashMap<>();
        }

        @Override
        public Timetable addHour(String activity, String day) {
            this.timetable.computeIfAbsent(activity, k -> new HashMap<>()).merge(day, 1, Integer::sum);
            return this;
        }

        @Override
        public Set<String> activities() {
            return this.timetable.keySet();
        }

        @Override
        public Set<String> days() {
            return this.timetable.values().stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toSet());
        }

        @Override
        public int getSingleData(String activity, String day) {
            return this.timetable.getOrDefault(activity, Collections.emptyMap()).getOrDefault(day, 0);
        }

        @Override
        public int sums(Set<String> activities, Set<String> days) {
            return this.timetable.entrySet().stream()
                    .filter(e -> activities.contains(e.getKey()))
                    .flatMap(e -> e.getValue().entrySet().stream())
                    .filter(e -> days.contains(e.getKey()))
                    .mapToInt(Map.Entry::getValue)
                    .sum();
        }
    }
}
