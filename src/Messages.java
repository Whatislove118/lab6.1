
import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListSet;

public class Messages implements Serializable {
    private static final long serialVersionUID = 1L;

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
    public Messages(String command) {
        this.command = command;
    }
    private String password;
    private String login;
    private String command;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ConcurrentSkipListSet<Olders> getFile() {
        return file;
    }

    public void setFile(ConcurrentSkipListSet<Olders> file) {
        this.file = file;
    }

    private ConcurrentSkipListSet<Olders> file=null;

    public Olders getArgument() {
        return this.argument;
    }

    private  Olders argument;
    private String answer;

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCommand() {
        return this.command;
    }


    public Messages(String c, Olders a){
        this.command=c;
        this.argument = a;
        System.out.println("Запрос к серверу сформирован");
    }
    public Messages(String c, String login, String password){
        this.command=c;
        this.login=login;
        this.password=password;
    }
    public Messages(String c, String login){
        this.command=c;
        this.login=login;
    }
    @Override
    public String toString() {
        return   this.command + " " + this.argument;
    }


}

