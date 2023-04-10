package com.example.withouthibernate;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class Application implements HttpHandler {

    static Mysqlconne dao = new Mysqlconne();
    
    @Override
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();

 

		if (requestMethod.equalsIgnoreCase("POST")) {
			// Get the name parameter from the request body
			String requestBody = new String(exchange.getRequestBody().readAllBytes());
			String[] parts = requestBody.split("=");
            System.out.println(parts);
	
                String[] namevalue_Gmail =parts[1].split("&"); // name , second paramter of gmail 
				String[] gmailvalue_Coure=parts[2].split("&");
                String coursevalue = parts[3];
                String name=URLDecoder.decode(namevalue_Gmail[0], StandardCharsets.UTF_8.toString());
                String course=URLDecoder.decode(coursevalue, StandardCharsets.UTF_8.toString());
                String gamil=URLDecoder.decode(gmailvalue_Coure[0], StandardCharsets.UTF_8.toString());
                
                if (namevalue_Gmail.length==2 && namevalue_Gmail[1].equals("email")){

                    
                    Student student = new Student(name,course, gamil);
                    dao.saveStudent(student);

               
                exchange.sendResponseHeaders(200, 0);
				OutputStream responseBody = exchange.getResponseBody();
				responseBody.write(("Saved Student: " + namevalue_Gmail[0]).getBytes());
				responseBody.close(); }
			 else {
				exchange.sendResponseHeaders(400, 0);
				exchange.getResponseBody().close();
			}}

       
		 else if (requestMethod.equalsIgnoreCase("GET")) {
			// Get the name parameter from the query string
			String query = exchange.getRequestURI().getQuery();
            //get by email 
            if(query != null && query.startsWith("email=")){
                String email = query.substring(6);
                System.out.println(email);
                Student student = dao.getStudentByEmail(email);
                String st= student.toString();
                exchange.sendResponseHeaders(200, st.length());
				OutputStream responseBody = exchange.getResponseBody();
				responseBody.write(st.getBytes());
				responseBody.close();

            }
			
            //get all 
            else if (query!=null && query.startsWith("all")) {
				String name = query.substring(5);
                String listStudent ="";
                 List<Student> students = dao.getStudents();
                for (Student student : students) {
                   listStudent=listStudent+student.toString()+"\n";
                }
                exchange.sendResponseHeaders(200, listStudent.length());
				OutputStream responseBody = exchange.getResponseBody();
				responseBody.write(listStudent.getBytes());
				responseBody.close();}

            // put (update method)
           else if(query != null && query.startsWith("update=")){
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
			String[] parts = requestBody.split("=");
            System.out.println(parts);
            String[] oldnamelist =parts[1].split("&"); // name , second paramter of gmail 
            String[] newnamelist=parts[2].split("&");
            String oldnamevalue=oldnamelist[0];
            String newnamevalue=newnamelist[0];
            String newname=URLDecoder.decode(newnamevalue, StandardCharsets.UTF_8.toString());
            String oldname=URLDecoder.decode(oldnamevalue, StandardCharsets.UTF_8.toString());
            System.out.println("oldname : "+oldname+"\n new name :"+newname);

            Student student = dao.getStudentByName(oldname);
            System.out.print(student.toString());
            student.setName(newname);
            dao.updateStudent(student, student.getEmail());
            String response = "Email have been updated ";
            exchange.sendResponseHeaders(200, response.length());
				OutputStream responseBody = exchange.getResponseBody();
				responseBody.write(response.getBytes());
				responseBody.close();
            }

             // delete method 

            else if(query != null && query.startsWith("delete=")){
                String email = query.substring(7);
                System.out.println(email);
                dao.deleteStudent(email);
                String response = "Student have been deleted ";
                exchange.sendResponseHeaders(200, response.length());
				OutputStream responseBody = exchange.getResponseBody();
				responseBody.write(response.getBytes());
				responseBody.close();

            }

            // error handler 
           
			else {
				exchange.sendResponseHeaders(400, 0);
				exchange.getResponseBody().close();
			}}
            // method handler
		 else {
			exchange.sendResponseHeaders(405, 0);
			exchange.getResponseBody().close();
		}}


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        dao.databaseConnection();
        //Creates a database connection
        System.out.println("Creating Database Connection"); 
        // dao.createTables();

    
        
        HttpServer server = HttpServer.create(new InetSocketAddress(5000), 0);
		server.createContext("/", new Application());
		server.start();
		System.out.println("Server started on port 5000");}

        // //Insert 2 students into the database
        // System.out.println("Inserting Students to the database");
        // Student peter = new Student("Peter John", "peter@gmail.com", "Computer Science");
        // Student cathy = new Student("Catherine Williams", "catherine@gmail.com", "Electrical engineering");
        // dao.saveStudent(peter);
        // dao.saveStudent(cathy);

        // //Get all Students from the database
        // System.out.println("Getting all students from the database");
        // List<Student> students = dao.getStudents();
        // for (Student student : students) {
        //     System.out.println(student.toString());
        // }

        // //Get Student by email address
   
        // //Updating student name
        // System.out.println("updating student name");
        // //Update Catherine Williams name to Catherine William
        // cathy.setName("Catherine William");
        // dao.updateStudent(cathy, cathy.getEmail());

        // //delete student
        // System.out.println("Deleting a student from the database");
        // dao.deleteStudent("peter@gmail.com");

        // //close the database connection
        // System.out.println("Closing the database connection");
        // dao.close();


    }
