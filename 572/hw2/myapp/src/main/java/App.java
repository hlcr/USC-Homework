import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class App
{

    private static void initialization(){

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("D:/fetch_DailyMail.csv");
            outputStream.write("url,statue code\n".getBytes());
            outputStream.flush();
            outputStream.close();
            outputStream = new FileOutputStream("D:/visit_DailyMail.csv");
            outputStream.write("url,size,number of outlinks,file type\n".getBytes());
            outputStream.flush();
            outputStream.close();
            outputStream = new FileOutputStream("D:/url_DailyMail.csv");
            outputStream.write("url,indicator\n".getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        initialization();
        final int MAX_CRAWL_DEPTH = 8;
        final int NUMBER_OF_CRAWELRS = 20;
        final String CRAWL_STORAGE = "E:/data/";

        /*
         * Instantiate crawl config
         */
        CrawlConfig config = new CrawlConfig();
//        config.setIncludeHttpsPages(true);

        config.setCrawlStorageFolder(CRAWL_STORAGE);
        config.setMaxDepthOfCrawling(MAX_CRAWL_DEPTH);
        config.setIncludeBinaryContentInCrawling(true);
        config.setMaxPagesToFetch(20000);

        /*
         * Instantiate controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * Add seed URLs
         */
//        controller.addSeed("https://i.dailymail.co.uk/1s/2019/02/06/23/9490960-0-image-a-18_1549496413803.jpg");
        controller.addSeed("https://www.dailymail.co.uk/news/");

        /*
         * Start the crawl.
         */
        controller.start(MyCrawler.class, NUMBER_OF_CRAWELRS);
    }
}