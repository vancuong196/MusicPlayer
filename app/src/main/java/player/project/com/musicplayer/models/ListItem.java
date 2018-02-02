package player.project.com.musicplayer.models;

/**
 * Created by Cuong on 2/1/2018.
 */

public class ListItem {
    private String title;
    private String imageURL;

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public ListItem(String title, String imageURL) {

        this.title = title;
        this.imageURL = imageURL;
    }
}
