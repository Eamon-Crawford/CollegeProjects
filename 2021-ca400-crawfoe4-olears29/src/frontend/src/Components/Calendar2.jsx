import FullCalendar from "@fullcalendar/react";
import React, {useRef, useState} from "react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from '@fullcalendar/interaction'
import timeGridPlugin from '@fullcalendar/timegrid'
import axios from "axios";

export default function Calendar() {
    const calendarRef = useRef(null);
    const [SID, setSID] = useState("");

    const apiLink ="http://0.0.0.0:23567/users/complete-timetable/";
    const sid = {SID};
    const url = apiLink + sid;
    
    axios.get(url).then(response => {
        this.setState({data : response.data});
    })
        return(
        <section>
            <form>
            <input placeholder="Student ID" value={SID} onChange={e => setSID(e.target.value)} />
            </form>
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
                events={[this.state.data]}
                />
            </div>
        </section>
    )
}