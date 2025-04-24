import { useState } from 'react';

function PersonForm() {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        birthDate: '',
        email: '',
        phoneNumber: '',
    });

    const [messages, setMessages] = useState([]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessages([]); // clear previous messages

        try {
            const response = await fetch('http://localhost:8080/persons', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            });

            const result = await response.json();
            if (result.valid) {
                setMessages(['Person saved successfully!']);
                setFormData({ firstName: '', lastName: '', birthDate: '', email: '', phoneNumber: '' });
            } else {
                setMessages(result.messages);
            }
        } catch (err) {
            console.error(err);
            setMessages(['Something went wrong. Please try again.']);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input name="firstName" placeholder="First Name" value={formData.firstName} onChange={handleChange} />
            <input name="lastName" placeholder="Last Name" value={formData.lastName} onChange={handleChange} />
            <input name="birthDate" type="date" value={formData.birthDate} onChange={handleChange} />
            <input name="email" placeholder="Email" value={formData.email} onChange={handleChange} />
            <input name="phoneNumber" placeholder="Phone Number" value={formData.phoneNumber} onChange={handleChange} />
            <button type="submit">Save Person</button>

            <ul>
                {messages.map((msg, i) => <li key={i}>{msg}</li>)}
            </ul>
        </form>
    );
}

export default PersonForm;
