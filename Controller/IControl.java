package Controller;

import Model.Player;
import View.IView;

public interface IControl {
    public void setView(IView v);
    public void setPlayer(Player p);
}
