import React, { useState } from "react";
import '../App.css';

export default function NewPersonModal({ isOpen, onClose, onPersonAdded }) {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [birthDate, setBirthDate] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [internetSpeedMbps, setInternetSpeedMbps] = useState('50');
    const [messages, setMessages] = useState([]);

    if (!isOpen) return null;

    const handleAddPerson = async () => {
        setMessages([]);

        const newPerson = {
            firstName,
            lastName,
            birthDate,
            email,
            phoneNumber,
            internetSpeedMbps
        };

        try {
            const response = await fetch('http://localhost:8080/api/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newPerson),
            });

            const result = await response.json();

            if (result.valid) {
                await onPersonAdded();
                handleClose();
            } else {
                setMessages(result.messages);
            }

        } catch (error) {
            setMessages(["something went wrong"]);
        }
    };

    const handleClose = () => {
        setFirstName('');
        setLastName('');
        setBirthDate('');
        setEmail('');
        setPhoneNumber('');
        setInternetSpeedMbps('50');
        setMessages([]);
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <input
                    type="text"
                    placeholder="First name"
                    className="modal-input"
                    onChange={(e) => setFirstName(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="Last name"
                    className="modal-input"
                    onChange={(e) => setLastName(e.target.value)}
                />

                <div className="input-group">
                    <label className="modal-label">Birth date:</label>
                    <input
                        type="date"
                        className="modal-input"
                        value={birthDate}
                        onChange={(e) => setBirthDate(e.target.value)}
                    />
                </div>

                <input
                    type="email"
                    placeholder="Email"
                    className="modal-input"
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="Phone number"
                    className="modal-input"
                    onChange={(e) => setPhoneNumber(e.target.value)}
                />

                <div className="input-group">
                    <label className="modal-label">Home internet speed (mbps):</label>
                    <select
                        id="internetspeed"
                        className="dropdown"
                        value={internetSpeedMbps}
                        onChange={(e) => setInternetSpeedMbps(e.target.value)}
                    >
                        <option value="50">50</option>
                        <option value="100">100</option>
                        <option value="200">200</option>
                        <option value="400">400</option>
                        <option value="1000">1000</option>
                    </select>

                </div>

                {messages.length > 0 && (
                    <div className="message-box">
                        {messages.map((msg, index) => (
                            <p key={index}>{msg}</p>
                        ))}
                    </div>
                )}

                <button className="button" style={{ marginTop: '1rem' }} onClick={handleAddPerson}>
                    add new person
                </button>
                <button className="button" style={{ marginTop: '1rem' }} onClick={handleClose}>
                    close
                </button>
            </div>
        </div>
    );
}
