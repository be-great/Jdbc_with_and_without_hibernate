package Hibernate.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StudentDAO {

    public void addStudent(Student bean){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        addStudent(session,bean);
        tx.commit();
        session.close();

    }

    private void addStudent(Session session, Student bean){
        Student student = new Student();

        student.setName(bean.getName());
        student.setEmail(bean.getEmail());
        student.setCourse(bean.getCourse());

        session.save(student);
    }


    public List<Student> getStudents(){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Student");
        List<Student> students =  query.list();
        session.close();
        return students;
    }

    public int deleteStudent(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Student where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }

    public int updateStudent(int id, Student std){
        if(id <=0)
            return 0;
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Student set name = :name, email=:email, course=:course where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        query.setString("name",std.getName());
        query.setString("email",std.getEmail());
        query.setString("course",std.getCourse());

        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    public String getById(int id){
        Session session = SessionUtil.getSession();
        //System.out.println("################\nid number is :"+id);
        if(id <=0)
            return "null id number";
        org.hibernate.query.Query query = session.createQuery("from Student where id = :id");
        query.setParameter("id", id);

        final Object std = query.uniqueResult();// Get the single result, assuming the ID is unique

        if (std == null) {
            return  "No student found by this id :"+id;
            // User found, do something with it
        } else {
            return std.toString();
        }
    }

}

