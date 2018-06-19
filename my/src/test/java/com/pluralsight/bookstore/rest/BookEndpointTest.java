package com.pluralsight.bookstore.rest;


import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.model.Language;
import com.pluralsight.bookstore.repository.BookRepository;
import com.pluralsight.bookstore.util.IsbnGenerator;
import com.pluralsight.bookstore.util.NumberGenerator;
import com.pluralsight.bookstore.util.TextUtil;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;

@RunWith(Arquillian.class)
@RunAsClient
public class BookEndpointTest {

	private Response response;
	
	@Deployment(testable = false)
    public static Archive<?> createDeploymentPackage() {

        return ShrinkWrap.create(JavaArchive.class)
            .addClass(Book.class)
            .addClass(Language.class)
            .addClass(TextUtil.class)
            .addClass(NumberGenerator.class)
            .addClass(IsbnGenerator.class)
            .addClass(BookRepository.class)
            .addClass(BookEndpoint.class)
            .addClass(JAXRSConfiguration.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }
	
	
	@Test
	//@InSequence(1)
	public void shouldGetNoBook(@ArquillianResource URL baseURL) {
		//System.out.println(baseURL.toString());
		WebTarget webTarget = ClientBuilder.newClient().target(baseURL.toString()).path("bookstore-back/api/books/");
		response = webTarget.path("/count").request().get();
		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
		
		response = webTarget.request(MediaType.APPLICATION_JSON).get();
		//System.out.println(response.readEntity(String.class));
		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}
	
	/*@Test
	@InSequence(2)
	public void shouldGetNoBookFromFind(@ArquillianResource URL baseURL) {
		response = ClientBuilder.newClient().target(baseURL.toString()).path("bookstore-back/api/books/").request(MediaType.APPLICATION_JSON).get();
		//System.out.println(response.readEntity(String.class));
		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}*/
	
	
	@Test
	//@InSequence(20)
	public void createBook(@ArquillianResource() URL baseURL) {
		
//		// test counting books
//		Response response = webTarget.path("count").request().get();
//		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
//		
//		//Test find all
//		response = webTarget.request(MediaType.APPLICATION_JSON).get();
//		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
//		
        // Creates a book
        Book book = new Book("isbn", "title", 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description");
        response = ClientBuilder.newClient().target(baseURL.toString()).path("bookstore-back/api/books/").request(MediaType.APPLICATION_JSON).post(Entity.entity(book, MediaType.APPLICATION_JSON));
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		
	}
	
	
	
	
}
