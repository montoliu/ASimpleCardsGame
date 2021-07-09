package components;

public class Card
{
    public enum CardType
    {
        Number,
        Mult2,
        Div2
    }

    CardType type;
    int      number;
    int      id;      // Unique id.

    public Card(int id, CardType type, int number)
    {
        this.id     = id;
        this.type   = type;
        this.number = number;
    }

    public CardType getType()   { return type;   }
    public int      getNumber() { return number; }
    public int      getId()     { return id;     }

    public boolean isNumberCard()  { return type == CardType.Number; }
}
