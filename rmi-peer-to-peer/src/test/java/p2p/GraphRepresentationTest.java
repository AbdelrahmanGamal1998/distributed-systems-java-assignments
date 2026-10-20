package p2p;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphRepresentationTest {

	private final PrintStream originalOut = System.out;
	private ByteArrayOutputStream captured;
	private Graph_Representation graph;

	@BeforeEach
	void setUp() {
		captured = new ByteArrayOutputStream();
		System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
		graph = new Graph_Representation();
		// Inserting 50, 30, 70, 20, 40, 60, 80 gives a balanced tree of 7 nodes.
		for (int key : new int[] { 50, 30, 70, 20, 40, 60, 80 }) {
			graph.AddConnections(key, "value" + key);
		}
		captured.reset();
	}

	@AfterEach
	void tearDown() {
		System.setOut(originalOut);
	}

	private String output() {
		return captured.toString(StandardCharsets.UTF_8);
	}

	private String inOrder() {
		captured.reset();
		graph.Printing_InOrder(graph.root);
		return output().trim();
	}

	@Test
	void keepsKeysInOrderAndCountsThem() {
		assertEquals(7, graph.Size_Of_Graph(graph.root));
		assertEquals("20 30 40 50 60 70 80", inOrder());
	}

	@Test
	void rejectsDuplicateKeys() {
		graph.AddConnections(40, "again");

		assertTrue(output().contains("Cannot insert duplicated node"));
		assertEquals(7, graph.Size_Of_Graph(graph.root));
	}

	@Test
	void findsAKeyOnTheLeftAndOnTheRight() {
		graph.Find_And_Search(20);
		graph.Find_And_Search(80);

		assertTrue(output().contains("value20"));
		assertTrue(output().contains("value80"));
	}

	@Test
	void reportsAMissingKeyWithoutCrashing() {
		graph.Find_And_Search(35);

		assertTrue(output().contains("Not found."));
	}

	@Test
	void removesALeafANodeWithOneChildAndANodeWithTwoChildren() {
		assertTrue(graph.Remove_From_Graph(20)); // leaf
		assertEquals("30 40 50 60 70 80", inOrder());

		assertTrue(graph.Remove_From_Graph(30)); // now has one child (40)
		assertEquals("40 50 60 70 80", inOrder());

		assertTrue(graph.Remove_From_Graph(70)); // two children (60 and 80)
		assertEquals("40 50 60 80", inOrder());
	}

	@Test
	void removesTheRootAndReportsMissingKeys() {
		assertTrue(graph.Remove_From_Graph(50));
		assertEquals("20 30 40 60 70 80", inOrder());
		assertFalse(graph.Remove_From_Graph(999));
	}

	@Test
	void anEmptyGraphIsSafe() {
		Graph_Representation empty = new Graph_Representation();

		assertEquals(0, empty.Size_Of_Graph(empty.root));
		assertFalse(empty.Remove_From_Graph(1));
		assertNull(empty.root);
	}
}