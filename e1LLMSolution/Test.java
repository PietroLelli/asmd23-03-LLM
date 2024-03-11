package a01a.e1LLMSolution;

import static org.junit.Assert.*;

import java.util.*;

public class Test {

	/*
	 * Implementare l'interfaccia TimetableFactory come indicato nel metodo
	 * initFactory qui sotto. Realizza una factory per un concetto di tabella
	 * orari, catturato dall'interfaccia Timetable: essenzialmente è una tabella
	 * che ad ogni giorno e tipo di attività associa un numero di ore svolte (>=0).
	 * 
	 * Sono considerati opzionali ai fini della possibilità di correggere
	 * l'esercizio, ma concorrono comunque al raggiungimento della totalità del
	 * punteggio:
	 * 
	 * - implementazione di tutti i metodi della factory (ossia, nella parte
	 * obbligatoria è sufficiente implementarli tutti tranne uno a piacimento -- 
	 * il primo, empty, è obbligatorio)
	 * - la buona progettazione della soluzione, utilizzando soluzioni progettuali che portino a
	 * codice succinto che evita ripetizioni
	 * 
	 * Si tolga il commento dal metodo initFactory.
	 * 
	 * Indicazioni di punteggio:
	 * - correttezza della parte obbligatoria: 10 punti
	 * - correttezza della parte opzionale: 3 punti (ulteriore metodo della factory)
	 * - qualità della soluzione: 4 punti (per buon design)
	 * 
	 */

	private TimetableFactory factory;

	@org.junit.Before
	public void initFactory() {
		this.factory = new TimetableFactoryImpl();
	}

	@org.junit.Test
	public void testEmpty() {
		// una tabella vuota, senza giorni e attività
		Timetable table = this.factory.empty();
		assertEquals(Set.of(), table.activities());
		assertEquals(Set.of(), table.days());
		assertEquals(0, table.getSingleData("act1", "day2"));
		assertEquals(0, table.sums(Set.of("act"), Set.of("day")));

		// ora una tabella con un'ora di attività "act" nel giorno "day"
		table = table.addHour("act", "day");
		assertEquals(Set.of("act"), table.activities());
		assertEquals(Set.of("day"), table.days());
		assertEquals(1, table.getSingleData("act", "day"));
		assertEquals(0, table.getSingleData("act0", "day0")); // no
		assertEquals(1, table.sums(Set.of("act"), Set.of("day"))); // ore di "act" nel giorno "day"
		assertEquals(0, table.sums(Set.of("act"), Set.of("day0"))); // no
	}

	@org.junit.Test
	public void testSingle() {
		// single si comporta come une empty con aggiunta di un'ora, come sopra
		Timetable table = this.factory.single("act1", "day1");
		assertEquals(Set.of("act1"), table.activities());
		assertEquals(Set.of("day1"), table.days());
		assertEquals(1, table.getSingleData("act1", "day1"));
		assertEquals(0, table.getSingleData("act0", "day0"));
		assertEquals(1, table.sums(Set.of("act1"), Set.of("day1")));
		assertEquals(0, table.sums(Set.of("act1"), Set.of()));

		// aggiungo 3 ore a table, diventano 4
		table = table.addHour("act1", "day1"); // nota ora ho 2 ore per act1 in day1
		table = table.addHour("act1", "day2");
		table = table.addHour("act2", "day2");
		assertEquals(Set.of("act1", "act2"), table.activities());
		assertEquals(Set.of("day1", "day2"), table.days());
		assertEquals(2, table.getSingleData("act1", "day1"));
		assertEquals(1, table.getSingleData("act1", "day2"));
		assertEquals(1, table.getSingleData("act2", "day2"));
		assertEquals(0, table.getSingleData("act2", "day1"));
		assertEquals(2, table.sums(Set.of("act1"), Set.of("day1")));
		assertEquals(0, table.sums(Set.of("act2"), Set.of("day1")));
		assertEquals(4, table.sums(Set.of("act1", "act2"), Set.of("day1", "day2"))); // ore in totale
	}

	@org.junit.Test
	public void testJoin() {
		Timetable table1 = this.factory.empty()
			.addHour("act1", "day1")
			.addHour("act1", "day1")
			.addHour("act2", "day2");

		Timetable table2 = this.factory.empty()
			.addHour("act2", "day1")
			.addHour("act2", "day2")
			.addHour("act1", "day3")
			.addHour("act3", "day3");

		// unisco le ore di due table diverse: si sommano
		Timetable table = this.factory.join(table1, table2);
		assertEquals(Set.of("act1", "act2", "act3"), table.activities());
		assertEquals(Set.of("day1", "day2", "day3"), table.days());
		assertEquals(2, table.getSingleData("act1", "day1"));
		assertEquals(2, table.getSingleData("act2", "day2"));
		assertEquals(1, table.getSingleData("act1", "day3"));
		assertEquals(0, table.getSingleData("act2", "day3"));
		
		assertEquals(7, table.sums(Set.of("act1", "act2", "act3"), Set.of("day1", "day2", "day3")));
		assertEquals(6, table.sums(Set.of("act1", "act2"), Set.of("day1", "day2", "day3")));
		assertEquals(2, table.sums(Set.of("act1", "act2", "act3"), Set.of("day3")));
		
		// posso aggiungere una sola ora al solito
		table = table.addHour("act1", "day1");
		assertEquals(8, table.sums(Set.of("act1", "act2", "act3"), Set.of("day1", "day2", "day3")));
		
	}

	@org.junit.Test
	public void testBounds() {
		Timetable table = this.factory.empty()
			.addHour("act1", "day1")
			.addHour("act1", "day1")
			.addHour("act1", "day1")
			.addHour("act1", "day2")
			.addHour("act1", "day3")
			.addHour("act1", "day3")
			.addHour("act2", "day1")
			.addHour("act2", "day1")
			.addHour("act3", "day2")
			.addHour("act3", "day3");

		// data una tabella da 10 ore come sopra, le rimuovo tutte
		table = this.factory.cut(table, (a,d) -> 0);
		assertEquals(Set.of("act1", "act2", "act3"), table.activities());
		assertEquals(Set.of("day1", "day2", "day3"), table.days());
		assertEquals(0, table.sums(Set.of("act1", "act2", "act3"), Set.of("day1", "day2", "day3")));

		table = this.factory.empty()
			.addHour("act1", "day1")
			.addHour("act1", "day1")
			.addHour("act1", "day1")
			.addHour("act1", "day2")
			.addHour("act1", "day3")
			.addHour("act1", "day3")
			.addHour("act2", "day1")
			.addHour("act2", "day1")
			.addHour("act3", "day2")
			.addHour("act3", "day3");

		// data una tabella da 10 ore come sopra, al massimo ne consento 1 al giorno per attività, diventano 6
		table = this.factory.cut(table, (a,d) -> 1);
		assertEquals(Set.of("act1", "act2", "act3"), table.activities());
		assertEquals(Set.of("day1", "day2", "day3"), table.days());
		assertEquals(1, table.getSingleData("act1", "day1"));
		assertEquals(6, table.sums(Set.of("act1", "act2", "act3"), Set.of("day1", "day2", "day3")));
	}
}