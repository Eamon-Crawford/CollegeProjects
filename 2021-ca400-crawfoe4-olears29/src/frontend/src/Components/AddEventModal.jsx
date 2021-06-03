import React, {useState} from "react";
import Modal from "react-modal";
import DateTimePicker from "react-datetime-picker";
import "react-datetime/css/react-datetime.css"

export default function AddEventModal({isOpen, onClose, onEventAdded}) {
    const [title, setTitle] = useState("");
    const [start, setStart]= useState(new Date());
    const [end, setEnd]= useState(new Date());

    const onSubmit = (event) => {
        event.preventDefault();

        onEventAdded({
            title,
            start,
            end
        })
        onClose();
    }

    return (
        <Modal isOpen={isOpen} onRequestClose={onClose}>
            <form onSubmit={onSubmit}>
                <input placeholder="Title" value={title} onChange={e => setTitle(e.target.value)} />
                <label>Start Date</label>
                    <DateTimePicker value={start} onChange={date => setStart(date)}/>
                <label>End Date</label>
                    <DateTimePicker value={end} onChange={date => setEnd(date)} />
                <button>Add Lecture</button>
            </form>
        </Modal>
    )
}