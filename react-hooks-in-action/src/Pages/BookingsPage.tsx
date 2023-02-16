import React from "react";
import WeekPicker from "../Components/Bookings/WeekPicker";

const BookingsPage = () => {
    return (
        <main className="bookings-page">
            <p>Bookings!</p>
            <WeekPicker date={new Date()} />
        </main>
    );
};

export default BookingsPage;