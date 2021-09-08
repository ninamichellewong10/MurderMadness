package game;


/**
 * A card represents a GameObject which could either be a Player, Weapon or Estate in the Murder Madness game.
 * Each player is distributed cards at the beginning of the game, and they can be used to make accusations, and
 * refute other players cards.
 *
 * Model class
 */

public interface Card{

    // Returns name of card
    String getName();
}
