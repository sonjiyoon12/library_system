package my_test.utii;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author 손지윤
 * 데이터베이스 연결을 관리하는 유틸리티 클래스
 */
public class DatabaseUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library?serverTimezone=Asia/Seoul";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "asd1234";


    // 데이터베이스 연결 객체를 반환하는 함수
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
