package Controller;

import java.util.LinkedList;
import java.util.List;

import Model.DamageSemicircle;
import Model.ScreenElement;
import Model.Player;
import View.IView;
import View.RenderCircle;
import View.RenderSemiCircle;

public class Control extends Thread implements IControl{
    private Player p;
    private IView v;

    public Control(){
        this.start();
    }

    public void setPlayer(Player p){
        this.p = p;
    }

    public void setView(IView v){
        this.v = v;
    }

    /*possibly split into 2 threads to make updates faster */
    public void run(){
        try {
            Long sleept;
            while(true){
                sleept = System.currentTimeMillis();
                if(v != null && p != null){
                    mtov();
                    vtom();
                }
                sleept += 30 - System.currentTimeMillis();
                if(sleept>0){
                    sleep(sleept);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * taking model data to the view
     */
    private void mtov(){
        v.screenImport(p.getScreen().getLocation(), p.getScreen().getlocked());
        List<ScreenElement> l = p.getScreen().export();
        LinkedList<RenderCircle> out = new LinkedList<>();
        for(ScreenElement e: l){
            if(e.getClass() == Player.class){
                out.add(new RenderCircle(e.getPos(), e.getHP(), e.facedirNsize().b, e.facedirNsize().a, new int[]{0,0,255,255}));
            }else if(e.getClass() == DamageSemicircle.class){
                DamageSemicircle f = (DamageSemicircle) e;
                out.add(new RenderSemiCircle(f.getPos(), f.getHP(), f.facedirNsize().b, f.facedirNsize().a, f.getDeg(), new int[]{0,0,0,90}));
            }else if(e.getClass() == ScreenElement.class){
                out.add(new RenderCircle(e.getPos(), e.getHP(), e.facedirNsize().b, e.facedirNsize().a, new int[]{255,0,0,255}));
            }
        }
        v.elemImport(out);
    }

    /**
     * taking key inputs from the view to the model
     */
    private void vtom(){
        p.keyInput(v.getKeys(),v.getMouseX(),v.getMouseY());
    }
}
