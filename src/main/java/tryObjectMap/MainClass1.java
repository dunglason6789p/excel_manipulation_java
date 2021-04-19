package tryObjectMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ObjectMap;

public class MainClass1 {
    private static Logger logger = LoggerFactory.getLogger((new Object(){}).getClass());
    public static void main(String[] args) {
        ObjectMap objectMap = new ObjectMap();
        ///*
        objectMap.writeToPath(123, "a", "b", "c");
        log(objectMap.getMap());
        objectMap.writeToPath("sonnt", "a", "b", "c");
        log(objectMap.getMap());
        objectMap.writeToPath("overriden", "a", "b");
        log(objectMap.getMap());
        objectMap.writeToPath(1997, "a", "d", "e");
        log(objectMap.getMap());
        objectMap.writeToPath(2002, "a", "d", "e", "f");
        log(objectMap.getMap());
        //*/
        objectMap.appendChildToPath("x", "xv", "a", "d", "e", "f");
        log(objectMap.getMap());
        objectMap.appendChildToPath("x2", "x2v", "a", "d", "e", "f");
        log(objectMap.getMap());
        objectMap.appendChildToPath("x3", "x3v", "a", "d", "e");
        log(objectMap.getMap());
        int noop = 0;
    }
    private static void log(Object object) {
        logger.info("{}", object);
    }
}
