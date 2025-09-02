import { useEffect, useState } from "react";
import "./EmailList.css"; // import CSS

export default function EmailList() {
    const [emails, setEmails] = useState([]);
    const [expanded, setExpanded] = useState(null);

    useEffect(() => {
        fetch("http://localhost:8080/api/v1/email/emails") // adjust URL if needed
            .then((res) => res.json())
            .then((data) => setEmails(data))
            .catch((err) => console.error("Error fetching emails:", err));
    }, []);

    const toggleExpand = (index) => {
        setExpanded(expanded === index ? null : index);
    };

    return (
        <div className="email-container">
            <h1 className="email-title">📧 Inbox (Latest 5 Emails)</h1>

            {emails.length === 0 ? (
                <p className="no-emails">No emails found.</p>
            ) : (
                <ul className="email-list">
                    {emails.map((email, index) => (
                        <li
                            key={index}
                            className={`email-item ${expanded === index ? "expanded" : ""}`}
                            onClick={() => toggleExpand(index)}
                        >
                            <div className="email-subject">
                                {email.subjects || "No Subject"}
                            </div>

                            {expanded === index && (
                                <div className="email-content">
                                    {/* Render HTML safely */}
                                    <div
                                        className="email-html-content"
                                        dangerouslySetInnerHTML={{
                                            __html: email.content || "<p>No Content</p>",
                                        }}
                                    ></div>

                                    {email.files && email.files.length > 0 && (
                                        <div className="attachments">
                                            <strong>Attachments:</strong>
                                            <ul>
                                                {email.files.map((file, i) => (
                                                    <li key={i}>{file}</li>
                                                ))}
                                            </ul>
                                        </div>
                                    )}
                                </div>
                            )}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
