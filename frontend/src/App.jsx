import Navbar from './components/Navbar.jsx';
import PersonTable from './components/PersonTable';
import NewPersonModal from './components/NewPersonModal.jsx';

import './App.css'
import {useEffect, useState} from "react";

function App() {
    const [people, setPeople] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    const filteredPeople = people.filter((person) =>
        Object.values(person).some(value =>
            String(value).toLowerCase().includes(searchTerm.toLowerCase())
        )
    );

    const fetchPeopleAgain = () => {
        fetch('http://localhost:8080/api/people')
            .then(res => res.json())
            .then(data => setPeople(data));
    };

    useEffect(() => {
        fetchPeopleAgain();
    }, []);


    return (
        <div>
            <main className="main">
                <Navbar onSearch={setSearchTerm} onAddPerson={() => setIsModalOpen(true)} />
                <PersonTable people={filteredPeople} />
                <NewPersonModal
                    isOpen={isModalOpen}
                    onClose={() => setIsModalOpen(false)}
                    onPersonAdded={fetchPeopleAgain}
                />
            </main>
        </div>
)
}

export default App
