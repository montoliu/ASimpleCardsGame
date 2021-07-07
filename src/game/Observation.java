package game;

import actions.Action;
import components.Board;
import components.Card;
import components.Deck;
import components.Hand;

import java.util.ArrayList;
import java.util.List;

public class Observation
{
    Hand   p1_hand;
    Hand   p2_hand;
    Board  board;
    Deck   main_deck;
    Deck   discard_deck;
    int    turn;
    double p1_score;
    double p2_score;
    double factor;

    public Observation() {}

    public Observation(GameState gs)
    {
        turn         = gs.getTurn();
        p1_score     = gs.getP1Score();
        p2_score     = gs.getP2Score();
        board        = gs.getBoard().deepCopy();
        discard_deck = gs.getDiscardDeck().deepCopy();
        factor       = gs.getFactor();

        if (gs.isP1Turn()) FillP1(gs);
        else               FillP2(gs);
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

    // Return a list of all possible actions that can be played given the actual state of the observation (game state)
    public List<Action> getPossibleActions()
    {
        List<Action> actions = new ArrayList<>();
        Hand h;
        if (turn==1) h = p1_hand;
        else         h = p2_hand;

        for (int i=0; i < h.getNumberOfCards(); i++)
        {
            Card player_card = h.getCard(i);
            if (player_card.isNumberCard())
            {
                for (int j =0; j < board.getNumberOfCards(); j++)
                {
                    Card board_card = board.getCard(j);
                    Action a = new Action(i, j);
                    actions.add(a);
                }
            }
            else
            {
                Action a = new Action(i);
                actions.add(a);
            }
        }

        return actions;
    }

    public double getP1Score()     { return p1_score;     }
    public double getP2Score()     { return p2_score;     }
    public double getFactor()      { return factor;       }
    public int    getTurn()        { return turn;         }
    public Hand   getP1Hand()      { return p1_hand;      }
    public Hand   getP2Hand()      { return p2_hand;      }
    public Board  getBoard()       { return board;        }
    public Deck   getDiscardDeck() { return discard_deck; }
    public void addP1Score(double score) { p1_score += score; }
    public void addP2Score(double score) { p2_score += score; }

    public void updateFactor(double factor) { this.factor = this.factor * factor; }
    public void resetFactor()               { factor = 1.0; }


    public Observation deepCopy()
    {
        Observation new_object = new Observation();

        new_object.turn     = turn;
        new_object.p1_score = p1_score;
        new_object.p2_score = p2_score;
        new_object.factor   = factor;

        new_object.p1_hand      = p1_hand.deepCopy();
        new_object.p2_hand      = p2_hand.deepCopy();
        new_object.board        = board.deepCopy();
        new_object.main_deck    = main_deck.deepCopy();
        new_object.discard_deck = discard_deck.deepCopy();

        return new_object;
    }

    public String toString()
    {
        return    "P1 score    : "  + p1_score +
                "\nP2 score    : " + p2_score +
                "\nFactor      : " + factor +
                "\nP1 Hand     : " + p1_hand +
                "\nP2 Hand     : " + p2_hand +
                "\nBoard       : " + board +
                "\nMain deck   : " + main_deck +
                "\nDiscard deck: " + discard_deck;
    }
}
