package GUI;

import javafx.scene.control.ListView;

/**
 * Created by Maor Gershkovitch on 9/8/2016.
 */
public class ShowMoveListController {
    private ListView MoveListListView = new ListView<String>();

    public ListView getMoveListListView() {
        return MoveListListView;
    }
}
