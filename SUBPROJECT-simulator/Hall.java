/* Represents a hallway between two Rooms.
 * 
 * -These halls are always straight.
 * -The Hall extends the Room, but this doesn't make much semantic sense. Oh well!*/
class Hall extends Room
{
  private Direction direction;
  public Hall(int mx, int my, int mss, int mls, Direction mdirection){
     super(mx,my,
           //These crazy offsets has to do with accounting for the hall overlapping with the room.
           (mdirection.isHorizontal() ? mls + 2 : mss), 
           (mdirection.isVertical() ? mls + 2 : mss));
     direction = mdirection;
  }
   
   @Override
   public void addToData(char[][] data){
     clearHall(data);
     if (direction.isHorizontal()){
       drawHorizontalWalls(data);
     } else{
       drawVerticalWalls(data);
     }
  }
   
  //Clears the tiles that the Hall occupies.
  public void clearHall(char[][] data){
    for (int i = 0; i < width; i++){
      for (int j = 0; j < height; j++){
         data[x + i][y + j] = 'X';
      }
    }
  }
}