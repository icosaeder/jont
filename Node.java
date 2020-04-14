package onto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class Node {
    JSONObject m_node;
    Onto m_onto;
    Hashtable<String, Object> m_storage;

    public Node(JSONObject node, Onto onto) {
        m_node = node;
        m_onto = onto;
        m_storage = new Hashtable<String, Object>();
    }

    public int getID() {
        try {
            return m_node.getInt("id");
        } catch (JSONException e) {
            return -1;
        }
    }

    public String getName() {
        try {
            return m_node.getString("name");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getUniqueAttribute(String name) {
        try {
            return m_node.getJSONObject("attributes").getString(name);
        } catch (JSONException e) {
            return null;
        }
    }

    public String getAttribute(String name) {
        return m_onto.getInheritedAttributeOfNode(this, name);
    }

    public Hashtable<String, Object> getStorage() {
        return m_storage;
    }
}
