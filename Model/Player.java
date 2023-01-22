package Model;

import Help.Tuple;

public class Player extends ScreenElement{
    private World world;

    public Player(int xSize, int ySize){
        super(new Screen(new Tuple<Integer,Integer>(0,0), new Tuple<Integer,Integer>(1, 1)), null, false, null,new Tuple<Float,Float>(100.0f,100.0f), 10, 50, 5, 50, 500, 0, 0, 1);
        world = new World(this,new Tuple<Integer,Integer> (xSize,ySize));
        screen = world.getOrigin();
    }

    public Player(Tuple<Float,Float> pos, Tuple<Float,Float> HP, int size, int range, int facedir, int team){
        super(null, pos, false, null,HP, size, range, 0, 0, 0, 0, facedir, team);
    }

    public ScreenElement copy(){
        return new Player(pos,HP,size,atkrange,facedir, team);
    }

    public void setWorld(World w){
        world = w;
        screen = w.getOrigin();
    }

    @Override
    public void run() {
        while(true){
            try {
                sleep(1000);
                if(HP.a <= 0)
                    world.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @param keys boolean representing wether key [w,s,a,d,mouse left] are pressed
     * @param mx mouse X coordinte
     * @param my mouse y position
     */
    public void keyInput(int[] keys, int mx, int my){
        //System.out.println("MouseX: "+ mx + ", MouseY: " + my);
        //System.out.println("keyinputs: w: "+keys[0]+", a: "+keys[2]+", s: "+keys[1]+", d: "+ keys[3]+", mouseL: "+ keys[4]+ ", mouseX: "+ mx+", mouseY: "+my);
        if(keys[0] - keys[1] == 0){
            //System.out.println("neither up nor down");
            facedir = (keys[2] - keys[3] == -1) ? (90) : ((keys[2] - keys[3] == 1) ? (270) : (facedir));
        }else if(keys[0] - keys[1] == 1){
            facedir = (keys[2] - keys[3] == -1) ? (45) : ((keys[2] - keys[3] == 1) ? (315) : (0));
        }else{
            facedir = (keys[2] - keys[3] == -1) ? (135) : ((keys[2] - keys[3] == 1) ? (225) : (180));
        }
        if(keys[0] - keys[1] != 0 || keys[2] - keys[3] != 0){
            pos = moveto();
        }
        if(pos.b-size < 5 && pos.a+size > screen.size.a/2-20 && pos.a-size < screen.size.a/2+20 && !screen.getlocked()){
            moveScreen(new boolean[]{false,false,false,true});
        }else if(pos.b+size > screen.size.b-5 && pos.a+size > screen.size.a/2-20 && pos.a-size < screen.size.a/2+20 && !screen.getlocked()){
            moveScreen(new boolean[]{false,false,true,false});
        }else if(pos.a+size > screen.size.a-5 && pos.b+size > screen.size.b/2-20 && pos.b-size < screen.size.b/2+20 && !screen.getlocked()){
            moveScreen(new boolean[]{false,true,false,false});
        }else if(pos.a-size < 5 && pos.b+size > screen.size.b/2-20 && pos.b-size < screen.size.b/2+20 && !screen.getlocked()){
            //check if left door
            //System.out.println("Door left");
            moveScreen(new boolean[]{true,false,false,false});
        }
        if(keys[4] == 1){
            atk((int)betweenAngle(new Tuple<Float,Float>((float)mx,(float)my)));
        }
    }

    private void moveScreen(boolean[] next){
        if(next[0]||next[1]){
            pos = new Tuple<Float,Float>(screen.size.a-pos.a,pos.b);
        }else{
            pos = new Tuple<Float,Float>(pos.a,screen.size.b - pos.b);
        }
        screen.remove(this);
        screen.setActivity(false);
        screen = world.nextScreen(screen, next);
        //screen.add(this);
        screen.setActivity(true);
        screen.add(this);
        //System.out.println("pos: "+ pos.toString());
    }
    
    public Screen getScreen(){
        return screen;
    }

    public void reset(Screen newScreen){
        screen = newScreen;
        pos = new Tuple<Float,Float>((float)newScreen.size.a/2, (float)newScreen.size.b/2);
        HP = new Tuple<Float,Float>(100.0f,100.0f);
    }
}
