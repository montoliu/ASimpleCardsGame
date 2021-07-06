package game;

public class Card
{
    int type;
    int number;

    public Card(int type, int number)
    {
        this.type = type;
        this.number = number;
    }

    public int getType()   { return type;   }
    public int getNumber() { return number; }
}
