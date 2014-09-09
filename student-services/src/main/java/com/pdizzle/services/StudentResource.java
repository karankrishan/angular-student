package com.pdizzle.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pdizzle.models.Student;


@Path("/students")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface StudentResource {
	
    @GET
    @Path("/")
    public Response getAllStudents();	

    @DELETE
    @Path("/{id}")
    public Response removeStudent(@PathParam("id") final int studentId);
    
    
    @POST
    @Path("/")
    public Response addStudent(final Student student);
    //return location of new resource
    //Return 201 if student did not exists, and include Location header
    //200 or 204  if did
    
    @PUT
    @Path("/{id}")
    public Response updateStudent(@PathParam("id") final int studentId, final Student student);
    
}
