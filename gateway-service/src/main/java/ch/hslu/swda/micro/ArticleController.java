package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.article.BookDTO;
import ch.hslu.swda.model.log.LogEntry;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
@Tag(name = "ArticleRegistry")

@Controller("/api/v1/articles")
public class ArticleController {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    private final Integer PAGESIZE = 100;


    @Get("/books")
    @Operation(
            summary = "Get paginated list of books",
            description = "Fetches a paginated list of books based on the provided page and size parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of books"),
                    @ApiResponse(responseCode = "400", description = "Invalid page or size parameter"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public HttpResponse<?> getBooks(
            @Parameter(
                    description = "The page number to retrieve (zero-based index). Must be a non-negative integer.",
                    example = "0",
                    required = true
            )
            @QueryValue("page") final Integer page,
            @Parameter(
                    description = "The number of books per page. Must be between 1 and 100.",
                    example = "10",
                    required = true
            )
            @QueryValue("size") Integer size
    ) {
        // Validate the input parameters
        if (page == null || page < 0) {
            return HttpResponse.badRequest("Page parameter must be a non-negative integer.");
        }
        if (size == null || size < 1 || size > PAGESIZE) {
            return HttpResponse.badRequest("Size parameter must be between 1 and 100.");
        }

        // Adjust size within valid bounds

        List<BookDTO> books = new ArrayList<>();
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "articles.books.get", String.format("{\"page\":%d, \"size\":%d}", page, size));
            LOG.info("Got reply from bus: [{}]. Sending reply to client", reply);
            books = mapper.readValue(reply, Argument.listOf(BookDTO.class));
            return HttpResponse.ok(books);
        } catch (IOException | TimeoutException | InterruptedException e) {
            LOG.error("Error while fetching books: {}", e.getMessage());
            return HttpResponse.serverError("An error occurred while processing the request. Please try again later.");
        }
    }






}
