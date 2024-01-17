package org.example.virtual.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Optional;

/**
 * Page scraper.
 */
public class PageScraper {

    RestClient restClient = createRestClient();

    private static RestClient createRestClient() {
        return RestClient.create(new RestTemplate(simpleClientHttpRequestFactory()));
    }

    /**
     * Create a {@link SimpleClientHttpRequestFactory} that follows redirects.
     *
     * @return a {@link SimpleClientHttpRequestFactory} that follows redirects
     */
    private static SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        return new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
                connection.setInstanceFollowRedirects(true);
            }
        };
    }

    /**
     * Download the page at the given URL.
     *
     * @param url the URL of the page to download
     * @return the content of the page
     */
    private String downloadPage(String url) {
        return restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }


    /**
     * Parse the given HTML content. If the content is {@code null}, an empty document is returned.
     *
     * @param htmlContent the HTML content to parse
     * @return the parsed document
     */
    private Document parse(String htmlContent) {
        return Jsoup.parse(Optional.ofNullable(htmlContent).orElse("<body></body>"));
    }

    /**
     * Scrape the page at the given URL. Then return a {@link Page} object containing the scraped data.
     *
     * @param url the URL of the page to scrape
     * @return the scraped page
     */
    public Page scrape(String url) {
        Document document = parse(downloadPage(url));
        return new Page(
                URI.create(url),
                document.title(),
                document.select("meta[name=keywords]").eachAttr("content"),
                document.selectXpath("//a[string-length(@href) > 0]")
                        .stream()
                        .map(element -> getHref(url, element.attr("href")))
                        .toList()
        );
    }

    /**
     * Get the absolute URL of the given href. If the href is already absolute, it is returned as is.
     *
     * @param url  the URL of the page
     * @param href the href to get the absolute URL of
     * @return the absolute URL of the given href
     */
    private static String getHref(String url, String href) {
        if (!href.startsWith("http")) {
            URI uri = URI.create(url);
            return uri.getScheme() + "://" + uri.getHost() + (!href.startsWith("/") ? "/" : "") + href;
        }
        return href;
    }
}
