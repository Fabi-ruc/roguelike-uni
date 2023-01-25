package View;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import Help.Tuple;
import processing.core.PApplet;
import processing.core.PImage;

public class View extends PApplet implements IView {
    private Tuple<Integer, Integer> size;
    private LinkedList<RenderCircle> elems = new LinkedList<>();
    private float distance = 0.0f;
    public int[] keys = new int[]{0, 0, 0, 0, 0};
    private Tuple<Integer, Integer> screenID;
    private boolean screenLocked = false;
    private Semaphore elemsSemaphore = new Semaphore(1);
    private PImage tutorial;

    public View(Tuple<Integer, Integer> size) {
        this.size = size;
        PApplet.runSketch(new String[]{"test"}, this);
    }

    public void settings() {
        super.size(size.a + 100, size.b + 100);
    }

    public void setup() {
        tutorial = loadImage("Resources/tutorial.png");
        if(tutorial.width/size.a <= tutorial.height/size.b){
            tutorial.resize((int)(0.9f*size.a*tutorial.height/size.b),(int)(0.9f*tutorial.height));
        }else{
            tutorial.resize((int)(0.9f*tutorial.width),(int)(0.9f*size.b*tutorial.width/size.a));
        }
        imageMode(CENTER);
        stroke(0, 0);
        while (screenID == null){}
    }

    public void draw() {
        backdraw();
        doordraw();
        try {
            elemsSemaphore.acquire();
            //System.out.println("drawing all elems");
            for (RenderCircle e : elems) {
                //System.out.println("\tdrawing elem e");
                e.draw(super.g, this);
                //System.out.println(" ");
            }
            elemsSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void backdraw() {
        int r = 5 * distance < 100 ? (int) (5 * distance) : 100;
        int g = 5 * (20 - distance) > 0 ? (int) (5 * (20 - distance)) : 0;
        fill(color(r, g, 20));
        //fill(255);
        rect(50,50,size.a,size.b);
        if(distance == 0){
            tint(255,150);
            image(tutorial,size.a/2+50,size.b/2+50);
        }
        fill(120);
        rect(0, 0, 50, size.b + 100);
        rect(size.a + 50, 0, 50, size.b + 100);
        rect(0, 0, size.a + 100, 50);
        rect(0, size.b + 50, size.a + 100, 50);
        fill(0, 50);
        textSize(100);
        textAlign(CENTER, CENTER);
        text(screenID.a + "," + screenID.b, (size.a + 100) / 2, (size.b + 100) / 2);
    }

    private void doordraw() {
        if (screenLocked) {
            fill(color(139, 69, 19));
        } else {
            fill(0);
        }
        rect(20, size.b / 2 + 30, 30, 40);
        ellipse(20, size.b / 2 + 50, 20, 40);
        rect(size.a + 50, size.b / 2 + 30, 30, 40);
        ellipse(size.a + 80, size.b / 2 + 50, 20, 40);
        rect(size.a / 2 + 30, 20, 40, 30);
        ellipse(size.a / 2 + 50, 20, 40, 20);
        rect(size.a / 2 + 30, size.b + 50, 40, 30);
        ellipse(size.a / 2 + 50, size.b + 80, 40, 20);
    }

    public void keyPressed() {
        if (key == 'w' || key == 'W')
            keys[0] = 1;
        if (key == 'a' || key == 'A')
            keys[2] = 1;
        if (key == 's' || key == 'S')
            keys[1] = 1;
        if (key == 'd' || key == 'D')
            keys[3] = 1;
        //System.out.println("keyPressed: w: "+keys[0]);
    }

    public void keyReleased() {
        if (key == 'w' || key == 'W')
            keys[0] = 0;
        if (key == 'a' || key == 'A')
            keys[2] = 0;
        if (key == 's' || key == 'S')
            keys[1] = 0;
        if (key == 'd' || key == 'D')
            keys[3] = 0;
    }

    public void mousePressed() {
        if (mouseButton == LEFT) {
            keys[4] = 1;
        }
    }

    public void mouseReleased() {
        if (mouseButton == LEFT) {
            keys[4] = 0;
        }
    }

    //needed screen cordinates, all screen elems pos, size, hp, type, friendly
    public void elemImport(LinkedList<RenderCircle> in) {
        try {
            elemsSemaphore.acquire();
            elems = in;
            elemsSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void screenImport(Tuple<Integer, Integer> screenID, boolean locked) {
        /*if(screenID.a == 0 && screenID.b == 0 && (this.screenID.a != screenID.b || this.screenID.b != screenID.b)){
            String user = System.getProperty("user.name");
            //save distance to json?
        }*/
        this.screenID = screenID;
        this.screenLocked = locked;
        distance = sqrt(screenID.a * screenID.a + screenID.b * screenID.b);
    }

    @Override
    public int getMouseX() {
        return mouseX - 50;
    }

    @Override
    public int getMouseY() {
        return mouseY - 50;
    }

    @Override
    public int[] getKeys() {
        return keys;
    }
}
