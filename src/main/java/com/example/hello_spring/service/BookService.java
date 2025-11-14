package com.example.hello_spring.service;

import com.example.hello_spring.dto.request.BookRequestDTO;
import com.example.hello_spring.dto.response.BookResponseDTO;
import com.example.hello_spring.entity.Book;
import com.example.hello_spring.exception.ResourceNotFoundException;
import com.example.hello_spring.repository.BookRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookService {
    private final BookRepository bookRepository;

    private BookResponseDTO convertToDTO(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear()
        );
    }

    private Book findId(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
    }

    // create a new book
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        log.info("Creating book: {}", bookRequestDTO.title());
        Book book = Book.builder()
                .title(bookRequestDTO.title())
                .author(bookRequestDTO.author())
                .isbn(bookRequestDTO.isbn())
                .publishedYear(bookRequestDTO.publishedYear())
                .build();
        return convertToDTO(bookRepository.save(book));
    }

    // get a book by id
    @Transactional
    public BookResponseDTO getBookById(Long id) {
        log.info("Getting book with id: {}", id);
        return convertToDTO(findId(id));
    }

    // get alls books
    @Transactional
    public Iterable<BookResponseDTO> getAllBooks() {
        log.info("Getting all books");
        return bookRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // update book by id
    @Transactional
    public BookResponseDTO updateBooks(Long id, BookRequestDTO requestDTO) {
        log.info("Updating book with id: {}", id);
        Book book = findId(id);
        book.setTitle(requestDTO.title());
        book.setAuthor(requestDTO.author());
        book.setIsbn(requestDTO.isbn());
        book.setPublishedYear(requestDTO.publishedYear());

        return convertToDTO(bookRepository.save(book));
    }

    // delete by id
    @Transactional
    public String deleteBooks(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book with id " + id + " not found");
        }
        log.info("Deleting book with id: {}", id);
        bookRepository.deleteById(id);
        return "Book with id " + id + " deleted";
    }
}
