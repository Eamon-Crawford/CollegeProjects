import React from 'react';
import './App.css';
import Home from './Pages/HomePage/Home';
import Graph from './Pages/Graphing/Graph';
import TimetableS from './Pages/TimetableStudent/TimetableStudent';
import TimetableL from './Pages/TimetableLecturer/TimetableLecturer';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Navbar from './Components/Navbar';

function App() {
  return (
    <Router>
      <Navbar />
      <Switch>
        <Route path='/' exact component={Home} />
        <Route path='/Graphing' component={Graph} />
        <Route path='/TimetableLecturer' component={TimetableL} />
        <Route path='/TimetableStudent' component={TimetableS} />
      </Switch>
    </Router>
  );
}

export default App;