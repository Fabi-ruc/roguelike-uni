package View;

import Help.Tuple;
import processing.core.PApplet;
import processing.core.PGraphics;

public class RenderSemiCircle extends RenderCircle {
    private int degrees;

    public RenderSemiCircle(Tuple<Float, Float> pos, Tuple<Float, Float> HP, int size, int facedir, int degrees, int[] c) {
        super(pos, HP, size, facedir, c);
        this.degrees = degrees;
    }

    public void draw(PGraphics g, PApplet p) {
        g.fill(p.color(c[0] * 1 / 3, c[1] * 1 / 3, c[2] * 1 / 3, c[3] * 1 / 3));
        g.arc(pos.a + 50, pos.b + 50, size * 2, size * 2, PApplet.radians(facedir - degrees / 2), PApplet.radians(facedir + degrees / 2));
        g.fill(p.color(c[0], c[1], c[2], c[3]));
        g.arc(pos.a + 50, pos.b + 50, size * 2 * HPfrac(), size * 2 * HPfrac(), PApplet.radians(facedir - degrees / 2), PApplet.radians(facedir + degrees / 2));
    }

}