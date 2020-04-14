package onto;

import org.json.JSONException;
import org.json.JSONObject;

public class Link {
    JSONObject m_link;

    public Link(JSONObject link) {
        m_link = link;
    }

    public int getID() {
        try {
            return m_link.getInt("id");
        } catch (JSONException e) {
            return -1;
        }
    }

    public int getSourceID() {
        try {
            return m_link.getInt("source_node_id");
        } catch (JSONException e) {
            return -1;
        }
    }

    public int getTargetID() {
        try {
            return m_link.getInt("destination_node_id");
        } catch (JSONException e) {
            return -1;
        }
    }

    public String getName() {
        try {
            return m_link.getString("name");
        } catch (JSONException e) {
            return null;
        }
    }
}
