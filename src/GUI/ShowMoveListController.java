package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * Created by Maor Gershkovitch on 9/8/2016.
 */
public class ShowMoveListController {
    @FXML
    private ListView<String> MoveListListView = new ListView<>();

    public ListView getMoveListListView() {
        return MoveListListView;
    }
}
