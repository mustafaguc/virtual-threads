# Virtual Threads Application

This is a Java application that uses Spring Boot and virtual threads to scrape web pages.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 21 or higher
- Gradle

### Installing

1. Clone the repository
2. Navigate to the project directory
3. Run `./gradlew build` to build the project

## Running the Application

Run `./gradlew bootRun` to start the application.

## API Endpoints

- `GET /scrape?url={url}`: Scrapes the page at the given URL and returns the scraped page.
- `GET /scrapePagesIn?url={url}`: Scrapes the page at the given URL and all the pages linked from it. Returns a message indicating the number of links discovered.

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Jsoup](https://jsoup.org/)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details