package com.pluralsight.bookstore.rest;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.repository.BookRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/books")
@Api("Book")
public class BookEndpoint {

	@Inject
	private BookRepository bookRepository;

	/**
	 * @param id
	 * @return
	 * @see com.pluralsight.bookstore.repository.BookRepository#find(java.lang.Long)
	 */
	@GET
	@Path("/{id : \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "return a book given an identifier", response = Book.class)
	@ApiResponses({
		@ApiResponse(code = 200, message = "Book Found"),
		@ApiResponse(code = 404, message= "Book Not Found"),
		@ApiResponse(code = 400, message= "Invalid input. ID cannot be lower than 1")
		
	})
	public Response getBook(@PathParam("id") @Min(1) Long id) {
		Book book = bookRepository.find(id);
		if( book == null) return Response.status(Response.Status.NOT_FOUND).build();
		return Response.ok(book).build();
	}

	/**
	 * @return
	 * @see com.pluralsight.bookstore.repository.BookRepository#findAll()
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Return all the books")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Found Books"),
		@ApiResponse(code = 204, message = "No Books found")
	})
	public Response getBooks() {
		List<Book> books = bookRepository.findAll();
		//System.out.println(books.toString());
		if( books == null || books.isEmpty() || books.size() == 0 || books.get(0) == null ) 
			return Response.noContent().build();
		return Response.ok(books).build();
	}

	/**
	 * @return
	 * @see com.pluralsight.bookstore.repository.BookRepository#countAll()
	 */
	@GET
	@Path("/count")
	@ApiOperation(value = "Return number of books", response = Long.class)
	@ApiResponses({
		@ApiResponse(code = 200, message = "Found number of Books"),
		@ApiResponse(code = 204, message = "No Books in the database")
	})
	public Response countBooks() {
		Long noOfBooks = bookRepository.countAll();
		if(noOfBooks == 0) return Response.noContent().build();
		return Response.ok(noOfBooks).build();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return bookRepository.hashCode();
	}

	/**
	 * @param book
	 * @return
	 * @see com.pluralsight.bookstore.repository.BookRepository#create(com.pluralsight.bookstore.model.Book)
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create book given a JSON represenation", response = Book.class)
	@ApiResponses({
		@ApiResponse(code = 201, message = "Created book"),
		@ApiResponse(code = 415, message = "Format Not JSON")
	})
	public Response createBook(Book book, @Context UriInfo uriInfo) {
		book =  bookRepository.create(book);
		URI createdURI = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build();
		return Response.created(createdURI).build();
	}

	/**
	 * @param id
	 * @see com.pluralsight.bookstore.repository.BookRepository#delete(java.lang.Long)
	 */
	@DELETE
	@Path("/{id : \\d+}")
	@ApiOperation(value = "Delete book given its ID", response = Book.class)
	@ApiResponses({
		@ApiResponse(code = 204, message = "Book has been deleted"),
		@ApiResponse(code = 400, message = "Invalid input. ID cannot be lower than 1"),
		@ApiResponse(code = 500, message = "Book Not Found")
	})
	public Response deleteBook(@PathParam("id") @Min(1) Long id) {
		bookRepository.delete(id);
		return Response.noContent().build();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return bookRepository.equals(obj);
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return bookRepository.toString();
	}

}
