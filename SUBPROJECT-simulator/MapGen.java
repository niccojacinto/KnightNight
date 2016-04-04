import java.util.Random;
import java.util.ArrayList;

public class MapGen{
  private static Random rng = new Random();
  
  private static final int ROOM_WIDTH_MIN = 5;
  private static final int ROOM_WIDTH_MAX = 9;
  private static final int ROOM_HEIGHT_MIN = 5;
  private static final int ROOM_HEIGHT_MAX = 9;
  private static final int HALL_SHORT_SIDE_MIN = 3;
  private static final int HALL_SHORT_SIDE_MAX = 6;
  private static final int HALL_LONG_SIDE_MIN = 6;
  private static final int HALL_LONG_SIDE_MAX = 12;
  
  //Procedurally generate a fine Map.
  public static void generateMap(ArrayList<Room> closedRooms, ArrayList<Hall> halls, Room mapBounds){
    //0. Set up variables
    
    ArrayList<Room> openRooms = new ArrayList<Room>();
    
    Room currentRoom;
    int tempWidth;
    int tempHeight;
    //Room tempRoom;
    //Hall tempHall;
    /*
    Direction tempDirection = Direction.UP;
    */
    
    //1. Start with a room in the middle of random size
    tempWidth = getRandomInt(ROOM_WIDTH_MIN, ROOM_WIDTH_MAX);
    tempHeight = getRandomInt(ROOM_WIDTH_MIN, ROOM_WIDTH_MAX);
    currentRoom = new Room(mapBounds.width()/2 - tempWidth/2,  mapBounds.height()/2 - tempHeight/2, tempWidth, tempHeight);
    
    if (currentRoom.isOutside(mapBounds)){
      throw new Error("The initial room is bigger than the map. The map size needs to be increased.");
    } else {
      openRooms.add(currentRoom);
    }
    
    /*TEST CODE*/
    closedRooms.add(currentRoom);
    
    /*
    //When the open room list is empty, the map is complete.
    while(openRooms.size() != 0){
      //Randomly iterate the list of open rooms.
      int index = getRandomInt(0, openRooms.size() - 1);
      currentRoom = openRooms.get(index);
      
      //Randomly pick a free side
      tempDirection = currentRoom.getFreeSide();
      
      //Calculate min and max bounds of the hall
      int hallLongSideMin;
      int hallShortSideMin;
      int hallLongSideMax;
      int hallShortSideMax;
      if (tempDirection.isHorizontal()){
        hallLongSideMin = HALL_LONG_SIDE_MIN;
        hallShortSideMin = HALL_SHORT_SIDE_MIN > currentRoom.height() ? HALL_SHORT_SIDE_MIN : currentRoom.height();
        hallShortSideMax = HALL_SHORT_SIDE_MAX > currentRoom.height() ? currentRoom.width() : HALL_SHORT_SIDE_MAX;
        hallLongSideMax = HALL_LONG_SIDE_MAX;
      } else {
        hallLongSideMin = HALL_LONG_SIDE_MIN;
        hallShortSideMin = HALL_SHORT_SIDE_MIN > currentRoom.width() ? HALL_SHORT_SIDE_MIN : currentRoom.width();
        hallShortSideMax = HALL_SHORT_SIDE_MAX > currentRoom.width() ? currentRoom.width() : HALL_SHORT_SIDE_MAX;
        hallLongSideMax = HALL_LONG_SIDE_MAX;
      }
      
      //do random bounds checking..
      
      int tempHallShortSide = getRandomInt(hallShortSideMin, hallShortSideMax);
      int tempHallLongSide = getRandomInt(hallLongSideMin, hallLongSideMax);
      int tempRoomWidth = getRandomInt(ROOM_WIDTH_MIN, ROOM_WIDTH_MAX);
      int tempRoomHeight = getRandomInt(ROOM_HEIGHT_MIN, ROOM_HEIGHT_MAX);
      
      boolean successful = false;
      boolean cannotCreateNewHallAndRoom = tempHallLongSide < hallLongSideMin
        || tempHallShortSide < hallShortSideMin
        || tempRoomWidth < ROOM_WIDTH_MIN
        || tempRoomHeight < ROOM_HEIGHT_MIN;
      System.out.println("1a open room list size: " + openRooms.size());
      System.out.println("1b current room: " + currentRoom.width() + ", " + currentRoom.height());
      System.out.println("1c current direction: " + tempDirection);
      //Try to create the hall and room iteratively
      while (!successful && !cannotCreateNewHallAndRoom){
        System.out.println("2a trying hall size: " + tempHallShortSide + ", " + tempHallLongSide);
        //Create a random hall attached to the Room.
        tempHall = generateHallAt(currentRoom, tempDirection, tempHallShortSide, tempHallLongSide);
        
        //Create a random room attached to the Hall.
        //tempRoom = new Room(-1,-1,-1,-1);
        
        //Check if they overlap with any openRooms, closedRooms, or Halls.
        //Make the check one size bigger than usual - so that created walls are not Adjacent to any other wall.
        //If they overlap, try again with smaller size constraints for either Hall or Room.
        if (/*checkForOverlaps(tempRoom, openRooms) && *//*checkForOverlaps(tempHall, currentRoom, openRooms)) {
          if (rng.nextInt() < 0.5){
            if (rng.nextInt() < 0.8){
              tempHallLongSide--;
            } else {
              tempHallShortSide--;
            }
          } else {
            if (rng.nextInt() < 0.5){
              tempRoomWidth--;
            } else {
              tempRoomHeight--;
            } 
          } // else
        } else {
          //Add the new room to a list of open rooms, and the hall to the list of halls.
          //openRooms.add(tempRoom);
          System.out.println("3b added hall: ");
          halls.add(tempHall);
          successful = true;
        }
        
        //When the size constraints become too low, set exit condition to true
        cannotCreateNewHallAndRoom = tempHallLongSide < hallLongSideMin
          || tempHallShortSide < hallShortSideMin
          || tempRoomWidth < ROOM_WIDTH_MIN
          || tempRoomHeight < ROOM_HEIGHT_MIN;
      }//while
      //Close this side.
      System.out.println("4a Closed side: " + tempDirection);
      currentRoom.closeSide(tempDirection);
      
      //If all sides are full, then add it to the closed list. 
      if (!currentRoom.areSidesFree()){
        rooms.add(currentRoom);
        openRooms.remove(currentRoom);
      }//if
    }//while(openRooms.size() != 0)
    */
  }//public void generateMap(){
  
