package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {

   //Initializing variables
   private static Connection conn = null;
   private static Statement stmt = null;
   private static ResultSet rs = null;
   private static int numRowsAffected = 0;
   private static PreparedStatement ps = null;
   private static Scanner in = new Scanner(System.in);

   public static void main (String[] args) throws SQLException {
      ArrayList<Student> studentList = new ArrayList<>();
      ArrayList<Course> courseList = new ArrayList<>();
      ArrayList<StudentCourse> studentCourseList = new ArrayList<>();

      //could also do the following
      //List<Student> studentList = new List <Person>():
      //Variables used to Connect to Database
      String dbURL = "jdbc:mysql://localhost:3306/hotsummer";
      String user = "root";
      String password = "Course12!";
      try {
         //connecting to DB
         conn = DriverManager.getConnection(dbURL, user, password);
         while (true) {

            //loop displaying a menu for options for users to select
            System.out.print("Main Menu \n");
            System.out.print("1: List students courses (search by name)\n");
            System.out.print("2: List courses enrollment list \n");
            System.out.print("3: Add a course\n");
            System.out.print("4: Add a student \n");
            System.out.print("5: Enroll a student into a course\n");
            System.out.print("6: Unenroll a studen from a course \n");
            System.out.print("7: Remove a Course\n");
            System.out.print("8: Delete a student\n");
            System.out.print("Please choose what you would like to do\n");
            int choice = in.nextInt();
            switch (choice) {
               //List all courses student is enrolled in given firstname & lastname 
               case 1:
                  listAllStudents();
                  String listCoursesSQL = "SELECT course.name, course.time "
                          + "FROM student, course, studentcourse "
                          + "where studentcourse.studentId = student.id AND "
                          + "studentcourse.courseId = course.id AND student.firstname = ? AND student.lastname = ?";
                  System.out.println("First Name:");
                  String userInputFirstName = in.next();
                  System.out.println("Last Name:");
                  String userInputLastName = in.next();
                  ps = conn.prepareStatement(listCoursesSQL);
                  ps.setString(1, userInputFirstName);
                  ps.setString(2, userInputLastName);
                  rs = ps.executeQuery();
                  if (!rs.next()) {
                     System.out.println(userInputFirstName + " is not enrolled in any classes");
                  }
                  else {
                     System.out.printf("\nList of Courses %s %s is Enrolled In\n", userInputFirstName, userInputLastName);
                     System.out.println("*****************************************");
                     System.out.printf("%15s %15s\n", "Course", "Time");
                     System.out.println("*****************************************");
                     do {
                        String course = rs.getString(1);
                        String time = rs.getString(2);
                        System.out.printf("%15s %15s\n", course, time);
                     }
                     while (rs.next());
                     System.out.println("*****************************************");
                  }
                  break;
               //List all students given a coursename
               case 2:
                  listAllCourses();
                  String listStudentsSQL = "SELECT student.id, student.firstname, student.lastname, student.age "
                          + "FROM student, course, studentcourse "
                          + "where studentcourse.studentId = student.id AND "
                          + "studentcourse.courseId = course.id AND course.name=?";
                  System.out.println("Course name:");
                  String userCourseName = in.next();
                  ps = conn.prepareStatement(listStudentsSQL);
                  ps.setString(1, userCourseName);
                  rs = ps.executeQuery();
                  if (!rs.next()) {
                     System.out.println("No students are currently enrolled in " + userCourseName);
                  }
                  else {
                     System.out.printf("\nList of Students enrolled in %s\n", userCourseName);
                     System.out.println("***************************************************************");
                     System.out.printf("%15s %15s %15s %15s\n", "Student ID", "First Name", "Last Name", "Age");
                     System.out.println("***************************************************************");
                     do {
                        int studentid = rs.getInt(1);
                        String firstname = rs.getString(2);
                        String lastname = rs.getString(3);
                        int age = rs.getInt(4);
                        System.out.printf("%15d %15s %15s %15s\n", studentid, firstname, lastname, age);
                     }
                     while (rs.next());
                     System.out.println("***************************************************************");
                  }
                  break;
               //ADD A COURSE
               case 3:
                  Course course = new Course();
                  listAllCourses();
                  String insertCourse = "INSERT INTO course (name, time)" + "VALUES (?, ?)";
                  ps = conn.prepareStatement(insertCourse);
                  System.out.print("To add a course, enter the follwoing:\n");
                  System.out.println("Name:");
                  String courseName = in.next();
                  System.out.println("Time:");
                  String courseTime = in.next();
                  ps.setString(1, courseName);
                  ps.setString(2, courseTime);
                  numRowsAffected = ps.executeUpdate();
                  System.out.println("\n********" + numRowsAffected + " row(s) were affected********\n");

                  course.setCourseName(courseName);
                  course.setCourseTime(courseTime);
                  courseList.add(course);
                  System.out.println(course.getCourseTime() + " " + course.getCourseName());
                  break;
               //ADD A STUDENT
               case 4:
                  Student student = new Student();
                  listAllStudents();
                  String insertStudent = "INSERT INTO student (firstname, lastname, age)" + "Values (? , ? ,?)";
                  ps = conn.prepareStatement(insertStudent);
                  System.out.println("To add a student, enter the following:\n");
                  System.out.println("First Name:");
                  String studentFirstName = in.next();
                  System.out.println("Last Name:");
                  String studentLastName = in.next();
                  System.out.println("Age:");
                  int studentAge = in.nextInt();
                  ps.setString(1, studentFirstName);
                  ps.setString(2, studentLastName);
                  ps.setInt(3, studentAge);
                  numRowsAffected = ps.executeUpdate();
                  System.out.println("\n********" + numRowsAffected + " row(s) were affected********\n");
                  student.setFirstName(studentFirstName);
                  student.setLastName(studentLastName);
                  student.setStudentAge(studentAge);
                  studentList.add(student);
                  System.out.println(student.getFirstName() + " " + student.getLastName() + "" + student.getStudentAge());
                  break;


               //ENROLL A STUDENT IN A COURSE
               case 5:
                  StudentCourse studentCourse = new StudentCourse();
                  listAllStudents();
                  listAllCourses();
                  String enrollCourse = "INSERT INTO studentcourse (studentId, courseid)" + "Values (?,?)";
                  ps = conn.prepareStatement(enrollCourse);
                  System.out.println("To enroll a student in a course, enter the following:\n");
                  System.out.println("Student ID:");
                  int studentId = in.nextInt();
                  System.out.println("Course ID:");
                  int courseId = in.nextInt();
                  ps.setInt(1, studentId);
                  ps.setInt(2, courseId);
                  numRowsAffected = ps.executeUpdate();
                  System.out.print("\n********" + numRowsAffected + " row(s) were affected********\n");
                  studentCourse.setStudentID(studentId);
                  studentCourse.setCourseID(courseId);
                  studentCourseList.add(studentCourse);
                  break;
               //UNENROLL STUDENT FROM A COURSE
               case 6:
                  listAllStudents();
                  String unenrollCourse = "DELETE FROM studentcourse where studentid = ? AND courseid = ?";
                  ps = conn.prepareStatement(unenrollCourse);
                  System.out.println("To unenroll a student from a course, enter the following:\n");
                  System.out.println("Student ID:");
                  int unenrollStudentId = in.nextInt();
                  System.out.println("Course ID:");
                  int unenrollCourseId = in.nextInt();
                  ps.setInt(1, unenrollStudentId);
                  ps.setInt(2, unenrollCourseId);
                  numRowsAffected = ps.executeUpdate();
                  System.out.print("\n********" + numRowsAffected + " row(s) were affected********\n");
                  // i want to loop through the array but i will need access to the student ID number which is only stored in SQL and not in java
//                  for (int i = 0; i < studentCourseList.length(); i++) {
//                     if (unenrollStudentId == student.) {
//                        studentCourseList.remove(i);
//                     }
//                  }

                  break;
               //Remove COURSE    
               case 7:
                  String removeCourse = "DELETE from course where id=?";
                  ps = conn.prepareStatement(removeCourse);
                  System.out.print("To delete a course, enter the following:\n");
                  System.out.print("ID:");
                  int removeCourseId = in.nextInt();
                  ps.setInt(1, removeCourseId);
                  numRowsAffected = ps.executeUpdate();
                  System.out.print(numRowsAffected + " row(s) were affected\n");

                  //looping through array to remove indexed course from array list. need to find a way to sync ID in java with SQL
//                  for (int i = 0; i < courseList.size(); i++) {
//                     if (removeCourseID == course.getCourseID){
//                        courseList.remove(i);
//                     }
//                  }

                  //REMOVE STUDENT    
                  break;
               case 8:
                  listAllStudents();
                  String removeStudent = "DELETE from student where id=?";
                  ps = conn.prepareStatement(removeStudent);
                  System.out.println("To remove a student, enter the following:\n");
                  System.out.println("ID:");
                  int removeStudentId = in.nextInt();
                  ps.setInt(1, removeStudentId);
                  numRowsAffected = ps.executeUpdate();
                  System.out.println("\n********" + numRowsAffected + " row(s) were affected********\n");
//                   for (int i = 0; i < courseList.size(); i++) {
//                     if (removeStudentID == student.getStudentID){
//                        studentList.remove(i);
//                     }
//                  }

            }
         }
      }
      catch (SQLException e) {
         e.printStackTrace();
      }

      if (conn != null) {
         conn.close();
      }
      if (stmt != null) {
         stmt.close();
      }
      if (rs != null) {
         rs.close();
      }
   }

   public static void listAllStudents () throws SQLException {
      String listStudents = "SELECT *" + "FROM student";
      ps = conn.prepareStatement(listStudents);
      rs = ps.executeQuery();
      System.out.println("\nListing Student Table");
      System.out.println("*****************************************");
      System.out.printf("%2s %15s %15s\n", "ID", "First Name", "Last Name");
      while (rs.next()) {
         System.out.printf("%2d %15s %15s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
      }
      System.out.println("*****************************************");
   }

   public static void listAllCourses () throws SQLException {
      String listCourses = "SELECT *" + "FROM course";
      ps = conn.prepareStatement(listCourses);
      rs = ps.executeQuery();
      System.out.println("\nListing Course Table");
      System.out.println("*****************************************");
      System.out.printf("%2s %15s %15s\n", "ID", "Name", "Time");
      while (rs.next()) {
         System.out.printf("%2d %15s %15s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
      }
      System.out.println("*****************************************");
   }
}
