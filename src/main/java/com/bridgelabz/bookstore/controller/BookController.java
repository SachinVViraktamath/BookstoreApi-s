package com.bridgelabz.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.bookstore.dto.BookDto;
import com.bridgelabz.bookstore.dto.ReviewDto;
import com.bridgelabz.bookstore.entity.Book;
import com.bridgelabz.bookstore.entity.Reviews;
import com.bridgelabz.bookstore.entity.Users;
import com.bridgelabz.bookstore.exception.BookException;
import com.bridgelabz.bookstore.exception.SellerException;
import com.bridgelabz.bookstore.exception.UserException;
import com.bridgelabz.bookstore.response.Response;
import com.bridgelabz.bookstore.service.BookService;
import com.bridgelabz.bookstore.service.ElasticSearchService;
import com.bridgelabz.bookstore.service.UserWishListService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookController {

	@Autowired
	private ElasticSearchService elasticService;

	@Autowired
	private BookService bookService;

	@Autowired
	private UserWishListService userWishListService;
	
	
	@ApiOperation(value = "Api diplay all books",response = Iterable.class)
	@GetMapping("/displayAll")
	public ResponseEntity<Response> displayAllBooks() throws BookException {
		List<Book> books = bookService.displayBooks();		
			return ResponseEntity.ok().body(new Response(HttpStatus.FOUND, "books for user displayed", books));
		
	}


	@ApiOperation(value = "Api diplay single book",response = Iterable.class)
	@GetMapping("/display_single_book")
	public ResponseEntity<Response> displayParticularBook(@RequestParam("id") Long id) throws BookException {
		Book book = bookService.displaySingleBook(id);		
			return ResponseEntity.ok().body(new Response(HttpStatus.FOUND, "Particular book not displayed", book));
	}

	
	@ApiOperation(value = "Api search the book",response = Iterable.class)
	@GetMapping("/search")
	public ResponseEntity<Response> searchBooks(@RequestParam("title") String title) {
		List<Book> books = null;
		try {
			books = elasticService.getBookByTitleAndAuthor(title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (books != null)
			return ResponseEntity.ok().body(new Response(HttpStatus.FOUND, "Books searched and Found", books));
		else
			return ResponseEntity.badRequest()
					.body(new Response(HttpStatus.NOT_FOUND, "Books searched and not Found", books));

	}

	@ApiOperation(value = "Api for sort books in asc order by price",response = Iterable.class)
	@GetMapping("/sortbyprice/Asc")
	public ResponseEntity<Response> sortByPriceLowtoHigh() throws BookException {
		List<Book> books = bookService.sortByPriceAsc();		
			return ResponseEntity.ok().body(new Response(HttpStatus.FOUND, "Book by Price low to high Found", books));
		
	}
	@ApiOperation(value = "Api for sort books in desc order by price",response = Iterable.class)
	@GetMapping("/sortbyprice/Desc")
	public ResponseEntity<Response> sortByPriceHightoLow() throws BookException {
		List<Book> books = bookService.sortByPriceDesc();	
			return ResponseEntity.ok().body(new Response(HttpStatus.FOUND, "Book by Price high to low Found", books));
		

	}

	@ApiOperation(value = "Api for sort book by new arrivals",response = Iterable.class)
	@GetMapping("/sortbynewest")
	public ResponseEntity<Response> sortByNewestArrivals() throws BookException {
		List<Book> books = bookService.sortByNewest();		
			return ResponseEntity.ok().body(new Response(HttpStatus.FOUND, "Book by Newest Arrivals Found", books));
		
	}
	/* API for seller adding books for approval */
	@PostMapping("/addBook")
	@ApiOperation("seller adding books")
	public ResponseEntity<Response> addBook(@RequestBody BookDto dto, @RequestHeader("token") String token)
			throws SellerException, Exception {

		Book addBook = bookService.addBook(token, dto);

		return ResponseEntity.ok()
				.body(new Response(HttpStatus.ACCEPTED, "verification mail has send successfully", addBook));

	}
	@PutMapping ("/updateBook")
	@ApiOperation("updating book details")
	public ResponseEntity<Response> updateBook(@RequestBody BookDto dto, @RequestHeader("token") String token,@RequestParam Long bookId) throws BookException{
		Book book=bookService.updateBook(token, bookId, dto);

		return ResponseEntity.ok()
				.body(new Response(HttpStatus.ACCEPTED, "bookDetails are verified", book));
}
	

	@ApiOperation(value = "Api for rating and review the book")
	@PutMapping("/ratingreview")
	public ResponseEntity<Response> writeReview(@RequestBody ReviewDto review,@RequestHeader(name="token") String token, @RequestParam Long bookId) throws UserException, BookException{
		bookService.writeReviewAndRating(token, review, bookId);

		return ResponseEntity.ok()
				.body(new Response(HttpStatus.ACCEPTED, "bookDetails are verified", review));

	}
	@ApiOperation(value = "Api for view all rating and review")
	@GetMapping("/viewratings")
	public ResponseEntity<Response> getBookRatingAndReview(@RequestParam Long bookId){
		List<Reviews> review= bookService.getRatingsOfBook(bookId);
			return	ResponseEntity.ok()
				.body(new Response(HttpStatus.ACCEPTED, "bookDetails are verified", review));
}
	
	
	
	@ApiOperation(value = "Adding the books to the Whishlist",response = Iterable.class)
	@PostMapping(value="/add_books_WhishList/{token}")
	public ResponseEntity<Response> addBooksToWhilist(@PathVariable("token") String token,@RequestParam("bookId") long bookId) throws Exception {
		    Users whishlist = userWishListService.addBooksTiWishList(token, bookId);
		    		
			return ResponseEntity.ok()
					.body(new Response(HttpStatus.ACCEPTED, "bookDetails are verified", whishlist));
  	
	}
	@ApiOperation(value = "Getting the books from Whishlist",response = Iterable.class)
	@GetMapping(value="/books_cart/{token}")
	public ResponseEntity<Response> getBooksfromCart(@PathVariable("token") String token) throws Exception {
		    List<Book> whishlist = userWishListService.viewAllBooksFromWishList(token);
		    		
		    return ResponseEntity.ok()
					.body(new Response(HttpStatus.ACCEPTED, "bookDetails are verified", whishlist));
	}
	
	@ApiOperation(value = "Removing the books to the Whishlist",response = Iterable.class)
	@PostMapping(value="/remove_books_WhishList/{token}")
	public ResponseEntity<Response> removeBooksToWhilist(@PathVariable("token") String token,@RequestParam("bookId") long bookId) throws Exception {
		    Users whishlist = userWishListService.removeBooksTiWishList(token, bookId);
		    		
			return ResponseEntity.ok()
					.body(new Response(HttpStatus.ACCEPTED, "bookDetails are verified", whishlist));
 	
	}
	
	
}
