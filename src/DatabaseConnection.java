import javax.mail.MessagingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/lab7";
    private static final String USER = "vlad";
    private static final String PASS = "ygt183";

    public static void connectionToDatabase(Messages messages) {
        String commandToDb = messages.getCommand();
        Olders argOldersToDb = messages.getArgument();
        String loginToDb = messages.getLogin();
        String passwordToDb = messages.getPassword();
        String result = " ";
        System.out.println(loginToDb);
        System.out.println(passwordToDb);
        System.out.println("Происходит подключение к базе данных");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка подключиения к базе данных! Проверьте путь к Driver!");
            messages.setAnswer("Ошибка подключиения к базе данных! Проверьте путь к Driver!");
        }
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = connection.createStatement()) {
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            switch (commandToDb) {
                //check success ++++
                case "info":
                    //++
                    if (CheckUserIntoDb.checkUser(messages) == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    messages.setAnswer("База данных представляет из себя набор старцев(обьектов Olders)," + "\n" +
                            "каждый из которых имеет поля :" + "\n" +
                            "id(уникальный идентификатор)" + "\n" +
                            "name(имя старца)" + "\n" +
                            "userid(уникальный идентификатор пользователя, который является его владельцем)" + "\n" +
                            "dateofinit-дата инициализация старца");
                    break;
                case "exit":
                    //++
                    if (CheckUserIntoDb.checkUser(messages) == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    System.exit(0);
                    break;
                case "help":
                    //++
                    if (CheckUserIntoDb.checkUser(messages) == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    messages.setAnswer("Команды программы : \n add {Object} - данная команда добавляет в базу данных старца Olders \n" +
                            "add_if_max {Object} - данная команда добавляет в базу данных старца Olders," +
                            "если id у входящего обьекта больше, чем максимальное значение id у обьектов Olders,находящихся  в базе данных \n " +
                            "remove {Object} - команда удаляет старца из базы данных(если владельцем этого обьекта являетесь именно вы) \n" +
                            "remove_lower {Object} - команда удаляет все обьекты из базы данных, id которых меньше id входящего обьекта Olders (если владельцем этого обьекта являетесь именно вы) \n" +
                            "info - выводит основную информацию о программе \n" +
                            "clear - очистка базы данных(если владельцем этого обьекта являетесь именно вы)\n" +
                            "show - вывод содержимого базы данных\n" +
                            "import - импортирует содержимое файла клиента на сервер \n" +
                            "load - загружает содержимое файла на сервере \n" +
                            "save - сохраняет содержимое файла \n" +
                            "exit - выход из программы");
                    break;
                case "show":
                    //++
                    if (CheckUserIntoDb.checkUser(messages) == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    commandToDb = "SELECT * FROM olders";
                    resultSet = statement.executeQuery(commandToDb);
                    result = "ID" + "\t" + "NAME" + "\t" + "USERID" + "\t" + "DATEOFINIT";
                    result += "\n";
                    while (resultSet.next()) {
                        result += resultSet.getString("id");
                        result += "\t";
                        result += resultSet.getString("name");
                        result += "\t";
                        result += resultSet.getString("userid");
                        result += "\t";
                        result += resultSet.getString("dateofinit");
                        result += "\n";
                    }
                    messages.setAnswer(result);
                    break;
                case "add":
                    //check success ++++
                    if (CheckUserIntoDb.checkUser(messages) == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    argOldersToDb.setUserID(CheckUserIntoDb.checkUser(messages));
                    commandToDb = "SELECT id FROM olders where id=?";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    preparedStatement.setInt(1, argOldersToDb.getId());
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        messages.setAnswer("Обьект уже существует в базе данных! Попробуйте еще раз");
                        break;
                    }
                    argOldersToDb.setDateOfInitialization(OffsetDateTime.now());
                    commandToDb = "INSERT INTO olders (id,name,userid,dateofinit) VALUES (?,?,?,?)";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    preparedStatement.setLong(1, argOldersToDb.getId());
                    preparedStatement.setString(2, argOldersToDb.getName());
                    preparedStatement.setInt(3, argOldersToDb.getUserID());
                    preparedStatement.setString(4, argOldersToDb.getDateOfInitialization().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    preparedStatement.executeUpdate();
                    System.out.println(argOldersToDb.toString());
                    messages.setAnswer("Обьект " + argOldersToDb.toString() + " был добавлен в коллекцию");
                    break;
                case "add_if_max":
                    if (CheckUserIntoDb.checkUser(messages) == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    //check access
                    argOldersToDb.setDateOfInitialization(OffsetDateTime.now());
                    argOldersToDb.setUserID(CheckUserIntoDb.checkUser(messages));
                    commandToDb = "SELECT id FROM olders where id=?";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    preparedStatement.setInt(1, argOldersToDb.getId());
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        messages.setAnswer("Обьект уже существует в базе данных! Попробуйте еще раз");
                        break;
                    }
                    commandToDb = "SELECT * FROM olders where id>?";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    preparedStatement.setInt(1, argOldersToDb.getId());
                    resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        commandToDb = "insert into olders (id,name,userid,dateofinit) VALUES (?,?,?,?)";
                        preparedStatement = connection.prepareStatement(commandToDb);
                        preparedStatement.setInt(1, argOldersToDb.getId());
                        preparedStatement.setString(2, argOldersToDb.getName());
                        preparedStatement.setInt(3, argOldersToDb.getUserID());
                        preparedStatement.setString(4,  argOldersToDb.getDateOfInitialization().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                        preparedStatement.executeUpdate();
                        messages.setAnswer("Обьект " + argOldersToDb.toString() + " был добавлен в коллекцию");
                        break;
                    }
                    messages.setAnswer("Данный обьект не максимальный!");
                    break;
                case "clear":
                    //++
                    if (CheckUserIntoDb.checkUser(messages) == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    commandToDb = "DELETE FROM olders WHERE userid=?";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    preparedStatement.setInt(1, CheckUserIntoDb.checkUser(messages));
                    if (preparedStatement.executeUpdate() > 0) {
                        messages.setAnswer("База данных успешно очищена!(Были удалены только те обьекты, владельцами которых являлись вы)");
                        break;
                    }
                    messages.setAnswer("Обьекты не удалены тк либо вы не являетесь владельцем, либо коллекция итак пуста!");
                    break;
                case "remove":
                    //check success +++
                    argOldersToDb.setUserID(CheckUserIntoDb.checkUser(messages));
                    if (argOldersToDb.getUserID() == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    commandToDb = "select * FROM olders WHERE id = ?";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    preparedStatement.setInt(1, argOldersToDb.getId());
                    resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        messages.setAnswer("Обьекта не существует в базе данных!");
                        break;
                    }
                    commandToDb = "DELETE  FROM olders WHERE id=? AND userid=?";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    preparedStatement.setInt(1, argOldersToDb.getId());
                    preparedStatement.setInt(2, argOldersToDb.getUserID());
                    if (preparedStatement.executeUpdate() > 0) {
                        messages.setAnswer("Обьект успешно удален!");
                        break;
                    }

                    messages.setAnswer("Обьект не может быть удален , так как вы не являетесь его владельцем!");
                    break;
                case "import":
                    ConcurrentSkipListSet<Olders> file = messages.getFile();
                    if (CheckUserIntoDb.checkUser(messages) == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    commandToDb = "DELETE FROM olders";
                    statement.executeUpdate(commandToDb);
                    commandToDb = "insert into olders (id,name,userid,dateofinit) VALUES (?,?,?,?)";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    for (Olders o : file) {
                        o.setUserID(CheckUserIntoDb.checkUser(messages));
                        o.setDateOfInitialization(OffsetDateTime.now());
                        preparedStatement.setInt(1, o.getId());
                        preparedStatement.setString(2, o.getName());
                        preparedStatement.setInt(3, o.getUserID());
                        preparedStatement.setString(4,  o.getDateOfInitialization().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                        preparedStatement.executeUpdate();
                    }
                    messages.setAnswer("Данные из файла были успешно импортированы в базу данных!");
                    break;
                case "remove_lower":
                    //check required and code
                    argOldersToDb.setUserID(CheckUserIntoDb.checkUser(messages));
                    if (argOldersToDb.getUserID() == -1) {
                        messages.setAnswer("Identification failed");
                        break;
                    }
                    commandToDb = "DELETE FROM olders WHERE id<? AND userid=?";
                    preparedStatement = connection.prepareStatement(commandToDb);
                    preparedStatement.setInt(1, argOldersToDb.getId());
                    preparedStatement.setInt(2, argOldersToDb.getUserID());
                    if (preparedStatement.executeUpdate() > 0) {
                        messages.setAnswer("Обьекты ,владельцем которых вы являетесь, были удалены из коллекции!");
                        break;
                    }
                    messages.setAnswer("Обьекты не были удалены тк не соответствуют условию(меньше заданного и вы являетесь их владельцем");
                    break;
                case "reg":
                    //check success +++
                    preparedStatement = connection.prepareStatement("SELECT login From users WHERE login=?");
                    preparedStatement.setString(1, loginToDb);
                    ResultSet resultSetToRegistration = preparedStatement.executeQuery();
                    if (resultSetToRegistration.next()) {
                        System.out.println("fail");
                        messages.setAnswer("Registration failed");
                        break;
                    }
                    passwordToDb = new Random().ints(12, 97, 122).mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
                    System.out.println(passwordToDb);
                    try {
                        MailSender.sendEmail(loginToDb, passwordToDb);
                    } catch (MessagingException e) {
                        System.out.println("Ошибка отправки сообщения! Возможно, данной почты не существует! Попробуйте еще раз!");
                        messages.setAnswer("Illegal mailaddress");
                        break;
                    }
                    try {
                        passwordToDb = hashPassword(passwordToDb);
                    } catch (NoSuchAlgorithmException e) {
                        System.out.println("Данного алгоритма не существует!");
                        messages.setAnswer("Registration failed");
                        break;
                    }
                    preparedStatement = connection.prepareStatement("insert into users (login, password) VALUES (?,?)");
                    preparedStatement.setString(1, loginToDb);
                    preparedStatement.setString(2, passwordToDb);
                    preparedStatement.execute();

                    //System.out.println(passwordToDb);
                    messages.setAnswer("Registration success");
                    break;
                case "Authorization":
                    //check success
                    try {
                        passwordToDb = hashPassword(passwordToDb);
                    } catch (NoSuchAlgorithmException e) {
                        System.out.println("Данного алгоритма не существует!");
                        messages.setAnswer("Authorization failed");
                        break;
                    }
                    preparedStatement = connection.prepareStatement("select login,password from users where login=? and password=?");
                    preparedStatement.setString(1, loginToDb);
                    preparedStatement.setString(2, passwordToDb);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        messages.setAnswer("Authorization success");
                        break;
                    }
                    messages.setAnswer("Authorization failed");
                    break;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("Ошибка подключения к базе данных! Проверьте правильность введенных данных!");
            messages.setAnswer("Ошибка подключения к базе данных! Проверьте правильность введенных данных!");
        }
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest passDigest = MessageDigest.getInstance("SHA-384");
        byte[] passBytes = passDigest.digest(password.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : passBytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }
}
