package com.example.demo.utils;

import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xssf.usermodel.XSSFChart;

public class ChartComponent {
    /**
    /**
     * make the border of anchor square
     * @param chart current chart
     * @param setVal check status of border square or not
     */
    public static void setRoundedCorners(XSSFChart chart, boolean setVal) {
        if (chart.getCTChartSpace().getRoundedCorners() == null) chart.getCTChartSpace().addNewRoundedCorners();
        chart.getCTChartSpace().getRoundedCorners().setVal(setVal);
    }
    /**
     * create label for chart and set position for legend of chart
     * @param chart current chart need
     *
     */
    public static void createLabelGraph(XSSFChart chart) {
        /**
         * format label for barchart
         */
        chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(0).addNewDLbls();
        chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(0).getDLbls().addNewShowVal().setVal(true);
        chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(0).getDLbls().addNewShowLegendKey().setVal(false);
        chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(0).getDLbls().addNewShowCatName().setVal(false);
        chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(0).getDLbls().addNewShowSerName().setVal(false);
        /**
         * format label for line graph
         */
        chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(0).addNewDLbls();
        chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(0).getDLbls()
                .addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(new byte[]{(byte) 255, (byte) 255, (byte) 255});
        chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(0).getDLbls().addNewShowVal().setVal(true);
        chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(0).getDLbls().addNewShowLegendKey().setVal(false);
        chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(0).getDLbls().addNewShowCatName().setVal(false);
        chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(0).getDLbls().addNewShowSerName().setVal(false);
    }
    // new byte[] {(byte)255, 0 ,0}
    public static void setColorAxis(XSSFChart chart, int position, byte[] color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        chart.getAxes().get(position).getOrAddShapeProperties().setLineProperties(line);
    }
}
