package com.knightnight.game.MapGen;

//Lists the type of special objects that can appear on a Map.
public enum ObjectType{
  STARTPOINT(MapConstants.STARTPOINT),
    ENDPOINT(MapConstants.ENDPOINT),
    KEY(MapConstants.KEY);
  
  private char value;
  
  ObjectType(char mvalue){
    value = mvalue;
  }
  
  public char value(){
    return value;
  }
}