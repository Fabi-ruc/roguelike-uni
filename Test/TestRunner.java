package Test;

import Help.Tuple;
import Model.DamageSemicircle;
import Model.Player;
import Model.Screen;
import Model.ScreenElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestRunner {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void TupleTest() {
        double x = Math.random() * 100;
        double y = Math.random() * 100;
        Tuple<Double, Double> t = new Tuple<>(x, y);
        assertEquals(x, t.a);
        assertEquals(y, t.b);
        assertEquals(x + ":" + y, t.toString());
        Player p = new Player(100, 100, false);
        Tuple<ScreenElement, Integer> u = new Tuple<>(p, 7);
        assertEquals(p, u.a);
        assertEquals(7, u.b);
        assertEquals(p.toString() + ":" + 7, u.toString());
    }

    @Test
    void degreesTest() {
        Player p = new Player(100, 0, false);
        DamageSemicircle s = null;
        while (s == null) {
            p.keyInput(new int[]{0, 0, 0, 0, 1}, 0, 0);
            for (ScreenElement e : p.getScreen().export()) {
                if (e.getClass() == DamageSemicircle.class) {
                    s = (DamageSemicircle) e;
                }
            }
        }
        assertEquals(s.getDeg(), 50);
    }

    @Test
    void copyTest() {
        Player p = new Player(100, 100, false);
        p.keyInput(new int[]{1, 1, 0, 0}, 1, 1);
        ScreenElement pii = p.copy();
        assertEquals(p.getPos(), pii.getPos());
        assertEquals(p.getHP(), pii.getHP());
        assertEquals(p.facedirNsize().a, pii.facedirNsize().a);
        assertEquals(p.facedirNsize().b, pii.facedirNsize().b);
        assertEquals(p.friendly(1), pii.friendly(1));
        assertEquals(p.friendly(-1), pii.friendly(-1));
    }

    @Test
    void friendlyTest() {
        Player p = new Player(100, 100, false);
        assertTrue(p.friendly(1));
        assertTrue(p.friendly(Integer.MAX_VALUE));
        assertTrue(p.friendly(257));
        assertTrue(p.friendly(0));
        assertFalse(p.friendly(-1));
        assertFalse(p.friendly(-255));
        goUP(p);
        ScreenElement e = p.getScreen().export().get(0);
        assertFalse(e.friendly(1));
        assertFalse(e.friendly(Integer.MAX_VALUE));
        assertFalse(e.friendly(257));
        assertTrue(e.friendly(0));
        assertTrue(e.friendly(-1));
        assertTrue(e.friendly(-255));
    }

    @Test
    void screenSizeTest() {
        Player p = new Player(0, 0, false);
        int x = 0, y = 0;
        for (int i = 0; i < 10; i++) {
            x = (int) (Math.random() * 100);
            y = (int) (Math.random() * 100);
            p = new Player(x, y, false);
            assertEquals(x, p.getScreen().getSize().a);
            assertEquals(y, p.getScreen().getSize().b);
        }
        goUP(p);
        assertEquals(x, p.getScreen().getSize().a);
        assertEquals(y, p.getScreen().getSize().b);
    }

    @Test
    void newPlayerMapOriginTest() {
        Player p = new Player(100, 100, false);
        assertEquals(p.getScreen().getLocation().toString(), (new Tuple<Integer, Integer>(0, 0)).toString());
    }

    @Test
    void BasicNextScreenTest() {
        Player p = new Player(100, 100, false);
        Screen o = p.getScreen();
        goUP(p);
        assertEquals((new Tuple<Integer, Integer>(1, 0)).toString(), p.getScreen().getLocation().toString());
        //p.stop();
        System.out.println("---------------------------------------------------------------");
        p = new Player(100, 100, false);
        while (p.getPos().b > 25) {
            p.keyInput(new int[]{0, 1, 0, 0, 0}, 0, 0);
            System.out.println("Pos: " + p.getPos().toString());
        }
        assertEquals((new Tuple<Integer, Integer>(-1, 0)).toString(), p.getScreen().getLocation().toString());
        System.out.println("---------------------------------------------------------------");
        p = new Player(100, 100, false);
        while (p.getPos().a < 75) {
            p.keyInput(new int[]{0, 0, 1, 0, 0}, 0, 0);
            System.out.println("Pos: " + p.getPos().toString());
        }
        assertEquals((new Tuple<Integer, Integer>(0, -1)).toString(), p.getScreen().getLocation().toString());
        System.out.println("---------------------------------------------------------------");
        p = new Player(100, 100, false);
        while (p.getPos().a > 25) {
            p.keyInput(new int[]{0, 0, 0, 1, 0}, 0, 0);
            System.out.println("Pos: " + p.getPos().toString());
        }
        assertEquals((new Tuple<Integer, Integer>(0, 1)).toString(), p.getScreen().getLocation().toString());
    }

    @Test
    void goingEvenFurtherBeyond() {
        Player p = new Player(100, 100, false);
        Screen o = p.getScreen();
        goUP(p);
        assertEquals((new Tuple<Integer, Integer>(1, 0)).toString(), p.getScreen().getLocation().toString());
        assertTrue(p.getScreen().getlocked());
        fightScreen(p);
        assertFalse(p.getScreen().getlocked());
        goUP(p);
        assertEquals((new Tuple<Integer, Integer>(2, 0)).toString(), p.getScreen().getLocation().toString());
    }

    @Test
    void returnOriginScreenTest() {
        Player p = new Player(100, 100, false);
        Screen o = p.getScreen();
        while (p.getPos().b < 75) {
            p.keyInput(new int[]{1, 0, 0, 0, 0}, 0, 0);
            System.out.println("Pos: " + p.getPos().toString());
        }
        assertEquals((new Tuple<Integer, Integer>(1, 0)).toString(), p.getScreen().getLocation().toString());
        fightScreen(p);
        while (p.getPos().b > 50) {
            p.keyInput(new int[]{0, 1, 0, 0, 0}, 0, 0);
            System.out.println("Pos: " + p.getPos().toString());
        }
        assertEquals((new Tuple<Integer, Integer>(0, 0)).toString(), p.getScreen().getLocation().toString());
        assertEquals(o, p.getScreen());
    }

    @Test
    void resetTest() {
        Player p = new Player(100, 100, false);
        p.start();
        Screen o = p.getScreen();
        while (p.getPos().b < 75) {
            p.keyInput(new int[]{1, 0, 0, 0, 0}, 0, 0);
            System.out.println("Pos: " + p.getPos().toString());
        }
        assertEquals((new Tuple<Integer, Integer>(1, 0)).toString(), p.getScreen().getLocation().toString());
        while (p.getHP().a >= 0) {
            System.out.println("HP: " + p.getHP().a);
        }
        while (p.getHP().a < 0) {
            System.out.println("waiting to respawn");
        }
        assertNotEquals(o, p.getScreen());
        assertEquals((new Tuple<Integer, Integer>(0, 0)).toString(), p.getScreen().getLocation().toString());
        assertEquals(100.0f, p.getHP().a);
        assertEquals(new Tuple<Float, Float>(50.0f, 50.0f).toString(), p.getPos().toString());
    }

    @Test
    void atkDirectionTest() {
        Player p = new Player(100, 100, false);
        for (int i = 0; i < 100; i++) {
            Tuple<Float, Float> mousepos = new Tuple<Float, Float>((float) ((int) Math.random() * 100), (float) ((int) Math.random() * 100));
            p.keyInput(new int[]{0, 0, 0, 0, 1}, (int) ((float) mousepos.a), (int) ((float) mousepos.b));
            List<ScreenElement> elems = p.getScreen().export();
            elems = elems.stream().filter(x -> x.getClass() == DamageSemicircle.class).toList();
            assertTrue(elems.size() >= 1);
            DamageSemicircle d = (DamageSemicircle) elems.get(0);
            assertEquals(p.getPos().toString(), d.getPos().toString());
            assertEquals((int) betweenAngle(p.getPos(), new Tuple<>(mousepos.a, mousepos.b)), d.facedirNsize().a);
        }
    }

    @Test
    void enemyPathfindTest() {
        Player p = new Player(100, 100, false);
        long wait;
        while (betweenDistance(new Tuple<>((float) ((int) p.getScreen().getLocation().a), (float) ((int) p.getScreen().getLocation().b)), new Tuple<Float, Float>(3.0f, 0.0f)) > 0) {
            goUP(p);
            wait = System.currentTimeMillis();
            while (System.currentTimeMillis() - wait < 1000) {
            }
            System.out.println("\nfinished waiting");
            int psize = p.facedirNsize().b;
            int esize;
            Tuple<Float, Float> ppos = p.getPos();
            for (ScreenElement e : p.getScreen().export()) {
                esize = e.facedirNsize().b;
                Tuple<Float, Float> epos = e.getPos();
                assertTrue(20 - psize >= betweenDistance(ppos, epos) - esize - psize);
            }
            fightScreen(p);
        }
    }

    @Test
    void atkDmgTest() {
        Player p = new Player(100, 100, false);
        while (betweenDistance(new Tuple<Float, Float>((float) (int) p.getScreen().getLocation().a, (float) (int) p.getScreen().getLocation().b), new Tuple<>(5.0f, 0.0f)) > 0) {
            if (p.getScreen().getlocked()) {
                List<ScreenElement> elems_before = p.getScreen().export();
                ScreenElement target = elems_before.get(0);
                int size = p.facedirNsize().b;
                int target_size = target.facedirNsize().b;
                while (50 - target.facedirNsize().b < betweenDistance(p.getPos(), target.getPos()) - size - target_size) {
                    //waiting for target to come into range
                    target = p.getScreen().export().get(0);
                }
                System.out.println("test atk!!!");
                p.keyInput(new int[]{0, 0, 0, 0, 1}, (int) ((float) target.getPos().a), (int) ((float) target.getPos().b));
                long wait = System.currentTimeMillis();
                while (System.currentTimeMillis() - wait < 1000) {
                    //waiting to make sure atk connected
                }
                List<ScreenElement> elems_after = p.getScreen().export();
                if (elems_after.stream().filter(x -> x.getClass() != Player.class && x.getClass() != DamageSemicircle.class).count() == elems_before.stream().filter(x -> x.getClass() != Player.class && x.getClass() != DamageSemicircle.class).count()) {
                    assertFalse(elems_after.get(0).getHP().a >= elems_before.get(0).getHP().a);
                    System.out.println("Elem had reduced HP");
                } else {
                    assertTrue(elems_after.stream().filter(x -> x.getClass() != Player.class && x.getClass() != DamageSemicircle.class).count() <= elems_before.stream().filter(x -> x.getClass() != Player.class && x.getClass() != DamageSemicircle.class).count());
                    System.out.println("Elem was deleted");
                }
                fightScreen(p);
                wait = System.currentTimeMillis();
                while (System.currentTimeMillis() - wait < 500) {
                    //waiting to make sure atk relaods
                }
            }
            goUP(p);
            System.out.println("------------------------------------------------------");
        }
        System.out.println("reached end screen");

    }

    @Test
    void outputTest() {
        Player p = new Player(100, 100, false);
        long wait;
        float phealth;
        for (int i = 1; i < 3; i++) {
            wait = System.currentTimeMillis();
            while (System.currentTimeMillis() - wait < 500) {
            }
            String expected = "CurrentScreen: " + p.getScreen().getLocation().toString() + " contains:";
            phealth = p.getHP().a;
            outputStreamCaptor.reset();
            p.getScreen().textout();
            for (ScreenElement e : p.getScreen().export()) {
                if (e.getClass() == Player.class) {
                    expected += e.getClass() + "Pos:" + e.getPos().toString() + "HP:" + phealth + "/" + e.getHP().b;
                } else {
                    expected += e.getClass() + "Pos:" + e.getPos().toString() + "HP:" + e.getHP().a + "/" + e.getHP().b;
                }

            }
            expected = expected.replaceAll("\\s+", "");
            assertEquals(expected, outputStreamCaptor.toString().replaceAll("\\s+", ""));
            fightScreen(p);
            goUP(p);
        }
    }

    @Test
    void wrongInputTest() {
        Player p = new Player(100, 100, false);
        p.keyInput(new int[]{1, 1, 1}, 1, 1);
        String expected = "keyInput had a wrong format\nMake sure that Key Inputs are only called with an int array of length 4 and 2 integers".replaceAll("\\s+", "");
        assertEquals(expected, outputStreamCaptor.toString().replaceAll("\\s+", ""));
        outputStreamCaptor.reset();
        p.keyInput(new int[]{1, 1, 1, -1, 1}, 1, 1);
        assertEquals(expected, outputStreamCaptor.toString().replaceAll("\\s+", ""));
        outputStreamCaptor.reset();
        p.keyInput(new int[]{1, 5, 1, 1}, 1, 1);
        assertEquals(expected, outputStreamCaptor.toString().replaceAll("\\s+", ""));
        outputStreamCaptor.reset();
    }

    @Test
    void textoutflagtestTest() {
        Player p = new Player(100, 100, true);
        p.start();
        String expected = ("CurrentScreen: " + p.getScreen().getLocation().toString() + " contains:" + p.getClass() + "Pos:" + p.getPos().toString() + "HP:" + p.getHP().a + "/" + p.getHP().b).replaceAll("\\s+", "");
        long wait = System.currentTimeMillis();
        while (System.currentTimeMillis() - wait < 500) {
        }
        String actual = outputStreamCaptor.toString().replaceAll("\\s+", "");

        assertEquals(expected, actual);
    }

    private void fightScreen(Player p) {
        System.out.println("fighting started");
        int size = p.facedirNsize().b;
        List<ScreenElement> targets = p.getScreen().export();
        while (p.getScreen().getlocked()) {
            ScreenElement target = targets.get(0);
            int target_size = target.facedirNsize().b;
            if (50 - target.facedirNsize().b >= betweenDistance(p.getPos(), target.getPos()) - size - target_size) {
                p.keyInput(new int[]{0, 0, 0, 0, 1}, (int) ((float) target.getPos().a), (int) ((float) target.getPos().b));
            }
            targets = p.getScreen().export();
        }
        System.out.println("fighting ended");
    }

    private double betweenDistance(Tuple<Float, Float> pos, Tuple<Float, Float> opos) {
        return Math.sqrt((opos.a - pos.a) * (opos.a - pos.a) + (opos.b - pos.b) * (opos.b - pos.b));
    }

    private int betweenAngle(Tuple<Float, Float> pos, Tuple<Float, Float> opos) {
        return Math.floorMod((int) Math.toDegrees(Math.atan2(opos.b - pos.b, opos.a - pos.a)), 360);
    }

    private void goUP(Player p) {
        Tuple<Integer, Integer> curLoc = p.getScreen().getLocation();
        while (p.getScreen().getLocation().a.equals(curLoc.a)) {
            p.keyInput(new int[]{1, 0, 0, 0, 0}, 0, 0);
        }
    }
}
