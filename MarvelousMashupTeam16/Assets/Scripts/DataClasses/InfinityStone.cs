public class InfinityStone : IFieldContent
{
    public const int GREEN = 1;
    public const int YELLOW = 2;
    public const int PURPLE = 3;
    public const int BLUE = 4;
    public const int RED = 5;
    public const int ORANGE = 6;

    public int stone;
    public int cooldown;
    public int defaultcooldownTime;

    public override string ToString()
    {
        return (stone == GREEN ? "Green" :
                   stone == YELLOW ? "Yellow" :
                   stone == PURPLE ? "Purple" :
                   stone == BLUE ? "Blue" :
                   stone == RED ? "Red" :
                   stone == ORANGE ? "Orange" : "Undefined")
               + " InfinityStone";
    }
}