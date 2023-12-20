package com.phyllipesa.erudio.unittests.mockito.services;

import com.phyllipesa.erudio.data.vo.v1.BookVO;
import com.phyllipesa.erudio.exceptions.RequiredObjectIsNullException;
import com.phyllipesa.erudio.mapper.EntityMapper;
import com.phyllipesa.erudio.models.Book;
import com.phyllipesa.erudio.repositories.BookRepository;
import com.phyllipesa.erudio.services.BookService;
import com.phyllipesa.erudio.unittests.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @Mock
    EntityMapper entityMapper;

    @BeforeEach
    void setUpMocks() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Book> list = input.mockEntityList();
        List<BookVO> voList = input.mockVOList();

        when(repository.findAll()).thenReturn(list);
        when(entityMapper.parseListObject(list, BookVO.class)).thenReturn(voList);

        var books = service.findAll();

        assertNotNull(books);
        assertEquals(14, books.size());

        var bookOne = books.get(1);
        assertNotNull(bookOne.getKey());
        assertNotNull(bookOne.getLinks());
        assertTrue(bookOne.toString().contains("links: [</api/book/1>;rel=\"self\"]"));
        assertEquals("some author Test1", bookOne.getAuthor());
        assertNotNull(bookOne.getlaunchDate());
        assertEquals(25D, bookOne.getPrice());
        assertEquals("some title Test1", bookOne.getTitle());

        var bookFour = books.get(4);
        assertNotNull(bookFour.getKey());
        assertNotNull(bookFour.getLinks());
        assertTrue(bookFour.toString().contains("links: [</api/book/4>;rel=\"self\"]"));
        assertEquals("some author Test4", bookFour.getAuthor());
        assertNotNull(bookFour.getlaunchDate());
        assertEquals(25D, bookFour.getPrice());
        assertEquals("some title Test4", bookFour.getTitle());

        var bookSeven = books.get(7);
        assertNotNull(bookSeven.getKey());
        assertNotNull(bookSeven.getLinks());
        assertTrue(bookSeven.toString().contains("links: [</api/book/7>;rel=\"self\"]"));
        assertEquals("some author Test7", bookSeven.getAuthor());
        assertNotNull(bookSeven.getlaunchDate());
        assertEquals(25D, bookSeven.getPrice());
        assertEquals("some title Test7", bookSeven.getTitle());
    }

    @Test
    void findById() {
        Book entity = input.mockEntity();
        BookVO vo = input.mockVO();
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(entityMapper.parseObject(entity, BookVO.class)).thenReturn(vo);

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/1>;rel=\"self\"]"));
        assertEquals("some author Test0", result.getAuthor());
        assertNotNull(result.getlaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("some title Test0", result.getTitle());
    }

    @Test
    void create() {
        Book entity = input.mockEntity(1);
        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);

        when(entityMapper.parseObject(vo, Book.class)).thenReturn(persisted);
        when(repository.save(entity)).thenReturn(persisted);
        when(entityMapper.parseObject(persisted, BookVO.class)).thenReturn(vo);

        var result = service.create(vo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/1>;rel=\"self\"]"));
        assertEquals("some author Test1", result.getAuthor());
        assertNotNull(result.getlaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("some title Test1", result.getTitle());
    }

    @Test
    void createWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);
        when(entityMapper.parseObject(persisted, BookVO.class)).thenReturn(vo);

        var result = service.update(vo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/1>;rel=\"self\"]"));
        assertEquals("some author Test1", result.getAuthor());
        assertNotNull(result.getlaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("some title Test1", result.getTitle());
    }

    @Test
    void updateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Book entity = input.mockEntity();
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.delete(1L);
    }
}