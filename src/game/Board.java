package game;

import java.util.ArrayList;
import java.util.List;

public class Board
{
    private List<Card> cards;
    public Board()
    {
        cards = new ArrayList<>();
    }

    public void clear()
    {
        cards.clear();
    }

    public void add(Card c)
    {
        cards.add(c);
    }

    public void addAll(List<Card> l)
    {
        for (Card c : l)
            add(c);
    }

    public List<Card> getCards () { return cards; }

    public boolean isEmpty() { return cards.isEmpty(); }

    public Board deepCopy()
    {
        Board new_object = new Board();
        for (Card c:cards)
        {
            if (c.isNumberCard())
            {
                NumberCard new_c = new NumberCard(c.getNumber());
                new_object.add(new_c);
            }
            else
            {
                SpecialCard new_c = new SpecialCard(c.getType());
                new_object.add(new_c);
            }
        }
        return new_object;
    }


    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (Card c : cards)
            s.append(c).append(" ");
        return s.toString();
    }
}
