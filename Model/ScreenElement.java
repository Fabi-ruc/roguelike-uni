package Model;

import Help.Tuple;

public abstract class ScreenElement extends Thread {
    protected Tuple<Float, Float> pos, HP;
    protected int atkrange, atkdmg, atkspeed, atkdelay, facedir, speed, size, team;
    protected long lastatk;
    protected ScreenElement target;
    protected Screen screen;

    public ScreenElement(Screen screen, Tuple<Float, Float> pos, boolean rdmPosFlag, ScreenElement target, Tuple<Float, Float> HP, int size, int range, int speed, int atkdmg, int atkspeed, int atkdelay, int facedir, int team) {
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
        //if screen is null, keep pos (case player & copies), else rndm pos
        this.pos = (!rdmPosFlag) ? (pos) : (screen.rdmPos());
    }

    public abstract ScreenElement copy();

    public Tuple<Float, Float> getPos() {
        return pos;
    }

    public Tuple<Float, Float> getHP() {
        return HP;
    }

    public boolean friendly(int oposTeam) {
        return team * oposTeam >= 0;
    }

    public Tuple<Integer, Integer> facedirNsize() {
        return new Tuple<Integer, Integer>(facedir, size);
    }

    public void SetTarget(ScreenElement e) {
        target = e;
    }

    public void run() {
        try {
            while (HP.a > 0 && screen.getActive()) {
                if (target != null) {
                    //just going in player direction for now, if obstacles are ever added, then readd pathfind()
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

    protected void pathfind() {
        facedir = 90 + (int) betweenAngle(target.pos);
    }

    protected double betweenDistance(Tuple<Float, Float> opos) {
        return Math.sqrt((opos.a - pos.a) * (opos.a - pos.a) + (opos.b - pos.b) * (opos.b - pos.b));
    }

    protected double betweenAngle(Tuple<Float, Float> opos) {
        return (float) Math.toDegrees(Math.atan2(opos.b - pos.b, opos.a - pos.a));
    }

    protected void atk(int dir) {
        if (System.currentTimeMillis() - lastatk > atkspeed) {
            lastatk = System.currentTimeMillis();
            ScreenElement n = new DamageSemicircle(screen, pos, new Tuple<Float, Float>(5.0f, 5.0f), atkrange, atkdmg, 1000, dir, team, 50);
            screen.add(n);
            n.start();
        }
    }

    public Tuple<Float, Float> moveto() {
        float newx = (pos.a + speed * (float) Math.sin(Math.toRadians(facedir)) > size) ? ((pos.a + speed * (float) Math.sin(Math.toRadians(facedir)) < screen.size.a - size) ? (pos.a + speed * (float) Math.sin(Math.toRadians(facedir))) : (screen.size.a - size)) : size;
        float newy = (pos.b - speed * (float) Math.cos(Math.toRadians(facedir)) > size) ? ((pos.b - speed * (float) Math.cos(Math.toRadians(facedir)) < screen.size.b - size) ? (pos.b - speed * (float) Math.cos(Math.toRadians(facedir))) : (screen.size.b - size)) : size;
        return new Tuple<Float, Float>(newx, newy);
    }
}
