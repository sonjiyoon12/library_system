package my_test.dao;

import my_test.dto.Book;
import my_test.utii.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    // 새 도서를 데이터베이스에 추가
    public void addBook(Book book) throws SQLException{
        String sql = "INSERT INTO books (title, author, publisher, publication_year, isbn) \n" +
                "VALUES (?, ?, ?, ?, ?) ";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,book.getTitle());
            pstmt.setString(2,book.getAuthor());
            pstmt.setString(3,book.getPublisher());
            pstmt.setInt(4,book.getPublicationYear());
            pstmt.setString(5,book.getIsbn());
            pstmt.executeUpdate();
        }
    }

    // 모든 도서 목록 조회
    public List<Book> getAllBooks() throws SQLException{
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM books ";
        try(Connection conn = DatabaseUtil.getConnection();
            Statement stmt = conn.createStatement()){

        }
    }





}
