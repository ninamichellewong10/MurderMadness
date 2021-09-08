package game;
/**
* A card represents a game object, which is either a Player, Weapon or Estate.
 *
 * Model class
 */
public interface GameObject extends Card{
    Square getSquare();
}
