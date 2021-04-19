package mainPk;

import config.MyFileName;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ValueWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static util.CollectionUtil.forEachIndexed;

public class MainClass3 {
    private static Logger logger = LoggerFactory.getLogger(MainClass1.class);
    public static void main(String[] args) throws Exception {

    }
    private int SectionIdInt = 0;
    public String generateNewSectionId() {
        return "XXX"+(SectionIdInt++);
    }
    public Map<String, String> mapSectionNameToId = new HashMap<>();
    public void mainAction() throws IOException {
        String fileLocation = MyFileName.INPUT_EXCEL_FILE_PATH;
        FileInputStream file = new FileInputStream(new File(fileLocation));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, Map<Integer, String>> readResult = new HashMap<>();
        forEachIndexed(sheet, new AbstractMap.SimpleEntry<>(1, 4), (row, rowIndex) -> {
            for (int columnIndex = 5; columnIndex <= 40; columnIndex++) {
                Cell cell = row.getCell(columnIndex);
                String currentSectionName = cell.getStringCellValue();
                if (currentSectionName != null) {
                    currentSectionName = currentSectionName.trim();
                    createSectionIdIfNeeded(currentSectionName);
                    String sectionId = getSectionId(currentSectionName);

                }
            }
        });

        logger.info("{}", readResult);
    }
    public void createSectionIdIfNeeded(String sectionName) {
        if (mapSectionNameToId.get(sectionName) == null) {
            mapSectionNameToId.put(sectionName, generateNewSectionId());
        }
    }
    public String getSectionId(String sectionName) {
        return mapSectionNameToId.get(sectionName);
    }
}
