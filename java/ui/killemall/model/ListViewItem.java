package ui.killemall.model;

public class ListViewItem {
    String date;
    String time;
    String timestamp;
    String latitude;
    String longitude;

    public ListViewItem(String timestamp, String latitude, String longitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
