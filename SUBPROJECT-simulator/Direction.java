public enum Direction{
    LEFT(0), 
    UP(1),
    RIGHT(2), 
    DOWN(3);
  
    private final int index;
    Direction (int mindex){
      index = mindex;
    }
    
    public int index(){return index;}
    public boolean isHorizontal(){
        return this == LEFT || this == RIGHT;
    }
    
    public boolean isVertical(){
      return this == UP || this == DOWN;
    }
    
    public static Direction getDirection(int index){
      for (Direction dir : Direction.values()) {
        if(dir.index() == index){
          return dir;
        }
      }
      throw new Error("Invalid direction index given: " + index);
    }
}
