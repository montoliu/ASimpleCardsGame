package components;

import java.util.*;

public class Deck
{
    private List<Card> cards;
    private int        seed;

    public Deck()
    {
        cards = new ArrayList<>();
        seed  = -1;
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

    public void addAll(CardCollection cc)
    {
        for (Card c : cc.getCards())
        {
            add(c);
        }
    }

    public Deck deepCopy()
    {
        Deck new_object = new Deck();
        for (Card c:cards)
        {
            if (c.isNumberCard())
            {
                NumberCard new_c = new NumberCard(c.getId(), c.getNumber());
                new_object.add(new_c);
            }
            else
            {
                SpecialCard new_c = new SpecialCard(c.getId(), c.getType());
                new_object.add(new_c);
            }
        }
        return new_object;
    }

    public Card pop()
    {
        Card c = cards.get(0);
        cards.remove(0);
        return c;
    }

    public List<Card> popAll()
    {
        List<Card> l = new ArrayList<>();
        while (!cards.isEmpty())
        {
            Card c = pop();
            l.add(c);
        }
        return l;
    }

    public void shuffle()
    {
        Random r;
        if (seed != -1)
            r = new Random(seed);
        else
            r = new Random();

        Collections.shuffle(cards, r);
    }

    public void setSeed(int seed)    {  this.seed = seed;  }

    public boolean isEmpty() { return cards.isEmpty(); }

    public List<Card> getCards () { return cards; }

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (Card c : cards)
            s.append(c).append(" ");
        return s.toString();
    }
}
