package com.tm.timesheet.service.util;

import com.tm.commonapi.constants.TimesheetConstants;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;

public class TimesheetDetailedReportBuilder {
    public static final String COL_EXPR_PREFIX = "col";

    public static final String COL_HEADER_EXPR_PREFIX = "header";

    private final static int SPACE_BETWEEN_COLS = 5;

    private final static int HEADER_COLUMN_HEIGHT = 68;

    private final static int DETAIL_COLUMN_HEIGHT = 20;

    private final static int HEADER_BAND_HEIGHT = 70;

    private final static int DETAIL_BAND_HEIGHT = 22;

    private final static int MARGIN = 1;   

    private JasperDesign jasperDesign;

    private int numColumns;

    public TimesheetDetailedReportBuilder(JasperDesign jasperDesign, int numColumns) {
        this.jasperDesign = jasperDesign;
        this.numColumns = numColumns;
    }

    public void addDynamicColumns() throws JRException {

        JRDesignBand detailBand = new JRDesignBand();
        JRDesignBand headerBand = new JRDesignBand();

        JRDesignStyle normalStyle = getNormalStyle();
        JRDesignStyle columnHeaderStyle = getColumnHeaderStyle();

        jasperDesign.addStyle(normalStyle);
        jasperDesign.addStyle(columnHeaderStyle);

        int xPos = MARGIN;
        for (int i = 0; i < numColumns; i++) {
            boolean isLeft = false;
            boolean isRotateNeeded = false;
            int columnWidth = 0;
            if (i == 0) {
                columnWidth = 95;
                isLeft = true;
            } else if (i == 1) {
                columnWidth = 85;
                isLeft = true;
            } else if (i == (numColumns - 1)) {
                columnWidth = 50;
            } else {
                columnWidth = 25;
                isRotateNeeded = true;
            }
            JRDesignField field = new JRDesignField();
            field.setName(COL_EXPR_PREFIX + i);
            field.setValueClass(java.lang.String.class);
            jasperDesign.addField(field);

            JRDesignField headerField = new JRDesignField();
            headerField.setName(COL_HEADER_EXPR_PREFIX + i);
            headerField.setValueClass(java.lang.String.class);
            jasperDesign.addField(headerField);

            headerBand.setHeight(HEADER_BAND_HEIGHT);
            JRDesignTextField colHeaderField = new JRDesignTextField();
            colHeaderField.setX(xPos);
            colHeaderField.setY(2);

            colHeaderField.setWidth(columnWidth);
            colHeaderField.setHeight(HEADER_COLUMN_HEIGHT);
            colHeaderField.setStyle(getColumnHeaderStyle());
            colHeaderField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
            colHeaderField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
            
            if (isRotateNeeded) {
                colHeaderField.setRotation(RotationEnum.LEFT);
                colHeaderField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
                colHeaderField.setVerticalTextAlign(VerticalTextAlignEnum.JUSTIFIED);
                colHeaderField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
            } else {
                colHeaderField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);

            }

            colHeaderField.setBold(true);
            JRDesignExpression headerExpression = new JRDesignExpression();
            headerExpression.setValueClass(java.lang.String.class);
            headerExpression.setText("$F{" + COL_HEADER_EXPR_PREFIX + i + "}");
            colHeaderField.setExpression(headerExpression);
            colHeaderField.getLineBox().getPen().setLineWidth(0.5f);
            headerBand.addElement(colHeaderField);

            detailBand.setHeight(DETAIL_BAND_HEIGHT);
            JRDesignTextField textField = new JRDesignTextField();
            textField.setX(xPos);
            textField.setY(2);
            textField.setWidth(columnWidth);
            textField.setHeight(DETAIL_COLUMN_HEIGHT);
            if (isLeft) {
                textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                textField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            } else{
                textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            }           
            textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
            textField.setStyle(normalStyle);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.lang.String.class);
            expression.setText("$F{" + COL_EXPR_PREFIX + i + "}");
            textField.getLineBox().getPen().setLineWidth(0.2f);
            textField.setExpression(expression);
            detailBand.addElement(textField);

            xPos = xPos + (columnWidth - 5) + SPACE_BETWEEN_COLS;
        }
        jasperDesign.setColumnHeader(headerBand);
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);
    }

    private JRDesignStyle getNormalStyle() {
        JRDesignStyle normalStyle = new JRDesignStyle();
        normalStyle.setName("Sans_Normal");
        normalStyle.setDefault(true);
        normalStyle.setFontName(TimesheetConstants.SANS_SERIF);
        normalStyle.setFontSize(8);
        normalStyle.setPdfFontName(TimesheetConstants.HELVETICA);
        normalStyle.setPdfEncoding(TimesheetConstants.ENCODING_TYPE);
        normalStyle.setPdfEmbedded(false);
        return normalStyle;
    }

    private JRDesignStyle getColumnHeaderStyle() {
        JRDesignStyle columnHeaderStyle = new JRDesignStyle();
        columnHeaderStyle.setName(TimesheetConstants.SANS_SERIF);
        columnHeaderStyle.setDefault(false);
        columnHeaderStyle.setFontName(TimesheetConstants.SANS_SERIF);
        columnHeaderStyle.setFontSize(10);
        columnHeaderStyle.setBold(true);
        columnHeaderStyle.setPdfFontName(TimesheetConstants.HELVETICA);
        columnHeaderStyle.setPdfEncoding(TimesheetConstants.ENCODING_TYPE);
        columnHeaderStyle.setPdfEmbedded(false);
        return columnHeaderStyle;
    }

}
