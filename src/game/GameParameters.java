package game;

public class GameParameters
{
    public int number_cards_on_hand;
    public int number_cards_on_board;
    public int number_of_action_points;
    public int min_number;
    public int max_number;
    public int number_cards_limit_number;
    public int number_cards_normal_number;
    public int number_cards_mult2;
    public int number_cards_div2;

    public GameParameters ()
    {
        number_cards_on_board      = 10;
        number_cards_on_hand       = 6;
        number_of_action_points    = 3;
        min_number                 = 1;
        max_number                 = 6;
        number_cards_limit_number  = 4;
        number_cards_normal_number = 6;
        number_cards_mult2         = 4;
        number_cards_div2          = 4;
    }
}
