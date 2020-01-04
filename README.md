# CourseSpotter

CourseSpotter is a Java web application built with Spring Boot that will periodically check CUNYfirst (City University of New York's student information system) to see course availability for closed courses where all seats have been taken. The information that the user inputs are CUNY College Name, Subject, Course Number, Phone Number and/or Email Address. When the course is available, the application sends a text message and/or email to the student.

## Technologies

- Frameworks: Spring Boot, Vaadin
- APIs: Twilio, SendGrid
- Database: Microsoft SQL Server
- Build Tool: Maven
- Target Platform: Desktop, Mobile
- Operating Systems: Windows, Mac, Linux

## Configuration

In the `config.properties` file, the following keys need to be set
- db.connection (MSSQL Connection String)
- db.username (MSSQL Username)
- db.password (MSSQL Password)
- twilio.accountsid (From Twilio)
- twilio.authtoken (From Twilio)
- twilio.number (From Twilio)
- sendgrid.key (From SendGrid)

Execute the .sql file located at `sql/CourseSpotter.sql` in SQL Server Management Studio or your preferred tool to create the database table.