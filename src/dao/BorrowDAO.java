package dao;

import dto.Borrow;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    // 도서 대출을 처리 기능
    public void borrowBook(int bookId, int studentPk) throws SQLException {
        // 대출 가능 여부 -- select(books)
        // 대출 한다면 --> insert(borrows)
        // 대출이 실행 되었다면 --> update (books-> available)

        String checkSql = "select available from books where id = ? ";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
            checkPstmt.setInt(1, bookId);
            ResultSet rs1 = checkPstmt.executeQuery();
            if (rs1.next() && rs1.getBoolean("available")) {
                // insert, update
                String insertSql = "insert into borrows (student_id,book_id, borrow_date)\n" +
                        " values(?, ?, current_date) ";
                String updateSql = "update books set available = false where id = ? ";

                try (PreparedStatement borrowStmt = conn.prepareStatement(insertSql);
                     PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    borrowStmt.setInt(1, studentPk);
                    borrowStmt.setInt(2, bookId);
                    System.out.println("-----------------------------------------");
                    updateStmt.setInt(1, bookId);
                    borrowStmt.executeUpdate();
                    updateStmt.executeUpdate();
                }

            } else {
                throw new SQLException("도서가 대출 불가능 합니다.");
            }
        }
    }

    // 현재 대출 중인 도서 목록을 조회
    public List<Borrow> getBorrowedBooks() throws SQLException {
        List<Borrow> borrowList = new ArrayList<>();
        String sql = "select * from borrows where return_date is null ";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Borrow borrowDTO = new Borrow();
                borrowDTO.setId(rs.getInt("id"));
                borrowDTO.setBookId(rs.getInt("book_id"));
                borrowDTO.setStudentId(rs.getInt("student_id"));
                // JAVA DTO 에서 데이터 타입은 LocalDate 이다.
                // 하지만 JDBC API 에서 아직은 LocalDate 타입을 지원하지 않는다.
                // JDBC API  재공하는 날짜 데이터 타입은 Date 이다.
                // rs.getLocalDate <-- 아직은 지원 안함
                // rs.getDate("borrow_date");
                borrowDTO.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                borrowList.add(borrowDTO);
            }
        }
        return borrowList;
    }


    // 도서 반납을 처리하는 기능 추가
    // 1. borrows 테이블에 책 정보 조회(check) -- select (복합 조건)
    // 2. borrows 테이블에 return_date 수정 --- update
    // 3. books 테이블에 available 수정 --- update
    public void returnBook(int bookId, int studentPk) throws SQLException {
        // studentPk --> borrows 테이블에 student_id 컬럼이다.
        // 즉, students 테이블에 pk를 의미한다.
        String sql = "select * from borrows where book_id = ? and student_id = ? and return_date is null ";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkPstmt = conn.prepareStatement(sql)) {
            checkPstmt.setInt(1, bookId);
            checkPstmt.setInt(2, studentPk);
            ResultSet rs = checkPstmt.executeQuery();

            if (rs.next()) {
                String updateSql1 = "update borrows set return_date = current_date where id = ? ";
                String updateSql2 = "update books set available = true where id = ? ";

                try (PreparedStatement borrowStmt = conn.prepareStatement(updateSql1);
                     PreparedStatement updateStmt = conn.prepareStatement(updateSql2)) {
                    // 조회된 borrows 테이블 pk 값에 접근해서 return_date 값을 오늘(반납일자) update 처리 해야 한다.
                    borrowStmt.setInt(1, rs.getInt("id"));
                    borrowStmt.executeUpdate();
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                }
            }
        }
    }

    public static void main(String[] args) {
        // 대출 실행 테스트
        BorrowDAO borrowDAO = new BorrowDAO();
        try {
            // borrowDAO.borrowBook(4, 2);
            //현재 대출 중인 책 목록 조회
            for (int i = 0; i < borrowDAO.getBorrowedBooks().size(); i++) {
                System.out.println(borrowDAO.getBorrowedBooks().get(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
