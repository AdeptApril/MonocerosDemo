/**
 * Created by Computer on 8/16/2017.
 */
public class ObjPosition {
    private int posX;
    private int posY;
    private int posZ;
    private int rotX;
    private int rotY;
    private int rotZ;

    ObjPosition()
    {
        posX = 0;
        posY = 0;
        posZ = 0;
        rotX = 0;
        rotY = 0;
        rotZ = 0;
    }

    ObjPosition(int x, int y, int z)
    {
        posX = x;
        posY = y;
        posZ = z;
        rotX = 0;
        rotY = 0;
        rotZ = 0;
    }

    ObjPosition(int x, int y, int z, int rotateX, int rotateY, int rotateZ)
    {
        posX = x;
        posY = y;
        posZ = z;
        rotX = rotateX;
        rotY = rotateY;
        rotZ = rotateZ;
    }

    public int getX() {
        return posX;
    }

    public int getRotX() {
        return rotX;
    }

    public int getRotY() {
        return rotY;
    }

    public int getRotZ() {
        return rotZ;
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
