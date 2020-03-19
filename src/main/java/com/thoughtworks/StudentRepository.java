package com.thoughtworks;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
  public static final String URL = "jdbc:mysql://127.0.0.1:3306/grace?useUnicode=true&characterEncoding=utf-8&serverTimezone=Hongkong";
  public static final String USER = "root";
  public static final String PASSWORD = "guoer123";

  public void save(List<Student> students) throws SQLException {
    for (Student student : students) {
      save(student);
    }
  }

  public void save(Student student) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
    String sql = "INSERT INTO student_sys VALUES(?, ?, ?, ?, ?, ?)";
    PreparedStatement pre = conn.prepareStatement(sql);
    prepareStatement(pre, student);
    pre.execute();
  }

  private void prepareStatement(PreparedStatement pre, Student student) throws SQLException {
    pre.setString(1, student.getId());
    pre.setString(2, student.getName());
    pre.setString(3, student.getGender());
    pre.setInt(4, student.getAdmissionYear());
    pre.setDate(5, new java.sql.Date(student.getBirthday().getTime()));
    pre.setString(6, student.getClassId());
  }

  public List<Student> query() throws SQLException {
    List<Student> stuList = new ArrayList<>();
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
    String sql = "SELECT * FROM student_sys";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    return getStuList(stuList, rs);
  }

  private List<Student> getStuList(List<Student> stuList, ResultSet rs) throws SQLException {
    Student stu;
    while (rs.next()) {
      stu = new Student();
      stu.setId(rs.getString("id"));
      stu.setName(rs.getString("name"));
      stu.setGender(rs.getString("gender"));
      stu.setAdmissionYear(rs.getInt("admissionYear"));
      stu.setBirthday(rs.getDate("birthday"));
      stu.setClassId(rs.getString("classId"));

      stuList.add(stu);
    }
    return stuList;
  }

  public List<Student> queryByClassId(String classId) throws SQLException {
    List<Student> stuList = new ArrayList<>();
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
    String sql = "SELECT * FROM student_sys WHERE classId=? ORDER BY id DESC";
    PreparedStatement pre = conn.prepareStatement(sql);
    pre.setString(1, classId);
    ResultSet rs = pre.executeQuery();
    return getStuList(stuList, rs);
  }

  public void update(String id, Student student) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
    String sql = "UPDATE student_sys set id=?, name=?, gender=?, admissionYear=?, birthday=?, classId=? WHERE id=?";
    PreparedStatement pre = conn.prepareStatement(sql);
    prepareStatement(pre, student);
    pre.setString(7, id);

    pre.execute();
  }

  public void delete(String id) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
    String sql = "DELETE FROM student_sys WHERE id=?";
    PreparedStatement pre = conn.prepareStatement(sql);
    pre.setString(1, id);

    pre.execute();
  }
}
