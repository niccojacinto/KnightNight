import java.util.ArrayList;

/*A tile-based Map consisting of Rooms and Halls.*/
class Map {
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
  
  //Procedurally generate a fine Map.
  private void generateMap(){
    rooms = new ArrayList<Room>(); 
    halls = new ArrayList<Hall>();
    MapGen.generateMap(rooms, halls, new Room(0, 0, width, height));
  }//public void generateMap(){
  
  //Inserts the room and hall data into the char array.
  private void generateData(){
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
}