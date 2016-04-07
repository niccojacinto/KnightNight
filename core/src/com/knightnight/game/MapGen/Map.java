package com.knightnight.game.MapGen;

import java.util.ArrayList;

/*A tile-based Map consisting of Rooms and Halls.*/
public class Map {
  private static final char VOID = MapConstants.VOID;
  
  //Map size
  private int width;
  private int height;
  
  //Actual map data
  private ArrayList<Room> rooms;
  private ArrayList<Hall> halls;
  private ArrayList<SpecialObject> objects;
  private char[][] data;//[x][y] grid based index
  public Map(int mwidth, int mheight){
    width = mwidth;
    height = mheight;
    
    //init map
    generateMap();
    generateData();
  }

  public char[][] getData() {
    return data;
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

  //Returns an x-coordinate near by a certain x coordinate.
  //There are two versions: v1 returns a coordinate around a given x value.
  //v2 returns a coordinate a lot lower or higher than the given x value.
  public int getXNear(int x, boolean v1) {
    return v1
            ? MapGen.getRandomInt(x - 5, x + 5)
            : MapGen.getRandomInt(0,1) == 0 ? MapGen.getRandomInt(x - 10, x - 4) : MapGen.getRandomInt(x + 4, x + 10);
  }

  //Returns an y-coordinate near by a certain y coordinate.
  //Refer to the getXNear method for how 'v1' works.
  public int getYNear(int y, boolean v1) {
    return v1
            ? MapGen.getRandomInt(y - 5, y + 5)
            : MapGen.getRandomInt(0,1) == 0 ? MapGen.getRandomInt(y - 10, y - 4) : MapGen.getRandomInt(y + 4, y + 10);
  }
  
  //Procedurally generate a fine Map.
  private void generateMap(){
    int failures = 0;
    do
    {
      rooms = new ArrayList<Room>(); 
      halls = new ArrayList<Hall>();
      objects = new ArrayList<SpecialObject>();
      if (failures++ >= 100){
         throw new Error("Failed over 100 times trying to create a playable level. "
                           +"Something must be off with the mapgen constants or map size- "
                           + "width: " + width + ", height: " + height);
      }
    }while (!MapGen.generateMap(rooms, halls, objects, new Room(0, 0, width - 1, height - 1, -1)));
  }//public void generateMap(){
  
  //Inserts the room and hall data into the char array.
  private void generateData(){
    data = new char[width][height];
    
    for (int x = 0; x < width; x++){
      for (int y = 0; y < height; y++){
        data[x][y] = VOID;
      }
    }
    
    for (int i = 0; i < rooms.size(); i++){
      rooms.get(i).addToData(data);
    }
    
    for (int i = 0; i < halls.size(); i++){
      halls.get(i).addToData(data);
    }
    
    for (int i = 0; i < objects.size(); i++){
      objects.get(i).addToData(data);
    }
  }
}