import java.util.Random;

/*A tile-based Map consisting of Rooms and Halls.*/
class Map {
  private int width;
  private int height;
  
  private Room[] rooms;
  private Hall[] halls;
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
    Random rng = new Random();
    
    //Create a room
    rooms = new Room[1];
    rooms[0] = new Room(1,1,6,5);
    halls = new Hall[1];
    halls[0] = new Hall(2, 5, 3, 7, false);
    
    /*Algorithm thoughts*/
    //Attaching a hall to a room..
    //First, the sides of the room depend on the sides of the hall.
    //Then, one side must be picked.
    //Then, the coordinates must match up with the room...
    //Create a hall and room, put together the rooms with a hall. 
    //the location of the hall depends on the first room, the location of the second room depends on the hall..?
    
    //When creating a hall, determine if there is FOUR spaces (a 1 length hall, 3x3 room) on a given side of a room.
    //Also add collision checking for this hypothetical 1 length hall and 3x3 room.
    //Repeat this process of hall and room creating on all the rooms in the array until no rooms allow this creation anymore.
    
    /*Algorithm Pseudocode Draft 1*/
    //Create a room in the middle of random size
    //Add it to a list of open rooms.
    //Randomly iterate the list of open rooms.
    //Check if it's possible to add a hall+room to the list of open rooms. If so, do it.
       //Problem: Finding the limit in how big i can make the hall/room - especially since hall and room is kind of
       //min and max in a way.  I suppose i can either make it a range and just try a random one, then iteratively downsize
       // either hall or room until it's possible? And if it goes below minimum then it's a fail.
    
       //Make the check one size bigger than usual - so that created walls are not Adjacent to any other wall.
    //Add the new room to a list of open rooms, and the hall to the list of halls.
    //If it's not possible to do this operation, move this room to the closed list.
    //When the open room list is empty, the map is complete.
  }
  
  //Inserts the room and hall data into the char array.
  public void generateData(){
    for (int x = 0; x < width; x++){
      for (int y = 0; y < height; y++){
         data[x][y] = 'X';
      }
    }
    
    for (int i = 0; i < rooms.length; i++){
       rooms[i].addToData(data);
    }
    
    for (int i = 0; i < halls.length; i++){
       halls[i].addToData(data);
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
}