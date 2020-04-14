package onto;

import org.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Onto {
    JSONObject m_onto;
    Node[] m_nodes;
    Link[] m_links;

    public Onto(InputStream ontoFile) throws IOException, JSONException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(ontoFile));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        String ontoString = sb.toString();
        m_onto = new JSONObject(ontoString);

        JSONArray nodes = m_onto.getJSONArray("nodes");
        int n = nodes.length();
        m_nodes = new Node[n];
        for (int i = 0; i < n; ++i) {
            m_nodes[i] = new Node(nodes.getJSONObject(i), this);
        }

        JSONArray links = m_onto.getJSONArray("relations");
        n = links.length();
        m_links = new Link[n];
        for (int i = 0; i < n; ++i) {
            m_links[i] = new Link(links.getJSONObject(i));
        }
    }

    public Node[] getNodes() {
        return m_nodes;
    }

    public Link[] getLinks() {
        return m_links;
    }

    public Node getNodeByID(int id) {
        for (int i = 0; i < m_nodes.length; ++i) {
            if (m_nodes[i].getID() == id) {
                return m_nodes[i];
            }
        }
        return null;
    }

    public Link getLinkByID(int id) {
        for (int i = 0; i < m_links.length; ++i) {
            if (m_links[i].getID() == id) {
                return m_links[i];
            }
        }
        return null;
    }

    public ArrayList<Node> getNodesByName(String name) {
        ArrayList<Node> result = new ArrayList<Node>();
        for (int i = 0; i < m_nodes.length; ++i) {
            if (m_nodes[i].getName().equals(name))
                result.add(m_nodes[i]);
        }
        return result;
    }

    public Node getFirstNodeByName(String name) {
        for (int i = 0; i < m_nodes.length; ++i) {
            if (m_nodes[i].getName().equals(name)) {
                return m_nodes[i];
            }
        }
        return null;
    }

    public ArrayList<Node> getNodesLinkedFrom(Node node, String linkName) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        int id = node.getID();
        for (int i = 0; i < m_links.length; ++i) {
            if (m_links[i].getSourceID() == id && m_links[i].getName().equals(linkName)) {
                nodes.add(getNodeByID(m_links[i].getTargetID()));
            }
        }
        return nodes;
    }

    public ArrayList<Node> getNodesLinkedTo(Node node, String linkName) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        int id = node.getID();
        for (int i = 0; i < m_links.length; ++i) {
            if (m_links[i].getTargetID() == id && m_links[i].getName().equals(linkName)) {
                nodes.add(getNodeByID(m_links[i].getSourceID()));
            }
        }
        return nodes;
    }

    public ArrayList<Node> getTypedNodesLinkedFrom(Node node, String linkName, String typeName) {
        ArrayList<Node> result = new ArrayList<Node>();
        ArrayList<Node> linked = getNodesLinkedFrom(node, linkName);
        for (int i = 0, n = linked.size(); i < n; ++i) {
            Node lNode = linked.get(i);
            ArrayList<Node> protos = getNodesLinkedFrom(lNode, "is_a");
            for (int j = 0, m = protos.size(); j < m; ++j) {
                if (protos.get(j).getName().equals(typeName)) {
                    result.add(lNode);
                    break;
                }
            }
        }
        return result;
    }

    public ArrayList<Node> getTypedNodesLinkedTo(Node node, String linkName, String typeName) {
        ArrayList<Node> result = new ArrayList<Node>();
        ArrayList<Node> linked = getNodesLinkedTo(node, linkName);
        for (int i = 0, n = linked.size(); i < n; ++i) {
            Node lNode = linked.get(i);
            ArrayList<Node> protos = getNodesLinkedFrom(lNode, "is_a");
            for (int j = 0, m = protos.size(); j < m; ++j) {
                if (protos.get(j).getName().equals(typeName)) {
                    result.add(lNode);
                    break;
                }
            }
        }
        return result;
    }

    public boolean isNodeOfType(Node node, String typeName) {
        ArrayList<Node> protos = getNodesLinkedFrom(node, "is_a");
        for (int i = 0, n = protos.size(); i < n; ++i) {
            if (protos.get(i).getName().equals(typeName)) {
                return true;
            }
        }
        return false;
    }

    public String getInheritedAttributeOfNode(Node node, String name) {
        String result = node.getUniqueAttribute(name);
        if (result != null)
            return result;
        ArrayList<Node> protos = getNodesLinkedFrom(node, "is_a");
        for (int i = 0, n = protos.size(); i < n; ++i) {
            result = getInheritedAttributeOfNode(protos.get(i), name);
            if (result != null)
                return result;
        }
        return null;
    }
}
