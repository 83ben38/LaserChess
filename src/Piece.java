import acm.graphics.*;
import svu.csc213.Dialog;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Piece extends GCompound {
    static ArrayList<String> s = new ArrayList<>();
    static{
        s.add("N");
        s.add("S");
        s.add("E");
        s.add("W");
        s.add("NE");
        s.add("NW");
        s.add("SE");
        s.add("SW");
        s.add("n");
        s.add("s");
        s.add("e");
        s.add("w");
        s.add("ne");
        s.add("nw");
        s.add("se");
        s.add("sw");
    }
    public static HashMap<String,PieceType> pieceTypes = new HashMap<>();
    static{
        pieceTypes.put("b", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                GRect r = new GRect(size,size);
                r.setFillColor(background);
                r.setFilled(true);
                piece.add(r);
            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{false,false,false};
            }

            @Override
            public boolean isPiece() {
                return false;
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                return new int[]{-x,-y};
            }
        });
        pieceTypes.put("m", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                GRect r = new GRect(size,size);
                r.setFillColor(background);
                r.setFilled(true);
                piece.add(r);
                GPolygon p = new GPolygon();
                int[][] vertexes = new int[3][2];
                int vertexesDone = 0;
                if (piece.rotation%4 != 0){
                    vertexes[vertexesDone] = new int[]{0,0};
                    vertexesDone++;
                }
                if (piece.rotation%4 != 3){
                    vertexes[vertexesDone] = new int[]{size,0};
                    vertexesDone++;
                }
                if (piece.rotation%4 != 1){
                    vertexes[vertexesDone] = new int[]{0,size};
                    vertexesDone++;
                }
                if (piece.rotation%4 != 2){
                    vertexes[vertexesDone] = new int[]{size,size};
                    vertexesDone++;
                }
                p.addVertex(vertexes[0][0],vertexes[0][1]);
                p.addVertex(vertexes[1][0],vertexes[1][1]);
                p.addVertex(vertexes[2][0],vertexes[2][1]);
                p.setFillColor(color);
                p.setFilled(true);
                piece.add(p);

            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{true,true,false};
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                boolean destroy = switch (piece.rotation % 4){
                    case 0  -> x == 1 || y == 1;
                    case 1 -> x == 1 || y == -1;
                    case 2 -> x == -1 || y == -1;
                    default -> x == -1 || y == 1;
                };
                if (destroy) {
                    piece.becomeEmpty();
                    return new int[0];
                }
                else{
                    return new int[]{
                            switch (piece.rotation % 4){
                                case 0  -> y == -1 ? -1 : 0;
                                case 1 -> y == 1 ? -1 : 0;
                                case 2 -> y == 1 ? 1 : 0;
                                default -> y == -1 ? 1 : 0;
                            },switch (piece.rotation % 4){
                        case 0  -> x == -1 ? -1 : 0;
                        case 1 -> x == -1 ? 1 : 0;
                        case 2 -> x == 1 ? 1 : 0;
                        default -> x == 1 ? -1 : 0;
                    }
                    };
                }
            }

            @Override
            public boolean canRotate(Piece piece, boolean clockWise) {
                return true;
            }

            @Override
            public boolean canMove(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return !LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }
        });
        pieceTypes.put("s", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                GRect r = new GRect(size,size);
                r.setFillColor(color);
                r.setFilled(true);
                piece.add(r);
                GRect t = new GRect(piece.rotation%2==1 ? size/4 : size,piece.rotation%2==1 ? size : size/4);
                t.setFillColor(background);
                t.setFilled(true);
                piece.add(t, piece.rotation/2==1?size-(piece.rotation%2==1 ? size/4 : size):0,piece.rotation/2==1?size-(piece.rotation%2==1 ? size : size/4):0);
            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{true,true,false};
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                if (!switch (piece.rotation%4){
                    case 0 -> y == -1;
                    case 1 -> x == -1;
                    case 2 -> y == 1;
                    default -> x == 1;
                }) {
                    piece.becomeEmpty();
                }
                return new int[0];
            }
            @Override
            public boolean canRotate(Piece piece, boolean clockWise) {
                return true;
            }

            @Override
            public boolean canMove(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return !LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }
        });
        pieceTypes.put("k", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                GRect r = new GRect(size,size);
                r.setFillColor(color);
                r.setFilled(true);
                piece.add(r);
            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{false,true,false};
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece, LaserChess screen) {
                for (int j = 0; j < 1000; j++) {
                    new Thread(() -> piece.explode(screen)).start();
                    screen.pause(1);
                }
                screen.pause(15*100);
                return moveLaser(x, y, piece);
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                Dialog.showMessage("Game over!");
                System.exit(0);
                return new int[0];
            }
            @Override
            public boolean canMove(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return !LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }
        });
        pieceTypes.put("r", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                GRect r = new GRect(size,size);
                r.setFillColor(background);
                r.setFilled(true);
                piece.add(r);
                GPolygon p = new GPolygon();
                if (piece.rotation%2==1){
                    p.addVertex(0,0);
                    p.addVertex(size/4,0);
                    p.addVertex(size,size*3/4);
                    p.addVertex(size,size);
                    p.addVertex(size*3/4,size);
                    p.addVertex(0,size/4);
                }
                else{
                    p.addVertex(size,0);
                    p.addVertex(size*3/4,0);
                    p.addVertex(0,size*3/4);
                    p.addVertex(0,size);
                    p.addVertex(size/4,size);
                    p.addVertex(size,size/4);
                }
                p.setFilled(true);
                p.setFillColor(color);
                piece.add(p);
            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{true,true,true};
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                if (piece.rotation%2==1) {
                    return new int[]{-y, -x};
                }
                else{
                    return new int[]{y,x};
                }
            }
            @Override
            public boolean canRotate(Piece piece, boolean clockWise) {
                return true;
            }

            @Override
            public boolean canMove(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return !LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }

            @Override
            public boolean canSwitch(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }
        });
        pieceTypes.put("sp", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                piece.rotation %= 4;
                GRect r = new GRect(size,size);
                r.setFillColor(background);
                r.setFilled(true);
                piece.add(r);
                GPolygon t1 = new GPolygon();
                int[][] corners = new int[][]{{0,0},{size,0},{size,size},{0,size},{0,0}};
                t1.addVertex(corners[4-piece.rotation][0],corners[4-piece.rotation][1]);
                t1.addVertex(corners[3-piece.rotation][0],corners[3-piece.rotation][1]);
                int[][] middles = new int[][]{{size/2,size},{0,size/2},{size/2,0},{size,size/2}};
                t1.addVertex(middles[3-piece.rotation][0],middles[3-piece.rotation][1]);
                t1.setFilled(true);
                t1.setFillColor(color);
                piece.add(t1);
            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{true,true,false};
            }
            @Override
            public boolean canRotate(Piece piece, boolean clockWise) {
                return true;
            }

            @Override
            public boolean canMove(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return !LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                int[] pointing = switch (piece.rotation % 4){
                    case 0 -> new int[]{1,0};
                    case 1 -> new int[]{0,-1};
                    case 2 -> new int[]{-1,0};
                    case 3 -> new int[]{0,1};
                    default -> new int[0];
                };
                int[] coming = new int[]{x,y};
                if (pointing[0] == -coming[0] && pointing[1] == -coming[1]) {
                    piece.becomeEmpty();
                    return new int[0];
                }
                if (pointing[0] == coming[0] && pointing[1] == coming[1]) {
                    return piece.rotation % 2 == 0 ? new int[]{0,1,0,-1} : new int[]{1,0,-1,0};
                }
                return pointing;
            }
        });
        pieceTypes.put("po", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                GRect v = new GRect(size,size);
                GOval d = new GOval(size,size);
                v.setFilled(true);
                d.setFilled(true);
                v.setFillColor(background);
                d.setFillColor(color);
                piece.add(v);
                piece.add(d);
            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{false,true,false};
            }
            @Override
            public boolean canMove(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return !LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }
            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                return new int[0];
            }
        });
        pieceTypes.put("re", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                GRect back = new GRect(size,size);
                back.setFilled(true);
                back.setFillColor(background);
                piece.add(back);
                size = size/3;
                for (int i = 0; i < 4; i++) {
                    GPolygon p = new GPolygon();
                    int[][] vertexes = new int[3][2];
                    int vertexesDone = 0;
                    if (i%4 != 0){
                        vertexes[vertexesDone] = new int[]{0,0};
                        vertexesDone++;
                    }
                    if (i%4 != 3){
                        vertexes[vertexesDone] = new int[]{size,0};
                        vertexesDone++;
                    }
                    if (i%4 != 1){
                        vertexes[vertexesDone] = new int[]{0,size};
                        vertexesDone++;
                    }
                    if (i%4 != 2){
                        vertexes[vertexesDone] = new int[]{size,size};
                    }
                    p.addVertex(vertexes[0][0],vertexes[0][1]);
                    p.addVertex(vertexes[1][0],vertexes[1][1]);
                    p.addVertex(vertexes[2][0],vertexes[2][1]);
                    p.setFillColor(color);
                    p.setFilled(true);
                    piece.add(p, i % 2 * (size), (i + 1) % 2 * size);
                    if (i > 0 && i < 3){
                        p.move((i + 1) % 2 * size * 2, i % 2 * size * 2);
                    }
                }
            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{false, true, true};
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                return new int[]{-y,x};
            }
            @Override
            public boolean canMove(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return !LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }

            @Override
            public boolean canSwitch(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }
        });
        pieceTypes.put("ro", new PieceType() {
            @Override
            public void draw(int size, Color color, Color background, Piece piece) {
                GRect back = new GRect(size,size);
                back.setFilled(true);
                back.setFillColor(background);
                piece.add(back);
                size = size/3;
                for (int i = 0; i < 4; i++) {
                    GPolygon p = new GPolygon();
                    int[][] vertexes = new int[3][2];
                    int vertexesDone = 0;
                    if (i%4 != 3){
                        vertexes[vertexesDone] = new int[]{0,0};
                        vertexesDone++;
                    }
                    if (i%4 != 2){
                        vertexes[vertexesDone] = new int[]{size,0};
                        vertexesDone++;
                    }
                    if (i%4 != 0){
                        vertexes[vertexesDone] = new int[]{0,size};
                        vertexesDone++;
                    }
                    if (i%4 != 1){
                        vertexes[vertexesDone] = new int[]{size,size};
                    }
                    p.addVertex(vertexes[0][0],vertexes[0][1]);
                    p.addVertex(vertexes[1][0],vertexes[1][1]);
                    p.addVertex(vertexes[2][0],vertexes[2][1]);
                    p.setFillColor(color);
                    p.setFilled(true);
                    piece.add(p, i % 2 * (size), (i + 1) % 2 * size);
                    if (i > 0 && i < 3){
                        p.move((i + 1) % 2 * size * 2, i % 2 * size * 2);
                    }
                }
            }

            @Override
            public boolean[] canDoStuff() {
                return new boolean[]{false, true, true};
            }

            @Override
            public int[] moveLaser(int x, int y, Piece piece) {
                return new int[]{y,-x};
            }
            @Override
            public boolean canMove(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return !LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }

            @Override
            public boolean canSwitch(Piece piece, int x, int y) {
                if (piece.x + x >=0 && piece.x + x < LaserChess.size[0] && piece.y + y >=0 && piece.y + y < LaserChess.size[1]) {
                    return LaserChess.board.get(piece.x+x).get(piece.y+y).isPiece();
                }
                return false;
            }
        });
    }
    public int x;
    public int y;
    public int rotation;
    public String pieceType;

    int hits = 0;
    int team;
    public Piece(String type, int team, int x, int y, int rotation){
        this.pieceType = type;
        this.rotation = rotation;
        this.team=team;
        draw();
        this.x=x;
        this.y=y;
        addMouseListener(getMouseListener());
    }
    public boolean[] canDoStuff(){
        return pieceTypes.get(pieceType).canDoStuff();
    }
    public void draw(){
        removeAll();
        pieceTypes.get(pieceType).draw(LaserChess.tileSize,team == 1 ? Color.blue : team == 0 ? Color.red : Color.green, Color.gray,this);
    }
    public void setPiece(String type, int team, int rotation){
        this.pieceType = type;
        this.rotation = rotation;
        this.team=team;
        draw();
    }
    public void becomeEmpty(){
        pieceType = "b";
        rotation = 0;
        draw();
    }
    public int[] moveLaser(int x, int y, LaserChess screen) {
        return pieceTypes.get(pieceType).moveLaser(x,y,this,screen);
    }
    public boolean isPiece(){
        return pieceTypes.get(pieceType).isPiece();
    }
    public void rotate(boolean clockwise){
        rotation += clockwise ? -1:1;
        if (rotation < 0){
            rotation += 4;
        }
        draw();
    }
    public void move(int x, int y){
        LaserChess.board.get(this.x+x).get(this.y+y).setPiece(pieceType,team,rotation);
        becomeEmpty();
    }
    public void switchPlaces(int x, int y){
        String type = pieceType;
        int team = this.team;
        int rotation = this.rotation;
        Piece victim = LaserChess.board.get(this.x+x).get(this.y+y);
        setPiece(victim.pieceType,victim.team,victim.rotation);
        victim.setPiece(type,team,rotation);
    }
    public MouseListener getMouseListener(){
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPiece()){
                    if ((LaserChess.teamTurn ? 1 : 0) == team){
                        ArrayList<String> possibilities = new ArrayList<>();
                        if (canDoStuff()[0]){
                            possibilities.add("Rotate");
                        }
                        if (canDoStuff()[1]){
                            possibilities.add("Move");
                        }
                        possibilities.add("Cancel");
                        String question = "";
                        for (String s: possibilities) {
                            question = question + s + ", ";
                        }
                        question = question.substring(0,question.length()-2);
                        question = question + "?";
                        if (canDoStuff()[0]){
                            possibilities.add("rotate");
                            possibilities.add("r");
                            possibilities.add("R");
                        }
                        if (canDoStuff()[1]){
                            possibilities.add("move");
                            possibilities.add("m");
                            possibilities.add("M");
                        }
                        possibilities.add("c");
                        possibilities.add("C");
                        possibilities.add("cancel");
                        String answer = Dialog.getString("Would you like to " + question);
                        if (answer == null){
                            return;
                        }
                        while(!possibilities.contains(answer)){
                            answer = Dialog.getString("Would you like to " + question);
                            if (answer == null){
                                return;
                            }
                        }
                        final String original = answer;
                        if (original.equalsIgnoreCase("cancel") || original.equalsIgnoreCase("c")) {
                            return;
                        }
                        if (answer.equalsIgnoreCase("Rotate") || answer.equalsIgnoreCase("r")){
                            rotate(Dialog.getYesOrNo("Clockwise?"));
                        }
                        else{
                            answer = Dialog.getString("Which direction? (N,E,S,W,NE,SW...)");
                            while(!s.contains(answer)){
                                answer = Dialog.getString("Which direction? (N,E,S,W,NE,SW...)");
                            }
                            int y = 0;
                            int x = 0;
                            if (answer.contains("N") || answer.contains("n")){
                                y = -1;
                            }
                            else if (answer.contains("S")|| answer.contains("s")){
                                y = 1;
                            }
                            if (answer.contains("E")|| answer.contains("e")){
                                x = 1;
                            }
                            else if (answer.contains("W")|| answer.contains("w")){
                                x = -1;
                            }
                            if (!canDoStuff()[2]) {
                                while (canMove(x, y)) {
                                    answer = Dialog.getString("Which direction? (N,E,S,W,NE,SW...)");
                                    while (!s.contains(answer)) {
                                        answer = Dialog.getString("Which direction? (N,E,S,W,NE,SW...)");
                                    }
                                    y = 0;
                                    x = 0;
                                    if (answer.contains("N")) {
                                        y = -1;
                                    } else if (answer.contains("S")) {
                                        y = 1;
                                    }
                                    if (answer.contains("E")) {
                                        x = 1;
                                    } else if (answer.contains("W")) {
                                        x = -1;
                                    }
                                }
                            }
                            switchPlaces(x,y);
                        }
                        LaserChess.moved = true;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
    public boolean canMove(int x, int y){
        return !pieceTypes.get(pieceType).canMove(this,x,y);
    }
    public boolean canSwitch(int x, int y){
        return !pieceTypes.get(pieceType).canSwitch(this,x,y);
    }
    public static int randomInt(int from, int to){
        return (int) (Math.floor(Math.random() * (to-from+1)) + from);
    }
    public void explode(LaserChess screen){
        int x2 = (int) (getX() + getWidth()/2);
        int y2 = (int) (getY() + getHeight()/2);
            GLine l = new GLine(x2, y2, x2 + (Math.random()-0.5), y2 + (Math.random()-0.5));
            l.scale(4);
            l.setColor(team == 1 ? Color.blue : Color.red);
            screen.add(l);
            for (int i = 0; i < 50; i++) {
                l.scale(1.1);
                screen.pause(10);
            }
    }
}
