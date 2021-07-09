package components;

import components.Card;

public class SpecialCard extends Card
{
    public SpecialCard(int id, CardType type) { super(id, type, -1); }

    public String toString()
    {
        String s = "[" + id + "] ";
        if (type == CardType.Mult2) s += "X2";
        else                        s += "%2";

        return s;
    }
}
