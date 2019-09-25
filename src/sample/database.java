package sample;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class database extends Main{

    private Connection con;
    private Statement statement;
    private ResultSet res;

    public database(Connection con, Statement statement, ResultSet res){
        this.con = con;
        this.statement = statement;
        this.res = res;
    }

    public Connection getConnection(){
        return con;
    }

    public Statement getStatement(){
        return statement;
    }

    public ResultSet getResultSet() {
        return res;
    }

    public void getUsers() throws SQLException{

        StringBuffer users = new StringBuffer();

        while(res.next()){

            users.append("Username: " + res.getString("first") + "\n");
        }
        System.out.println(users);
    }

    public boolean checkUserExists(String username) throws SQLException{
        res = statement.executeQuery("SELECT * FROM registration");
        while(res.next()){
            if(username.equals(res.getString("first"))){
                res.close();
                return true;
            }
        }
        res.close();
        return false;
    }
}
