package pakoswdt.file;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyleBuilder {

    public CellStyleBuilder(Workbook workbook) {
        this.workbook = workbook;
        this.cellStyle = workbook.createCellStyle();
        this.font = workbook.createFont();

        setDefaults();
    }

    private final Workbook workbook;
    private final CellStyle cellStyle;
    private final Font font;

    private short fontSize = 10;
    private String fontName = "Helvetica";

    private boolean bold = false;
    private boolean italic = false;

    private byte underline = Font.U_NONE;

    private boolean wrapText = false;

    private short borderColor = IndexedColors.BLACK.getIndex();
    private short borderThickness = CellStyle.BORDER_THIN;

    private void setDefaults() {
        this.font.setFontHeightInPoints(fontSize);
        this.font.setFontName(fontName);
    }

    public CellStyleBuilder fontSize(short fontSize) {
        this.font.setFontHeightInPoints(fontSize);
        return this;
    }

    public CellStyleBuilder fontName(String fontName) {
        this.font.setFontName(fontName);
        return this;
    }

    public CellStyleBuilder bold(boolean bold) {
        this.font.setBold(bold);
        return this;
    }

    public CellStyleBuilder italic(boolean italic) {
        this.font.setItalic(italic);
        return this;
    }

    public CellStyleBuilder underline(byte underline) {
        this.font.setUnderline(underline);
        return this;
    }

    public CellStyleBuilder wrapText(boolean wrapText) {
        this.cellStyle.setWrapText(wrapText);
        return this;
    }

    public CellStyleBuilder align(short align) {
        this.cellStyle.setAlignment(align);
        return this;
    }

    public CellStyleBuilder verticalAlign(short verticalAlign) {
        this.cellStyle.setVerticalAlignment(verticalAlign);
        return this;
    }

    public CellStyleBuilder borderTop() {
        this.cellStyle.setBorderTop(borderThickness);
        this.cellStyle.setTopBorderColor(borderColor);
        return this;
    }

    public CellStyleBuilder borderBottom() {
        this.cellStyle.setBorderBottom(borderThickness);
        this.cellStyle.setBottomBorderColor(borderColor);
        return this;
    }

    public CellStyleBuilder borderLeft() {
        this.cellStyle.setBorderLeft(borderThickness);
        this.cellStyle.setLeftBorderColor(borderColor);
        return this;
    }

    public CellStyleBuilder borderRight() {
        this.cellStyle.setBorderRight(borderThickness);
        this.cellStyle.setRightBorderColor(borderColor);
        return this;
    }

    public CellStyleBuilder borderAll() {
        return borderTop().borderBottom().borderLeft().borderRight();
    }

    public CellStyleBuilder dataFormat(String format) {
        this.cellStyle.setDataFormat(this.workbook.getCreationHelper().createDataFormat().getFormat(format));
        return this;
    }

    public CellStyle build() {
        cellStyle.setFont(font);
        return cellStyle;
    }
}