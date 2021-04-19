package mainPk;

import config.MyFileName;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static util.CollectionUtil.forEachIndexed;

public class MainClass2 {
    private static Logger logger = LoggerFactory.getLogger(MainClass1.class);
    public static void main(String[] args) throws Exception {
        String fileLocation = MyFileName.INPUT_EXCEL_FILE_PATH;
        FileInputStream file = new FileInputStream(new File(fileLocation));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, Map<Integer, String>> readResult = new HashMap<>();
        forEachIndexed(sheet, new AbstractMap.SimpleEntry<>(5, 40), (row, rowIndex) -> {
            forEachIndexed(row, new AbstractMap.SimpleEntry<>(1, 4), (cell, cellIndex) -> {
                try {
                    if (cell.getCellTypeEnum() == CellType.STRING) {
                        String cellValueStr = cell.getRichStringCellValue().getString();
                        readResult.computeIfAbsent(rowIndex, key -> new HashMap<>()).put(cellIndex, cellValueStr);
                    }
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                }
            });
        });

        logger.info("{}", readResult);
    }
}
