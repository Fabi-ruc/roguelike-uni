package Model;

import Help.Tuple;

public class DamageSemicircle extends ScreenElement {
    /**
     * the degrees of the partial circle
     */
    private final int degrees;

    /**
     * The Constructor of a DamageSemiCircle
     *
     * @param screen   the Screen the DamageSemiCircle is located on
     * @param pos      the center point of the DamageSemiCircle (if it was a full circle)
     * @param HP       the HP of the DamageSemiCircle (Used to describe how long the SemiCircle will exist on screen)
     * @param size     the radius of the DamageSemiCircle
     * @param atkdmg   the Damage the DamageSemiCircle deals
     * @param atkspeed the time between attacks by the DamageSemiCircle
     * @param facedir  the direction the damageSemiCircle is facing (in degrees)
     * @param team     the team that spawned the damageSemiCircle, which will not be attacked
     * @param degrees  the degrees the damageSemiCircle is open
     */
    protected DamageSemicircle(Screen screen, Tuple<Float, Float> pos, Tuple<Float, Float> HP, int size, int atkdmg, int atkspeed, int facedir, int team, int degrees) {
        super(screen, pos, false, null, HP, size, Integer.MAX_VALUE, 0, atkdmg, atkspeed, 0, Math.floorMod(facedir, 360), team);
        this.degrees = degrees;
        this.target = this;
    }

    /**
     * A method that generates a copy of a DamageSemiCircle with all information needed for exporting
     *
     * @return
     */
    @Override
    public ScreenElement copy() {
        return new DamageSemicircle(null, pos, HP, size, 0, 0, facedir, team, degrees);
    }

    /**
     * A method that returns the Degrees the DamageSemiCircle is Open
     *
     * @return the degrees in degrees
     */
    public int getDeg() {
        return degrees;
    }

    /**
     * The method to reduce the HP of all ScreenElements, that are not DamageSemiCircles, located within the DamageSemiCircle
     *
     * @param unused Is not used, but needed because of the method it is overwriting
     */
    protected void atk(int unused) {
        if (System.currentTimeMillis() - lastatk > atkspeed) {
            lastatk = System.currentTimeMillis();
            try {
                screen.elemsSemaphore.acquire();
                for (ScreenElement e : screen.getElems()) {
                    if ((!friendly(e.team)) && (e.getClass() != this.getClass())) {
                        double moveby = Math.toRadians(facedir) * e.size / Math.sin(Math.toRadians(degrees / 2));
                        Tuple<Float, Float> hitboxorigin = new Tuple<Float, Float>(pos.a + (float) Math.cos(moveby), pos.b + (float) Math.sin(moveby));
                        if ((betweenDistance(e.getPos()) - size - e.size <= 0) && (Math.floorMod(180 + (int) e.betweenAngle(hitboxorigin), 360)
                                >= facedir - degrees / 2) && (Math.floorMod(180 + (int) e.betweenAngle(hitboxorigin), 360) <= facedir + degrees / 2))
                            e.HP = new Tuple<Float, Float>(e.HP.a - atkdmg, e.HP.b);
                    }
                }
                screen.elemsSemaphore.release();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * A method used as an HP reducer for DamageSemiCircles.
     * It is supposed to set the direction a ScreenElement is facing in the direction of its target,
     * but since DamageSemiCircles don't move for now and the facedir is instead used to describe the direction the SemiCircle is open in it has to be overwritten.
     */
    protected void pathfind() {
        this.HP = new Tuple<Float, Float>(this.HP.a - 1, this.HP.b);
    }
}
