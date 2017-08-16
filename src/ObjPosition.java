/**
 * Created by Computer on 8/16/2017.
 */
public class ObjPosition {
    int posX;
    int posY;
    int posZ;

    ObjPosition()
    {
        posX = 0;
        posY = 0;
        posZ = 0;
    }

    ObjPosition(int x, int y, int z)
    {
        posX = x;
        posY = y;
        posZ = z;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public int getZ() {
        return posZ;
    }

    public void setX(int posX) {
        this.posX = posX;
    }

    public void setY(int posY) {
        this.posY = posY;
    }

    public void setZ(int posZ) {
        this.posZ = posZ;
    }
}
