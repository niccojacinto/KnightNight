import java.util.Random;
import java.util.ArrayList;

/*A tile-based Map consisting of Rooms and Halls.*/
class Map {
  private static Random rng = new Random();
  
  private int width;
  private int height;
  
  private ArrayList<Room> rooms;
  private ArrayList<Hall> halls;
  private char[][] data;//[x][y] grid based index
  public Map(int mwidth, int mheight){
    width = mwidth;
    height = mheight;
    data = new char[mwidth][mheight];
    
    //init map
    generateMap();
    generateData();
  }
  
  //Procedurally generate a fine Map.
  public void generateMap(){
    /*Constants for mapgen*/
    final int ROOM_WIDTH_MIN = 5;
    final int ROOM_WIDTH_MAX = 9;
    final int ROOM_HEIGHT_MIN = 5;
    final int ROOM_HEIGHT_MAX = 9;
    final int HALL_SHORT_SIDE_MIN = 3;
    final int HALL_SHORT_SIDE_MAX = 6;
    final int HALL_LONG_SIDE_MIN = 6;
    final int HALL_LONG_SIDE_MAX = 12;
    
    //Set up variables
    Room mapBounds = new Room(0, 0, width, height);
    ArrayList<Room> openRooms = new ArrayList<Room>();
    rooms = new ArrayList<Room>(); //Closed list of rooms.
    halls = new ArrayList<Hall>();
    Room currentRoom;
    Room tempRoom;
    Hall tempHall;
    int tempWidth;
    int tempHeight;
    Direction tempDirection = Direction.UP;
    
    /*Algorithm starts here!!*/
    //Create a room in the middle of random size
    tempWidth = getRandomInt(ROOM_WIDTH_MIN, ROOM_WIDTH_MAX);
    tempHeight = getRandomInt(ROOM_WIDTH_MIN, ROOM_WIDTH_MAX);
    currentRoom = new Room(width/2 - tempWidth/2, 
                           height/2 - tempHeight/2, 
                           tempWidth, 
                           tempHeight);
    
    if (currentRoom.isOutside(mapBounds)){
      throw new Error("The initial room is bigger than the map. The map size needs to be increased.");
    } else {
      openRooms.add(currentRoom);
    }
    
    
    //functions needed:
    //generate a hall at a certain coordinate, and a room at a certain end of a hall
    //Hall size should be constrained by room wall size.
    //the new room size is also constrained by the ha'lls size.
    
    
    //When the open room list is empty, the map is complete.
    while(openRooms.size() != 0){
      //Randomly iterate the list of open rooms.
      int index = getRandomInt(0, openRooms.size() - 1);
      currentRoom = openRooms.get(index);
      
      //Randomly pick a free side
      tempDirection = currentRoom.getFreeSide();
      
      //Try to attach a hall and a room to that one side.
      int tempHallShortSide = getRandomInt(HALL_SHORT_SIDE_MIN, HALL_SHORT_SIDE_MAX);
      int tempHallLongSide = getRandomInt(HALL_LONG_SIDE_MIN, HALL_LONG_SIDE_MAX);
      int tempRoomWidth = getRandomInt(ROOM_WIDTH_MIN, ROOM_WIDTH_MAX);
      int tempRoomHeight = getRandomInt(ROOM_HEIGHT_MIN, ROOM_HEIGHT_MAX);
      
      
       //do some ternary operator, add greater of constant or room dimension
      int hallLongSideMin;
      int hallShortSideMin;
      if (tempDirection.isHorizontal()){
        hallLongSideMin = HALL_LONG_SIDE_MIN;
        hallShortSideMin = HALL_SHORT_SIDE_MIN > currentRoom.height() ? HALL_SHORT_SIDE_MIN : currentRoom.height();
      } else {
        hallLongSideMin = HALL_LONG_SIDE_MIN > currentRoom.width() ? HALL_LONG_SIDE_MIN : currentRoom.width();
        hallShortSideMin = HALL_SHORT_SIDE_MIN;
      }
        
      boolean successful = false;
      boolean cannotCreateNewHallAndRoom = tempHallLongSide < HALL_LONG_SIDE_MIN
          || tempHallShortSide < HALL_SHORT_SIDE_MIN
          || tempRoomWidth < ROOM_WIDTH_MIN
          || tempRoomHeight < ROOM_HEIGHT_MIN;
      System.out.println("1a open room list size: " + openRooms.size());
      System.out.println("1b current room: " + currentRoom.width() + ", " + currentRoom.height());
      while (!successful && !cannotCreateNewHallAndRoom){
        System.out.println("2a trying hall size: " + tempHallShortSide + ", " + tempHallLongSide);
        //Create a random hall attached to the Room.
        tempHall = generateHallAt(currentRoom, tempDirection, tempHallShortSide, tempHallLongSide);
        
        //Create a random room attached to the Hall.
        //tempRoom = new Room(-1,-1,-1,-1);
        
        //Check if they overlap with any openRooms, closedRooms, or Halls.
        //Make the check one size bigger than usual - so that created walls are not Adjacent to any other wall.
        //If they overlap, try again with smaller size constraints for either Hall or Room.
        if (/*checkForOverlaps(tempRoom, openRooms) && */checkForOverlaps(tempHall, openRooms)) {
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
  }//public void generateMap(){
  
  //Inserts the room and hall data into the char array.
  public void generateData(){
    for (int x = 0; x < width; x++){
      for (int y = 0; y < height; y++){
        data[x][y] = 'X';
      }
    }
    
    for (int i = 0; i < rooms.size(); i++){
      rooms.get(i).addToData(data);
    }
    
    for (int i = 0; i < halls.size(); i++){
      halls.get(i).addToData(data);
    }
  }
  
  //Outputs the data as a String and prints it.
  public void out(){
    String str;
    System.out.println("MAP: ");
    for (int y = 0; y < height; y++){
      str = "";
      for (int x = 0; x < width; x++){
        str += data[x][y];
      }
      System.out.println(str);
    }
  }
  
  private Hall generateHallAt(Room r, Direction d, int shortSide, int longSide){
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
  
  private Room generateRoomAt(Hall h, Direction d, int width, int height){
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
  
  //Checks if a given hall and room overlap with any other room or hall.
  private boolean checkForOverlaps(Room r, ArrayList<Room> openRooms){
    //Check open rooms
    for (int i = 0; i < openRooms.size(); i++){
      if (openRooms.get(i) != r && r.overlaps(openRooms.get(i), 1)){
        return true;
      }
    }
    
    //Check closed rooms
    for (int i = 0; i < rooms.size(); i++){
      if (r.overlaps(rooms.get(i), 1)){
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
  
  //Returns a pseudo random number between the two ranges inclusive.
  public static int getRandomInt(int min, int max){
    return rng.nextInt(++max - min) + min;
  }
}