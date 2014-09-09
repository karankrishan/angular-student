package com.pdizzle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pdizzle.models.Student;

@Repository
public class StudentServicesDao {

  private final Logger log = LoggerFactory.getLogger(StudentServicesDao.class);

  private static final String INSERT_SQL =
      "Insert into Student (FirstName, MiddleName, LastName, Address, Address2, City, State, ZipCode) values (?,?,?,?,?,?,?,?)";
  private static final String SELECT_ALL_SQL = "SELECT * FROM Student";
  private static final String UPDATE_SQL =
      "UPDATE Student SET FirstName=?, LastName=?, MiddleName=?, Address=?, Address2=?, City=? , State=?, ZipCode=? where StudentId=?";
  private static final String SELECT_STUDENT_SQL = "Select * from Student Where studentId= ?";
  private static final String SELECT_STUDENT_BY_NAME_SQL = "Select top 1 * from Student Where firstName= ? and lastName= ?";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  /**
   * Create a new students with provided values. Currently does not prevent duplicates. A new student with 
   * the exact same information as an existing entry, will result in a new record being created, with 
   * it's own unique Student Id.
   * 
   * @param student Populated student object, with id left blank. Method not null-safe, so pass in a student.
   * @return the Student with a populated Id.
   */
  public Student createUser(final Student student) {
    KeyHolder holder = new GeneratedKeyHolder();

    jdbcTemplate.update(new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, student.getFirstName());
        ps.setString(2, student.getMiddleName());
        ps.setString(3, student.getLastName());
        ps.setString(4, student.getAddress());
        ps.setString(5, student.getAddress2());
        ps.setString(6, student.getCity());
        ps.setString(7, student.getState());
        ps.setString(8, student.getZipCode());
        return ps;
      }
    }, holder);

    return getStudent(holder.getKey().intValue());
  }

  /**
   * Get student matching studentId
   * 
   * @param studentId Id of student
   * @return Student associated with id.
   */
  public Student getStudent(final int studentId) {
    try {
      return jdbcTemplate.queryForObject(SELECT_STUDENT_SQL, new StudentRowMapper(), studentId);
    } catch (EmptyResultDataAccessException e) {
        log.debug("No one found matching id: {}", studentId);
      return null;
    }
  }
  
  /**
   * Special note! This method will only return the first occurrence of x number of rows matching input params.
   * @param firstName
   * @param lastName
   * @return First student matching the passed in first/last name
   */
  public Student getStudent(final String firstName, final String lastName){
    try {
      return jdbcTemplate.queryForObject(SELECT_STUDENT_BY_NAME_SQL, new StudentRowMapper(), firstName, lastName);
    } catch (EmptyResultDataAccessException e) {
        log.debug("No one found matching id: {} {}", firstName, lastName);
      return null;
    }
  }

  /**
   * Updates and existing student
   * 
   * @param studentId The id of the student to update.
   * @param student The updated student values. Method not null-safe, so pass in a Student.
   * @return True if the student was found and updated. False if not.
   */
  public boolean updateStudent(int studentId, Student student) {
    if (studentId != student.getId()) {
      throw new IllegalArgumentException("The Id parameter does not match the id on the student");
    }

    int rowsUpdated =
        jdbcTemplate.update(UPDATE_SQL,
            new Object[] {student.getFirstName(), student.getLastName(), student.getMiddleName(), student.getAddress(),
                student.getAddress2(), student.getCity(), student.getState(), student.getZipCode(), studentId});

    return rowsUpdated > 0;
  }

  /**
   * Get list of all students from db.
   * 
   * @return Returns all students from db.
   */
  public List<Student> getAllStudents() {
    return jdbcTemplate.query(SELECT_ALL_SQL, new StudentRowMapper(), new Object[] {});
  }

  /**
   * Deletes student from database. If user not found, nothing happens.
   * 
   * @param id
   * @return True if a record was deleted. False if not.
   */
  public boolean deleteStudent(int id) {
    int rowsDeleted = jdbcTemplate.update("Delete from Student where studentId= ?", new Object[] {id});
    return rowsDeleted > 0;
  }

  /**
   * Maps a row in db to Student model class
   * 
   * @author cdpxpb
   * 
   */
  private class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
      Student student = new Student();
      student.setId(rs.getInt("StudentId"));
      student.setFirstName(rs.getString("FirstName"));
      student.setLastName(rs.getString("Lastname"));
      student.setMiddleName(rs.getString("MiddleName"));
      student.setAddress(rs.getString("Address"));
      student.setAddress2(rs.getString("Address2"));
      student.setCity(rs.getString("City"));
      student.setState(rs.getString("State"));
      student.setZipCode(rs.getString("ZipCode"));
      return student;
    }
  }

}
