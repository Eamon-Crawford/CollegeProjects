import axios from 'axios';
import React, { useState, useEffect } from 'react'
import {Bar} from 'react-chartjs-2'

const DynamicChart = () => {
    const [chartData, setChartData]  = useState({});
    const [attendanceNumber, setAttendanceNumber] = useState([]);
    const [ModuleName, setModuleName] = useState([]);
    const [Course, setCourse] = useState([])
    
    changeCourse(e){
        var selectedCourse = e.target.value;
        const apiLink ="http://0.0.0.0:23567/lecture/course/";
        const course = {Course};
        const url = apiLink + course;
        axios.get(url).then(response => {
            const collection = response.data;
            this.setState({data : response.data});
        })
    }

 const Chart = () => {
        let attNum = [];
        let modNam = [];

            setChartData({
                labels: modNam,
                datasets: [{
                                             label: 'module attendance',
                                             data: attNum,
                                             backgroundColor: [
                                                 'rgba(255, 99, 132, 0.2)',
                                                 'rgba(54, 162, 235, 0.2)',
                                                 'rgba(255, 206, 86, 0.2)',
                                                 'rgba(75, 192, 192, 0.2)',
                                                 'rgba(153, 102, 255, 0.2)',
                                                 'rgba(255, 159, 64, 0.2)',
                                                 'rgba(255, 99, 132, 0.2)',
                                                 'rgba(54, 162, 235, 0.2)',
                                                 'rgba(255, 206, 86, 0.2)',
                                                 'rgba(75, 192, 192, 0.2)',
                                                 'rgba(153, 102, 255, 0.2)',
                                                 'rgba(255, 159, 64, 0.2)',
                                                 'rgba(255, 99, 132, 0.2)',
                                                 'rgba(54, 162, 235, 0.2)',
                                                 'rgba(255, 206, 86, 0.2)',
                                                 'rgba(75, 192, 192, 0.2)',
                                                 'rgba(153, 102, 255, 0.2)',
                                                 'rgba(255, 159, 64, 0.2)',
                                                 'rgba(255, 99, 132, 0.2)',
                                                 'rgba(54, 162, 235, 0.2)',
                                                 'rgba(255, 206, 86, 0.2)',
                                                 'rgba(75, 192, 192, 0.2)',
                                                 'rgba(153, 102, 255, 0.2)',
                                                 'rgba(255, 159, 64, 0.2)',
                                             ],
                                             borderColor: [
                                                 'rgba(255, 99, 132, 1)',
                                                 'rgba(54, 162, 235, 1)',
                                                 'rgba(255, 206, 86, 1)',
                                                 'rgba(75, 192, 192, 1)',
                                                 'rgba(153, 102, 255, 1)',
                                                 'rgba(255, 159, 64, 1)',
                                                 'rgba(255, 99, 132, 1)',
                                                 'rgba(54, 162, 235, 1)',
                                                 'rgba(255, 206, 86, 1)',
                                                 'rgba(75, 192, 192, 1)',
                                                 'rgba(153, 102, 255, 1)',
                                                 'rgba(255, 159, 64, 1)',
                                                 'rgba(255, 99, 132, 1)',
                                                 'rgba(54, 162, 235, 1)',
                                                 'rgba(255, 206, 86, 1)',
                                                 'rgba(75, 192, 192, 1)',
                                                 'rgba(153, 102, 255, 1)',
                                                 'rgba(255, 159, 64, 1)',
                                                 'rgba(255, 99, 132, 1)',
                                                 'rgba(54, 162, 235, 1)',
                                                 'rgba(255, 206, 86, 1)',
                                                 'rgba(75, 192, 192, 1)',
                                                 'rgba(153, 102, 255, 1)',
                                                 'rgba(255, 159, 64, 1)',
                                             ],
                                             borderWidth: 1
                                         }]
            });
        }
    }
    useEffect(() => {
        Chart();
      }, []);
      return(
          <div className="App">
              <h1>Bar Chart</h1>
              <div>
              <div>
                    <p className='dropDownHead'>Click to change location:</p>
                    <select defaultValue={this.props.Course} onChange={this.changeCourse}>
                        <option value='CA1'>CA1</option>
                        <option value='CASE4'>CASE4</option>
                    </select>
                </div>

                  <Bar
                    data={chartData}
                    options={{
                        responsive:true,
                        title: { text: "Module Attendance", display: true },
                        scales:{
                            yAxes:{
                                ticks:{
                                    beginAtZero: true
                                }
                            }
                        }
                    }}
                  />
              </div>
          </div>
      )

export default DynamicChart;