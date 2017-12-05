import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathTest {

	public static final String TEST_EDGES = "testedges.txt";
	public static final String TEST_VERTICES = "testvertices.txt";

	MyGraph g = readGraph(TEST_VERTICES, TEST_EDGES);

	@Test(expected = IllegalArgumentException.class)
	public void A1_testIllegalSource() {
		g.shortestPath(new Vertex("YUP"), new Vertex("SFO"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void A2_testIllegalDestination() {
		g.shortestPath(new Vertex("SEA"), new Vertex("GLO"));
	}

	@Test
	public void B1_testIdenticalSourceAndDestination() {
		Path result = g.shortestPath(new Vertex("SFO"), new Vertex("SFO"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("SFO"));
		if (!result.vertices.equals(path)) {
			fail("Path is incorrect.");
		}
		assertEquals(0, result.cost);
	}

	@Test
	public void B2_testIdenticalSourceAndDestination() {
		Path result = g.shortestPath(new Vertex("X"), new Vertex("X"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("X"));
		if (!result.vertices.equals(path)) {
			fail("Path is incorrect.");
		}
		assertEquals(0, result.cost);
	}

	@Test
	public void C1_testSEAToSFO() {
		Path result = g.shortestPath(new Vertex("SEA"), new Vertex("SFO"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("SEA"));
		path.add(new Vertex("JFK"));
		path.add(new Vertex("SFO"));
		if (!result.vertices.equals(path)) {
			fail("Path is incorrect.");
		}
		assertEquals(79, result.cost);
	}

	@Test
	public void C2_testJFKToSFO() {
		Path result = g.shortestPath(new Vertex("JFK"), new Vertex("SFO"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("JFK"));
		path.add(new Vertex("SFO"));
		if (!result.vertices.equals(path)) {
			fail("Path is incorrect.");
		}
		assertEquals(result.cost, 30);
	}

	@Test
	public void C3_testSFOToSEA() {
		Path result = g.shortestPath(new Vertex("SFO"), new Vertex("SEA"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("SFO"));
		path.add(new Vertex("SEA"));
		if (!result.vertices.equals(path)) {
			fail("Path is incorrect.");
		}
		assertEquals(1, result.cost);
	}

	@Test
	public void C4_estINDtoSFO() {
		Path result = g.shortestPath(new Vertex("IND"), new Vertex("SFO"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("IND"));
		path.add(new Vertex("SEA"));
		path.add(new Vertex("JFK"));
		path.add(new Vertex("SFO"));
		if (!result.vertices.equals(path)) {
			System.out.println("IND to SFO Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(99, result.cost);
	}

	@Test
	public void C5_testB1toB5() {
		Path result = g.shortestPath(new Vertex("B1"), new Vertex("B5"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("B1"));
		path.add(new Vertex("B2"));
		path.add(new Vertex("B3"));
		path.add(new Vertex("B4"));
		path.add(new Vertex("B5"));
		if (!result.vertices.equals(path)) {
			System.out.println("B1 to B5 Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(40, result.cost);
	}

	@Test
	public void C6_testB2toB5() {
		Path result = g.shortestPath(new Vertex("B2"), new Vertex("B5"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("B2"));
		path.add(new Vertex("B3"));
		path.add(new Vertex("B4"));
		path.add(new Vertex("B5"));
		if (!result.vertices.equals(path)) {
			System.out.println("B1 to B5 Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(30, result.cost);
	}

	@Test
	public void C7_testA2toA4() {
		Path result = g.shortestPath(new Vertex("A2"), new Vertex("A4"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("A2"));
		path.add(new Vertex("A1"));
		path.add(new Vertex("A4"));
		if (!result.vertices.equals(path)) {
			System.out.println("A2 to A4 Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(15, result.cost);
	}

	@Test
	public void C8_testXtoZ() {
		Path result = g.shortestPath(new Vertex("X"), new Vertex("Z"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("X"));
		path.add(new Vertex("Z"));
		if (!result.vertices.equals(path)) {
			System.out.println("X to Z Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(15, result.cost);
	}

	/*
	 * Since trying to figure out the connections on number labels
	 * from the file is awful:
	 * ------ UNDIRECTED ------
	 * 
	 * (1) --2-- (2) --2-- (3)
	 *            |         |
	 *            3         6
	 *            |         |
	 *           (4) --4-- (5)
	 */
	
	@Test
	public void C9_test3to1() {
		Path result = g.shortestPath(new Vertex("3"), new Vertex("1"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("3"));
		path.add(new Vertex("2"));
		path.add(new Vertex("1"));
		if (!result.vertices.equals(path)) {
			System.out.println("3 to 1 Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(4, result.cost);
	}

	@Test
	public void C10_test1to5() {
		Path result = g.shortestPath(new Vertex("1"), new Vertex("5"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("1"));
		path.add(new Vertex("2"));
		path.add(new Vertex("4"));
		path.add(new Vertex("5"));
		if (!result.vertices.equals(path)) {
			System.out.println("1 to 5 Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(9, result.cost);
	}

	@Test
	public void C11_test5to3() {
		Path result = g.shortestPath(new Vertex("5"), new Vertex("3"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("5"));
		path.add(new Vertex("3"));
		if (!result.vertices.equals(path)) {
			System.out.println("5 to 3 Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(6, result.cost);
	}

	@Test
	public void C12_test5to2() {
		Path result = g.shortestPath(new Vertex("5"), new Vertex("2"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("5"));
		path.add(new Vertex("4"));
		path.add(new Vertex("2"));
		if (!result.vertices.equals(path)) {
			System.out.println("5 to 2 Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(7, result.cost);
	}
	
	@Test
	public void C13_testINDtoSEA() {
		Path result = g.shortestPath(new Vertex("IND"), new Vertex("SEA"));
		if (result == null) {
			fail("Path returned was null.");
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		path.add(new Vertex("IND"));
		path.add(new Vertex("SEA"));
		if (!result.vertices.equals(path)) {
			System.out.println("IND to SEA Failed!");
			System.out.println("Expected path: " + path);
			System.out.println("Actual path: " + result.vertices);
			fail("Path is incorrect.");
		}
		assertEquals(20, result.cost);
	}

	@Test
	public void D1_testNonNull() {
		Path result = g.shortestPath(new Vertex("A"), new Vertex("H"));
		assertNotNull(result);
	}

	@Test
	public void D2_testUnconnectedSourceDestination() {
		Path result = g.shortestPath(new Vertex("A"), new Vertex("SEA"));
		assertNull(result);
	}

	@Test
	public void D3_testUnconnectedSourceDestination2() {
		Path result = g.shortestPath(new Vertex("X"), new Vertex("H"));
		assertNull(result);
	}

	@Test
	public void D4_testUnconnectedSourceDestination3() {
		Path result = g.shortestPath(new Vertex("SFO"), new Vertex("X"));
		assertNull(result);
	}
	
	@Test
	public void D5_testUnconnectedSourceDestination4() {
		Path result = g.shortestPath(new Vertex("1"), new Vertex("A1"));
		assertNull(result);
	}
	
	@Test
	public void D6_testUnconnectedSourceDestination5() {
		Path result = g.shortestPath(new Vertex("Y"), new Vertex("4"));
		assertNull(result);
	}

	/**
	 * Create and return the graph from given input files
	 * 
	 * @param f1
	 *            vertices
	 * @param f2
	 *            edges
	 * @return MyGraph from the given input
	 */
	public static MyGraph readGraph(String f1, String f2) {
		Scanner s = null;
		try {
			s = new Scanner(new File(f1));
		} catch (FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: " + f1);
			System.exit(2);
		}

		Collection<Vertex> v = new ArrayList<Vertex>();
		while (s.hasNext())
			v.add(new Vertex(s.next()));

		try {
			s = new Scanner(new File(f2));
		} catch (FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: " + f2);
			System.exit(2);
		}

		Collection<Edge> e = new ArrayList<Edge>();
		while (s.hasNext()) {
			try {
				Vertex a = new Vertex(s.next());
				Vertex b = new Vertex(s.next());
				int w = s.nextInt();
				e.add(new Edge(a, b, w));
			} catch (NoSuchElementException e2) {
				System.err.println("EDGE FILE FORMAT INCORRECT");
				System.exit(3);
			}
		}

		return new MyGraph(v, e);
	}
}
