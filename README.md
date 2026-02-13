# 🧩 Job Seeking Application

## 📘 Overview

The **Job Seeking Application** is a simple yet efficient GUI application developed in **Java**. It simulates the core functionalities of a real-world job portal — allowing **users** to register and apply for jobs, and **admins** to post and manage job listings.

It uses `.dat` files for data persistence, maintaining information on users, jobs, and applications in a light-weight and easily manageable format.

---

## ⚙️ Features

### 👥 User Module

* Register and login as a job seeker
* View all available jobs
* Apply for suitable positions
* View personal application history

### 🏢 Admin Module

* Login using admin credentials
* Post, view, and manage job listings
* View applications for specific jobs

### 🗃️ Data Management

* **users.dat:** Stores user information (ID, name, email, password, qualification)
* **jobs.dat:** Contains job postings (Job ID, title, company, description, skills required, location)
* **applications.dat:** Maintains application records (Application ID, User ID, Job ID, status)

---

## 📁 Project Structure

```
JobApplicationApp/
│
└── src/
    └── com/
        └── jobportal/
            │
            ├── model/                  # Data Models
            │   ├── User.java
            │   ├── Job.java
            │   └── Application.java
            │
            ├── data/                   # In-Memory Storage (NEW!)
            │   └── DataStore.java      # Replaces SQL Database
            │
            └── ui/                     # GUI Windows
                ├── LoginFrame.java     # Run this file, contains main method!
                ├── RegistrationFrame.java
                ├── EmployerDashboard.java
                └── JobSeekerDashboard.java
```

---

## 🧠 Functionality Flow

1. **User/ Admin Login**

   * User can register or log in.
   * Admin logs in with predefined credentials.
   * Upload resume.

2. **Job Management**

   * Admin can add or view jobs.
   * User can view and apply to jobs.

3. **Applications**

   * Applications are recorded in `applications.dat`.
   * Admin can view all applications per job.

4. **Data Persistence**

   * All data (users, jobs, and applications) is saved using the `pickle` module to ensure long-term storage.

---

## 💡 Example Data

### users.dat

| User ID | Name  | Email                                   | Qualification | Password |
| ------- | ----- | --------------------------------------- | ------------- | -------- |
| 101     | User1 | [example1@mail.com](mailto:example1@mail.com) | B.Tech        | 1234     |
| 102     | User2  | [example2@mail.com](mailto:example2@mail.com)   | M.Sc          | 5678     |

### jobs.dat

| Job ID | Title             | Company  | Skills      | Location  |
| ------ | ----------------- | -------- | ----------- | --------- |
| 201    | Software Engineer | XYZ | Python, SQL | Mumbai    |
| 202    | Data Analyst      | ABC | R, Power BI | Bangalore |

### applications.dat

| App ID | User ID | Job ID | Status       |
| ------ | ------- | ------ | ------------ |
| 301    | 101     | 201    | Submitted    |
| 302    | 102     | 202    | Under Review |

---

## 🚀 Benefits

* Simple file-based database system — no SQL required
* Modular structure for easier maintenance
* Supports both user and admin functionalities
* Clear data separation for scalability

---

## 🔮 Future Scope

* More smooth and fresh looking GUI using **Java Swing** or **Java AWT**
* Job **search & filter** functionality

---

## 🧰 Technologies Used

* **Language:** Java
* **Data Storage:** `.dat` files for storage





