package components;

import components.Card;

public class SpecialCard extends Card
{
    public SpecialCard(CardType type)
    {
        super(type, -1);
    }

    public String toString()
    {
        String s = "";

        if (type == CardType.Mult2)
            s += "X2";
        else
            s += "%2";

        return s;
    }
}
