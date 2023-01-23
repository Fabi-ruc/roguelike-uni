package View;

import Help.Tuple;
import processing.core.PApplet;
import processing.core.PGraphics;

public class RenderCircle {
    protected Tuple<Float, Float> pos, HP;
    protected int size;
    protected int facedir;
    protected int[] c;

    public RenderCircle(Tuple<Float, Float> pos, Tuple<Float, Float> HP, int size, int facedir, int[] c) {
        this.pos = pos;
        this.HP = HP;
        this.size = size;
        this.facedir = facedir;
        this.c = c;
    }

    //make sub classes wich override, and make this abstract!
    public void draw(PGraphics g, PApplet p) {
        //System.out.println("player.draw, pos: "+ pos.a + ", "+pos.b);
        g.fill(p.color(c[0] * 1 / 3, c[1] * 1 / 3, c[2] * 1 / 3, c[3] * 1 / 3));
        g.ellipse(pos.a + 50, pos.b + 50, size * 2, size * 2);
        g.fill(p.color(c[0], c[1], c[2], c[3]));
        g.arc(pos.a + 50, pos.b + 50, size * 2, size * 2, 0, PApplet.radians(360 * HPfrac()));
        //g.fill(0);
        //g.ellipse(, size, facedir, facedir);
    }

    protected float HPfrac() {
        return HP.a / HP.b;
    }

}
