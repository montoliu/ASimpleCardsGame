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

    public Card(CardType type, int number)
    {
        this.type   = type;
        this.number = number;
    }

    public CardType getType()   { return type;   }
    public int      getNumber() { return number; }

    public boolean isNumberCard()  { return type == CardType.Number; }
}
