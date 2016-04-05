import java.util.Random;
import java.util.ArrayList;

public class MapGen{
  private static Random rng = new Random();
  
  private static final int ROOM_WIDTH_MIN = 7;
  private static final int ROOM_WIDTH_MAX = 16;
  private static final int ROOM_HEIGHT_MIN = 7;
  private static final int ROOM_HEIGHT_MAX = 16;
  private static final int HALL_SHORT_SIDE_MIN = 3;
  private static final int HALL_SHORT_SIDE_MAX = 6;
  private static final int HALL_LONG_SIDE_MIN = 6;
  private static final int HALL_LONG_SIDE_MAX = 17;
  
  //Procedurally generate a fine Map.
  public static void generateMap(ArrayList<Room> closedRooms, ArrayList<Hall> halls, ArrayList<SpecialObject> objects,
                                 Room mapBounds){
    //0. Set up variables
    ArrayList<Room> openRooms = new ArrayList<Room>();
    Room currentRoom;
    Room startingRoom;
    Direction currentDirection = Direction.UP;
    
    //1. Start with a room in the middle of random size
    {
      int tempWidth = getRandomInt(ROOM_WIDTH_MIN, ROOM_WIDTH_MAX);
      int tempHeight = getRandomInt(ROOM_WIDTH_MIN, ROOM_WIDTH_MAX);
      startingRoom = new Room(mapBounds.width()/2 - tempWidth/2,  mapBounds.height()/2 - tempHeight/2,
                             tempWidth, tempHeight, 0);
    }
    
    if (startingRoom.isOutside(mapBounds)){
      throw new Error("The initial room is bigger than the map. The map size needs to be increased.");
    } else {
      openRooms.add(startingRoom);
    }
    
    //2. When the open room list is empty, the map is complete.
    while(openRooms.size() != 0){
      //2a. Get a random room from the list of open rooms.
      currentRoom = openRooms.get(getRandomInt(0, openRooms.size() - 1));
      
      //2a. Pick a random, free side.
      currentDirection = currentRoom.getFreeSide();
      System.out.println("2a current direction: " + currentDirection);
      
      //2b. Try to generate hall & room
      tryToGenerateHallAndRoom(currentRoom, currentDirection, openRooms, closedRooms, halls, mapBounds);
      
      //2c. Close this side.
      currentRoom.closeSide(currentDirection);
      
      //2c. If all sides are full, then add it to the closed list. 
      if (!currentRoom.areSidesFree()){
        closedRooms.add(currentRoom);
        openRooms.remove(currentRoom);
      }//if
    }//while(openRooms.size() != 0)
    
    System.out.println("Map generated!");
    
    //5a. Add a start and end point to the Map!
    {
      objects.add(new SpecialObject(startingRoom.getCenterX(), startingRoom.getCenterY(), ObjectType.STARTPOINT));
      Room endRoom = getRoomWithMaxDepth(closedRooms);
      objects.add(new SpecialObject(endRoom.getCenterX(), endRoom.getCenterY(), ObjectType.ENDPOINT));
      Room keyRoom = getRandomRoomIgnoring(startingRoom, endRoom, closedRooms);
      objects.add(new SpecialObject(keyRoom.getCenterX(), keyRoom.getCenterY(), ObjectType.KEY));
    }
    
  }//public void generateMap(){
  
  //Returns a pseudo random number between the two ranges inclusive.
  public static int getRandomInt(int min, int max){
    return rng.nextInt(++max - min) + min;
  }
  
