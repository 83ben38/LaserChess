import acm.graphics.GCompound;
import acm.graphics.GLine;
import acm.graphics.GPolygon;
import acm.program.GraphicsProgram;
import svu.csc213.Dialog;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class LaserChess extends GraphicsProgram {
    static int[] size;
    static boolean teamTurn = false;
    static final int tileSize = 64;
    static boolean moved = false;
    static Map m1 = rotateMap(() -> new String[][]{
            {"00b","00b","00b","00b","02s","00k","02s","02m","00b","00b"},
            {"00b","00b","01m","00b","00b","00re","00b","00b","00b","00b"},
            {"00b","00b","00b","10m","00b","00b","00b","00b","00b","00b"},
            {"03m","00b","12sp","10po","01r","00r","10po","02m","00b","10m"},
            {"02m","00b","10m","00po","10r","11r","00po","00sp","00b","11m"},
            {"00b","00b","00b","00b","00b","00b","02m","00b","00b","00b"},
            {"00b","00b","00b","00b","10re","00b","00b","13m","00b","00b"},
            {"00b","00b","10m","10s","10k","10s","00b","00b","00b","00b"},
    });
    static Map m2 = rotateMap(() -> new String[][]{
            {"10re","00ro","10r","01r","00r","11r","10r","01r","00re","00k"},
            {"00ro","20re","11r","20r","10r","01r","21r","00r","20ro","00re"},
            {"10r","11r","20r","10r","01r","10po","01r","21r","00r","01r"},
            {"01r","00r","10r","01r","20po","20re","00po","01r","11r","10r"},
            {"00r","01r","11r","10po","20re","20po","11r","00r","10r","11r"},
            {"11r","10r","21r","11r","00po","11r","00r","20r","01r","00r"},
            {"10re","20ro","10r","21r","11r","00r","20r","01r","20re","10ro"},
            {"10k","10re","11r","00r","01r","10r","11r","00r","10ro","00re"},
    });
    static Map m4 = rotateMap(() -> new String[][]{
            {"00b","00b","00b","00b","02s","00k","02s","02r","00b","00b"},
            {"00b","00b","00b","00b","00b","20po","00b","00b","00b","00b"},
            {"00b","00b","00b","10m","00b","00b","03m","00b","00b","00b"},
            {"03m","11m","00b","00b","10ro","00r","00b","00b","03sp","10m"},
            {"02m","11sp","00b","00b","10r","00ro","00b","00b","03m","11m"},
            {"00b","00b","00b","11m","00b","00b","02m","00b","00b","00b"},
            {"00b","00b","00b","00b","20po","00b","00b","00b","00b","00b"},
            {"00b","00b","10r","10s","10k","10s","00b","00b","00b","00b"},
    });
    static Map m3 = rotateMap(() -> new String[][]{
            {"00b","00b","00b","00b","01m","02s","02m","00b","00b","00b"},
            {"00b","00b","20re","00b","00b","00k","00b","00b","00b","00b"},
            {"00sp","00b","00b","00b","01m","02s","02r","00b","00b","00b"},
            {"01sp","00b","03m","00b","10po","20po","10po","00b","00b","00b"},
            {"00b","00b","00b","00po","20po","00po","00b","11m","00b","13sp"},
            {"00b","00b","00b","10r","10s","13m","00b","00b","00b","12sp"},
            {"00b","00b","00b","00b","10k","00b","00b","20re","00b","00b"},
            {"00b","00b","00b","10m","10s","13m","00b","00b","00b","00b"},
    });
    static Map m5 = rotateMap(() -> new String[][]{
            {"00b","00b","00b","00b","01m","00k","02m","00b","00b","10r"},
            {"00b","00b","00b","00b","00b","02s","02m","00b","00b","00b"},
            {"00sp","00b","00b","02r","00b","02s","00b","00b","00b","00b"},
            {"03m","00b","00b","00b","10po","00po","00b","00b","11m","00b"},
            {"00b","03m","00b","00b","00po","10po","00b","00b","00b","11m"},
            {"00b","00b","00b","00b","10s","00b","10r","00b","00b","12sp"},
            {"00b","00b","00b","10m","10s","00b","00b","00b","00b","00b"},
            {"00r","00b","00b","10m","10k","13m","00b","00b","00b","00b"},
    });
    static Map sophie = rotateMap(() -> new String[][]{
            {"00b","00b","00b","00b","00k","10m","02m","00b","00b","00b"},
            {"00b","00b","00b","02s","00b","03s","00b","00b","00b","11m"},
            {"03m","00b","00b","00b","01m","02m","00b","10r","00b","10m"},
            {"00b","00b","00b","00b","00b","00b","00b","01r","00b","00b"},
            {"00b","00b","11r","00b","00b","00b","00b","00b","00b","00b"},
            {"02m","00b","00r","00b","10m","13m","00b","00b","00b","11m"},
            {"03m","00b","00b","00b","11s","00b","10s","00b","00b","00b"},
            {"00b","00b","00b","10m","02m","10k","00b","00b","00b","00b"},
    });
    static ArrayList<ArrayList<Piece>> board = new ArrayList<>();
    public static void main(String[] args) {
        new LaserChess().start();
    }

    @Override
    public void run() {
        Map[] maps = new Map[]{m1,m2,m3,m4,m5};
        Map map;
        int answer = Dialog.getInteger("What number map would you like?");
        while (answer-1 >= maps.length){
            answer = Dialog.getInteger("What number map would you like?");
        }
        map = maps[answer-1];
        doMap(map);
        while(true){
            teamTurn = !teamTurn;
            waitForMove();
            shootLaser(teamTurn);
        }
    }
    public void waitForMove(){
        moved = false;
        while(!moved){
            pause(1);
        }
    }
    public void doMap(Map map){
        size = new int[] {map.map().length, map.map()[0].length};
        for (int i = 0; i < size[0]; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < size[1]; j++) {
                Piece piece = new Piece(map.map()[i][j].substring(2),map.map()[i][j].charAt(0) - '0',i,j, Integer.parseInt(map.map()[i][j].charAt(1) + ""));
                add(piece,i*tileSize,j*tileSize);
                board.get(i).add(piece);
            }
        }
    }
    public void shootLaser(boolean team){
        for (ArrayList<Piece> some : board) {
            for (Piece d : some) {
                d.hits = 0;
            }
        }
        ArrayList<GLine> l;
        if (!team){
            l = shootLaser(new int[]{0,-1},new int[]{0,1},Color.red, new ArrayList<>());
        }
        else{
            l = shootLaser(new int[]{size[0]-1,size[1]},new int[]{0,-1},Color.blue, new ArrayList<>());
        }
        pause(1000);
        Dialog.showMessage("It is now " + (team ? "red" : "blue") + "'s turn.");
        while (l.size() > 0){
            remove(l.remove(0));
        }
        for (int i = 0; i < getGCanvas().getElementCount(); i++) {
            if (getGCanvas().getElement(i) instanceof GLine){
                remove(getGCanvas().getElement(i));
            }
        }
    }
    public ArrayList<GLine> shootLaser(int[] current, int[] goingTo, Color color, ArrayList<GLine> p){
        GLine laser = new GLine((current[0] + 0.5)*tileSize,(current[1] + 0.5)*tileSize, (current[0]+ goingTo[0] + 0.5)*tileSize, (current[1] + goingTo[1] + 0.5)*tileSize);
        laser.setColor(color);
        add(laser);
        p.add(laser);
        int[] move;
        try {
            move = board.get(current[0] + goingTo[0]).get(current[1] + goingTo[1]).moveLaser(-goingTo[0], -goingTo[1],this);
        }
        catch(IndexOutOfBoundsException e){
            return p;
        }
        {
            Piece a = board.get(current[0] + goingTo[0]).get(current[1] + goingTo[1]);
            if (a.pieceType.equals("po")) {
                pause(100);
                for (ArrayList<Piece> some : board) {
                    for (Piece d : some) {
                        if (d.pieceType.equals("po") && d != a && d.team == a.team) {
                            current = new int[]{d.x, d.y};
                            return shootLaser(current, goingTo, color, p);
                        }
                    }
                }
                return shootLaser(current, goingTo, color, p);
            }
        }
        board.get(current[0] + goingTo[0]).get(current[1] + goingTo[1]).hits++;
        if (board.get(current[0] + goingTo[0]).get(current[1] + goingTo[1]).hits > 4){
            move = new int[0];
        }
        if (move.length <  1){
            return p;
        }
        current[0] += goingTo[0];
        current[1] += goingTo[1];
        pause(100);
        if (move.length < 3) {
            return shootLaser(current, move, color, p);
        }
        int[] move1 = new int[]{move[0],move[1]};
        int[] move2 = new int[]{move[2],move[3]};
        int[] current2 = new int[]{current[0],current[1]};
        int[] finalCurrent = current;
        new Thread(() -> shootLaser(finalCurrent,move1,color,p)).start();
        new Thread(() -> shootLaser(current2, move2, color, p)).start();
        boolean z = true;
        int prevSize = p.size();
        while (z){
            pause(100);
            if (p.size() == prevSize){
                z = false;
            }
            prevSize = p.size();
        }
        return p;
    }
    public static Map rotateMap(Map map){
        String[][] results = new String[map.map()[0].length][map.map().length];
        for (int i = 0; i < map.map().length; i++) {
            for (int j = 0; j < map.map()[0].length; j++) {
                results[j][i] = map.map()[i][j];
            }
        }
        return () -> results;
    }
}
