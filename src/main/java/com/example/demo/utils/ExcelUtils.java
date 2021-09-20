package com.example.demo.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.demo.model.Student;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




public class ExcelUtils {
    public static final int DATA_TABLE_START_ROW = 3;
    public static final int RECORD_PER_PAGE = 33;
    public static final int CHART_SUBTABLE_START_COLUMN = 10;

    public static void tryExport(FileOutputStream outputStream, List<Student> students) throws IOException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        CellStyle titleStyle = createStyle(workbook, true, false);
        CellStyle headerStyle = createStyle(workbook, true, true);
        CellStyle bodyStyle = createStyle(workbook, false, true);

        XSSFSheet sheet = workbook.createSheet("Students report");

        sheet.setAutobreaks(true);
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setFitHeight((short) 10);
        printSetup.setFitWidth((short) 5);

        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(4, 7000);

        // create title
        Row titleRow = createRow(sheet, 0, titleStyle, 2, "List students");

        // create header

        createRow(sheet, 2, headerStyle, 0, "Id", "Name", "Gender", "Dob", "Phone");

        Map<String, Integer> yobCounter = new HashMap<>();

        int rowNum = DATA_TABLE_START_ROW;
        for (Student student : students) {


            // counting the year of birth for the chart
            String dob = student.getDob();
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MM yyyy");
            Date date = inputFormat.parse(dob);
            String outputDateStr = outputFormat.format(date);

//            int yearSeparator = dob.lastIndexOf("-");

            String yob = dob.substring(0,4);
            Integer currentCount = yobCounter.get(yob);
            yobCounter.put(yob, currentCount != null ? currentCount + 1 : 1);
            // create student row
            List<String> rowValues = new ArrayList<>();
            rowValues.add(String.valueOf(student.getStudent_id()));
            rowValues.add(student.getName());
            rowValues.add(student.getGender());
            rowValues.add(outputDateStr);
            rowValues.add(student.getPhoneNumber());

            createRow(sheet, rowNum, bodyStyle, 0, rowValues.toArray(new String[0]));

            boolean shouldBreakPage = rowNum != DATA_TABLE_START_ROW && (rowNum - DATA_TABLE_START_ROW) % RECORD_PER_PAGE == 0;
            if (shouldBreakPage) {
                sheet.setRowBreak(rowNum);

                createRow(sheet, ++rowNum, titleStyle, 2, "List students");
                rowNum += 2;
                if (students.size() > 34) {
                    createRow(sheet, rowNum, headerStyle, 0, "Id", "Name", "Gender", "Dob", "Phone");
                }
            }

            rowNum++;
        }

        // create sub-table for the chart
        int currentColumn = CHART_SUBTABLE_START_COLUMN;
        XSSFRow counterRow = sheet.createRow(1);
        for (String yob : yobCounter.keySet()) {
            Cell yearCell = titleRow.createCell(currentColumn);
            yearCell.setCellValue(Double.parseDouble(yob));
            Cell yearCounterCell = counterRow.createCell(currentColumn);
            yearCounterCell.setCellValue(yobCounter.get(yob));
            currentColumn++;
        }

        createChart(sheet, rowNum + 3, yobCounter.keySet().size());

        workbook.write(outputStream);
        workbook.close();
    }

    private static void createChart(XSSFSheet sheet, int startRow, int numberOfYears) {
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, startRow, 5, startRow + 10);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Student Report");
        chart.setTitleOverlay(false);
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        // Use a category axis for the bottom axis.
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Year of birth");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Students");
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        XDDFDataSource<Double> xs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(0, 0, CHART_SUBTABLE_START_COLUMN, CHART_SUBTABLE_START_COLUMN + numberOfYears - 1));
        XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, CHART_SUBTABLE_START_COLUMN, CHART_SUBTABLE_START_COLUMN + numberOfYears - 1));

        XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFChartData.Series series = data.addSeries(xs, ys1);
        series.setTitle("Number of students", null);
        chart.plot(data);

        XDDFBarChartData bar = (XDDFBarChartData) data;
        bar.setBarDirection(BarDirection.COL);
    }

    private static Row createRow(XSSFSheet sheet, int rowNumber, CellStyle rowStyle, int startCell, String... values) {
        Row row = sheet.createRow(rowNumber);

        for (String value : values) {
            Cell cell = row.createCell(startCell++);
            cell.setCellValue(value);
            cell.setCellStyle(rowStyle);
        }

        return row;
    }

    private static CellStyle createStyle(XSSFWorkbook workbook, boolean isFontBold, boolean isBorder) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Tahoma");
        font.setBold(isFontBold);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);

        if (isBorder) {
            short blackColorIndex = IndexedColors.BLACK.getIndex();

            style.setBorderTop(BorderStyle.THIN);
            style.setTopBorderColor(blackColorIndex);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(blackColorIndex);
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(blackColorIndex);
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(blackColorIndex);
        }

        return style;
    }
}
