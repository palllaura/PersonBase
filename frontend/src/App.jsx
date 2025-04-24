import logo from './assets/logo.png'
import './App.css'
import {useEffect, useState} from "react";

function App() {

    const [people, setPeople] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/api/people')
            .then(response => {
                if (!response.ok) throw new Error("Network response was not ok");
                return response.json();
            })
            .then(data => {
                console.log('Received:', data); // add this line
                setPeople(data);
            })
            .catch(error => console.error('Error fetching people:', error));
    }, []);


    return (
        <div>
            <img src={logo} className="logo" alt="personbase logo"/>
            <ul>
                {people.map((person) => (
                    <li key={person.id}>{person.firstName}</li>
                ))}
            </ul>
        </div>
)
}

export default App
