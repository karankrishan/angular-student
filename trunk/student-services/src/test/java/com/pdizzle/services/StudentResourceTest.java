package com.pdizzle.services;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;


import com.pdizzle.models.Student;

/**
 * TODO: Turn this class into more of an integration test suite. Changing to be
 * a static main method now, so it does not make the server blow up, when tests
 * are run before startup.
 * 
 * @author cdpxpb
 *
 */
public class StudentResourceTest {


  public static void main(String[] args) {
      ResteasyClient client = new ResteasyClientBuilder().build();
      ResteasyWebTarget target = client.target("http://localhost:8081");
      StudentResource service = target.proxy(StudentResource.class);
      Response response= service.getAllStudents();
      List<Student> students= response.readEntity(new GenericType<List<Student>>(){});
      System.out.println(students);
    }
}
