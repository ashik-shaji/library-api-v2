package com.example.library_v2.controller;

import com.example.library_v2.entity.Author;
import com.example.library_v2.entity.Book;
import com.example.library_v2.entity.Publisher;
import com.example.library_v2.repository.AuthorRepository;
import com.example.library_v2.repository.BookRepository;
import com.example.library_v2.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@RestController
public class LibraryController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public LibraryController(BookRepository bookRepository,
                             AuthorRepository authorRepository,
                             PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    @PostMapping(path = "addBook")
    public void addBook(@RequestBody Book book) {
        bookRepository.save(book);
    }

    @GetMapping(path = "getBooks")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @GetMapping(path = "getAuthors")
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping(path = "getPublishers")
    public List<Publisher> getPublishers() {
        return publisherRepository.findAll();
    }

    @DeleteMapping(path = "{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId) {
        boolean exists = bookRepository.existsById(bookId);
        if (!exists) {
            throw new IllegalStateException("Book with id " + bookId + " does not exists");
        }
        bookRepository.deleteById(bookId);
    }

    @PutMapping(path = "{bookId}")
    @Transactional
    public void updateBookName(
            @PathVariable("bookId") Long bookId,
            @RequestParam String bookName,
            @RequestParam String publisherName
    ) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book with id " + bookId + " does not exists"));
        if (bookName != null && bookName.length() > 0 && !Objects.equals(book.getBookName(), bookName)) {
            book.setBookName(bookName);
        }
        if (publisherName != null && publisherName.length() > 0 && !Objects.equals(book.getPublisher().getPublisherName(), publisherName)) {
            book.getPublisher().setPublisherName(publisherName);
        }
    }
}
