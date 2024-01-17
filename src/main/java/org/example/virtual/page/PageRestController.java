package org.example.virtual.page;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * A REST controller that exposes a GET endpoint to scrape a page.
 */
@RestController
public class PageRestController {

    private static final Logger LOGGER = Logger.getLogger(PageRestController.class.getName());

    PageScraper scraper = new PageScraper();

    /**
     * Scrape a page.
     *
     * @param url the URL of the page to scrape
     * @return the scraped page
     */
    @GetMapping("/scrape")
    public Page scrapePage(@RequestParam String url) {
        LOGGER.info("Starting to scrape page : " + url);
        Page page = null;
        try {
            page = scraper.scrape(url);
            LOGGER.info("Page scraped in " + Thread.currentThread() + " thread : " + page.url());
        } catch (Exception e) {
            LOGGER.warning("Page could not scraped : " + url);
        }
        return page;
    }

    /**
     * Scrape a page and all the pages linked from it. This method uses a virtual thread per task executor.
     *
     * @param url the URL of the page to scrape
     * @return a message indicating the number of links discovered
     */
    @GetMapping("/scrapePagesIn")
    public String scrapePagesIn(@RequestParam String url) {
        Page scrapedPage = scrapePage(url);

        try (ExecutorService myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            scrapedPage
                    .links()
                    .forEach(link -> myExecutor.execute(() -> scrapePage(link)));
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
        return "Discovered " + scrapedPage.links().size() + " links, inspect server logs to see the progress of the scraping.";
    }
}
