package com.knightnight.game.MapGen;

/**
 * Created by Totomato on 06/04/2016.
 */
public class MapConstants {
    public static final char ENDPOINT = 'E';
    public static final char FLOOR = '-';
    public static final char KEY = 'K';
    public static final char STARTPOINT = 'S';
    public static final char VOID = '@';
    public static final char ROOM_WALL = '|';
    public static final char HALL_WALL = '&';
    
    //Notes: These values include the walls of the room/hall itself.
    //So the minimum value of any of these constants is 3, because 1 tile is for the inside and the other 2 tiles are for the walls.
    public static final int ROOM_WIDTH_MIN = 5;
    public static final int ROOM_WIDTH_MAX = 9;
    public static final int ROOM_HEIGHT_MIN = 5;
    public static final int ROOM_HEIGHT_MAX = 9;
    public static final int HALL_SHORT_SIDE_MIN = 3;
    public static final int HALL_SHORT_SIDE_MAX = 9;
    public static final int HALL_LONG_SIDE_MIN = 6;
    public static final int HALL_LONG_SIDE_MAX = 9;
}
