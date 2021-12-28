package pl.camp.it.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    static Connection connection = null;

    public static void main(String[] args) {
        connect();

        User user = new User();
        user.setId(0);
        user.setName("Paweł");
        user.setSurname("Jakiś");
        user.setLogin("pawcio2");
        user.setPassword("pawcio2");

        //saveUser2(user);
        //deleteUser(13);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Bob");
        user2.setSurname("Marley");
        user2.setLogin("bob");
        user2.setPassword("bob");

        //updateUser(user2);

        List<User> allUsers = getAllUsers();
        System.out.println(allUsers);

        User userFromDb = getUserByLogin("mateusz");
        System.out.println(userFromDb);
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&characterEncoding=utf8", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveUser(User user) {
        try {
            String sql = new StringBuilder()
                    .append("INSERT INTO tuser (id, name, surname, login, password) VALUES (NULL, '")
                    .append(user.getName())
                    .append("', '")
                    .append(user.getSurname())
                    .append("', '")
                    .append(user.getLogin())
                    .append("', '")
                    .append(user.getPassword())
                    .append("');")
                    .toString();

            System.out.println(sql);

            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void saveUser2(User user) {
        try {
            String sql = "INSERT INTO tuser (id, name, surname, login, password) VALUES (NULL, ?, ?, ?, ?);";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteUser(int id) {
        try {
            String sql = "DELETE FROM tuser WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void updateUser(User user) {
        try {
            String sql = "UPDATE tuser SET name = ?, surname = ?, login = ?, password = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tuser";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                result.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("login"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    public static User getUserByLogin(String login) {
        try {
            String sql = "SELECT * FROM tuser WHERE login = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("login"),
                        rs.getString("password")
                );
            } else {
                return null;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
