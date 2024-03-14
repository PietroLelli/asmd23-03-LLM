package a01a.task2LLMTestGeneration;

import java.util.function.*;

public interface TimetableFactory {
	
	/**
	 * @return a new timetable with 0 hours, no activities and no days
	 */
	Timetable empty();

	/**
	 * @param activity
	 * @param day
	 * @return a new timetable with just one hour, spent on @activity at the given @day
	 */
	Timetable single(String activity, String day);

	/**
	 * @param table1
	 * @param table2
	 * @return a new timetable obtainied by joining (summing) the two timetables:
	 * - activities and days are the union
	 * - the overall number of hours is the sum of the two
	 */
	Timetable join(Timetable table1, Timetable table2);

	/**
	 * @param table
	 * @param bounds
	 * @return a new timetable, similar to @table, but where the number of hours spent on an 
	 * activity @a in day @d is not allowed to be more than bounds.apply(a,d)
	 */
	Timetable cut(Timetable table, BiFunction<String, String, Integer> bounds);

}
