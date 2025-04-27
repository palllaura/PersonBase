import React, { useState } from "react";
import '../App.css';

export default function NewPersonModal({ onClose, onPersonAdded }) {
    const [state, setState] = useState({
        firstName: "",
        lastName: "",
        birthDate: "",
        email: "",
        phoneNumber: "",
        internetSpeed: "",
        errorMessages: []
    })
    const [validationErrors, setValidationErrors] = useState([])

    const handleAddPerson = async () => {
        setValidationErrors([])

        try {
            const response = await fetch('http://localhost:8080/api/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    firstName: state.firstName,
                    lastName: state.lastName,
                    birthDate: state.birthDate,
                    email: state.email,
                    phoneNumber: state.phoneNumber,
                    internetSpeedMbps: state.internetSpeed
                })
            });

            const result = await response.json();

            if (result.valid) {
                await onPersonAdded();
                onClose();
            } else {
                setValidationErrors(result.messages)
            }
        } catch (error) {
            setValidationErrors(["Something went wrong"])
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <input
                    type="text"
                    placeholder="First name"
                    className="modal-input"
                    onChange={(e) => setState({ ...state, firstName: e.target.value })}
                />
                <input
                    type="text"
                    placeholder="Last name"
                    className="modal-input"
                    onChange={(e) => setState({ ...state, lastName: e.target.value })}
                />

                <div className="input-group">
                    <span className="modal-label">Birth date:</span>
                    <input
                        type="date"
                        className="modal-input"
                        onChange={(e) => setState({ ...state, birthDate: e.target.value })}
                    />
                </div>

                <input
                    type="email"
                    placeholder="Email"
                    className="modal-input"
                    onChange={(e) => setState({ ...state, email: e.target.value })}
                />
                <input
                    type="text"
                    placeholder="Phone number"
                    className="modal-input"
                    onChange={(e) => setState({ ...state, phoneNumber: e.target.value })}
                />

                <div className="input-group">
                    <span className="modal-label">Home internet speed:</span>
                    <select
                        id="internetspeed"
                        className="dropdown"
                        onChange={(e) => {
                            const selectedSpeed = e.target.value;
                            setState({
                                ...state,
                                internetSpeed: `${selectedSpeed}`
                            });
                        }}
                    >
                        <option value="50 Mbps">50 Mbps</option>
                        <option value="100 Mbps">100 Mbps</option>
                        <option value="200 Mbps">200 Mbps</option>
                        <option value="400 Mbps">400 Mbps</option>
                        <option value="1000 Mbps">1000 Mbps</option>
                    </select>
                </div>

                {validationErrors.length > 0 && (
                    <div className="message-box">
                        {validationErrors.map((msg, index) => (
                            <p key={index}>{msg}</p>
                        ))}
                    </div>
                )}

                <button className="button" style={{ marginTop: '1rem' }} onClick={handleAddPerson}>
                    add new person
                </button>
                <button className="button" style={{ marginTop: '1rem' }} onClick={onClose}>
                    close
                </button>
            </div>
        </div>
    );
}
