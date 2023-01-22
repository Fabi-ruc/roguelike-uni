import Controller.*;
import Help.Tuple;
import Model.Player;
import View.View;
import processing.core.PApplet;

public class Main {
    public static void main(String[] args) {
        System.out.println("go");
        Tuple<Integer,Integer> size = new Tuple<Integer,Integer>(1000,600);
        Player p = new Player(size.a, size.b);
        p.start();
        View v = new View(size);
        control c = new control();
        c.setView(v);
        c.setPlayer(p);
        PApplet.runSketch(new String[]{"test"}, v);
    }
    
}