  private static void tryToGenerateHallAndRoom(Room currentRoom, Direction currentDirection, ArrayList<Room> openRooms,
                                               ArrayList<Room> closedRooms, ArrayList<Hall> halls, Room mapBounds){
    //3a. Calculate boundaries of Hall and Room, in accordance to what they're attached to.
    final int currentRoomHallSide = currentRoom.getDimensionOppositeDirection(currentDirection);
    
    //Return if the hall itself must be wider than the room's wall.
    if (HALL_SHORT_SIDE_MIN > currentRoomHallSide) return;
    
    //For hall short side max, it's either the max or the height of the wall (whichever is smaler)
    final int HALL_SHORT_SIDE_MAX_ADJUSTED = 
      HALL_SHORT_SIDE_MAX < currentRoomHallSide ? HALL_SHORT_SIDE_MAX : currentRoomHallSide;
    
    //Break if the new room's short side can never wide enough for the hall.
    if (getRoomMaxAlong(currentDirection.getRotatedClockwise()) < HALL_SHORT_SIDE_MIN) return;
    
    //For room to hall short side, the min is either the hall side or room min (whichever is bigger)
    final int ROOM_SHORT_SIDE_MIN_ADJUSTED =
      getRoomMinAlong(currentDirection.getRotatedClockwise()) > HALL_SHORT_SIDE_MIN 
      ? getRoomMinAlong(currentDirection.getRotatedClockwise()) : HALL_SHORT_SIDE_MIN;
    
    //3b. Return if bounds are invalid.
    if (HALL_SHORT_SIDE_MIN > HALL_SHORT_SIDE_MAX_ADJUSTED
          || HALL_LONG_SIDE_MIN > HALL_LONG_SIDE_MIN
          || ROOM_SHORT_SIDE_MIN_ADJUSTED > getRoomMaxAlong(currentDirection.getRotatedClockwise())
          || getRoomMinAlong(currentDirection) > getRoomMaxAlong(currentDirection)){
      return;
    }
    
    //3c. Calculate Hall and Room dimensions
    int hallShortSide = getRandomInt(HALL_SHORT_SIDE_MIN, HALL_SHORT_SIDE_MAX_ADJUSTED);
    int hallLongSide = getRandomInt(HALL_LONG_SIDE_MIN, HALL_LONG_SIDE_MIN);
    //Short side of Room corresponds to the Hall's short side.
    //These values will be converted into 'width' and 'height' in the generateRoomAt() function.
    int roomShortSide = getRandomInt(ROOM_SHORT_SIDE_MIN_ADJUSTED,
                                     getRoomMaxAlong(currentDirection.getRotatedClockwise()));
    int roomLongSide = getRandomInt(getRoomMinAlong(currentDirection), getRoomMaxAlong(currentDirection));
    
    //3d. Loop until the dimensions are below the minimum bounds, or a Hall&Room has been created.
    do
    {
      //4a. Generate Hall. An extra Hall is created that is one size bigger - this is used to prevent
      //generating halls that are adjacent. The same thing happens for the new Room.
      Hall newHall = generateHallAt(currentRoom, currentDirection, hallShortSide, hallLongSide);
      Hall overlapHall = new Hall(newHall.X()-1, newHall.Y() -1,
                                  newHall.width(), newHall.height(), newHall.direction(), -1);
      
      //4b. Generate Room 
      Room newRoom = generateRoomAt(newHall, currentDirection, roomShortSide, roomLongSide);
      Room overlapRoom = new Room(newRoom.X()-1, newRoom.Y() -1, newRoom.width(), newRoom.height(), -1);
      
      //4c. Check for Hall And Room OutOfBounds & Overlaps.
      if (overlapHall.isOutside(mapBounds) 
            || checkForOverlaps(overlapHall, currentRoom, openRooms, closedRooms, halls)
            || overlapRoom.isOutside(mapBounds) 
            || checkForOverlaps(overlapRoom, overlapHall, openRooms, closedRooms, halls)){
        //Decrement dimensions of either Hall or Room.
        if (rng.nextInt() < 0.5){
          if (rng.nextInt() < 0.8){
            hallLongSide--;
          } else {
            hallShortSide--;
          }
        } else {
          if (rng.nextInt() < 0.5){
            roomLongSide--;
          } else {
            roomShortSide--;
          }
        }
      } else {
        //Add to lists and return.
        System.out.println("4c. Created hall of dimensions: w-" + newHall.width() + ", h-" + newHall.height()); 
        System.out.println("4c. Created room of dimensions: w-" + newRoom.width() + ", h-" + newRoom.height()); 
        halls.add(newHall);
        openRooms.add(newRoom);
        return;
      }
    } while (!areDimensionsBelowMinimum(hallShortSide, hallLongSide, roomShortSide, roomLongSide, currentDirection));
  }
  
