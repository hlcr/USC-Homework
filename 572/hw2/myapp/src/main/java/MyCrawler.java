
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler{

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(jpeg|gif|jpg|png|tiff|raw|html|doc|pdf)|/)$");


    public static boolean is_valid(WebURL webUrl){
//        if(!url.contains("dailymail.co.uk/") || !url.contains("://")){
//            return false;
//        }
//        url = url.substring(url.indexOf("://")+3, url.length());
//        url = url.substring(0,url.indexOf('/'));
//        return url.contains("dailymail.co.uk");
        return webUrl.getDomain().equals("dailymail.co.uk");
    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        super.handlePageStatusCode(webUrl, statusCode, statusDescription);
        Page page = new Page(webUrl);
        String url = page.getWebURL().getURL();
        // fetch.csv should have urls from news site domain only
        if(is_valid(page.getWebURL())){
            page.setStatusCode(statusCode);
            WriteData wd1 = new WriteData(page,1,url);
            wd1.start();
        }
    }

    /**
     * Specify whether the given url should be crawled or not based on
     * the crawling logic. Here URLs with extensions css, js etc will not be visited
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
//        System.out.println("shouldVisit: " + url.getURL().toLowerCase());



        String href = url.getURL().toLowerCase();

        boolean result = FILTERS.matcher(href).matches();
        if(result){
            result = is_valid(url);
        }

//        if(result)
//            System.out.println("URL Should Visit");
//        else
//            System.out.println("URL Should not Visit");

        return result;
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by the program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        int code = page.getStatusCode();
//        System.out.println("URL: " + url);
        page.getContentType();

        if (page.getParseData() instanceof HtmlParseData) {
            WriteData wd1 = new WriteData(page, 3, url);
            wd1.start();
        }


        if(code>=200 && code < 300){
            WriteData wd2 = new WriteData(page,2,url);
            wd2.start();
        }

    }
}