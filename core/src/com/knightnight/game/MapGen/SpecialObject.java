package com.knightnight.game.MapGen;

//Special objects used in the Map class to represent things such as
//start points, end points, or keys.
public class SpecialObject{
  private int x;
  private int y;
  private ObjectType type;
  
  public SpecialObject(int mx, int my, ObjectType mtype){
    x = mx;
    y = my;
    type = mtype;
  }
  
  //Adds the Room to the Map's data.
  public void addToData(char[][] data){
    data[x][y] = type.value();
  }
}