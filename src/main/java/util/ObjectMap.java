package util;

import java.util.HashMap;
import java.util.Map;

public class ObjectMap {
    private final Map<String, Object> internalMap = new HashMap<>();
    public ObjectMap writeToPath(Object value, String... path) {
        Map<String, Object> parentNode = null;
        Object currentNode = internalMap;
        for (int i = 0; i <= path.length-2; i++) {
            parentNode = (Map<String, Object>)currentNode;
            currentNode = parentNode.get(path[i]);
            if (! (currentNode instanceof Map)) {
                currentNode = new HashMap<>();
                parentNode.put(path[i], currentNode);
            }
        }
        String endOfPath = path[path.length-1];
        ((Map<String, Object>)currentNode).put(endOfPath, value);
        return this;
    }
    public ObjectMap appendChildToPath(String childKey, Object value, String... path) {
        Map<String, Object> parentNode = null;
        Object currentNode = internalMap;
        for (int i = 0; i <= path.length-1; i++) {
            parentNode = (Map<String, Object>)currentNode;
            currentNode = parentNode.get(path[i]);
            if (! (currentNode instanceof Map)) {
                currentNode = new HashMap<>();
                parentNode.put(path[i], currentNode);
            }
        }
        ((Map<String, Object>)currentNode).put(childKey, value);
        return this;
    }
    public Map<String, Object> getMap() {
        return internalMap;
    }
}
