
package com.prchny.base.utils.productbulk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ProductBulkUploadXlsGeneratorUtil {
  
  private static final int NUM_ROWS = 500;
  
  private static final String OUTPUT_FILENAME = "/home/gautam/test123dss4.xls";
  
  private final List<ProductBulkUtilColumn> columnsList =
      new ArrayList<ProductBulkUtilColumn>();
  
  private WritableSheet excelSheet;
  
  private WritableWorkbook workbook;
  
  private WritableCellFormat cellFormat;
  
  private WritableCellFormat headerCellFormat;
  
  public static void main(final String[] args) throws IOException,
      WriteException {
  
    final ProductBulkUploadXlsGeneratorUtil myUtil =
        new ProductBulkUploadXlsGeneratorUtil();
    myUtil.generateXls();
  }
  
  public ProductBulkUploadXlsGeneratorUtil() {
  
  }
  
  public void generateXls() throws IOException, WriteException {
  
    init();
    initXlsFile();
    
    writeHeadersToXls();
    generateDataForRows();
    
    close();
  }
  
  private void close() throws IOException, WriteException {
  
    workbook.write();
    workbook.close();
  }
  
  private void initXlsFile() throws IOException, WriteException {
  
    final WorkbookSettings wbSettings = new WorkbookSettings();
    wbSettings.setLocale(new Locale("en", "EN"));
    
    workbook = Workbook.createWorkbook(new File(OUTPUT_FILENAME), wbSettings);
    workbook.createSheet("Report", 0);
    
    excelSheet = workbook.getSheet(0);
    
    cellFormat =
        new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
    cellFormat.setWrap(true);
    
    headerCellFormat =
        new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10,
            WritableFont.BOLD));
    headerCellFormat.setWrap(true);
  }
  
  private void generateDataForRows() throws WriteException {
  
    for (int rowCount = 1; rowCount <= NUM_ROWS; rowCount++) {
      for (int columnCount = 0; columnCount < columnsList.size(); columnCount++) {
        final ProductBulkUtilColumn productBulkUtilColumn =
            columnsList.get(columnCount);
        if (productBulkUtilColumn.containsIntValue()) {
          addCellToXls(columnCount, rowCount,
              productBulkUtilColumn.getNextIntItemFromCount(rowCount));
        } else {
          addCellToXls(columnCount, rowCount,
              productBulkUtilColumn.getNextStringItemFromCount(rowCount));
        }
      }
    }
  }
  
  private void writeHeadersToXls() throws WriteException {
  
    for (int i = 0; i < columnsList.size(); i++) {
      final ProductBulkUtilColumn productBulkUtilColumn = columnsList.get(i);
      addCellToXls(i, 0, productBulkUtilColumn.getHeaderValue(), true);
    }
  }
  
  private void addCellToXls(final int column, final int row, final String s,
      final Boolean isHeader) throws RowsExceededException, WriteException {
  
    final Label label =
        (isHeader) ? new Label(column, row, s, headerCellFormat) : new Label(
            column, row, s, cellFormat);
    excelSheet.addCell(label);
  }
  
  private void addCellToXls(final int column, final int row, final String s)
      throws RowsExceededException, WriteException {
  
    addCellToXls(column, row, s, false);
  }
  
  private void addCellToXls(final int column, final int row, final int i)
      throws RowsExceededException, WriteException {
  
    final Number number = new Number(column, row, i, cellFormat);
    excelSheet.addCell(number);
  }
  
  private void init() {
  
    columnsList.add(new ProductBulkUtilColumn("COL_OFFER", "Offer", true));
    columnsList.add(new ProductBulkUtilColumn("COL_VENDOR_CODE", "Vendor Code",
        "0cac8d"));
    columnsList
        .add(new ProductBulkUtilColumn("COL_PRODUCT_NAME", "Product Name",
            "Trouser", " Full Sleeves T-Shirt", "Jeans Fixed Waist"));
    columnsList.add(new ProductBulkUtilColumn("COL_SKU", "SKU", false));
    columnsList.add(new ProductBulkUtilColumn("COL_INVENTORY", "Inventory",
        "10"));
    columnsList.add(new ProductBulkUtilColumn("COL_PRODUCT_CATEGORY",
        "Product Category", "electronic-camcorders", "computers-desktops",
        "electronic-telephones", "men-apparel-ethnic-wear",
        "perfumes-beauty-fragrances", "lifestyle-watches",
        "jewelry-fashion-jewelry", "jewelry-silver", "footwear-mens-footwear",
        "mobiles-accessories"));
    columnsList
        .add(new ProductBulkUtilColumn(
            "COL_PRODUCT_DESCRIPTION",
            "Product Description",
            "Test Production Lilliput is a leader in the organized market for kids apparel in India with a presence across categories – from infants to boys and girls up to age 10. They have focused on the kids wear market since their inception and have successfully established “Lilliput” as a leading brand among consumers. They offer an extensive product portfolio of garments for infants, toddlers and juniors. They also offer a wide range of kids’ footwear, toys, LLby care products, nursery furniture and hard goods, such as strollers, walkers and cycles. In addition, they have an attractive range of kids’ accessories."));
    columnsList.add(new ProductBulkUtilColumn("COL_BRAND", "BrandId", "1"));
    columnsList
        .add(new ProductBulkUtilColumn("COL_WARRANTY", "Warranty", "NA"));
    columnsList.add(new ProductBulkUtilColumn("COL_LENGTH", "Length (cm)", 100,
        150, 200, 300));
    columnsList.add(new ProductBulkUtilColumn("COL_WIDTH", "Width (cm)", 100,
        150, 200, 300));
    columnsList.add(new ProductBulkUtilColumn("COL_HEIGHT", "Height (cm)", 100,
        150, 200, 300));
    columnsList.add(new ProductBulkUtilColumn("COL_WEIGHT", "Weight (kg)", 55,
        65, 70));
    columnsList
        .add(new ProductBulkUtilColumn("COL_MRP", "MRP", "1195", "1999"));
    columnsList.add(new ProductBulkUtilColumn("COL_SP_PRICE", "SP Price", 1,
        20, 50));
    columnsList.add(new ProductBulkUtilColumn("COL_VENDOR_PRICE",
        "Vendor Price", 1010, 587));
    columnsList.add(new ProductBulkUtilColumn("COL_COMMISSION",
        "Commission (%)", 10));
    columnsList.add(new ProductBulkUtilColumn("COL_SHIPPING_GROUP",
        "Shipping Group", "COD-PRODUCTS", "STD"));
    columnsList.add(new ProductBulkUtilColumn("COL_COURIER_ARRAGNEMENT_BY",
        "Courier Arrangement by", "SNAPDEAL", "VENDOR"));
    columnsList.add(new ProductBulkUtilColumn("COL_COURIER_COST_BOURNE_BY",
        "Courier Cost Bourne By", "SNAPDEAL", "VENDOR"));
    columnsList.add(new ProductBulkUtilColumn("COL_COURIER_COST",
        "Courier Cost", 20, 30, 70));
    columnsList.add(new ProductBulkUtilColumn("COL_DELIVERY_TIME",
        "Delivery Time (in days)", "7 to 10"));
    columnsList.add(new ProductBulkUtilColumn("COL_SUBTITLE", "Sub-Title",
        "Stylish trousers for boys", "Stylish full-sleeve t-shirts for girls",
        "Fashionable clothing for kids"));
    columnsList.add(new ProductBulkUtilColumn("COL_START_DATE",
        "Start Date (DD/MM/YYYY)", "13_07_2012_017_00_00"));
    columnsList.add(new ProductBulkUtilColumn("COL_END_DATE",
        "End Date (DD/MM/YYYY)", "31_12_2012_03_00_00"));
    columnsList.add(new ProductBulkUtilColumn("COL_BD_EMAIL", "BD Email",
        "abc@xyz.com", "def@pqr.com"));
    columnsList.add(new ProductBulkUtilColumn("COL_ATTRIBUTE_1_NAME",
        "Attribute1-Name", "Size"));
    columnsList.add(new ProductBulkUtilColumn("COL_ATTRIBUTE_1_VALUE",
        "Attribute1-Value", "'11/12", "7/J (UK SIZE)", "1/Y (UK SIZE)",
        "12/J (UK SIZE)", "2/Y (UK SIZE)", "'6", "'8"));
    columnsList.add(new ProductBulkUtilColumn("COL_ATTRIBUTE_2_NAME",
        "Attribute2-Name", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_ATTRIBUTE_2_VALUE",
        "Attribute2-Value", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_ATTRIBUTE_3_NAME",
        "Attribute3-Name", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_ATTRIBUTE_3_VALUE",
        "Attribute3-Value", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_IMAGES_MAIN", "Main Images",
        "test.jpg"));
    columnsList
        .add(new ProductBulkUtilColumn(
            "COL_TECH_SPECS",
            "Tech specs",
            "{\"Heading 1\":{\"key 1\":\"value 111111111111111111111111111111111111\",\"key 2\":\"value 222222222222222222222\"},\"Heading3\":{\"key 3\":\"value3\",\"key4\":\"value4444444444444444\"}}"));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_1_NAME",
        "Filter1-Name", "Brand"));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_1_VALUE",
        "Filter1-Value", "B1", "B2"));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_2_NAME",
        "Filter2-Name", "Price"));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_2_VALUE",
        "Filter2-Value", "P1", "P2"));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_3_NAME",
        "Filter3-Name", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_3_VALUE",
        "Filter3-Value", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_4_NAME",
        "Filter4-Name", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_4_VALUE",
        "Filter4-Value", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_5_NAME",
        "Filter5-Name", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_5_VALUE",
        "Filter5-Value", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_6_NAME",
        "Filter6-Name", ""));
    columnsList.add(new ProductBulkUtilColumn("COL_FILTER_6_VALUE",
        "Filter6-Value", ""));
    columnsList.add(new ProductBulkUtilColumn("PROCUREMENT_SLA",
        "Procurement SLA", 1));
    columnsList.add(new ProductBulkUtilColumn("WAREHOUSE_PROCESSING_SLA",
        "Warehouse Processing SLA", 1));
  }
}
