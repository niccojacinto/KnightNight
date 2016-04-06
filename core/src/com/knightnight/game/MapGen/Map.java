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
  
  //Procedurally generate a fine Map.
  private void generateMap(){
    rooms = new ArrayList<Room>(); 
    halls = new ArrayList<Hall>();
    objects = new ArrayList<SpecialObject>();
    MapGen.generateMap(rooms, halls, objects, new Room(0, 0, width - 1, height - 1, -1));
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

  private int getRandomX() {

    return MapGen.getRandomInt(0, width - 1);
  }

  private int getRandomY(){
    return MapGen.getRandomInt(0, height - 1);
  }
}