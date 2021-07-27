package rules;

import actions.Action;
import components.Card;
import components.CardCollection;
import components.Deck;
import game.GameState;

import java.util.List;

public class FullHandForwardModel implements ForwardModel
{
    @Override
    public boolean step(GameState gs, Action a)
    {
        gs.decrementActionPointsLeft();

        CardCollection h;
        if (gs.getTurn() == 1) h = gs.getP1Hand();
        else                   h = gs.getP2Hand();

        CardCollection b = gs.getBoard();

        Card card_to_play = h.getCard(a.GetPlayerCardId());

        if (card_to_play==null)
        {
            assignMinPossibleScore(gs, gs.getTurn());
            return false;
        }

        if (a.GetOnPlayerCardId() != -1)
        {
            Card card_on_play = b.getCard(a.GetOnPlayerCardId());
            if (card_on_play==null)
            {
                assignMinPossibleScore(gs, gs.getTurn());
                return false;
            }

            int    n1    = card_to_play.getNumber();
            int    n2    = card_on_play.getNumber();
            double score = n1 - n2;

            if (gs.getFactor() != 1.0)
            {
                score = score * gs.getFactor();
                gs.resetFactor();
            }

            if (gs.getTurn() == 1) gs.addP1Score(score);
            else                   gs.addP2Score(score);

            gs.getDiscardDeck().add(card_to_play);
            gs.getDiscardDeck().add(card_on_play);
            h.remove(a.GetPlayerCardId());
            b.remove(a.GetOnPlayerCardId());
        }
        else
        {
            if (card_to_play.getType() == Card.CardType.Mult2) gs.updateFactor(2.0);
            else                                               gs.updateFactor(0.5);

            gs.getDiscardDeck().add(card_to_play);
            h.remove(a.GetPlayerCardId());
        }

        // check if terminal
        if (gs.getBoard().isEmpty())
            gs.setToTerminal();

        return true;
    }

    void assignMinPossibleScore(GameState gs, int player)
    {
        double min_possible_score = Math.pow(2.0, gs.getGameParameters().number_of_action_points-1) *
                (gs.getGameParameters().min_number - gs.getGameParameters().max_number);
        if (player == 1)  gs.addP1Score(min_possible_score);
        else              gs.addP2Score(min_possible_score);

    }

    @Override
    public void onPlayerTurnEnded(GameState gs)
    {
        // In this set of rules cards are drawn after player turn.
        CardCollection hand      = gs.getP1Hand();
        Deck           main_deck = gs.getMainDeck();

        for (int i=0; i<gs.getGameParameters().number_cards_on_hand - hand.getSize(); i++)
        {
            if (main_deck.isEmpty())
            {
                Deck discard_deck = gs.getDiscardDeck();
                List<Card> cards = discard_deck.popAll();
                main_deck.addAll(cards);
                main_deck.shuffle();
            }
            Card c = main_deck.pop();
            hand.add(c);
        }
    }
}
