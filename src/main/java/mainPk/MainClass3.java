package mainPk;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.MyFileName;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ObjectMap;
import util.ValueWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static util.CollectionUtil.forEachIndexed;

public class MainClass3 {
    private static Logger logger = LoggerFactory.getLogger(MainClass1.class);
    public static void main(String[] args) throws Exception {
        MainClass3 mainClass = new MainClass3();
        mainClass.mainAction();
    }
    private int SectionIdInt = 1;
    public String generateNewSectionId() {
        return "XXX"+(SectionIdInt++);
    }
    public Map<String, String> mapSectionNameToId = new HashMap<>();
    public Map<String, Integer> mapSectionNameToQACount = new HashMap<>();
    public void mainAction() throws IOException {
        String fileLocation = MyFileName.INPUT_EXCEL_FILE_PATH;
        FileInputStream file = new FileInputStream(new File(fileLocation));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        ObjectMap objectMap = new ObjectMap(LinkedHashMap::new);

        forEachIndexed(sheet, new AbstractMap.SimpleEntry<>(5, 40), (row, rowIndex) -> {
            String sectionName = row.getCell(1).getStringCellValue().trim();
            String sectionId = createSectionIdIfNeeded(sectionName);
            String question = row.getCell(2).getStringCellValue().trim();
            String answer = row.getCell(3).getStringCellValue().trim();
            String link = row.getCell(4).getStringCellValue().trim();
            int noop = 0;
            int nextQAIndex = nextQAIndex(sectionName);
            objectMap.appendChildToPath("Q", question,
                    sectionId, "BODY", "QA"+nextQAIndex);
            objectMap.appendChildToPath("A", answer,
                    sectionId, "BODY", "QA"+nextQAIndex);
            objectMap.appendChildToPath("URL", link,
                    sectionId, "BODY", "QA"+nextQAIndex);
        });

        ObjectMapper jsonUtil = new ObjectMapper();
        String json = jsonUtil.writerWithDefaultPrettyPrinter().writeValueAsString(objectMap.getMap());
        //logger.info("{}", objectMap.getMap());
        logger.info("{}", json);
    }
    public String createSectionIdIfNeeded(String sectionName) {
        String abc = mapSectionNameToId.get(sectionName);
        if (abc == null) {
            String xxx = generateNewSectionId();
            mapSectionNameToId.put(sectionName, xxx);
            return xxx;
        } else {
            return abc;
        }
    }
    public int nextQAIndex(String sectionName) {
        Integer abc = mapSectionNameToQACount.get(sectionName);
        int xxx = (abc == null) ? 1 : abc + 1;
        mapSectionNameToQACount.put(sectionName, xxx);
        return xxx;
    }
}
