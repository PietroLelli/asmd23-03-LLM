# Task 1: Code Generation

## LLM: ChatGPT

### First request:
>implement the following interfaces (Timetable.java, TimetableFactory.java) in order to pass all tests in the Test.java file, using the Pair.java class if necessary. In the commented code there are further explainations to undestand better what each method must do.  
>Timetable.java here: [Timetable.java]  
>TimetableFactory.java here: [TimetableFactory.java]  
>Test.java here: [Test.java]  
>Pair.java here: [Pair.java]  

The answer is:  
To implement the Timetable and TimetableFactory interfaces, let's start with the Timetable interface:
```
package a01a.e1;

import java.util.*;

public interface Timetable {
    
    Timetable addHour(String activity, String day);
    
    Set<String> activities();

    Set<String> days();

    int getSingleData(String activity, String day);

    int sums(Set<String> activities, Set<String> days);
}
```

Now, let's implement the TimetableFactory interface:
```
package a01a.e1;

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
        newData.putAll(((TimetableImpl) table1).getData());
        newData.putAll(((TimetableImpl) table2).getData());
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
```
This implementation should fulfill all the requirements and pass the provided tests. Let me know if you need further clarification or assistance!

### Second request:
>This test fail: 
    assertEquals(2, table.getSingleData("act2", "day2"));
with this output:
```
java.lang.AssertionError: 
Expected :2
Actual   :1
```

The answer is:
I apologize for the oversight. It seems there was an issue with the join method in the TimetableFactoryImpl class. I'll provide an updated implementation for the join method:
```
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

```
This modification ensures that the join operation correctly sums the hours for each activity on the same day. Please replace the join method in your TimetableFactoryImpl class with the updated version, and re-run your tests. It should resolve the issue you encountered.

### Considerations:
By passing the source code of the interfaces and tests, ChatGPT was able to generate correct implementations that passed the tests in a few requests.  
On the first attempt, one out of four tests failed, so it was necessary to make a second request with the error message that the test returned in the output. The second implementation provided was correct and did not need to be modified.

## LLM: GitHub Copilot
### First request
>Create the CopilotTimetableFactoryImpl and the CopilotTimeTableImpl in the same file to pass all the tests in the file Test.java in the current package.

The answer is:    
Sure, let's create the CopilotTimetableFactoryImpl and CopilotTimeTableImpl classes. These classes will implement the TimetableFactory and Timetable interfaces respectively. We will use the same methods and logic as in the ChatGPTTimetableFactoryImpl and TimetableImpl classes.
```
package a01a.task1LLMCodeGeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class CopilotTimetableFactoryImpl implements TimetableFactory {

    @Override
    public Timetable empty() {
        return new CopilotTimetableImpl(new HashMap<>());
    }

    @Override
    public Timetable single(String activity, String day) {
        Map<Pair<String, String>, Integer> data = new HashMap<>();
        data.put(new Pair<>(activity, day), 1);
        return new CopilotTimetableImpl(data);
    }

    @Override
    public Timetable join(Timetable table1, Timetable table2) {
        Map<Pair<String, String>, Integer> newData = new HashMap<>();

        for (Map.Entry<Pair<String, String>, Integer> entry : ((CopilotTimetableImpl) table1).getData().entrySet()) {
            newData.put(entry.getKey(), newData.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }

        for (Map.Entry<Pair<String, String>, Integer> entry : ((CopilotTimetableImpl) table2).getData().entrySet()) {
            newData.put(entry.getKey(), newData.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }

        return new CopilotTimetableImpl(newData);
    }

    @Override
    public Timetable cut(Timetable table, BiFunction<String, String, Integer> bounds) {
        Map<Pair<String, String>, Integer> newData = new HashMap<>();
        Map<Pair<String, String>, Integer> oldData = ((CopilotTimetableImpl) table).getData();

        for (Map.Entry<Pair<String, String>, Integer> entry : oldData.entrySet()) {
            String activity = entry.getKey().get1();
            String day = entry.getKey().get2();
            int hours = entry.getValue();
            int maxHours = bounds.apply(activity, day);
            int newHours = Math.min(hours, maxHours);
            newData.put(new Pair<>(activity, day), newHours);
        }

        return new CopilotTimetableImpl(newData);
    }

    private static class CopilotTimetableImpl implements Timetable {
        private final Map<Pair<String, String>, Integer> data;

        public CopilotTimetableImpl(Map<Pair<String, String>, Integer> data) {
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
            return new CopilotTimetableImpl(newData);
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
```
### Considerations:
The code generated by GitHub Copilot passed the tests after only one request, so it is very efficient and convenient, also because it is integrated with the development IDE.     
The request was made after the version generated via ChatGPT had already been developed, so Copilot was able to develop a correct version of the code even with an already working version in mind.
This feature can be very useful if we want the generated code to come closer to our programming style.


# Task 2: Testing
## LLM: ChatGPT
### First request
> From this implementation write me JUnit tests for the methods of the class:
> TimetableFactoryImpl.java here: [TimetableFactoryImpl.java]

The answer is:    
To write JUnit tests for the methods of the TimetableFactoryImpl class, we need to test the functionality of the methods empty(), single(), join(), and cut(). We will create a new class for the JUnit tests and write test methods to cover different scenarios for each method. Here's an example of how you can write JUnit tests for this class:

```

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.BiFunction;
import java.util.Set;

class TimetableFactoryImplTest {

    private TimetableFactoryImpl timetableFactory;

    @BeforeEach
    void setUp() {
        timetableFactory = new TimetableFactoryImpl();
    }

    @Test
    void testEmpty() {
        Timetable emptyTimetable = timetableFactory.empty();
        assertNotNull(emptyTimetable);
        assertEquals(0, emptyTimetable.sums(Set.of(), Set.of()));
    }

    @Test
    void testSingle() {
        Timetable singleTimetable = timetableFactory.single("Study", "Monday");
        assertNotNull(singleTimetable);
        assertEquals(1, singleTimetable.sums(Set.of("Study"), Set.of("Monday")));
        assertEquals(0, singleTimetable.sums(Set.of("Study"), Set.of("Tuesday")));
    }

    @Test
    void testJoin() {
        Timetable table1 = timetableFactory.single("Study", "Monday");
        Timetable table2 = timetableFactory.single("Play", "Tuesday");

        Timetable joinedTimetable = timetableFactory.join(table1, table2);

        assertNotNull(joinedTimetable);
        assertEquals(1, joinedTimetable.sums(Set.of("Study"), Set.of("Monday")));
        assertEquals(1, joinedTimetable.sums(Set.of("Play"), Set.of("Tuesday")));
        assertEquals(0, joinedTimetable.sums(Set.of("Study"), Set.of("Tuesday")));
    }

    @Test
    void testCut() {
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
    }
}
```
### Second request
> Test more cases with assertions in each test method.

The answer is:    
Certainly! Let's expand the test cases with additional assertions to cover more scenarios within each test method:

```
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.BiFunction;
import java.util.Set;

class TimetableFactoryImplTest {

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

```
In this expanded test class:    

Each test method now includes additional assertions to cover more cases.
We've added assertions to check scenarios where activities or days are not present in the timetable, ensuring the correctness of the implementation.

### Considerations:
ChatGPT proved useful in the creation of the JUnit tests, passing the implementation code managed with a few requests to generate working tests covering all the methods to be tested. 
    However, the result obtained is certainly less effective than the tests in the solutions, as it only tests the basic operation of the method without delving into more specific cases, even when asked explicitly.

