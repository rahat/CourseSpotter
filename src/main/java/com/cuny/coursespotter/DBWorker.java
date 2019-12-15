package com.cuny.coursespotter;

import com.sendgrid.helpers.mail.objects.Content;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBWorker {

    private String connectionString;

    private String username;

    private String password;

    private String number;

    private static MessageService messageService = new MessageService();

    public DBWorker() {
        loadConfiguration();
    }

    /**
     * Fetch necessary info from the configuration file
     */
    private void loadConfiguration() {

        try (InputStream config = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();

            if (config == null) {
                System.out.println("Unable to read config file.");
                return;
            }

            properties.load(config);

            connectionString = properties.getProperty("db.connection");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
            number = properties.getProperty("twilio.number");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert the specified data into the database.
     *
     * @param email        Email Address
     * @param phone        Phone Number
     * @param college      College Name
     * @param courseName   Course Name
     * @param courseNumber Course Number
     * @param classID      Class Code
     * @return Operation Result
     */
    public int insertData(String email, String phone, String college, String courseName, int courseNumber, int classID) {

        try (Connection conn = DriverManager.getConnection(connectionString, username, password);
             PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO CourseSpotter (Email, Phone, College, CourseName, CourseNumber, ClassID) VALUES (?,?,?,?,?,?)")) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, college);
            preparedStatement.setString(4, courseName);
            preparedStatement.setInt(5, courseNumber);
            preparedStatement.setInt(6, classID);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Check the database, delete entries when a course opens and use the message service to notify user.
     *
     * @return Operation Result
     */
    public int checkData() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(connectionString, username, password);

            Statement connectionStatement = connection.createStatement();
            ResultSet resultSet = connectionStatement.executeQuery("SELECT * FROM CourseSpotter");
            while (resultSet.next()) {
                String email = resultSet.getString("Email");
                String phone = resultSet.getString("Phone");
                String college = resultSet.getString("College");
                String courseName = resultSet.getString("CourseName");
                int courseNumber = resultSet.getInt("CourseNumber");
                int classID = resultSet.getInt("ClassID");

                CourseInfo latestInfo = Parser.fetchByClassNumber(college, "1202", String.valueOf(classID));

                if (latestInfo.getStatus().equals("")) continue;
                if (latestInfo.getStatus().equals("Open")) {
                    if (!email.isEmpty()) {
                        Content content = new Content("text/plain", "Your class " + courseName + " " + courseNumber + " with Professor " + latestInfo.getInstructor() + " is now open.");
                        messageService.sendEmail("admin@coursespotter", "CourseSpotter Notification", email, content);
                    }
                    if (!phone.isEmpty()) {
                        messageService.sendText(phone, number, "Your class " + courseName + " " + courseNumber + " with Professor " + latestInfo.getInstructor() + " is now open.");
                    }
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM CourseSpotter WHERE Email=? AND Phone=? AND ClassID=?");

                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, phone);
                    preparedStatement.setInt(3, classID);

                    return preparedStatement.executeUpdate();
                }
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
