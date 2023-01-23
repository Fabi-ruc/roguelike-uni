package Model;

import java.util.HashMap;

import Help.Tuple;

public class Player extends ScreenElement {
    private HashMap<String, Screen> map = new HashMap<>();
    public final Tuple<Integer, Integer> screenSize;

    public Player(int xSize, int ySize) {
        super(new Screen(new Tuple<Integer, Integer>(0, 0), new Tuple<Integer, Integer>(1, 1)), null, false, null, new Tuple<Float, Float>(100.0f, 100.0f), 10, 50, 5, 50, 500, 0, 0, 1);
        this.screenSize = new Tuple<Integer, Integer>(xSize, ySize);
        reset();
    }

    public Player(Tuple<Float, Float> pos, Tuple<Float, Float> HP, int size, int range, int facedir, int team) {
        super(null, pos, false, null, HP, size, range, 0, 0, 0, 0, facedir, team);
        screenSize = new Tuple<Integer, Integer>(1, 1);
    }

    public ScreenElement copy() {
        return new Player(pos, HP, size, atkrange, facedir, team);
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000);
                if (HP.a <= 0)
                    reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param keys boolean representing wether key [w,s,a,d,mouse left] are pressed
     * @param mx   mouse X coordinte
     * @param my   mouse y position
     */
    public void keyInput(int[] keys, int mx, int my) {
        //System.out.println("MouseX: "+ mx + ", MouseY: " + my);
        //System.out.println("keyinputs: w: "+keys[0]+", a: "+keys[2]+", s: "+keys[1]+", d: "+ keys[3]+", mouseL: "+ keys[4]+ ", mouseX: "+ mx+", mouseY: "+my);
        if (keys[0] - keys[1] == 0) {
            //System.out.println("neither up nor down");
            facedir = (keys[2] - keys[3] == -1) ? (90) : ((keys[2] - keys[3] == 1) ? (270) : (facedir));
        } else if (keys[0] - keys[1] == 1) {
            facedir = (keys[2] - keys[3] == -1) ? (45) : ((keys[2] - keys[3] == 1) ? (315) : (0));
        } else {
            facedir = (keys[2] - keys[3] == -1) ? (135) : ((keys[2] - keys[3] == 1) ? (225) : (180));
        }
        if (keys[0] - keys[1] != 0 || keys[2] - keys[3] != 0) {
            pos = moveto();
        }
        if (pos.b - size < 5 && pos.a + size > screen.size.a / 2 - 20 && pos.a - size < screen.size.a / 2 + 20 && !screen.getlocked()) {
            moveScreen(new boolean[]{false, false, false, true});
        } else if (pos.b + size > screen.size.b - 5 && pos.a + size > screen.size.a / 2 - 20 && pos.a - size < screen.size.a / 2 + 20 && !screen.getlocked()) {
            moveScreen(new boolean[]{false, false, true, false});
        } else if (pos.a + size > screen.size.a - 5 && pos.b + size > screen.size.b / 2 - 20 && pos.b - size < screen.size.b / 2 + 20 && !screen.getlocked()) {
            moveScreen(new boolean[]{false, true, false, false});
        } else if (pos.a - size < 5 && pos.b + size > screen.size.b / 2 - 20 && pos.b - size < screen.size.b / 2 + 20 && !screen.getlocked()) {
            //check if left door
            //System.out.println("Door left");
            moveScreen(new boolean[]{true, false, false, false});
        }
        if (keys[4] == 1) {
            atk((int) betweenAngle(new Tuple<Float, Float>((float) mx, (float) my)));
        }
    }

    private void moveScreen(boolean[] dir) {
        if (dir[0] || dir[1]) {
            pos = new Tuple<Float, Float>(screen.size.a - pos.a, pos.b);
        } else {
            pos = new Tuple<Float, Float>(pos.a, screen.size.b - pos.b);
        }
        screen.remove(this);
        screen.setActivity(false);

        Tuple<Integer, Integer> nextKey = new Tuple<Integer, Integer>((!dir[2]) ? (!dir[3] ? (screen.location.a) : (screen.location.a + 1)) : (screen.location.a - 1), (!dir[0]) ? (!dir[1] ? (screen.location.b) : (screen.location.b + 1)) : (screen.location.b - 1));
        if (map.containsKey(nextKey.toString())) {
            screen = map.get(nextKey.toString());
        } else {
            screen = new Screen(nextKey, screenSize);
            map.put(nextKey.toString(), screen);
        }

        screen.setActivity(true);
        screen.add(this);
    }

    public Screen getScreen() {
        return screen;
    }

    public void reset() {
        System.out.println("reset");
        map.forEach((k, screen) -> screen.setActivity(false));
        this.map = new HashMap<>();
        Tuple<Integer, Integer> location = new Tuple<Integer, Integer>(0, 0);
        Screen newScreen = new Screen(location, screenSize);
        newScreen.add(this);
        screen = newScreen;
        pos = new Tuple<Float, Float>((float) newScreen.size.a / 2, (float) newScreen.size.b / 2);
        HP = new Tuple<Float, Float>(100.0f, 100.0f);
        map.put(location.toString(), newScreen);
    }

    /**
     * you dum dum tupel cant be used as a key.... its a refernce, use string and to string instead
     *
     * @param cur the current screen
     * @param dir 4 dim array describing direction. [0] left, [1] right, [2] down, [3] up
     * @return
     */
}
