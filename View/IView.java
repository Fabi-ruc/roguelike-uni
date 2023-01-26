package View;

import java.util.LinkedList;

import Help.Tuple;

public interface IView {

    public void elemImport(LinkedList<RenderCircle> in);

    public void screenImport(Tuple<Integer, Integer> screenID, boolean locked);

    public int getMouseX();

    public int getMouseY();

    public int[] getKeys();

}
