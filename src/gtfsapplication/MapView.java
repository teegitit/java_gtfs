/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Project
 * Name: Kenneth McDonough, Thy Le
 * Created: 11/3/2020
 */
package gtfsapplication;

// import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import gtfsapplication.data.GTFS;
import gtfsapplication.data.Route;
import gtfsapplication.data.Stop;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * Shows and Plots information on Google Maps
 *
 * @author Kenneth McDonough, Thy Le
 * @version 11/3/2020
 */
public class MapView extends javafx.scene.layout.HBox implements Observer {
    private GTFS gtfs;
    
    private ImageView image;

    /**
     * Creates a new mapview
     * @param gtfs gtfs object
     * @author Kenneth McDonough
     */
    public MapView(GTFS gtfs) {
        this.gtfs = gtfs;
        image = new ImageView("https://i.imgur.com/XUgOR4D.png");
        this.getChildren().add(image);
    }

    /**
     * Notify the observer that it should update itself from the GTFS object.
     *
     * @param objects A list of String, Stop, Trip, StopTime or Route objects;
     *                all other types ignored
     * @author Thy Le
     */
    @Override
    public void update(List<Object> objects) {
        if (objects.size() < 1) {
            return;
        }

        String url = "https://www.mapquestapi.com/staticmap/v5/map?key=G6QM4Zn4lJlXZHnGc2BedjV7KxAZQPZY";

        Object o = objects.get(0);
        System.out.println("object in update map: " + o);

        if (o instanceof String) {
            url += "&center=" +
                gtfs.getMeanLatitude() + "," + gtfs.getMeanLongitude() +
                "&zoom=12&type=map&size=539,585";
        } else {
            url += "&size=539,585";
        }

        url += "&locations=";

        if (o instanceof Route) {
            Route route = (Route) o;
            for (Stop stop : gtfs.getStopsOnRoute(route)) {
                url += stop.getLatitude() + "," + stop.getLongitude() + "|marker-sm" + "||";

            }
        } else if (o instanceof Stop) {
            Stop stop = (Stop) o;
            url += stop.getLatitude() + "," + stop.getLongitude() + "|marker-sm" + "||" ;
        }
        image.setImage(new Image(url));
    }

}
