import React, { useState } from "react";
import '../App.css';

export default function EditPersonModal({ onClose, personToEdit, onEdited, onDeleted }) {
    const [state, setState] = useState({
        firstName: personToEdit.firstName,
        lastName: personToEdit.lastName,
        birthDate: personToEdit.birthDate,
        email: personToEdit.email,
        phoneNumber: personToEdit.phoneNumber,
        internetSpeed: personToEdit.internetSpeedMbps,
    })
    const [validationErrors, setValidationErrors] = useState([])

    const handleEditPerson = async () => {
        setValidationErrors([])

        try {
            const response = await fetch('http://localhost:8080/api/edit', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    id: personToEdit.id,
                    firstName: state.firstName,
                    lastName: state.lastName,
                    birthDate: state.birthDate,
                    email: state.email,
                    phoneNumber: state.phoneNumber,
                    internetSpeedMbps: state.internetSpeed
                }),
            });

            const result = await response.json();

            if (result.valid) {
                await onEdited();
                handleClose();
            } else {
                setValidationErrors(result.messages)
            }
        } catch (error) {
            setValidationErrors(["Something went wrong"])
        }
    };

    const handleDeletePerson = async () => {
        if (!window.confirm("Are you sure you want to delete this person?")) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/delete/${personToEdit.id}`, {
                method: 'DELETE',
            });
            const result = await response.json();

            if (result === true) {
                await onDeleted();
                handleClose();
            } else {
                setValidationErrors(["failed to delete person"]);
            }

        } catch (error) {
            setValidationErrors(["something went wrong while deleting"]);
        }
    };

    const handleClose = () => {
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <input
                    type="text"
                    value={state.firstName}
                    placeholder="First name"
                    className="modal-input"
                    onChange={(e) => setState({ ...state, firstName: e.target.value })}
                />
                <input
                    type="text"
                    value={state.lastName}
                    placeholder="Last name"
                    className="modal-input"
                    onChange={(e) => setState({ ...state, lastName: e.target.value })}
                />

                <div className="input-group">
                    <span className="modal-label">Birth date:</span>
                    <p>{state.birthDate}</p>
                </div>

                <input
                    type="email"
                    value={state.email}
                    placeholder="Email"
                    className="modal-input"
                    onChange={(e) => setState({ ...state, email: e.target.value })}
                />
                <input
                    type="text"
                    value={state.phoneNumber}
                    placeholder="Phone number"
                    className="modal-input"
                    onChange={(e) => setState({ ...state, phoneNumber: e.target.value })}
                />

                <div className="input-group">
                    <span className="modal-label">Home internet speed (mbps):</span>
                    <select
                        id="internetspeed"
                        className="dropdown"
                        value={state.internetSpeed}
                        onChange={(e) => setState({ ...state, internetSpeed: e.target.value })}
                    >
                        <option value="50">50</option>
                        <option value="100">100</option>
                        <option value="200">200</option>
                        <option value="400">400</option>
                        <option value="1000">1000</option>
                    </select>
                </div>

                {validationErrors.length > 0 && (
                    <div className="message-box">
                        {validationErrors.map((msg, index) => (
                            <p key={index}>{msg}</p>
                        ))}
                    </div>
                )}

                <button className="button" style={{ marginTop: '1rem' }} onClick={handleEditPerson}>
                    save changes
                </button>
                <button className="button" style={{ marginTop: '1rem' }} onClick={handleDeletePerson}>
                    delete person
                </button>
                <button className="button" style={{ marginTop: '1rem' }} onClick={handleClose}>
                    cancel
                </button>
            </div>
        </div>
    );
}
