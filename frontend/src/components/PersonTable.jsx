import React, { useState } from "react";
import EditPersonModal from './EditPersonModal';

const PersonTable = ({ people, didEdit, didDelete }) => {
    const [sortKey, setSortKey] = useState(null);
    const [sortAsc, setSortAsc] = useState(true);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [personToEdit, setPersonToEdit] = useState(null);

    const handleSort = (key) => {
        if (key === sortKey) {
            setSortAsc(!sortAsc);
        } else {
            setSortKey(key);
            setSortAsc(true);
        }
    };

    const sortedPeople = [...people].sort((a, b) => {
        if (!sortKey) return 0;

        const aValue = a[sortKey];
        const bValue = b[sortKey];

        const aIsNumber = typeof aValue === 'number';
        const bIsNumber = typeof bValue === 'number';

        if (aIsNumber && bIsNumber) {
            return sortAsc ? aValue - bValue : bValue - aValue;
        } else {
            const aStr = String(aValue).toLowerCase();
            const bStr = String(bValue).toLowerCase();
            if (aStr < bStr) return sortAsc ? -1 : 1;
            if (aStr > bStr) return sortAsc ? 1 : -1;
            return 0;
        }
    });

    const renderArrow = (key) => {
        if (sortKey !== key) return null;
        return sortAsc ? " △" : " ▽";
    };

    const handleEditClick = (person) => {
        setPersonToEdit(person);
        setIsEditModalOpen(true);
    };

    const handleCloseEditModal = () => {
        setIsEditModalOpen(false);
        setPersonToEdit(null);
    };

    return (
        <div>
            <table className="table">
                <thead>
                <tr>
                    <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort("id")}>
                        id{renderArrow("id")}
                    </th>
                    <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort("firstName")}>
                        first name{renderArrow("firstName")}
                    </th>
                    <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort("lastName")}>
                        last name{renderArrow("lastName")}
                    </th>
                    <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort("birthDate")}>
                        birthdate{renderArrow("birthDate")}
                    </th>
                    <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort("email")}>
                        email{renderArrow("email")}
                    </th>
                    <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort("phoneNumber")}>
                        phone{renderArrow("phoneNumber")}
                    </th>
                    <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort("internetSpeedMbps")}>
                        internet speed (mbps){renderArrow("internetSpeedMbps")}
                    </th>
                    <th className="px-4 py-2">actions</th>
                </tr>
                </thead>
                <tbody>
                {sortedPeople.map((person) => (
                    <tr key={person.id}>
                        <td className="px-4 py-2">{person.id}</td>
                        <td className="px-4 py-2">{person.firstName}</td>
                        <td className="px-4 py-2">{person.lastName}</td>
                        <td className="px-4 py-2">{person.birthDate}</td>
                        <td className="px-4 py-2">{person.email}</td>
                        <td className="px-4 py-2">{person.phoneNumber}</td>
                        <td className="px-4 py-2">{person.internetSpeedMbps}</td>
                        <td className="px-4 py-2">
                            <button
                                onClick={() => handleEditClick(person)}
                                className="button"
                            >
                                edit / delete
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {isEditModalOpen && (
                <EditPersonModal
                    isOpen={isEditModalOpen}
                    onClose={handleCloseEditModal}
                    personToEdit={personToEdit}
                    onEdited={didEdit}
                    onDeleted={didDelete}
                />
            )}
        </div>
    );
};

export default PersonTable;
