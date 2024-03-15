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
```java
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
```java
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
```java
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

## LLM: GitHub Copilot with alternative solution in the project
In this subtask I tried to have the implementation generated by GitHub Copilot, leaving examples of alternative solutions in the project directory to take inspiration from, in this case the version of the solution previously generated by ChatGPT.     

### First request
>Create the CopilotTimetableFactoryImpl and the CopilotTimeTableImpl in the same file to pass all the tests in the file Test.java in the current package.

The answer is:    
Sure, let's create the CopilotTimetableFactoryImpl and CopilotTimeTableImpl classes. These classes will implement the TimetableFactory and Timetable interfaces respectively. We will use the same methods and logic as in the ChatGPTTimetableFactoryImpl and TimetableImpl classes.
```java
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

## LLM: GitHub Copilot from zero
In this subtask, I tried to have the implementation generated without GitHub Copilot having examples of alternative solutions to take inspiration from in the directory.    
### First request
> Generate code of the class TimetableFactoryImpl that implements the interface
> TimetableFactory and the class TimetableImpl, that implements the interface
> Timetable, to pass all the tests.

This is the answer:
To pass all the tests, we need to implement the `TimetableFactory` interface and the `Timetable` interface. The `TimetableFactory` interface should have methods to create an empty timetable, a single timetable, join two timetables, and cut a timetable based on a condition. The `Timetable` interface should have methods to add an hour to an activity on a specific day, get the activities, get the days, get the single data of an activity on a specific day, and get the sum of hours for a set of activities on a set of days.

Here is the implementation of the `Timetable` interface:

```java
import java.util.*;

public class TimetableImpl implements Timetable {
    private Map<String, Map<String, Integer>> timetable;

    public TimetableImpl() {
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
```

And here is the implementation of the `TimetableFactory` interface:

```java
import java.util.function.BiFunction;

public class CopilotTimetableFactoryImpl implements TimetableFactory {
    @Override
    public Timetable empty() {
        return new TimetableImpl();
    }

    @Override
    public Timetable single(String activity, String day) {
        return new TimetableImpl().addHour(activity, day);
    }

    @Override
    public Timetable join(Timetable t1, Timetable t2) {
        Timetable joined = new TimetableImpl();
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
        Timetable cut = new TimetableImpl();
        for (String activity : t.activities()) {
            for (String day : t.days()) {
                for (int i = 0; i < Math.min(t.getSingleData(activity, day), f.apply(activity, day)); i++) {
                    cut.addHour(activity, day);
                }
            }
        }
        return cut;
    }
}
```
### Second request
> The test method testBounds return an error:
> java.lang.AssertionError: Expected :[act3, act2, act1] Actual :[]

The answer is:
It seems like the `cut` method in the `CopilotTimetableFactoryImpl` class is not working as expected. The `cut` method is supposed to reduce the number of hours for each activity on each day based on the provided function, but it seems to be removing all activities and days instead.

The issue might be in the implementation of the `cut` method. It's currently using `Math.min(t.getSingleData(activity, day), f.apply(activity, day))` to determine the number of hours to keep for each activity on each day. If the function `f` returns 0 for all activities and days, this would result in all hours being removed, which seems to be the case based on the test failure.

Here's a revised version of the `cut` method that should fix the issue:

```java
@Override
public Timetable cut(Timetable t, BiFunction<String, String, Integer> f) {
    Timetable cut = new TimetableImpl();
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
```

This version of the `cut` method still uses `Math.min(t.getSingleData(activity, day), f.apply(activity, day))` to determine the number of hours to keep, but it ensures that the `addHour` method is called the correct number of times to keep the desired number of hours for each activity on each day.   

### Consideration
The code generated from zero by GitHub Copilot, without an alternative version of the solution in the directory, failed to pass the tests even after displaying the error returned in the output, and needed to be corrected by the developer.    
I noted a important difference in the behaviour of GitHub Copilot between when it has useful examples to take inspiration from in the project directory and when it does not. 

# Task 2: Testing
## LLM: ChatGPT
### First request
> From this implementation write me JUnit tests for the methods of the class:
> TimetableFactoryImpl.java here: [TimetableFactoryImpl.java]

The answer is:    
To write JUnit tests for the methods of the TimetableFactoryImpl class, we need to test the functionality of the methods empty(), single(), join(), and cut(). We will create a new class for the JUnit tests and write test methods to cover different scenarios for each method. Here's an example of how you can write JUnit tests for this class:

```java
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

```java
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



