import FullCalendar from "@fullcalendar/react";
import React, {useRef, useState} from "react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from '@fullcalendar/interaction'
import timeGridPlugin from '@fullcalendar/timegrid'
import AddEventModal from "./AddEventModal";
import axios from "axios";
import moment from "moment";

export default function Calendar() {
    const [modalOpen, setModalOpen] = useState(false);
    const calendarRef = useRef(null);

    const onEventAdded = event => {
        let calendarApi = calendarRef.current.getApi()
        calendarApi.addEvent({
            start: moment(event.start).toDate(),
            end: moment(event.end).toDate(),
            title: event.title
        });
    }

    async function handleEventAdd(data) {
        await axios.post('http://0.0.0.0:23567/lecture', data.event);
    }

        return(
        <section>
            <button onClick={() => setModalOpen(true)}> Add Lecture</button>
            <button onClick={() =>setModalOpen(true)}> Delete Lecture</button>

            <div style={{postion:"relative", zIndex:0}}>
                <FullCalendar
                plugins={[dayGridPlugin, interactionPlugin, timeGridPlugin]}
                headerToolbar={{
                  left: 'prev,next today',
                  center: 'title',
                  right: 'dayGridMonth,timeGridWeek,timeGridDay'
                }}
                ref={calendarRef}
                initialView='dayGridMonth'
                eventAdd={event => handleEventAdd(event)}
                />
            </div>

            <AddEventModal 
            isOpen={modalOpen} 
            onClose={() => setModalOpen(false)} 
            onEventAdded={event => onEventAdded(event)} 
            />
        </section>
    )
}