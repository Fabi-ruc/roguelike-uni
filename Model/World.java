package Model;

import java.util.HashMap;
import Help.Tuple;

public class World {
    private HashMap<String, Screen> map = new HashMap<>();
    public final Tuple<Integer,Integer> size;
    private Player p; 

    public World(Player p, Tuple<Integer,Integer> size){
        this.p = p;
        this.size = size;
        reset();
    }

    public void reset(){
        System.out.println("reset");
        map.forEach((k,screen) -> screen.setActivity(false));
        this.map = new HashMap<>();
        Tuple<Integer, Integer> location = new Tuple<Integer,Integer>(0, 0);
        Screen newScreen = new Screen(location, size);
        newScreen.add(p);
        p.reset(newScreen);
        map.put(location.toString(), newScreen);
    }

    /**
     * you dum dum tupel cant be used as a key.... its a refernce, use string and to string instead
     * 
     * @param cur the current screen
     * @param dir 4 dim array describing direction. [0] left, [1] right, [2] down, [3] up
     * @return
     */
    public Screen nextScreen(Screen cur, boolean[] dir){
        Tuple<Integer,Integer> next = new Tuple<Integer,Integer>((!dir[2]) ? (!dir[3] ? (cur.location.a) : (cur.location.a+1)) : (cur.location.a-1),(!dir[0]) ? (!dir[1] ? (cur.location.b) : (cur.location.b+1)) : (cur.location.b-1));
        if(map.containsKey(next.toString())){
            return map.get(next.toString());
        }else{
            Screen nextScreen = new Screen(next, size);
            map.put(next.toString(),nextScreen);
            return nextScreen;
        }
    }

    public Screen getOrigin(){
        return map.get(new Tuple<Integer,Integer>(0, 0).toString());
    }
}