  //Returns if dimensions are below the minimum bounds
  private static boolean areDimensionsBelowMinimum(int hallShortSide, int hallLongSide, int roomShortSide,
                                                   int roomLongSide, Direction currentDirection){
    return hallShortSide < HALL_SHORT_SIDE_MIN || hallLongSide < HALL_LONG_SIDE_MIN
      || roomShortSide < getRoomMinAlong(currentDirection.getRotatedClockwise()) 
      || roomLongSide < getRoomMinAlong(currentDirection);
  }
  
  //Returns the Room's side minimum bound depending on the direction
  private static int getRoomMinAlong(Direction dir){
    if (dir == Direction.LEFT || dir == Direction.RIGHT){
      return ROOM_WIDTH_MIN;
    } else {
      return ROOM_HEIGHT_MIN;
    }
  }
  
  //Returns the Room's side maximum bound depending on the direction
  private static int getRoomMaxAlong(Direction dir){
    if (dir == Direction.LEFT || dir == Direction.RIGHT){
      return ROOM_WIDTH_MAX;
    } else {
      return ROOM_HEIGHT_MAX;
    }
  }
  
  //Generate a Hall next to a Room.
  private static Hall generateHallAt(Room r, Direction d, int shortSide, int longSide){
    switch (d){
      case LEFT:
        return new Hall( r.X() - longSide - 1, r.getRandomY(shortSide), shortSide, longSide, d, r.depth() + 1);
      case RIGHT:
        return new Hall( r.X() + r.width() - 1, r.getRandomY(shortSide), shortSide, longSide, d, r.depth() + 1);
      case UP:
        return new Hall( r.getRandomX(shortSide), r.Y() - longSide - 1, shortSide, longSide, d, r.depth() + 1);
      case DOWN:
        return new Hall( r.getRandomX(shortSide), r.Y() + r.height() - 1, shortSide, longSide, d, r.depth() + 1);
      default:
        throw new Error("Invalid direction supplied: " + d);
    }
  }
  
  private static Room generateRoomAt(Hall h, Direction d, int shortSide, int longSide){
    switch (d){
      case LEFT:
        return new Room(h.X() - longSide + 1, h.Y(), longSide, shortSide, h.depth() + 1);
      case RIGHT:
        return new Room(h.X() + h.width() - 1, h.Y(), longSide, shortSide, h.depth() + 1);
      case UP:
        return new Room(h.X(), h.Y() - longSide + 1, shortSide, longSide, h.depth() + 1);
      case DOWN:
        return new Room(h.X(), h.Y() + h.height() - 1, shortSide, longSide, h.depth() + 1);
      default:
        throw new Error("Invalid direction supplied: " + d);
    }
  }
  
  //Checks if a given hall and room overlap with any other room or hall. Ignores the second parameter.
  private static boolean checkForOverlaps(Room r, Room cr, ArrayList<Room> openRooms, ArrayList<Room> closedRooms,
                                          ArrayList<Hall> halls){
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
      if (halls.get(i) != cr && halls.get(i) != r && r.overlaps(halls.get(i), 1)){
        return true;
      }
    }
    
    return false;
  }
  
  //Returns the room with the highest 'depth'.
  private static Room getRoomWithMaxDepth(ArrayList<Room> rooms){
    Room maxRoom = rooms.get(0);
    int maxDepth = maxRoom.depth();
    for (int i = 1; i < rooms.size(); i++){
      if (rooms.get(i).depth() > maxDepth){
        maxRoom = rooms.get(i);
        maxDepth = maxRoom.depth();
      }
    }
    return maxRoom;
  }
  
  //Tries to return a random room while ignoring two specified rooms.
  private static Room getRandomRoomIgnoring(Room room1, Room room2, ArrayList<Room> rooms){
    //Assumptions: 
    //-that the rooms list is all unique. If it's not, then this can loop infinitely.
    //-That room1 and room2 are in rooms.
    
    if (rooms.size() > 2){
      Room randomRoom;
      do {
        randomRoom = rooms.get(getRandomInt(0, rooms.size() - 1));
      } while(randomRoom == room1 || randomRoom == room2);
      return randomRoom;
    } else {
      //If there's only two rooms, just return any of them.
      return rng.nextInt() > 0.5 ? room2 : room1;
    }
  }
}