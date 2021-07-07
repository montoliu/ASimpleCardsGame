package actions;

public class Action
{
    int played_card_id;
    int on_played_card_id;

    public Action(int played_card_id, int on_played_card_id)
    {
        this.played_card_id    = played_card_id;
        this.on_played_card_id = on_played_card_id;
    }

    public Action(int played_card_id)
    {
        this.played_card_id    = played_card_id;
        this.on_played_card_id = -1;
    }

    public int GetPlayerCardId()   { return played_card_id;    }
    public int GetOnPlayerCardId() { return on_played_card_id; }

    @Override
    public String toString()
    {
        return "Action [" + played_card_id + " " + on_played_card_id + "]";
    }
}
