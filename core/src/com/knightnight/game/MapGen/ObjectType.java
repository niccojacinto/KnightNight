package com.knightnight.game.MapGen;

//Lists the type of special objects that can appear on a Map.
public enum ObjectType{
  STARTPOINT('S'), 
    ENDPOINT('E'),
    KEY('K');
  
  private char value;
  
  ObjectType(char mvalue){
    value = mvalue;
  }
  
  public char value(){
    return value;
  }
}