package View;

import Help.Tuple;
import processing.core.PApplet;
import processing.core.PGraphics;

public class RenderCircle {
    protected final Tuple<Float, Float> pos, HP;
    protected final int size, facedir;
    protected final int[] c;

    public RenderCircle(Tuple<Float, Float> pos, Tuple<Float, Float> HP, int size, int facedir, int[] c) {
        this.pos = pos;
        this.HP = HP;
        this.size = size;
        this.facedir = facedir;
        this.c = c;
    }

    public void draw(PGraphics g, PApplet p) {
        //System.out.println("\t\tplayer.draw, pos: "+ pos.a + ", "+pos.b);
        g.fill(p.color(c[0] / 3, c[1] / 3, c[2] / 3, c[3] / 3));
        //g.fill(255);
        g.ellipse(pos.a + 50, pos.b + 50, size * 2, size * 2);
        g.fill(p.color(c[0], c[1], c[2], c[3]));
        //g.fill(0);
        g.arc(pos.a + 50, pos.b + 50, size * 2, size * 2, 0, PApplet.radians(360 * HPfrac()));
        //g.fill(0);
        //g.ellipse(, size, facedir, facedir);
    }

    protected float HPfrac() {
        return HP.a / HP.b;
    }

}
