package Model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import Help.Tuple;

/**
 * A Class describing every screen of the map
 */
public class Screen {
    /**
     * List of ScreenElements currently active on Screen
     */
    private final LinkedList<ScreenElement> elems = new LinkedList<ScreenElement>();
    /**
     * Tuples<Integer,Integer>(x,y) describing the screen size/location
     */
    private final Tuple<Integer, Integer> size, location;
    /**
     * A package-private Semaphore, to only allow one Thread access to the elemes list at a time
     */
    Semaphore elemsSemaphore = new Semaphore(1);
    /**
     * Booleans describing whether the screen is currently active/the doors are currently locked
     */
    private boolean activity = true, locked = true;

    /**
     * Public Constructor of a Screen, which generates different amounts of opponents depending on distance and a bit of randomness
     *
     * @param location A Tuple<Integer,Integer>(x location, y location) describing the location of the screen on the overall map in relation to the starting screen
     * @param size     A Tuple<Integer,Integer>(x, y) describing the max width and height of the screen
     */
    Screen(Tuple<Integer, Integer> location, Tuple<Integer, Integer> size) {
        this.size = size;
        this.location = location;
        for (int i = 0; i < (Math.random() + 0.75) * 2 * Math.log(Math.sqrt(location.a * location.a + location.b * location.b) + 1); i++) {
            float rdmHP = (int) (10 + 10 * Math.sqrt(location.a * location.a + location.b * location.b) * Math.random());
            ScreenElement b = new ScreenElement(this, null, true, null, new Tuple<Float, Float>(rdmHP, rdmHP), 8, 20, 6, 35, 1000, 100, 0, -1);
            add(b);
            b.start();
        }
        locked = elems.stream().anyMatch(elem -> !elem.friendly(1));
    }

    /**
     * A method that sets the current Screen's activity.
     * If it was active and there used to be ScreenElements on it, their threads will not continue to run (except for the player)
     *
     * @param activity a boolean deciding what value to set the activity to
     */
    void setActivity(boolean activity) {
        this.activity = activity;
    }

    /**
     * A method to get the current activity of a Screen
     *
     * @return true if active, false if not
     */
    boolean getActive() {
        return activity;
    }

    /**
     * A method to get if the doors of a Screen are currently locked
     *
     * @return true if locked, false if not
     */
    public boolean getlocked() {
        return locked;
    }

    /**
     * A method that returns the size of the screen
     *
     * @return Tuple<Integer, Integer>(x, y) describing the width and height of the screen
     */
    public Tuple<Integer, Integer> getSize() {
        return size;
    }

    /**
     * A method that returns the location of the screen on the map
     *
     * @return Tuple<Integer, Integer>(x, y) describing the offset of the screen from the origin on the x- and y-axis
     */
    public Tuple<Integer, Integer> getLocation() {
        return location;
    }

    /**
     * A method to export all the ScreenElements by creating copies
     *
     * @return A List<ScreenElements> with a copy of every screenElement on the screen
     */
    public List<ScreenElement> export() {
        try {
            elemsSemaphore.acquire();
            List<ScreenElement> out = elems.stream().map(ScreenElement::copy).collect(Collectors.toList());
            elemsSemaphore.release();
            return out;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return export();
        }
    }

    /**
     * A package internal method to get the LinkedList of screenElements
     *
     * @return A LinkedList<ScreenElements> containg all ScreenElements on this Screen
     */
    LinkedList<ScreenElement> getElems() {
        return elems;
    }

    /**
     * A method to remove a ScreenElement from the current screen
     *
     * @param e The ScreenElement to be removed
     */
    void remove(ScreenElement e) {
        try {
            elemsSemaphore.acquire();
            elems.remove(e);
            locked = elems.stream().anyMatch(elem -> !elem.friendly(1));
            elemsSemaphore.release();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * A method to add a ScreenElement from the current screen
     *
     * @param e The ScreenElement to be added
     */
    void add(ScreenElement e) {
        try {
            elemsSemaphore.acquire();
            elems.add(e);
            elemsSemaphore.release();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (e.getClass() == Player.class)
            elems.stream().filter(x -> !x.friendly(1)).forEach(x -> x.SetTarget(e));
    }

    /**
     * A method to Print the current ScreenId in the terminal
     * and call the textout() in every ScreenElement active on Screen to
     * print all relevant information about them in the terminal
     */
    public void textout() {
        System.out.println("\nCurrent Screen: " + location.toString() + " contains:");
        try {
            elemsSemaphore.acquire();
            elems.forEach(ScreenElement::textout);
            elemsSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}