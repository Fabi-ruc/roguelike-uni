import Controller.*;
import Help.Tuple;
import Model.Player;
import View.IView;
import View.View;

public class Main {
    public static void main(String[] args) {
        System.out.println("go");
        Tuple<Integer, Integer> size = new Tuple<Integer, Integer>(1000, 600);
        Player p = new Player(size.a, size.b, true, false);
        IView v = new View(size);
        IControl c = new Control();
        c.setView(v);
        c.setPlayer(p);
    }

}