package Model;

import java.util.Iterator;

import Help.Tuple;

public class DamageSemicircle extends ScreenElement {
    private int degrees;

    public DamageSemicircle(Screen screen, Tuple<Float, Float> pos, Tuple<Float, Float> HP, int size, int atkdmg, int atkspeed, int facedir, int team, int degrees) {
        //ScreenElement(Screen screen, Tuple<Float,Float> pos, boolean rdmPosFlag, ScreenElement target, int HP, int size, int range, int speed, int atkdmg, int atkspeed, int atkdelay, int facedir, int team)
        super(screen, pos, false, null, HP, size, Integer.MAX_VALUE, 0, atkdmg, atkspeed, 0, Math.floorMod(facedir, 360), team);
        this.degrees = degrees;
        this.target = this;
    }

    @Override
    public ScreenElement copy() {
        return new DamageSemicircle(null, pos, HP, size, 0, 0, facedir, team, degrees);
    }

    public int getDeg() {
        return degrees;
    }

    protected void atk(int unused) {
        if (System.currentTimeMillis() - lastatk > atkspeed) {
            lastatk = System.currentTimeMillis();
            try {
                screen.elemsSemaphore.acquire();
                Iterator<ScreenElement> iter = screen.elemsIter();
                while (iter.hasNext()) {
                    target = iter.next();
                    if (!friendly(target.team)) {
                        double moveby = Math.toRadians(facedir) * target.size / Math.sin(Math.toRadians(degrees / 2));
                        Tuple<Float, Float> hitboxorigin = new Tuple<Float, Float>(pos.a + (float) Math.cos(moveby), pos.b + (float) Math.sin(moveby));
                        if ((betweenDistance(target.getPos()) - size - target.size <= 0) && (Math.floorMod(180 + (int) target.betweenAngle(hitboxorigin), 360) >= facedir - degrees / 2) && (Math.floorMod(180 + (int) target.betweenAngle(hitboxorigin), 360) <= facedir + degrees / 2))
                            target.HP = new Tuple<Float, Float>(target.HP.a - atkdmg, target.HP.b);
                    }
                }
                screen.elemsSemaphore.release();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Coopted to be used as HP reducer, as damge field doesnt move for now
     * if moving damage circle ever implemented (e.g.: aiming magic), use pathfind with super.pathfind() added in :)
     */
    protected void pathfind() {
        target.HP = new Tuple<Float, Float>(target.HP.a - 1, target.HP.b);
    }
}
