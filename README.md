📧 Email Sender & Fetcher Application

A full-stack Email Application built with Spring Boot (backend) and ReactJS (frontend).
This app allows you to send emails (text, HTML, with attachments) and fetch the latest emails from a specific inbox.

🚀 Features
🔹 Backend (Spring Boot – email-sender)

Send plain text emails

Send HTML emails with rich formatting

Send emails with multiple attachments

Fetch emails from a specific inbox (IMAP)

Retrieve latest 5 emails with subject, content & attachments

REST API endpoints to connect with any frontend

🔹 Frontend (ReactJS – mail-sender)

Clean Gmail-like UI for inbox preview

Displays list of latest 5 emails

Expandable email view with HTML rendering

Shows attachments (if available)

Easy integration with Spring Boot API

🛠️ Tech Stack

Backend: Spring Boot, Jakarta Mail (JavaMail), Maven

Frontend: React.js, Fetch API

Language: Java, JavaScript

Mail Protocols: SMTP (send), IMAP (fetch)

📂 Project Structure
root
│── email-sender/    # Backend (Spring Boot)
│   ├── controller   # REST API endpoints
│   ├── service      # Email logic (send & fetch)
│   └── entity/model # Message model
│
│── mail-sender/     # Frontend (ReactJS)
│   ├── src/components  # UI Components
│   ├── src/styles      # CSS files
│   └── ...
│
└── README.md

⚡ How to Run
1️⃣ Backend (Spring Boot)

Navigate to the backend folder:

cd email-sender


Add your mail configuration in application.properties:

spring.mail.host=smtp.yourprovider.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_password


Run the backend:

mvn spring-boot:run


Backend will start at http://localhost:8080

2️⃣ Frontend (ReactJS)

Navigate to the frontend folder:

cd mail-sender


Install dependencies:

npm install


Start the frontend:

npm start


Frontend will start at http://localhost:3000
 and will fetch data from backend.

🎯 API Endpoints
Send Email
POST /api/send-email


Request body:

{
  "to": "receiver@example.com",
  "subject": "Test Email",
  "content": "<h1>Hello</h1> This is a test email",
  "attachments": ["file1.pdf", "image.png"]
}

Fetch Emails
GET /api/emails


Response:

[
  {
    "subjects": "Welcome!",
    "content": "<p>Hello, user!</p>",
    "files": []
  },
  ...
]

📌 Future Enhancements

OAuth2 support (Gmail secure login)

Pagination for fetching more emails

Database integration for storing sent/received emails

Richer frontend with search & filters

👨‍💻 Author

Made with ❤️ using Spring Boot + ReactJS
