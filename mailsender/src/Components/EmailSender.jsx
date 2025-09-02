import React, { useState } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { motion } from "framer-motion";
import "./EmailSender.css";

const EmailSender = () => {
    const [recipient, setRecipient] = useState(""); // Comma-separated emails
    const [subject, setSubject] = useState("");
    const [body, setBody] = useState("");
    const [attachments, setAttachments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});
    const [successMessage, setSuccessMessage] = useState("");
    const [value, setValue] = useState("");

    const quillModules = {
        toolbar: [
            [{ font: [] }, { size: [] }, { header: [1, 2, 3, 4, 5, 6, false] }],
            ["bold", "italic", "underline", "strike", "blockquote", "code-block"],
            [{ align: [] }, { color: [] }, { background: [] }],
            [{ list: "ordered" }, { list: "bullet" }, { indent: "-1" }, { indent: "+1" }],
            ["link", "image", "video"],
            ["clean"]
        ]
    };

    const validateEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());

    const handleFileUpload = (e) => {
        setAttachments([...attachments, ...Array.from(e.target.files)]);
    };

    const removeAttachment = (idx) => {
        setAttachments(attachments.filter((_, i) => i !== idx));
    };

    const validateForm = () => {
        let errs = {};
        const recipientsArray = recipient.split(",").map(r => r.trim()).filter(r => r);

        if (recipientsArray.length === 0) {
            errs.recipient = "Recipient required";
        } else if (!recipientsArray.every(validateEmail)) {
            errs.recipient = "One or more email addresses are invalid";
        }
        if (!subject.trim()) errs.subject = "Subject required";
        if (!body.trim()) errs.body = "Message required";

        setErrors(errs);
        return Object.keys(errs).length === 0;
    };

    const sendEmail = async () => {
        if (!validateForm()) return;
        try {
            setLoading(true);
            setErrors({});
            setSuccessMessage("");

            const recipientsArray = recipient.split(",").map(r => r.trim()).filter(r => r);
            let response;

            if (attachments.length === 0) {
                // Send as JSON (no file)
                response = await fetch("http://localhost:8080/api/v1/email/send", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        to: recipientsArray, // always array
                        subject,
                        body
                    }),
                });
            } else {
                const formData = new FormData();

                const emailRequest = {
                    to: recipientsArray, // always array
                    subject,
                    body,
                };

                formData.append(
                    "request",
                    new Blob([JSON.stringify(emailRequest)], { type: "application/json" })
                );

                attachments.forEach(file => formData.append("file", file));

                response = await fetch("http://localhost:8080/api/v1/email/send-with-file", {
                    method: "POST",
                    body: formData,
                });
            }

            if (!response.ok) throw new Error("Failed to send");

            setRecipient("");
            setSubject("");
            setBody("");
            setAttachments([]);
            setSuccessMessage("✅ Email sent successfully!");
        } catch (err) {
            setErrors({ api: err.message });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="app-background">
            <motion.div
                className="email-card"
                initial={{ opacity: 0, y: -20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6 }}
            >
                <h2 className="animated-heading">Compose Email</h2>

                {/* Recipient */}
                <div className="form-group">
                    <label>Recipient(s)</label>
                    <input
                        type="text"
                        value={recipient}
                        onChange={e => setRecipient(e.target.value)}
                        placeholder="Enter one or more emails, separated by commas"
                        required
                    />
                    {errors.recipient && <span className="error">{errors.recipient}</span>}
                </div>

                {/* Subject */}
                <div className="form-group">
                    <label>Subject</label>
                    <input
                        type="text"
                        value={subject}
                        onChange={e => setSubject(e.target.value)}
                        placeholder="Email Subject"
                        required
                    />
                    {errors.subject && <span className="error">{errors.subject}</span>}
                </div>

                {/* Editor */}
                {/* Message / HTML Input */}
                <div className="editor-wrapper">
                    <ReactQuill
                        value={body}
                        onChange={setBody}
                        modules={quillModules}
                        placeholder="Write your email..."
                        className="bg-white rounded-2xl shadow-lg border border-gray-300 focus-within:border-blue-500 transition-all min-h-[200px]"
                    />
                </div>


                {errors.body && <span className="error">{errors.body}</span>}

                {/* Attachments */}
                <div className="attachments-card">
                    <h3>Attachments</h3>
                    <input type="file" multiple onChange={handleFileUpload} />
                    {attachments.length > 0 ? (
                        <ul>
                            {attachments.map((file, idx) => (
                                <li key={idx} className="attachment-item">
                                    {file.name}
                                    <button onClick={() => removeAttachment(idx)}>✖</button>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="empty-attachments">No files attached</p>
                    )}
                </div>

                {/* Status Messages */}
                {errors.api && <span className="error">{errors.api}</span>}
                {successMessage && <span className="success">{successMessage}</span>}

                {/* Button */}
                <motion.button
                    whileHover={{ scale: loading ? 1 : 1.05 }}
                    whileTap={{ scale: loading ? 1 : 0.95 }}
                    onClick={sendEmail}
                    disabled={loading}
                    className={`send-btn ${loading ? "loading-btn" : ""}`}
                >
                    {loading ? (
                        <div className="spinner-wrapper">
                            <div className="spinner"></div>
                            <span>Sending...</span>
                        </div>
                    ) : (
                        "Send Email 🚀"
                    )}
                </motion.button>
            </motion.div>
        </div>
    );
};

export default EmailSender;
