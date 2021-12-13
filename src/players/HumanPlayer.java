package players;

import actions.Action;
import components.Card;
import components.CardCollection;
import game.GameState;
import rules.ForwardModel;

import java.util.Scanner;

//Player that is controlled by command line inputs
public class HumanPlayer implements Player
{
    @Override
    public Action act(GameState gs, int budget)
    {
        Scanner input = new Scanner(System.in);
        Action  action;
        CardCollection hand;

        if (gs.getTurn() == 1) hand = gs.getP1Hand();
        else                   hand = gs.getP2Hand();

        System.out.println(gs);
        System.out.println("----------------------------------------");

        System.out.println("Enter the id of the card to be played: ");
        int card_to_play_id = Integer.parseInt(input.nextLine());

        Card c = hand.getCard(card_to_play_id);

        if (c.isNumberCard())
        {
            System.out.println("Enter the ID of the target card: ");
            int card_on_play_id = Integer.parseInt(input.nextLine());
            action = new Action(card_to_play_id, card_on_play_id);
        }
        else
        {
            action = new Action(card_to_play_id, -1);
        }
        System.out.println("----------------------------------------");

        return action;
    }

    @Override
    public String title() { return "HumanPlayer"; }

    @Override
    public void setSeed(int seed) { }

    @Override
    public void setForwardModel(ForwardModel fm) { }
}
