package game;

import java.util.ArrayList;
import java.util.List;

public class Observation
{
    Hand  p1_hand;
    Hand  p2_hand;
    Board board;
    Deck  main_deck;
    Deck  discard_deck;
    int   turn;
    int   p1_score;
    int   p2_score;

    public Observation(GameState gs)
    {
        turn         = gs.getTurn();
        p1_score     = gs.getP1Score();
        p2_score     = gs.getP2Score();
        board        = gs.getBoard().deepCopy();
        discard_deck = gs.getDiscardDeck().deepCopy();

        if (gs.isP1Turn())
            FillP1(gs);
        else
            FillP2(gs);
    }

    private void FillP1(GameState gs)
    {
        p1_hand   = gs.getP1Hand().deepCopy();
        p2_hand   = new Hand();
        main_deck = new Deck();

        Deck temp = new Deck();
        temp.addAll(gs.getP2Hand().getCards());
        temp.addAll(gs.getMainDeck().getCards());
        temp.shuffle();

        // draw P2
        int n_cards_on_hand = gs.getP2Hand().getNumberOfCards();
        for (int i=0; i<n_cards_on_hand; i++)
        {
            Card c = temp.pop();
            p2_hand.add(c);
        }

        main_deck.addAll(temp.getCards());
    }

    private void FillP2(GameState gs)
    {
        p2_hand   = gs.getP2Hand().deepCopy();
        p1_hand   = new Hand();
        main_deck = new Deck();

        Deck temp = new Deck();
        temp.addAll(gs.getP1Hand().getCards());
        temp.addAll(gs.getMainDeck().getCards());
        temp.shuffle();

        // draw P1
        int n_cards_on_hand = gs.getP1Hand().getNumberOfCards();
        for (int i=0; i<n_cards_on_hand; i++)
        {
            Card c = temp.pop();
            p1_hand.add(c);
        }

        main_deck.addAll(temp.getCards());
    }


    public Observation Clone()
    {
        return null;
    }

    public List<Action> getPossibleActions()
    {
        List<Action> actions = new ArrayList<>();

        return actions;
    }

    public String toString()
    {
        return "\nP1 score    : "  + p1_score +
                "\nP2 score    : " + p2_score +
                "\nP1 Hand     : " + p1_hand +
                "\nP2 Hand     : " + p2_hand +
                "\nBoard       : " + board +
                "\nMain deck   : " + main_deck +
                "\nDiscard deck: " + discard_deck;
    }
}
