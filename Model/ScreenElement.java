package Model;

import Help.Tuple;

/**
 * An abstract class which describes Elements and functions that most screenElements share
 */
public class ScreenElement extends Thread {
    protected Tuple<Float, Float> pos, HP;
    protected int atkrange, atkdmg, atkspeed, atkdelay, facedir, speed, size, team;
    protected long lastatk;
    protected ScreenElement target;
    protected Screen screen;

    /**
     * The generic Constructor of screenElements which sets all Information a screenElement needs.
     *
     * @param screen     the screen the element is on
     * @param pos        the current position of the element as a Tuple<Float,Float>(x,y)
     * @param rdmPosFlag a flag which decides if a screen element generates a random position on its screen
     * @param target     the current target the screenElement is trying to attack
     * @param HP         the HP of the screenElement in the form of a Tuple<Float,Float>(current HP, max HP)
     * @param size       the radius of the screenElement
     * @param range      the AtkRange of the screenElement
     * @param speed      the distance the screenElement is moved every cycle
     * @param atkdmg     the damage an attack by the screenElement is dealt
     * @param atkspeed   the time between attacks
     * @param atkdelay   the time an attack is delayed
     * @param facedir    the direction a screenElement is facing
     * @param team       the team the screenElement is part of
     */
    ScreenElement(Screen screen, Tuple<Float, Float> pos, boolean rdmPosFlag, ScreenElement target, Tuple<Float, Float> HP, int size, int range, int speed, int atkdmg, int atkspeed, int atkdelay, int facedir, int team) {
        this.target = target;
        this.screen = screen;
        this.HP = HP;
        this.size = size;
        this.atkrange = range;
        this.speed = speed;
        this.atkdmg = atkdmg;
        this.atkspeed = atkspeed;
        this.atkdelay = atkdelay;
        this.facedir = facedir;
        this.team = team;
        //if rdmPosFlag and screen is not null, generate rdmPos, else keep pos (case player & copies)
        this.pos = (rdmPosFlag && screen != null) ? (new Tuple<Float, Float>((float) (Math.random() * screen.getSize().a), (float) (Math.random() * screen.getSize().b))) : (pos);
    }

    /**
     * An abstract Method that generates a copy of a subclass instance with all relevant information for Exporting
     *
     * @return A screenElement with all relevant Information for the view
     */
    ScreenElement copy() {
        return new ScreenElement(null, pos, false, null, HP, size, atkrange, speed, atkdmg, atkspeed, atkdelay, facedir, team);
    }

    /**
     * A method that returns the current position of a screenElement
     *
     * @return A Tuple<Float,Float>(xPosition, yPosition)
     */
    public Tuple<Float, Float> getPos() {
        return pos;
    }

    /**
     * A method that returns the current HP of a screenElement
     *
     * @return A Tuple<Float,Float>(current HP, max HP)
     */
    public Tuple<Float, Float> getHP() {
        return HP;
    }

    /**
     * A method to calculate whether another team is friendly or not
     *
     * @param oposTeam the opposing team as an int
     * @return true if friendly, else false
     */
    public boolean friendly(int oposTeam) {
        return team * oposTeam >= 0;
    }

    /**
     * A method that returns the direction a screenElement is facing and its Radius as a Tuple
     *
     * @return A Tuple<Integer,Integer>(direction facing, radius)
     */
    public Tuple<Integer, Integer> facedirNsize() {
        return new Tuple<Integer, Integer>(facedir, size);
    }

    /**
     * A method that sets the target of a ScreenElement
     *
     * @param e the new target of the ScreenElement
     */
    void SetTarget(ScreenElement e) {
        target = e;
    }

    /**
     * The screenElement Thread runs as long as its HP > 0 and its screen is active
     * If it has a target, it calculates the direction to face the target,
     * and then, if the target is in range attacks, else it moves in the direction of the target.
     * Once the HP is equal or lower than 0, the screenElement removes itself from the screen
     */
    public void run() {
        //screen.add(this);
        try {
            while (HP.a > 0 && screen.getActive()) {
                if (target != null) {
                    pathfind();
                    if (atkrange - target.size >= betweenDistance(target.pos) - size - target.size) {
                        sleep(atkdelay);
                        atk(facedir - 90);
                    } else {
                        pos = moveto();
                    }
                    sleep(50);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        screen.remove(this);
    }

    /**
     * A method that calculates the direction the screenElement should face to find its target
     * In the current implementation it just goes straight to the target, as there are no obstacles nor is there collision between screenElements
     */
    protected void pathfind() {
        facedir = 90 + (int) betweenAngle(target.pos);
    }

    /**
     * A method to calculate the distance between the center point of the screenElement and the passed coordinate
     *
     * @param opos A Tuple<Float,Float>(x,y) describing the point to measure the distance to
     * @return the distance between in the form of a double
     */
    protected double betweenDistance(Tuple<Float, Float> opos) {
        return Math.sqrt((opos.a - pos.a) * (opos.a - pos.a) + (opos.b - pos.b) * (opos.b - pos.b));
    }

    /**
     * A method to calculate the angle between the center point of the screenElement and the passed coordinate
     *
     * @param opos A Tuple<Float,Float>(x,y) describing the point to measure the angle between
     * @return the angle between in the form of a double and degrees
     */
    protected double betweenAngle(Tuple<Float, Float> opos) {
        return (float) Math.toDegrees(Math.atan2(opos.b - pos.b, opos.a - pos.a));
    }

    /**
     * A generic ATK method that spawns a DamageSemiCircle with the specifications of the spawne
     *
     * @param dir An int describing the direction the damageSemiCircle is facing in degrees
     */
    protected void atk(int dir) {
        if (System.currentTimeMillis() - lastatk > atkspeed) {
            lastatk = System.currentTimeMillis();
            ScreenElement n = new DamageSemicircle(screen, pos, new Tuple<Float, Float>(5.0f, 5.0f), atkrange, atkdmg, 1000, dir, team, 50);
            screen.add(n);
            n.start();
        }
    }

    /**
     * A method that calculates the new coordinates a screenElement would be at, after moving in the direction it is facing with its speed
     *
     * @return A Tuple<Float,Float>(new x position, new y position) describing the new Coordinates the screenElement would be at
     */
    protected Tuple<Float, Float> moveto() {
        float newx = (pos.a + speed * (float) Math.sin(Math.toRadians(facedir)) > size) ? ((pos.a + speed * (float) Math.sin(Math.toRadians(facedir)) < screen.getSize().a - size) ? (pos.a + speed * (float) Math.sin(Math.toRadians(facedir))) : (screen.getSize().a - size)) : size;
        float newy = (pos.b - speed * (float) Math.cos(Math.toRadians(facedir)) > size) ? ((pos.b - speed * (float) Math.cos(Math.toRadians(facedir)) < screen.getSize().b - size) ? (pos.b - speed * (float) Math.cos(Math.toRadians(facedir))) : (screen.getSize().b - size)) : size;
        return new Tuple<Float, Float>(newx, newy);
    }

    /**
     * A method that gives out the relevant screenElement information on the terminal
     */
    public void textout() {
        System.out.printf("\t%-30s %-30s %-30s\n", getClass(), "Pos: " + pos.toString(), "HP: " + HP.a + "/" + HP.b);
    }
}
