package Model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import Help.Tuple;

public class Screen {
    /**
     * List of Tupel (Obstaceltype,(topleftx,toplefty),(sizex,sizey)) describing all obstacles on screen
     * possibly replace with record!
     * or with tupel of (ObstacleType,Integer[topleftx,toplefty,sizex,sizey])
     */
    //private LinkedList<Tupel<Obstacle,Tupel<Tupel<Integer, Integer>,Tupel<Integer,Integer>>>> obstacles; Obstacles to be implemented at a later date
    private LinkedList<ScreenElement> elems = new LinkedList<ScreenElement>();
    Semaphore elemsSemaphore = new Semaphore(1);
    public final Tuple<Integer,Integer> size, location;
    private boolean activity = true ,locked = true;

    public Screen(Tuple<Integer,Integer> location, Tuple<Integer, Integer> size){
        this.size = size;
        this.location = location;
        rdmScreen();
    }

    void setActivity(boolean activity){
        //System.out.println("setActivity to: "+activivity);
        this.activity = activity;
    }

    boolean getActive(){
        return activity;
    }

    public boolean getlocked(){
        return locked;
    }

    public List<ScreenElement> export(){
        try {
            elemsSemaphore.acquire();
            List<ScreenElement> out = elems.stream().map(e -> e.copy()).collect(Collectors.toList());
            elemsSemaphore.release();
            return out;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return export();
        }
    }

    Iterator<ScreenElement> elemsIter(){
        return elems.iterator();
    }

    public void remove(ScreenElement e){
        try {
            elemsSemaphore.acquire();
            elems.remove(e);
            locked = (elems.stream().filter(elem -> !elem.friendly(1)).count() <= 0) ? false : true;
            elemsSemaphore.release();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public void add(ScreenElement e){
        try {
            elemsSemaphore.acquire();
            elems.add(e);
            elemsSemaphore.release();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if(e.getClass() == Player.class)
            elems.stream().filter(x -> !x.friendly(1)).forEach(x -> x.SetTarget(e));
    }

    public Tuple<Float, Float> rdmPos(){
        return new Tuple<Float,Float>((float)(Math.random()*size.a), (float)(Math.random()*size.b));
    }

    public void rdmScreen(){
        for(int i = 0; i < (Math.random()+0.75)*2*Math.log(Math.sqrt(location.a*location.a+location.b*location.b)+1); i++){
            float rdmHP = (int)(10+10*Math.sqrt(location.a*location.a+location.b*location.b)*Math.random());
            BasicEnemy b = new BasicEnemy(this,null,  true, new Tuple<Float,Float>(rdmHP,rdmHP), 8, 20, 6, 35, 1000, 0, -1);
            elems.add(b);
            b.start();
        }
        locked = (elems.stream().filter(elem -> !elem.friendly(1)).count() <= 0) ? false : true;
    }
}