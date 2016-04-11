package com.knightnight.game.MapGen;


/* Represents a hallway between two Rooms.
 * 
 * -These halls are always straight.
 * -The Hall extends the Room, but this doesn't make much semantic sense. Oh well!*/
class Hall extends Room
{
  private static final char WALL = MapConstants.HALL_WALL;
  private Direction direction;
  public Hall(int mx, int my, int mss, int mls, Direction mdirection, int mdepth){
     super(mx,my,
           //These crazy offsets has to do with accounting for the hall overlapping with the room.
           (mdirection.isHorizontal() ? mls + 2 : mss), 
           (mdirection.isVertical() ? mls + 2 : mss),
           mdepth); 
     direction = mdirection;
  }
  
  public Direction direction(){
    return direction;
  }
   
   @Override
   public void addToData(char[][] data){
     //clearHall(data);
     drawFloor(data);
     if (direction.isHorizontal()){
       drawHorizontalWalls(data, WALL);
     } else{
       drawVerticalWalls(data, WALL);
     }
  }
   
  //Clears the tiles that the Hall occupies.
  public void clearHall(char[][] data){
    for (int i = 0; i < width; i++){
      for (int j = 0; j < height; j++){
         data[x + i][y + j] = FLOOR;
      }
    }
  }
}