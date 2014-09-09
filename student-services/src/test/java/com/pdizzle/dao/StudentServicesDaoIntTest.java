package com.pdizzle.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.CollectionUtils;

import com.pdizzle.app.StudentServicesConfig;
import com.pdizzle.models.Student;


/**
 * Sanity check on db connectivity.
 * 
 * @author cdpxpb
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = StudentServicesConfig.class, loader = AnnotationConfigContextLoader.class)
public class StudentServicesDaoIntTest {

  private final Logger log = LoggerFactory.getLogger(StudentServicesDao.class);

  @Autowired
  StudentServicesDao dao;
  

  @Test
  public void testGetAllStudents() {
    List<Student> students = dao.getAllStudents();
    Assert.assertFalse("Failed to pull anything back from db,", CollectionUtils.isEmpty(students));
    printStudents(students);
  }

  /**
   * Why are these tests in the same method? 
   * Because unit tests can be run in any order, and I need the insert to happen before the update/delete.
   * So, out of haste, boom. One method to rule them all
   */
  @Test
  public void testAddUpdateAndRemovingStudent() {
    Student s = new Student();
    s.setFirstName("Integration");
    s.setLastName("Test");
    s.setAddress("123 Sesame St");
    s.setCity("Carmel");
    s.setState("IN");
    s.setZipCode("46033");
    Student newStudent = dao.createUser(s);
    Assert.assertTrue("Failed to create student", newStudent.getId() > -1);
    log.debug("Created: " + newStudent);

    newStudent.setAddress("125 Nightmare St");
    dao.updateStudent(s.getId(), s);
    testGetAllStudents();
    Assert.assertEquals(newStudent, dao.getStudent(newStudent.getId()));
    
    boolean wasSuccessful = dao.deleteStudent(newStudent.getId());
    Assert.assertTrue("Failed to remove test entry", wasSuccessful);
    log.debug("Removed: " + newStudent);
  }


  private void printStudents(List<Student> students) {
    for (Student s : students) {
      log.debug("\t" + s);
    }
  }
}
