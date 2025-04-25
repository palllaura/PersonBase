import Navbar from './components/Navbar.jsx';
import PersonTable from './components/PersonTable';

import './App.css'
import {useEffect, useState} from "react";

function App() {
    const [people, setPeople] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    const filteredPeople = people.filter((person) =>
        Object.values(person).some(value =>
            String(value).toLowerCase().includes(searchTerm.toLowerCase())
        )
    );

    useEffect(() => {
        fetch('http://localhost:8080/api/people')
            .then(res => res.json())
            .then(data => setPeople(data));
    }, []);

    return (
        <div>
            <main className="main">
                <Navbar onSearch={setSearchTerm} />
                <PersonTable people={filteredPeople} />
            </main>
        </div>
)
}

export default App
