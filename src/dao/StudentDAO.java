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
        // 쿼리문
        String sql = "insert into students (id, name, student_id) values(?, ?, ?) ";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, student.getId());
            pstmt.setString(2,student.getName());
            pstmt.setString(3,student.getStudentId());
            pstmt.executeQuery();
        }
    }

    // 모든 학생 목록을 조회하는 기능
    public List<Student> getAllStudents() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        String sql = "select * from students ";
        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
             ResultSet rs = pstmt.executeQuery(sql);

             while(rs.next()){
                 int id = rs.getInt("id");
                 String name = rs.getString("name");
                 String student_id = rs.getString("student_id");

                 Student student = new Student(id, name, student_id);
                 studentList.add(student);
             }
        }

        return studentList;
    }


    // 학생 student_id로 학생 인증(로그인 용) 기능 만들기
    public Student authenticateStudent(String studentId) throws SQLException {
        // 학생이 정확한 학번을 입력하면 Student 객체가 만들어져 리턴 됨
        // 학생이 잘못된 학번을 입력하면 null 값을 반환함
        // if -- return new Student()


        return null;
    }

    public static void main(String[] args) {
        StudentDAO studentDAO = new StudentDAO();
        try {
            studentDAO.getAllStudents();
            for(int i = 0; i < studentDAO.getAllStudents().size(); i++){
                System.out.println();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
