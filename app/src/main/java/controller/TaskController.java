/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.UIManager.getInt;
import model.Task;
import util.ConnectionFactory;
/**
 *
 * @author deiam
 */
public class TaskController {
    public void save(Task task){
        String sql = "INSERT INTO tasks ("
                + "idProject, "
                + "name, "
                + "description, "
                + "completed, "
                + "notes, "
                + "deadline, "
                + "createdAt, "
                + "updatedAt"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement statement = null;
        
        try{
            //Estabelecendo conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            
            //preparando a query
            statement = conn.prepareStatement(sql);
            
            //informando os valores
            statement.setInt(1, task.getIdProject());
            statement.setString(2, task.getName());
            statement.setString(3, task.getDescription());
            statement.setBoolean(4, task.isIsCompleted());
            statement.setString(5, task.getNotes());
            statement.setDate(6, new Date(task.getDeadline().getTime()));
            statement.setDate(7, new Date(task.getCreatedAt().getTime()));
            statement.setDate(8, new Date(task.getUpdatedAt().getTime()));
            //Executando a query
            statement.execute();
        }catch (Exception ex){
            throw new RuntimeException("Erro ao salvar a tarefa. " + ex.getMessage(), ex);
        }finally{
            ConnectionFactory.closeConnection(conn, statement);
        }
    }
        
    public void update(Task task){
        String sql = "UPDATE tasks SET "
                + "idProject = ?, name = ?, "
                + "description = ?, "
                + "completed = ?, "
                + "notes = ?, "
                + "deadline = ?,"
                + "createdAt = ?, "
                + "updatedAt = ?"
                + "WHERE id = ?;";
        
        Connection conn = null;
        PreparedStatement statement = null;
        
        try{
            conn = ConnectionFactory.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, task.getIdProject());
            statement.setString(2, task.getName());
            statement.setString(3, task.getDescription());
            statement.setBoolean(4, task.isIsCompleted());
            statement.setString(5, task.getNotes());
            statement.setDate(6, new java.sql.Date(task.getDeadline().getTime()));
            statement.setDate(7, new java.sql.Date(task.getCreatedAt().getTime()));
            statement.setDate(8, new java.sql.Date(task.getUpdatedAt().getTime()));
            statement.setInt(9, task.getId());
            statement.execute();
            
        }catch(Exception ex){
            throw new RuntimeException("Erro ao atualizar a tarefa." + ex.getMessage(), ex);
        }finally{
            ConnectionFactory.closeConnection(conn, statement);
        }
        
    }
    
    public void removeById(int taskId){
        String sql = "DELETE FROM tasks WHERE id = ?";
        Connection conn = null;
        PreparedStatement statement = null;
        
        try{
            //criação da conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            //preparando a query
            statement = conn.prepareStatement(sql);
            //informando os valores
            statement.setInt(1, taskId);
            //executando a query
            statement.execute();
        }catch(Exception ex){
            throw new RuntimeException("Erro ao apagar a tarefa" + ex.getMessage(), ex);
        }finally{
            ConnectionFactory.closeConnection(conn, statement);
        }
    }
    
    public List<Task> getAll(int idProject) throws SQLException{
        String sql = "SELECT * FROM tasks WHERE idProject = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        List<Task> tasks = new ArrayList<Task>();
        
        try{
            //criando a conexão
            conn = ConnectionFactory.getConnection();
            statement = conn.prepareStatement(sql);
            //informando os valores
            statement.setInt(1, idProject);
            //valor retornado pela execução da query
            resultSet = statement.executeQuery();
            
            //enquanto houver valores a serem percorridos
            while(resultSet.next()){
                Task task = new Task();
                task.setId(resultSet.getInt("id"));
                task.setIdProject(resultSet.getInt("idProject"));
                task.setName(resultSet.getString("name"));
                task.setDescription(resultSet.getString("description"));
                task.setNotes(resultSet.getString("notes"));
                task.setIsCompleted(resultSet.getBoolean("completed"));
                task.setDeadline(resultSet.getDate("deadline"));
                task.setCreatedAt(resultSet.getDate("createdAt"));
                task.setUpdatedAt(resultSet.getDate("updatedAt"));
                tasks.add(task);
            }
        }catch (RuntimeException ex){
            throw new RuntimeException("Erro ao buscar lista." + ex.getMessage(), ex);
        }finally{
            ConnectionFactory.closeConnection(conn, statement, resultSet);
        }
        //retorna lista de tarefas
        return tasks;
    }
    
}
