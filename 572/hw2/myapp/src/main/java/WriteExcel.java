import java.io.*;
import java.util.ArrayList;

import edu.uci.ics.crawler4j.crawler.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 写入Excel文件的方法（写表头，写数据）
 * @author lmb
 * @date 2017-3-16
 *
 */
public class WriteExcel {

    public static void write(int r) {
        try {
            String filepath = "D://fetch_DailyMail.xlsx";
            ArrayList<Object> write_list = new ArrayList<Object>();
            write_list.add(0);
            write_list.add("1232321");
            int num_row = 0;

            // open file
            FileInputStream fis = new FileInputStream(filepath);
            Workbook workbook = new XSSFWorkbook(fis);
            // create a sheet
            Sheet sheet = workbook.getSheetAt(0);
            sheet.setColumnWidth(0, 20000);
            Row row = sheet.createRow(r);

            // write data
            for (int j = 0; j < write_list.size(); j++) {
                Cell cell = row.createCell(j);
                if (write_list.get(j) instanceof Integer) {
                    cell.setCellValue((Integer) write_list.get(j));
                } else {
                    cell.setCellValue((String) write_list.get(j));
                }

            }

            // close input stream
            FileOutputStream outputStream = new FileOutputStream(filepath);
            workbook.write(outputStream);
            workbook.close();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialization(){
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("sheet1");


        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("D:/fetch_DailyMail.xlsx");
            workbook.write(outputStream);
            outputStream = new FileOutputStream("D:/visit_DailyMail.xlsx");
            workbook.write(outputStream);
            outputStream = new FileOutputStream("D:/url_DailyMail.xlsx");
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //主函数
    public static void main(String[] args) {
        WriteExcel we = new WriteExcel();
        we.initialization();
        for(int j=0; j < 10; j++)
            write(j);


    }
}


