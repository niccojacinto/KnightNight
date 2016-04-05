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
  
  private boolean[] isSideFree;
  public Room(int mx, int my, int mwidth, int mheight){
    x = mx;
    y = my;
    width = mwidth;
    height = mheight;
    if (width < MIN_WIDTH || height < MIN_HEIGHT){
       throw new Error("Room Width/Height size is below minimum: " + width + ", " + height);
    }
    isSideFree = new boolean[4];
    for (int i = 0; i < isSideFree.length; i++){
      isSideFree[i] = true;
    }
  }
  
  //Adds the Room to the Map's data.
  public void addToData(char[][] data){
    drawFloor(data);
    drawHorizontalWalls(data);
    drawVerticalWalls(data);
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
  
  public int getDimensionAlongDirection(Direction dir){
    if (dir == Direction.LEFT || dir == Direction.RIGHT){
      return width;
    } else {
      return height;
    }
  }
  
  public int getDimensionOppositeDirection(Direction dir){
    if (dir == Direction.LEFT || dir == Direction.RIGHT){
      return height;
    } else {
      return width;
    }
  }
  
  //Get a X-coordinate that corresponds to a Wall in this room.
  public int getRandomX(int constraint){
    if (constraint > width) {throw new Error("The constraint cannot be greater than the width.");}
    return MapGen.getRandomInt(x, x + width - constraint);
  }
  
  //Get a Y-coordinate that corresponds to a Wall in this room.
  public int getRandomY(int constraint){
    if (constraint > height) {throw new Error("The constraint cannot be greater than the height.");}
    return MapGen.getRandomInt(y, y + height - constraint);
  }
    
  //Returns a direction of a unused side of this room.
  public Direction getFreeSide(){
    if (areSidesFree()){
      int index;
      do {
        index = MapGen.getRandomInt(0,3);
      } while (!isSideFree[index]);
      return Direction.getDirection(index);
    } else {
      throw new Error("Method called in an illogical state. There are no sides free.");
    }
  }
  //Returns if this room's sides are usable.
  public boolean areSidesFree(){
    return isSideFree[0] || isSideFree[1] || isSideFree[2] || isSideFree[3];
  }
  
  /*Mutator*/
  //Closes a side of this room.
  public void closeSide(Direction direction){
    isSideFree[direction.index()] = false;
  }
  
  /*Room-room collision methods*/
  //Increases the size of a Room by its offset before testing for overlap.
  public boolean overlaps(Room other, int offset){
    return (x + width > other.X() - offset
       && x < other.X() + other.width() + offset
       && y + height > other.Y() - offset
       && y < other.Y() + other.width() + offset);
  }
  
  //Returns if this Room is outside or partially outside the other Room.
  public boolean isOutside(Room other){
    //use || for each side
    return x < other.X()
      || x + width > other.X() + other.width()
      || y < other.Y()
      || y + height > other.Y() + other.height();
  }
  
  /*Draw methods*/
  protected void drawFloor(char[][] data){
    for (int i=0; i < width;i++){
      for (int j=0; j < height;j++){
        data[x + i][y + j] = 'O';
      }
    }
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
}