

    package com.example.withouthibernate;
    import java.sql.*;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;
    
    public class Mysqlconne {
    
            private Connection connect = null;
            private Statement statement = null;
            private PreparedStatement preparedStatement = null;
            private ResultSet resultSet = null;
    
            private String student_name;
            private String student_email;
            private String student_course;
    
            public void databaseConnection() throws ClassNotFoundException, SQLException {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connect = DriverManager
                        .getConnection("jdbc:mysql://localhost:3306/school",
                                "root","Mysql2022$%");
                statement = connect.createStatement();
            }
            public void createTables(){
                try {
                   statement.executeUpdate("create table students(name varchar(255)," +
                            "                                                            email varchar(255)," +
                            "                                                            course varchar(255));");
    
            } catch (Exception e) {
            System.out.println(e.getMessage());
        }}
            public List<Student> getStudents() {
                List<Student> students = new ArrayList<>();
                try {
                    resultSet = statement
                            .executeQuery("select * from school.students");
                    while (resultSet.next()) {
                        student_name = resultSet.getString("name");
                        student_email = resultSet.getString("email");
                        student_course = resultSet.getString("course");
                        Student student = new Student(student_name, student_email, student_course);
                        students.add(student);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return students;
            }
    
            public Student getStudentByEmail(String email) {
                try {
                    resultSet = statement
                            .executeQuery("select * from school.students WHERE email= \'" + email + "\' LIMIT 1;");
                    while (resultSet.next()) {
                        student_name = resultSet.getString("name");
                        student_email = resultSet.getString("email");
                        student_course = resultSet.getString("course");
                    }
    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
    
                }
                return new Student(student_name, student_email, student_course);
            }
            public Student getStudentByName(String Name) {
                try {
                    resultSet = statement
                            .executeQuery("select * from school.students WHERE name= \'" +  "\' LIMIT 1;");
                    while (resultSet.next()) {
                        student_name = resultSet.getString("name");
                        student_email = resultSet.getString("email");
                        student_course = resultSet.getString("course");
                    }
    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
    
                }
                return new Student(student_name, student_email, student_course);
            }
            public void saveStudent(Student student) {
                try {
                    preparedStatement = connect
                            .prepareStatement("insert into  students values (?, ?, ?)");
                    preparedStatement.setString(1, student.getName());
                    preparedStatement.setString(2, student.getCourse());
                    preparedStatement.setString(3, student.getEmail());
                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
    
            }
    
            public void deleteStudent(String email) {
                try {
                    preparedStatement = connect
                            .prepareStatement("delete from school.students where email= ? ; ");
                    preparedStatement.setString(1, email);
                    preparedStatement.executeUpdate();
    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
    
                }
    
            }
    
            public void updateStudent(Student student, String email) {
                try {
                    preparedStatement = connect
                            .prepareStatement("update students set name= ? where email= ? ;");
                    preparedStatement.setString(1, student.getName());
                    preparedStatement.setString(2, email);
                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
    
            public void close() {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
    
                    if (statement != null) {
                        statement.close();
                    }
    
                    if (connect != null) {
                        connect.close();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
    
        }
    
    