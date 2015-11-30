/**
 * Class CustomAdapter:
 *
 * This class defines a custom adapter to hold the list item with the icon
 *
 * author: Anuj More (atm140330)
 */

package ui.killemall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<ListViewItem> items;
    private boolean listNotEmpty;
    private class ViewHolder {
        TextView textView1;
        TextView textView2;
    }

    public CustomAdapter(Context context, ArrayList<ListViewItem> items, boolean listNotEmpty) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.listNotEmpty = listNotEmpty;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.single_row, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.dateView);
            holder.textView2 = (TextView) convertView.findViewById(R.id.timeView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView1.setText(items.get(position).getDateTime());
        holder.textView2.setText(items.get(position).getLocation());
        return convertView;
    }

    // Disable clicking on the notice that displays for "Add a new item"
    @Override
    public boolean isEnabled(int position) {
        return listNotEmpty;
    }
}
