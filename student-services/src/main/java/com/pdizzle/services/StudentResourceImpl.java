package com.pdizzle.services;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdizzle.dao.StudentServicesDao;
import com.pdizzle.models.Student;

@Service
public class StudentResourceImpl implements StudentResource {
  private final Logger log = LoggerFactory.getLogger(StudentResourceImpl.class);

  @Autowired
  private StudentServicesDao dao;

  @Override
  public Response getAllStudents() {
    List<Student> students = dao.getAllStudents();
    log.debug("Returning {} students", students.size());
    return Response.ok(students).build();
  }

  @Override
  public Response removeStudent(int studentId) {
    if (studentId < 0){
      return Response.status(Status.BAD_REQUEST).build();
    }
    log.debug("Removing student with id: {}", studentId);
    boolean isDeleted = dao.deleteStudent(studentId);
    if (!isDeleted) {
      log.warn("Attempt to delete student with id [{}] was found to be missing.", studentId);
    }
    // Debated on return 404 if id was not found, but decided to just return 204 as the end goal is to not have it be
    // there, and it is not already. No reason to raise the alarms.
    return Response.status(Status.NO_CONTENT).build();
  }

  @Override
  public Response addStudent(Student student) {
    if (student == null){
      return Response.status(Status.BAD_REQUEST).build();
    }
    Student createdStudent= dao.createUser(student);
    return Response.created(URI.create("/students/" + createdStudent.getId())).build();
  }

  @Override
  public Response updateStudent(int studentId, Student student) {
    if (student == null || studentId != student.getId()){
      return Response.status(Status.BAD_REQUEST).build();
    }
    dao.updateStudent(studentId, student);
    return Response.status(Status.NO_CONTENT).build();
  }
}
