package com.knightnight.game.Objects;

/**
 * Used for getting tile coordinates from Key and other objects.
 * It'd probably be better to implement this in an abstract Actor class instead of Key,
 * but fuck it. We'll do it later :D
 * Created by Totomato on 06/04/2016.
 */
public interface IPosition {
    public int X();
    public int Y();
}
