package components;

import components.Card;

public class NumberCard extends Card
{
    public NumberCard(int id, int number)
    {
        super(id, CardType.Number, number);
    }

    public String toString()
    {
        return "[" +id + "] " + number;
    }
}
