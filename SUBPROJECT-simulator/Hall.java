/* Represents a hallway between two Rooms.
 * 
 * -These halls are always straight.
 * -The Hall extends the Room, but this doesn't make much semantic sense. Oh well!*/
class Hall extends Room
{
  /* When facing < 0, the halls are horizontal.
   * When facing >= 0, the halls are vertical.
   * */
  private boolean facingHorizontal;
  public Hall(int mx, int my, int mwidth, int mheight, boolean mfacingHorizontal){
     super(mx,my,
           //These crazy offsets has to do with accounting for the hall overlapping with the room.
           mwidth + (mfacingHorizontal ? 2 : 0), 
           mheight + (mfacingHorizontal ? 0 : 2));
     facingHorizontal = mfacingHorizontal;
  }
   
   @Override
   public void addToData(char[][] data){
     clearHall(data);
     if (facingHorizontal){
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