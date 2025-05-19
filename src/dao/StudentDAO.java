package dao;

import dto.Student;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 새 학생을 데이터베이스에 추가하는 기능
    public void addStudent(Student student) throws SQLException {
        // 1. 쿼리문 만들기 및 테스트 (DB 에서)
        String sql = "insert into students (name, student_id) values(?, ?) ";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getStudentId());
            pstmt.executeUpdate(); // 쿼리 실행
        }
    }

    // 모든 학생 목록을 조회하는 기능
    public List<Student> getAllStudents() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        String sql = "select * from students ";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery(sql);

            while (rs.next()) {
                Student studentDto = new Student();
                //String name = rs.getString("name");
                studentDto.setId((rs.getInt("id")));
                studentDto.setName(rs.getString("name"));
                studentDto.setStudentId(rs.getString("student_id"));
                studentList.add(studentDto);
            }
        }
        return studentList;
    }


    // 학생 student_id로 학생 인증(로그인 용) 기능 만들기
    public Student authenticateStudent(String studentId) throws SQLException {
        // 학생이 정확한 학번을 입력하면 Student 객체가 만들어져 리턴 됨
        // 학생이 잘못된 학번을 입력하면 null 값을 반환함
        // if -- return new Student()

        String sql = " select * from students where student_id = ? ";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                Student StudentDTO = new Student();
                StudentDTO.setId(rs.getInt("id"));
                StudentDTO.setName(rs.getString("name"));
                StudentDTO.setStudentId(rs.getString("student_id"));
                return StudentDTO;
            }
        }
        return null;
    }
}

