package components;

import java.util.*;

public class CardCollection
{
    private HashMap<Integer, Card> cards;

    public CardCollection() { cards = new HashMap<>();  }

    public void       clear()            { cards.clear();                          }
    public void       add(Card c)        { cards.put(c.getId(), c) ;               }
    public void       remove(int id)     { cards.remove(id);                       }
    public List<Card> getCards ()        { return new ArrayList<>(cards.values()); }
    public Card       getCard(int id)    { return cards.get(id);                   }
    public boolean    isEmpty()          { return cards.isEmpty();                 }
    public int        getNumberOfCards() { return cards.size();                    }

    public void addAll(CardCollection cc)
    {
        for (Map.Entry<Integer, Card> entry : cc.cards.entrySet())
        {
            Card c = entry.getValue();
            add(c);
        }
    }

    public CardCollection deepCopy()
    {
        CardCollection new_object = new CardCollection();
        for (Map.Entry<Integer, Card> entry : cards.entrySet())
        {
            Card c = entry.getValue();
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

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (Map.Entry<Integer, Card> entry : cards.entrySet())
        {
            Card c = entry.getValue();
            s.append(c).append(" ");
        }
        return s.toString();
    }
}
