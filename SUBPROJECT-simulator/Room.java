/*
 * Represents a room in a Map, which consists of four orthogonal walls. *
 * A room is always an axis-aligned rectangle. */
class Room {
  private static final int MIN_WIDTH = 3;
  private static final int MIN_HEIGHT = 3;
  
  //x and y represents top-left corner
  protected int x; 
  protected int y;
  protected int width;
  protected int height;
  public Room(int mx, int my, int mwidth, int mheight){
    x = mx;
    y = my;
    width = mwidth;
    height = mheight;
    if (width < MIN_WIDTH || height < MIN_HEIGHT){
       throw new Error("Room Width/Height size is below minimum: " + width + ", " + height);
    }
  }
  
  //Adds the Room to the Map's data.
  public void addToData(char[][] data){
    drawHorizontalWalls(data);
    drawVerticalWalls(data);
  }
  
  protected void drawHorizontalWalls(char[][] data){
    for (int i=0; i < width;i++){
      data[x + i][y] = 'W';
      data[x + i][y + height - 1] = 'W';
    }
  }
  
  protected void drawVerticalWalls(char[][] data){
    for (int i= 0; i < height;i++){ 
       //This redundantly draws over some Horizontal walls.
       //This is intentional, for scenarios when the Hall subclass calls only this function.
       data[x][y + i] = 'W';
       data[x + width - 1][y + i] = 'W';
    }
  }
  
  /*Accessors*/
  public int X(){
    return x;
  }
  
  public int Y(){
    return y;
  }
  
  public int width(){
    return width;
  }
  
  public int height(){
    return height;
  }
  
  public boolean overlaps(Room other){
    //Half-heartedly tested and confirmed to work.
    return (x + width > other.X()
       && x < other.X() + other.width()
       && y + height > other.Y()
       && y < other.Y() + other.width());
  }
}