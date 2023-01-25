package Model;

import java.util.HashMap;

import Help.Tuple;

/**
 * A class which Handles the Player Element and the Map the Game is taking place on
 */
public class Player extends ScreenElement {
    private HashMap<String, Screen> map = new HashMap<>();
    private Tuple<Integer, Integer> screenSize;
    private boolean textout = false;

    /**
     * The public constructor of A player.
     * It sets up the player Element and generates the starting Map and starting screen.
     *
     * @param xSize   ScreenWidth
     * @param ySize   ScreenHeight
     * @param textout if the current screenElements should be printed to the terminal
     */
    public Player(int xSize, int ySize, boolean textout) {
        super(new Screen(new Tuple<Integer, Integer>(0, 0), new Tuple<Integer, Integer>(1, 1)), null, false, null, new Tuple<Float, Float>(100.0f, 100.0f), 10, 50, 5, 50, 500, 0, 0, 1);
        this.screenSize = new Tuple<Integer, Integer>(xSize, ySize);
        this.textout = textout;
        reset();
    }

    /**
     * A constructor that generates a player with all relevant information for the view, the rest is set to null or 0.
     *
     * @param pos     the position of the copied Player in the form of a Tuple<Float,Float>(x position, y position)
     * @param HP      the HP of the copied Player in the form of a Tuple<Float,Float>(current HP, max HP)
     * @param size    the radius of the player hitbox
     * @param range   the range an attack by the player has
     * @param facedir the direction the player is currently facing
     * @param team    the team the player is a part of
     */
    private Player(Tuple<Float, Float> pos, Tuple<Float, Float> HP, int size, int range, int facedir, int team) {
        super(null, pos, false, null, HP, size, range, 0, 0, 0, 0, facedir, team);
    }

    /**
     * A method to generate a copy of the player for exporting
     *
     * @return Player with relevant information for the view
     */
    public ScreenElement copy() {
        return new Player(pos, HP, size, atkrange, facedir, team);
    }

    /**
     * The Player Thread runs forever.
     * It resets the Map if the Player HP has fallen below 0 and
     * prints all screenElements to the terminal if the textout flag is set
     */
    @Override
    public void run() {
        while (true) {
            try {
                if (HP.a <= 0)
                    reset();
                if (textout)
                    screen.textout();
                sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A method that handles the actions the player can do.
     * It delegates the movement and attacking function calls.
     * Furthermore, it also tests if the player is standing in front of a door adn then moves the player to the next screen.
     *
     * @param keys boolean representing wether key [w,s,a,d,mouse left] are pressed
     * @param mx   mouse X coordinte
     * @param my   mouse y position
     */
    public void keyInput(int[] keys, int mx, int my) {
        //wrong input handling
        if (keys.length < 5 || ((keys[0] < 0) || (keys[0] > 1)) || ((keys[1] < 0) || (keys[1] > 1)) || ((keys[2] < 0) || (keys[2] > 1)) || ((keys[3] < 0) || (keys[3] > 1))) {
            System.out.println("keyInput had a wrong format\nMake sure that Key Inputs are only called with an int array of length 4 and 2 integers");
            return;
        } else if (keys[0] - keys[1] == 0) {
            facedir = (keys[2] - keys[3] == -1) ? (90) : ((keys[2] - keys[3] == 1) ? (270) : (33));
        } else if (keys[0] - keys[1] == 1) {
            facedir = (keys[2] - keys[3] == -1) ? (45) : ((keys[2] - keys[3] == 1) ? (315) : (0));
        } else {
            facedir = (keys[2] - keys[3] == -1) ? (135) : ((keys[2] - keys[3] == 1) ? (225) : (180));
        }
        if (keys[0] - keys[1] != 0 || keys[2] - keys[3] != 0)
            pos = moveto();
        if (pos.b - size < 5 && pos.a + size > screen.getSize().a / 2 - 20 && pos.a - size < screen.getSize().a / 2 + 20 && !screen.getlocked()) {
            moveScreen(new boolean[]{false, false, false, true});
        } else if (pos.b + size > screen.getSize().b - 5 && pos.a + size > screen.getSize().a / 2 - 20 && pos.a - size < screen.getSize().a / 2 + 20 && !screen.getlocked()) {
            moveScreen(new boolean[]{false, false, true, false});
        } else if (pos.a + size > screen.getSize().a - 5 && pos.b + size > screen.getSize().b / 2 - 20 && pos.b - size < screen.getSize().b / 2 + 20 && !screen.getlocked()) {
            moveScreen(new boolean[]{false, true, false, false});
        } else if (pos.a - size < 5 && pos.b + size > screen.getSize().b / 2 - 20 && pos.b - size < screen.getSize().b / 2 + 20 && !screen.getlocked()) {
            moveScreen(new boolean[]{true, false, false, false});
        }
        if (keys[4] == 1)
            atk((int) betweenAngle(new Tuple<Float, Float>((float) mx, (float) my)));
    }

    /**
     * A method that calculates the next screen the player moves.
     * First it removes the player from the current screen and sets the screen to inactive, resulting in all threads running on it slowly dying.
     * It then either gets the nextScreen from the Hashmap, or generates a new Screen.
     * Finally, it moves the player to the correct position, sets the screen to active and adds the player to the screen
     *
     * @param dir 4 dimensional boolean array with translation for index 0: screen to the left, 1: right, 2: bottom, 3: top
     */
    private void moveScreen(boolean[] dir) {
        if (dir[0] || dir[1]) {
            pos = new Tuple<Float, Float>(screen.getSize().a - pos.a, pos.b);
        } else {
            pos = new Tuple<Float, Float>(pos.a, screen.getSize().b - pos.b);
        }
        screen.remove(this);
        screen.setActivity(false);
        Tuple<Integer, Integer> nextKey = new Tuple<Integer, Integer>((!dir[2]) ? (!dir[3] ? (screen.getLocation().a) : (screen.getLocation().a + 1)) : (screen.getLocation().a - 1), (!dir[0]) ? (!dir[1] ? (screen.getLocation().b) : (screen.getLocation().b + 1)) : (screen.getLocation().b - 1));
        if (map.containsKey(nextKey.toString())) {
            screen = map.get(nextKey.toString());
        } else {
            screen = new Screen(nextKey, screenSize);
            map.put(nextKey.toString(), screen);
        }
        screen.setActivity(true);
        screen.add(this);
    }

    /**
     * Returns the screen the player is currently on.
     *
     * @return curent screen the player is on
     */
    public Screen getScreen() {
        return screen;
    }

    /**
     * Reset is a Method that removes the player from the current screen, sets the activity of all screens on the current map to false.
     * It then generates a new Map and resets the players position and HP to their starting values.
     */
    private void reset() {
        screen.remove(this);
        map.forEach((k, screen) -> screen.setActivity(false));
        this.map = new HashMap<>();
        Tuple<Integer, Integer> location = new Tuple<Integer, Integer>(0, 0);
        screen = new Screen(location, screenSize);
        screen.add(this);
        pos = new Tuple<Float, Float>((float) screen.getSize().a / 2, (float) screen.getSize().b / 2);
        HP = new Tuple<Float, Float>(100.0f, 100.0f);
        map.put(location.toString(), screen);
    }
}
