package test;
/**
 * @author Rich Smith at ZenOfProgramming.com
 */
public class Student {
   private String firstName;
   private String lastName;
   private int studentAge;

   /**
    * @return the firstName
    */
   public String getFirstName () {
      return firstName;
   }

   /**
    * @param firstName the firstName to set
    */
   public void setFirstName (String firstName) {
      this.firstName = firstName;
   }

   /**
    * @return the lastName
    */
   public String getLastName () {
      return lastName;
   }

   /**
    * @param lastName the lastName to set
    */
   public void setLastName (String lastName) {
      this.lastName = lastName;
   }

   /**
    * @return the studentAge
    */
   public int getStudentAge () {
      return studentAge;
   }

   /**
    * @param studentAge the studentID to set
    */
   public void setStudentAge (int studentAge) {
      this.studentAge = studentAge;
   }

}