  //Returns a pseudo random number between the two ranges inclusive.
  public static int getRandomInt(int min, int max){
    return rng.nextInt(++max - min) + min;
  }
  
  private static Hall generateHallAt(Room r, Direction d, int shortSide, int longSide){
    switch (d){
      case LEFT:
        return new Hall( r.X() - longSide, r.getRandomY(shortSide), shortSide, longSide, d);
      case RIGHT:
        return new Hall( r.X() + r.width(), r.getRandomY(shortSide), shortSide, longSide, d);
      case UP:
        return new Hall( r.getRandomX(shortSide), r.Y() - longSide, shortSide, longSide, d);
      case DOWN:
        return new Hall( r.getRandomX(shortSide), r.Y() + r.height(), shortSide, longSide, d);
      default:
      throw new Error("Invalid direction supplied: " + d);
    }
  }
  
  private static Room generateRoomAt(Hall h, Direction d, int width, int height){
    switch (d){
      case LEFT:
        return null;
      case RIGHT:
        return null;
      case UP:
        return null;
      case DOWN:
        return null;
      default:
        throw new Error("Invalid direction supplied: " + d);
    }
  }
  
  //Checks if a given hall and room overlap with any other room or hall. Ignores current room.
  private static boolean checkForOverlaps(Room r, Room cr, ArrayList<Room> openRooms, ArrayList<Room> closedRooms, ArrayList<Hall> halls){
    //Check open rooms
    for (int i = 0; i < openRooms.size(); i++){
      if (openRooms.get(i) != cr && openRooms.get(i) != r && r.overlaps(openRooms.get(i), 1)){
        return true;
      }
    }
    
    //Check closed rooms
    for (int i = 0; i < closedRooms.size(); i++){
      if (closedRooms.get(i) != cr && r.overlaps(closedRooms.get(i), 1)){
        return true;
      }
    }
    
    //Check halls
    for (int i = 0; i < halls.size(); i++){
      if (halls.get(i) != r && r.overlaps(halls.get(i), 1)){
        return true;
      }
    }
    
    return false;
  }
}