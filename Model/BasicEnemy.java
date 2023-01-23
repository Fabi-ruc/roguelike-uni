package Model;

import Help.Tuple;

public class BasicEnemy extends ScreenElement {

    public BasicEnemy(Screen screen, Tuple<Float, Float> pos, boolean rdmPosFlag, Tuple<Float, Float> HP, int size, int range, int speed, int atkdmg, int atkspeed, int facedir, int team) {
        super(screen, pos, rdmPosFlag, null, HP, size, range, speed, atkdmg, atkspeed, 100, facedir, team);
    }

    public ScreenElement copy() {
        return new BasicEnemy(null, pos, false, HP, size, 0, 0, 0, 0, facedir, team);
    }

}
