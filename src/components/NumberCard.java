package components;

import components.Card;

public class NumberCard extends Card
{
    public NumberCard(int number)
    {
        super(CardType.Number, number);
    }

    public String toString()
    {
        return String.valueOf(number);
    }
}
