import React from 'react';
import Modal from 'react-modal';
import Calendar from '../../Components/Calendar';

Modal.setAppElement('#root')

function TimetableL() {
    return (
        <>
        <Calendar />
        </>
    );
}

export default TimetableL;
