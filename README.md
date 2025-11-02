<h1 align="center">
  <img width="575" height="71" alt="logo" src="https://github.com/user-attachments/assets/d5a5f191-22e6-4ace-b9e7-2f0c21262ae8"/>
</h1>

## Description

The project is a web application for viewing weather forecast in various locations around the world.

Application functionality:
- User sign up and sign in
<img width="2240" height="1400" src="https://github.com/user-attachments/assets/5e2da7e6-280a-48a3-962e-584d994219ad" />
<img width="2240" height="1400" alt="image" src="https://github.com/user-attachments/assets/3c1796a6-f642-4f19-983c-0cf95f239e82" />

- Search location by name
<img width="2240" height="1400" alt="image" src="https://github.com/user-attachments/assets/01058fb3-25eb-439a-8781-7f37ce8044d7" />

- Authorized users can view previously saved locations, as well as add new ones and delete them
<img width="2240" height="1400" alt="image" src="https://github.com/user-attachments/assets/223f0d27-f939-4084-a8dd-30a9d146eced" />

## Technology stack

| Icon                                                                                                                             | Name      | Version      |
| :------------------------------------------------------------------------------------------------------------------------------: | :-------: | :----------: |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/87c3003d-69f3-45a4-9bc0-9839cf25ea1b" /> | Java      | 21           |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/38fd6492-0966-4c4b-97a2-844477632452"/>  | Spring    | 6.2.8        |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/f7e069fd-8f84-442a-9ee4-5eaa8459345d" /> | Hibernate | 6.6.13.Final |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/9a9a44fc-3e41-4b74-a657-844d2d99b479" /> | MySql     | 9.3.0        |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/c2afb61b-498b-409f-bd20-b60f52f44f43" /> | Flyway    | 11.8.1       |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/d7d3da04-fe25-4e5b-9080-dc314a42fcc1" /> | H2        | 2.3.232      |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/8e306c25-eecf-431e-9834-c0d564a37dfc" /> | JUnit     | 5.13.0       |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/b923e76f-4dcf-4f76-8d7d-a6bcd5a7e828" /> | Mockito   | 5.14.2       |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/4c3855ae-aff4-4788-9417-08bd57bbd3b8" /> | Lombok    | 1.18.38      |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/fa41f9ef-d323-4ad3-88c6-b4fa8854b465" /> | Mapstruct | 1.6.3        |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/b8f8b7f5-cbe8-4ce6-8838-dd723a71c7ea" /> | Maven     | 3.9.9        |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/72b76346-2f93-4d59-9ebc-0a2887b27c96" /> | Tomcat    | 11           |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/0d313359-1c16-4d50-84fc-db09249a3d7c" /> | Thymeleaf | 3.1.3.RELEASE|
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/02fbbbfe-7f5d-4111-b4ca-d30ed90bbc30" /> | HTML      | 5            |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/3fcb1bdb-2400-443d-a074-7c3fa0062164" /> | CSS       | 3            |
| <img width="32" height="32" alt="image" src="https://github.com/user-attachments/assets/4dec4a2e-ec05-4a5e-a089-3c25b47c5468" /> | JS        | ES6+         |

## How to run a project locally
1. Sign up on https://openweathermap.org/ and get key to be able to receive weather information from open weather api.
2. Install **MySql 8.0.43+** or run via docker and create database.
3. Install **Tomcat 10+**.
4. Clone this repository and open project.
```
  git clone https://github.com/red-eyed-99/weather-forecast.git
```
5. For ease of launch, use **IntelliJ Idea**.
   - Add tomcat run confugration (**Run** -> **Edit Configurations...** -> **Add New Configuration** -> **Tomcat Server Local**).
   - Specify the path to the installed **Tomcat** in the **"Application server"** by clicking on **"Configure..."**.
   - Click on **"Fix"** button.
   - Choose artifact for deploy **"weather-forecast:war exploded"** and remove **"_war_exploded"** from application context path.
   - Add environment variables in the **"Startup/Connection"** section.
   ```
   DB_URL=jdbc:mysql://localhost:3306/your_database_name
   DB_DRIVER=com.mysql.cj.jdbc.Driver
   DB_USERNAME=your_database_username
   DB_PASSWORD=your_database_password
   HIBERNATE_DDL_AUTO=none
   HIBERNATE_SHOW_SQL=false
   HIBERNATE_FORMAT_SQL=false
   REST_CONNECT_TIMEOUT=5000
   REST_READ_TIMEOUT=30000
   OPENWEATHER_URL=https://api.openweathermap.org/data/2.5/weather
   OPENWEATHER_KEY=your_open_weather_key
   USER_SESSION_LIFETIME=3
   USER_SESSION_CLEANING_TIME=0 0 3 * * *
   ```
   - Click **"Apply"** button.
   - It should look something like this:
     
     <img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/583a018a-2178-497f-8469-15a6ef1184ef" />
     <img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/99800958-d628-4d7d-949f-0a1e7d37df3d" />
     <img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/1d5c5f8e-322c-4844-867c-2d1cb5b72499" />

   - Now you can launch the project and it will be available at http://localhost:8080/weather_forecast/
   
