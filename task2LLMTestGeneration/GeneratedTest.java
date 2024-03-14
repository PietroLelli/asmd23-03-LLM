package a01a.task2LLMTestGeneration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.BiFunction;
import java.util.Set;

class GeneratedTest {

    private TimetableFactoryImpl timetableFactory;

    @BeforeEach
    void setUp() {
        timetableFactory = new TimetableFactoryImpl();
    }

    @Test
    void testEmpty() {
        // Test empty timetable
        Timetable emptyTimetable = timetableFactory.empty();
        assertNotNull(emptyTimetable);
        assertEquals(0, emptyTimetable.sums(Set.of(), Set.of()));

        // Test sums with non-existing activity and day
        assertEquals(0, emptyTimetable.sums(Set.of("Study"), Set.of("Monday")));
    }

    @Test
    void testSingle() {
        // Test single timetable
        Timetable singleTimetable = timetableFactory.single("Study", "Monday");
        assertNotNull(singleTimetable);
        assertEquals(1, singleTimetable.sums(Set.of("Study"), Set.of("Monday")));
        assertEquals(0, singleTimetable.sums(Set.of("Study"), Set.of("Tuesday")));

        // Test sums with non-existing activity and day
        assertEquals(0, singleTimetable.sums(Set.of("Play"), Set.of("Monday")));
    }

    @Test
    void testJoin() {
        // Test join timetable
        Timetable table1 = timetableFactory.single("Study", "Monday");
        Timetable table2 = timetableFactory.single("Play", "Tuesday");
        Timetable joinedTimetable = timetableFactory.join(table1, table2);
        assertNotNull(joinedTimetable);
        assertEquals(1, joinedTimetable.sums(Set.of("Study"), Set.of("Monday")));
        assertEquals(1, joinedTimetable.sums(Set.of("Play"), Set.of("Tuesday")));
        assertEquals(0, joinedTimetable.sums(Set.of("Study"), Set.of("Tuesday")));

        // Test sums with non-existing activity and day
        assertEquals(0, joinedTimetable.sums(Set.of("Study"), Set.of("Wednesday")));
    }

    @Test
    void testCut() {
        // Test cut timetable
        Timetable table = timetableFactory.single("Study", "Monday");
        BiFunction<String, String, Integer> bounds = (activity, day) -> {
            if (activity.equals("Study") && day.equals("Monday")) {
                return 1;
            } else {
                return 0;
            }
        };
        Timetable cutTimetable = timetableFactory.cut(table, bounds);
        assertNotNull(cutTimetable);
        assertEquals(1, cutTimetable.sums(Set.of("Study"), Set.of("Monday")));
        assertEquals(0, cutTimetable.sums(Set.of("Study"), Set.of("Tuesday")));

        // Test sums with non-existing activity and day
        assertEquals(0, cutTimetable.sums(Set.of("Play"), Set.of("Monday")));
    }
}