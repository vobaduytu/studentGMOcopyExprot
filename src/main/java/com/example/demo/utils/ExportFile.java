package com.example.demo.utils;

import com.example.demo.model.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ExportFile {

    public static final int DATA_TABLE_START_ROW = 3;
    public static final int RECORD_PER_PAGE = 34;
    public static final int CHART_SUBTABLE_START_COLUMN = 6;

    public static ByteArrayInputStream contactListToExcelFile( List<Student> students) throws IOException, ParseException {
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
        sheet.setColumnWidth(4, 5000);

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
                if (students.size() > 35) {
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintSetup printSetups = sheet.getPrintSetup();
        printSetups.setLandscape(true);
        printSetups.setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.setPrintGridlines(true);
        workbook.write(outputStream);

        workbook.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void createChart(XSSFSheet sheet, int startRow, int numberOfYears) {



        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, startRow, 5, startRow + 10);

        XSSFChart chart = drawing.createChart(anchor);
        ChartComponent.setRoundedCorners(chart, false);
        chart.setTitleText("Student Report");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);


        // Use a category axis for the bottom axis.
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Year of birth");
        ChartComponent.setColorAxis(chart, 0, new byte[]{(byte) 204, (byte) 204, (byte) 204});



        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Year of birth");
        leftAxis.setCrossBetween(AxisCrossBetween.MIDPOINT_CATEGORY);
        leftAxis.setCrosses(AxisCrosses.MIN);
        ChartComponent.setColorAxis(chart, 1, new byte[]{(byte) 255, (byte) 255, (byte) 255});


        XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
        rightAxis.setTitle("quantity");
        rightAxis.setCrosses(AxisCrosses.MAX);
        rightAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        ChartComponent.setColorAxis(chart, 2, new byte[]{(byte) 255, (byte) 255, (byte) 255});
        bottomAxis.crossAxis(rightAxis);
        rightAxis.crossAxis(bottomAxis);





        XDDFDataSource<Double> xs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(0, 0, CHART_SUBTABLE_START_COLUMN, CHART_SUBTABLE_START_COLUMN + numberOfYears - 1));
        XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, CHART_SUBTABLE_START_COLUMN, CHART_SUBTABLE_START_COLUMN + numberOfYears - 1));
        XDDFNumericalDataSource<Double> ys0 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(0, 0, CHART_SUBTABLE_START_COLUMN, CHART_SUBTABLE_START_COLUMN + numberOfYears - 1));

        XDDFLineChartData dataLine = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, rightAxis);
        XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);

        data.setBarDirection(BarDirection.COL);
        data.setOverlap((byte) 100);

        XDDFChartData.Series series = data.addSeries(xs, ys0);
        series.setTitle("Year of birth", null);
        XDDFChartData.Series series1 = dataLine.addSeries(xs, ys1);
        series1.setTitle("quantity", null);


        ((XDDFLineChartData.Series) series1).setSmooth(false);
        ((XDDFLineChartData.Series) series1).setMarkerStyle(MarkerStyle.DOT);


        ChartComponent.createLabelGraph(chart);
        chart.plot(dataLine);
        chart.plot(data);

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
