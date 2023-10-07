# EmployeeEventManager

The "EmployeeEventManager" project is an application based on microservices architecture, created using various technologies and libraries
such as Spring Data JPA, Spring Web, Eureka, Lombok, Spring Data MongoDB, Spring for RabbitMQ, OpenFeign, and Java Mail Sender.
It is a project that integrates multiple advanced components, enabling the management of employees, departments, and the organization of events within a company.

The main features of the application include:

Employee Management - The application allows for the creation of new employees and stores essential information about each of them.

Department Management - You can create new departments within the company and assign employees to specific departments, making it easier to organize and structure teams.

Event Management - The application enables the creation of various types of events, such as meetings, training sessions, or company events. Each event has its details and a list of participants.

Integration with RabbitMQ - Thanks to Spring for RabbitMQ, the application handles asynchronous communication, which is useful for tasks such as sending notifications.

Email Notifications - The application allows for sending email notifications to employees participating in events, utilizing Java Mail Sender.

Multi-Database Data Handling - The project uses two different databases, PostgreSQL and MongoDB, to store data about employees, departments, and events. This allows for optimizing data storage based on their characteristics.

In addition, the application has been thoroughly tested, including testing in Postman for API endpoints and the development of unit tests to ensure robustness and reliability.
