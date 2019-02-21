import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

class Write{
    static final String filename1 = "D:/fetch_DailyMail.csv";
    static final String filename2 = "D:/visit_DailyMail.csv";
    static final String filename3 = "D:/url_DailyMail.csv";



    public static void write_csv(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    static private ArrayList<ArrayList<Object>> write_file1(Page page){
        ArrayList<ArrayList<Object>> t_result = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> result = new ArrayList<Object>();
        String url = page.getWebURL().getURL();
        result.add(url);
        int code = page.getStatusCode();
        result.add(code);
        t_result.add(result);
        return t_result;

    }

    static private ArrayList<ArrayList<Object>> write_file2(Page page){
        ArrayList<Object> result = new ArrayList<Object>();
        ArrayList<ArrayList<Object>> t_result = new ArrayList<ArrayList<Object>>();

        String web_url = page.getWebURL().getURL();
        result.add(web_url);

        int size = page.getContentData().length;
        result.add(size);

        int size_links = 0;
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            size_links = links.size();
        }
        result.add(size_links);

        String content_type = page.getContentType();
        // ignore the rest
        if(content_type.contains("text/html")){
            content_type = "text/html";
        }
        result.add(content_type);

        t_result.add(result);

        return t_result;

    }


    // put it in should visit
    static private ArrayList<ArrayList<Object>> write_file3(Page page){

        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            for(WebURL link:links){
                ArrayList<Object> temp_list = new ArrayList<Object>();
                String url = link.getURL();
                temp_list.add(url);
                boolean is_site = MyCrawler.is_valid(link);
                if(is_site){
                    temp_list.add("OK");
                }else{
                    temp_list.add("N_OK");
                }
                result.add(temp_list);
            }

        }

        return result;

    }


    private static void write(Page page, String url, int type){
            String filepath = "";
            ArrayList<ArrayList<Object>> write_list = null;

            // decide which file should be wrote
            if(type==1){
                filepath = filename1;
                write_list = write_file1(page);
            }else if (type == 2){
                filepath = filename2;
                write_list = write_file2(page);

            }else{
                filepath = filename3;
                write_list = write_file3(page);
            }

//            System.out.print("writing:" + filepath);


            String content = "";
            // write data
            for(ArrayList<Object> row_list: write_list){
                for (Object item:row_list){
                    content += String.valueOf(item);
                    content += ",";

                }
                content = content + "\r\n";

            }
//            System.out.print(content);

            write_csv(filepath,content);

            // close input stream

//            System.out.print("finished:" + filepath);

    }


    static public synchronized void Write_Excel(int type, String url, Page page) {
        write(page,url,type);
    }
}


public class WriteData extends Thread{

    private Page p;
    private int t;
    private String u;

    WriteData(Page page, int type, String url){
        p = page;
        t = type;
        u = url;
    }

    @Override
    public void run() {
        super.run();
        Write.Write_Excel(t,u,p);

    }
}



