import java.awt.*;

public interface PieceType {
    void draw(int size, Color color, Color background, Piece piece);
    boolean[] canDoStuff();

    default int[] moveLaser(int x, int y, Piece piece, LaserChess screen){
        return moveLaser(x,y,piece);
    }
    default boolean canRotate(Piece piece, boolean clockWise){
        return false;
    }
    default boolean canMove(Piece piece, int x, int y){
        return false;
    }
    default boolean canSwitch(Piece piece, int x, int y){
        return false;
    }
    default boolean isPiece(){
        return true;
    }
    int[] moveLaser(int x, int y, Piece piece);
}
