import React, { useState } from "react";

const PersonTable = ({ people, onEdit }) => {
    const [sortKey, setSortKey] = useState(null);
    const [sortAsc, setSortAsc] = useState(true);

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

        const aValue = a[sortKey]?.toString().toLowerCase();
        const bValue = b[sortKey]?.toString().toLowerCase();

        if (aValue < bValue) return sortAsc ? -1 : 1;
        if (aValue > bValue) return sortAsc ? 1 : -1;
        return 0;
    });

    const renderArrow = (key) => {
        if (sortKey !== key) return null;
        return sortAsc ? " △" : " ▽";
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
                    <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort("stillUsesLandline")}>
                        landline{renderArrow("stillUsesLandline")}
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
                        <td className="px-4 py-2">{person.stillUsesLandline ? "✓" : "-"}</td>
                        <td className="px-4 py-2">
                            <button
                                onClick={() => onEdit(person)}
                                className="button"
                            >
                                edit / delete
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default PersonTable;
