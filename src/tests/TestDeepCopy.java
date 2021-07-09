package tests;

import components.Card;
import components.Deck;
import components.NumberCard;
import components.SpecialCard;

public class TestDeepCopy
{
    public static void main(String[] args)
    {
        NumberCard c1 = new NumberCard(1, 1);
        NumberCard c2 = new NumberCard(2, 2);
        NumberCard c3 = new NumberCard(3, 3);
        NumberCard c4 = new NumberCard(4, 4);
        NumberCard c5 = new NumberCard(5, 5);
        SpecialCard c6 = new SpecialCard(6, Card.CardType.Mult2);
        SpecialCard c7 = new SpecialCard(7, Card.CardType.Div2);

        Deck d1 = new Deck();
        d1.add(c1);
        d1.add(c2);
        d1.add(c3);
        d1.add(c4);
        d1.add(c5);
        d1.add(c6);
        d1.add(c7);

        System.out.print("D1                -> ");
        System.out.println(d1);

        System.out.println("\nDeck d2 = d1;");
        Deck d2 = d1;

        System.out.print("D2                -> ");
        System.out.println(d2);
        d1.pop();
        System.out.print("D1 after p1.pop() -> ");
        System.out.println(d1);
        System.out.print("D2 after p1.pop() -> ");
        System.out.println(d2);

        System.out.println("\n----");


        System.out.println("\nDeck d3 = d1.deepCopy()");
        Deck d3 = d1.deepCopy();

        System.out.print("D3                -> ");
        System.out.println(d3);
        d1.pop();
        System.out.print("D1 after p1.pop() -> ");
        System.out.println(d1);
        System.out.print("D3 after p1.pop() -> ");
        System.out.println(d3);

    }
}
