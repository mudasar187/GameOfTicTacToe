package no.woact.ahmmud16.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import no.woact.ahmmud16.Database.Model.Player;
import no.woact.ahmmud16.R;

/**
 * This class is the adapter class, a "bridge" between the RecyclerView and the database
 * It's get the information from database and insert it into the list to show highscores for all player
 */
public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.MyViewHolder> {

    private List<Player> playerList;

    /**
     * Class contains the elements for widgets for each row that allow us to get accsess highscore_list_row.xml
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView rowPlayerTextView, rowWinTextView, rowDrawTextView, rowLossTextView;

        /**
         * Instantiates a new My view holder.
         *
         * @param view the view
         */
        public MyViewHolder(View view) {
            super(view);

            // Initialize resources
            initializeResources(view);
        }

        // Initialize resources
        private void initializeResources(View view) {
            rowPlayerTextView = view.findViewById(R.id.rowPlayerTextView);
            rowWinTextView = view.findViewById(R.id.rowWinTextView);
            rowDrawTextView = view.findViewById(R.id.rowDrawTextView);
            rowLossTextView = view.findViewById(R.id.rowLossTextView);
        }
    }

    /**
     * Get the list with all players
     *
     * @param playerList the player list
     */
    public HighScoreAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    /**
     * Setting up the highscore_list_row.xml file
     * @param parent
     * @param viewType
     * @return list view
     */
    @Override
    public HighScoreAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.highscore_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    /**
     * Set each value from player to be shown in these widgets
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.rowPlayerTextView.setText(player.getPlayerName());
        holder.rowWinTextView.setText(String.valueOf(player.getWin()));
        holder.rowDrawTextView.setText(String.valueOf(player.getDraw()));
        holder.rowLossTextView.setText(String.valueOf(player.getLoss()));
    }

    /**
     * Getting the size of the list
     * @return playerlist size
     */
    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
