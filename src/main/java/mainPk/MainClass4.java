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
import util.ClipboardUtil;
import util.ObjectMap;
import util.ValueWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static util.CollectionUtil.forEachIndexed;

public class MainClass4 {
    private static final Logger logger = LoggerFactory.getLogger(MainClass1.class);

    private static final String[] LINK_PLACEHOLDERS = {"（リンク）"};
    private static Predicate<String> Predicate_IsLinkPlaceholder = (string -> {
        for (String xxx : LINK_PLACEHOLDERS) {
            if (xxx.equals(string)) {
                return true;
            }
        }
        return false;
    });

    private static final String[] NO_LINK_INDICATOR = {
            "マニュアルなし",
            "アプリでの編集不可のためマニュアルなし"
    };
    private static Predicate<String> Predicate_IsNoLinkIndicator = (string -> {
        for (String xxx : NO_LINK_INDICATOR) {
            if (xxx.equals(string)) {
                return true;
            }
        }
        return false;
    });

    private static final String[] IGNORED_TEXT_LINK = {
            "打刻修正",
            "休暇申請"
    };
    private static Predicate<String> Predicate_IsIgnoredTextLink = (string -> {
        for (String xxx : IGNORED_TEXT_LINK) {
            if (xxx.equals(string)) {
                return true;
            }
        }
        return false;
    });

    public static void main(String[] args) throws Exception {
        MainClass4 mainClass = new MainClass4();
        mainClass.mainAction();
    }
    private int SectionIdInt = 1;
    public String generateNewSectionId() {
        return "SECTION"+(SectionIdInt++);
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
            String answerRawText = row.getCell(3).getStringCellValue().trim();
            String linkRawText = row.getCell(4).getStringCellValue().trim();

            int nextQAIndex = nextQAIndex(sectionName);
            objectMap.appendChildToPath("Q", question,
                    sectionId, "BODY", "QA"+nextQAIndex);
            objectMap.appendChildToPath("A", answerRawText,
                    sectionId, "BODY", "QA"+nextQAIndex);
            objectMap.appendChildToPath("URL", linkRawText,
                    sectionId, "BODY", "QA"+nextQAIndex);

            List<String> splitAnswer = splitByLineBreak(answerRawText);
            int numOfLinkPlaceHolders = (int)splitAnswer.stream().filter(Predicate_IsLinkPlaceholder).count();

            List<String> splitLink = splitByLineBreak(linkRawText).stream()
                    .filter(Predicate_IsNoLinkIndicator.negate())
                    .filter(Predicate_IsIgnoredTextLink.negate())
                    .collect(Collectors.toList());
            if (numOfLinkPlaceHolders != splitLink.size()) {
                throw new RuntimeException("FATAL: LINK PLACEHOLDERS NOT MATCH !!!");
            }

            int index_answerPart = -1;
            int index_LINK_PLACEHOLDER = -1;
            for (String answerPart : splitAnswer) {
                index_answerPart++;
                if (Predicate_IsLinkPlaceholder.test(answerPart)) {
                    index_LINK_PLACEHOLDER++;
                    String url = splitLink.get(index_LINK_PLACEHOLDER);
                    objectMap.appendChildToPath("I"+index_answerPart, url,
                            sectionId, "BODY", "QA"+nextQAIndex, "AS");
                } else {
                    objectMap.appendChildToPath("I"+index_answerPart, answerPart,
                            sectionId, "BODY", "QA"+nextQAIndex, "AS");
                }
            }
            int noop = 0;
        });

        ObjectMapper jsonUtil = new ObjectMapper();
        String json = jsonUtil.writerWithDefaultPrettyPrinter().writeValueAsString(objectMap.getMap());
        ClipboardUtil.copyToClipboard(json);
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
    public static List<String> splitByLineBreak(String string) {
        return Arrays.stream(string.split("\n")).map(String::trim).filter(it->!it.isEmpty()).collect(Collectors.toList());
    }
}
