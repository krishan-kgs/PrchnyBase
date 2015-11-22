
package com.prchny.base.utils.productbulk;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.write.WriteException;

public class ProductBulkEditCsvGeneratorUtil {
  
  private static final int NUM_SKU = 50000;
  
  private static final String OUTPUT_FOLDERNAME = "/home/gautam/dump/";
  
  String[] csvNames = {
      "batchProductUpdates", "batchHighlightsUpdate", "batchFiltersUpdate"
  };
  
  private final List<ProductBulkUtilColumn> columnsList =
      new ArrayList<ProductBulkUtilColumn>();
  
  private final List<ProductBulkUtilColumn> requiredColumnsList =
      new ArrayList<ProductBulkUtilColumn>();
  
  FileWriter writer;
  
  public static void main(final String[] args) throws IOException,
      WriteException {
  
    final ProductBulkEditCsvGeneratorUtil myUtil =
        new ProductBulkEditCsvGeneratorUtil();
    myUtil.generateCsv();
  }
  
  public ProductBulkEditCsvGeneratorUtil() {
  
  }
  
  public void generateCsv() throws IOException, WriteException {
  
    for (final String type : csvNames) {
      init(type);
      writeHeaders();
      writeData(type);
      close();
    }
  }
  
  private void close() throws IOException, WriteException {
  
    writer.flush();
    writer.close();
  }
  
  private void writeHeaders() throws WriteException, IOException {
  
    for (final ProductBulkUtilColumn column : requiredColumnsList) {
      writer.append(column.getHeaderValue() + ",");
    }
    for (int i = 0; i < (columnsList.size() - 1); i++) {
      writer.append(columnsList.get(i).getHeaderValue() + ",");
    }
    writer.append(columnsList.get(columnsList.size() - 1).getHeaderValue()
        + "\n");
  }
  
  private void writeData(final String type) throws WriteException, IOException {
  
    if (type.equals("batchProductUpdates")) {
      for (int sku = 1; sku <= NUM_SKU; sku++) {
        for (int nonNullColumnCount = 0; nonNullColumnCount < columnsList
            .size(); nonNullColumnCount++) {
          writer.append("0cac8d,testsku" + sku + ",");
          for (int columnCount = 0; columnCount < columnsList.size(); columnCount++) {
            final ProductBulkUtilColumn column = columnsList.get(columnCount);
            final String s1 =
                (columnCount == nonNullColumnCount) ? column
                    .getNextStringItemFromCount(sku) : " ";
            final String s2 =
                (columnCount == (columnsList.size() - 1)) ? "\n" : ",";
            writer.append(s1 + s2);
          }
        }
      }
    }
    if (type.equals("batchHighlightsUpdate")) {
      for (int sku = 1; sku <= NUM_SKU; sku++) {
        writer.append("0cac8d,testsku" + sku + ",");
        writer
            .append(columnsList.get(0).getNextStringItemFromCount(sku) + "\n");
      }
    }
    if (type.equals("batchFiltersUpdate")) {
      for (int sku = 1; sku <= NUM_SKU; sku++) {
        writer.append("0cac8d,testsku" + sku + ",");
        for (int columnCount = 0; columnCount < columnsList.size(); columnCount++) {
          final String s =
              (columnCount == (columnsList.size() - 1)) ? "\n" : ",";
          writer.append(columnsList.get(columnCount)
              .getNextStringItemFromCount(columnCount) + s);
        }
      }
    }
  }
  
  private void init(final String type) throws IOException {
  
    writer = new FileWriter(OUTPUT_FOLDERNAME + type + ".csv");
    
    requiredColumnsList.clear();
    columnsList.clear();
    
    requiredColumnsList.add(new ProductBulkUtilColumn("VENDOR_CODE",
        "vendor_code", "0cac8d"));
    requiredColumnsList.add(new ProductBulkUtilColumn("VENDOR_SKU", "sku",
        false));
    
    if (type.equals("batchProductUpdates")) {
      columnsList.add(new ProductBulkUtilColumn("END_TIME", "end_time",
          "25_12_2012_03_00_00"));
      columnsList.add(new ProductBulkUtilColumn("START_TIME", "start_time",
          "10_07_2012_017_00_00"));
      columnsList.add(new ProductBulkUtilColumn("MRP", "mrp", "999"));
      columnsList.add(new ProductBulkUtilColumn("SELLING_PRICE",
          "selling_price", "99"));
      columnsList.add(new ProductBulkUtilColumn("VENDOR_PRICE", "vendor_price",
          "499"));
      columnsList.add(new ProductBulkUtilColumn("SD_COMMISSION",
          "sd_commission", "5"));
      columnsList.add(new ProductBulkUtilColumn("COURIER_COST", "courier_cost",
          "30"));
      columnsList.add(new ProductBulkUtilColumn("SHIPPING_GROUP",
          "shipping_group", "COD-PRODUCTS", "STD"));
      columnsList.add(new ProductBulkUtilColumn("COURIER_COST_BORNE_BY",
          "courier_cost_bourne_by", "SNAPDEAL", "VENDOR"));
      columnsList.add(new ProductBulkUtilColumn("PRODUCT_NAME", "product_name",
          "Trouser", " Full Sleeves T-Shirt", "Jeans Fixed Waist"));
      columnsList.add(new ProductBulkUtilColumn("SUB_TITLE", "sub_title",
          "Stylish trousers for boys",
          "Stylish full-sleeve t-shirts for girls",
          "Fashionable clothing for kids"));
      columnsList.add(new ProductBulkUtilColumn("PRODUCT_DESCRIPTION",
          "product_description", "test description 1", "test description 2",
          "test description 3"));
      columnsList.add(new ProductBulkUtilColumn("COURIER_ARRANGEMENT_BY",
          "courier_arrangement_by", "SNAPDEAL", "VENDOR"));
      columnsList.add(new ProductBulkUtilColumn("PRODUCT_CATEGORY",
          "product_category", "computers"));
      columnsList.add(new ProductBulkUtilColumn("SIZE_CHART_ID",
          "size_chart_id", "10", "20"));
    }
    
    if (type.equals("batchHighlightsUpdate")) {
      columnsList.add(new ProductBulkUtilColumn("HIGHLIGHTS_UPDATE",
          "highlights", "test1$test2$test3$test4$test5"));
    }
    
    if (type.equals("batchFiltersUpdate")) {
      columnsList.add(new ProductBulkUtilColumn("PRODUCT_CATEGORY",
          "product_category", "computers"));
      columnsList.add(new ProductBulkUtilColumn("FN1", "fn1", "Brand"));
      columnsList.add(new ProductBulkUtilColumn("FV1", "fv1", "test_brand"));
      columnsList.add(new ProductBulkUtilColumn("FN2", "fn2", "Price"));
      columnsList.add(new ProductBulkUtilColumn("FV2", "fv2", "test_price"));
    }
  }
}
