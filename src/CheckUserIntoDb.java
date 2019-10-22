import java.security.NoSuchAlgorithmException;
import java.sql.*;
public class CheckUserIntoDb {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/lab7";
    private static final String USER = "vlad";
    private static final String PASS = "ygt183";

    public static int checkUser(Messages messages){
        String checkloginIntoDb= messages.getLogin();
        try {
            String checkPasswordIntoDb = DatabaseConnection.hashPassword(messages.getPassword());
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            System.out.println("Драйвер не найден!");
        }
        System.out.println("Идентификация пользователя!");
        try(Connection connection = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection.createStatement()){
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            String commandToDb="SELECT id FROM users WHERE login= ? AND password =? ";
            preparedStatement=connection.prepareStatement(commandToDb);
            preparedStatement.setString(1,checkloginIntoDb);
            preparedStatement.setString(2,checkPasswordIntoDb);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                System.out.println("идентификация пользователя " + checkloginIntoDb+" прошла успешна! Доступ разрешен");
                System.out.println(resultSet.getInt("id"));
                return resultSet.getInt("id");
            }
                System.out.println("Идентификация не прошла успешно!Проверьте введенные данные!");
                return -1;


        }catch(SQLException e){

        }
        }catch (NoSuchAlgorithmException e){
            System.out.println("Данный алгоритм не поддерживается");
        }
        return -1;
    }
}
