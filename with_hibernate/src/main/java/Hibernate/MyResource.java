package Hibernate;

import Hibernate.dao.Student;
import Hibernate.dao.StudentDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getSudent() {
        StudentDAO dao = new StudentDAO();
        List students = dao.getStudents();
        return students;
    }
    @GET
    @Path("/getbyid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getoneSudent(@PathParam("id") int id) {
        StudentDAO dao = new StudentDAO();
        String student = dao.getById(id);
        return student;
    }


    @POST
    @Path("/create")
    @Consumes("application/json")
    public Response addStudent(Student std){
        std.setName(std.getName());
        std.setEmail(std.getEmail());
        std.setCourse(std.getCourse());
        StudentDAO dao = new StudentDAO();
        dao.addStudent(std);

        return Response.ok().build();
    }

    @PUT
    @Path("/update/{id}")
    @Consumes("application/json")
    public Response updateStudent(@PathParam("id") int id, Student std){
        StudentDAO dao = new StudentDAO();
        int count = dao.updateStudent(id, std);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes("application/json")
    public Response deleteStudent(@PathParam("id") int id){
        StudentDAO dao = new StudentDAO();
        int count = dao.deleteStudent(id);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

}
