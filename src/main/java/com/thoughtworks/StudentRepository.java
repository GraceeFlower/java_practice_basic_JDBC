package com.thoughtworks;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public void save(List<Student> students) {
        students.forEach(this::save);
    }

    public void save(Student student) {
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = JDBCUtil.connectToDB();
            String sql = "INSERT INTO student_sys VALUES(?, ?, ?, ?, ?, ?)";
            pre = conn.prepareStatement(sql);
            prepareStatement(pre, student);
            pre.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.releaseSource(conn, pre);
        }
    }

    private void prepareStatement(PreparedStatement pre, Student student) {
        try {
            pre.setString(1, student.getId());
            pre.setString(2, student.getName());
            pre.setString(3, student.getGender());
            pre.setInt(4, student.getAdmissionYear());
            pre.setDate(5, new java.sql.Date(student.getBirthday().getTime()));
            pre.setString(6, student.getClassId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> query() {
        List<Student> stuList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.connectToDB();
            String sql = "SELECT * FROM student_sys";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return getStuList(stuList, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.releaseSource(conn, stmt, rs);
        }
        return stuList;
    }

    private List<Student> getStuList(List<Student> stuList, ResultSet rs) {
        Student stu;
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stuList;
    }

    public List<Student> queryByClassId(String classId) {
        List<Student> stuList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.connectToDB();
            String sql = "SELECT * FROM student_sys WHERE classId=? ORDER BY id DESC";
            pre = conn.prepareStatement(sql);
            pre.setString(1, classId);
            rs = pre.executeQuery();
            return getStuList(stuList, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.releaseSource(conn, pre, rs);
        }
        return stuList;
    }

    public void update(String id, Student student) {
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = JDBCUtil.connectToDB();
            String sql = "UPDATE student_sys " +
                "set id=?, name=?, gender=?, admissionYear=?, birthday=?, classId=? " +
                "WHERE id=?";
            pre = conn.prepareStatement(sql);
            prepareStatement(pre, student);
            pre.setString(7, id);
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.releaseSource(conn, pre);
        }
    }

    public void delete(String id) {
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = JDBCUtil.connectToDB();
            String sql = "DELETE FROM student_sys WHERE id=?";
            pre = conn.prepareStatement(sql);
            pre.setString(1, id);
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.releaseSource(conn, pre);
        }
    }
}
