package ui.killemall.model;

public class ListViewItem {
    String date;
    String time;
    String timestamp;
    String latitude;
    String longitude;

    public ListViewItem(String timestamp, String latitude, String longitude) {
        this.timestamp = timestamp;

        String[] datetime = timestamp.split("--");
        String date = datetime[0];
        String time = datetime[1];

        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDateTime() {
        return date + " @ " + time;
    }

    public String getLocation() {
        String l1, l2;
        if (longitude.charAt(0) == '-') {
            l1 = longitude.substring(1) + " °W";
        } else {
            l1 = longitude + " °E";
        }

        if (latitude.charAt(0) == '-') {
            l2 = latitude.substring(1) + " °S";
        } else {
            l2 = latitude + " °N";
        }

        return l1 + ", " + l2;
    }

}
