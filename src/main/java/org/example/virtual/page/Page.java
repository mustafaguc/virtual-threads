package org.example.virtual.page;

import java.net.URI;
import java.util.List;
/**
 * A page is a URI, a title, a list of keywords, and a list of links.
 */
public record Page(URI url, String title, List<String> keywords, List<String> links) {
}
