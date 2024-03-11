package a01a.e1LLMSolution;

import java.util.*;

/**
 * Models a timetable, a table in which each row reports the number of hours (>= 0) spent in each activity, and each column represents a given day.
 * Though not strictly necessary, a timetable implementation may be immutable.
 */
public interface Timetable {

	/**
	 * @param activity
	 * @param day
	 * @return a new timetable similar to 'this', but with an additional hour on @activity at the given @day
	 */
	Timetable addHour(String activity, String day);

	/**
	 * @return the set of (names of) activities (added so far)
	 */
	Set<String> activities();

	/**
	 * @return the set of (names of) days (added so far)
	 */
	Set<String> days();

	/**
	 * @param activity
	 * @param day
	 * @return how many hours were spent on @activity at the given @day
	 */
	int getSingleData(String activity, String day);

	/**
	 * @param activities
	 * @param days
	 * @return how many hours were spent overall on @activities at the given @days
	 */
	int sums(Set<String> activities, Set<String> days);
}
