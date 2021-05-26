import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';
import { AiFillIdcard } from 'react-icons/ai';
import { FaBars, FaTimes } from 'react-icons/fa';
import { IconContext } from 'react-icons/lib';

function Navbar() {
  const [click, setClick] = useState(false);
  const [button, setButton] = useState(true);

  const handleClick = () => setClick(!click);
  const closeMobileMenu = () => setClick(false);

  const showButton = () => {
    if (window.innerWidth <= 960) {
      setButton(false);
    } else {
      setButton(true);
    }
  };

  useEffect(() => {
    showButton();
    window.addEventListener('resize', showButton);
    return (
        window.removeEventListener('resize', showButton)
  )
  }, []);


  return (
    <>
      <IconContext.Provider value={{ color: '#fff' }}>
        <nav className='navbar'>
          <div className='navbar-container container'>
            <Link to='/' className='navbar-logo' onClick={closeMobileMenu}>
              <AiFillIdcard className='navbar-icon' />
              NFC Powered DCU
            </Link>
            <div className='menu-icon' onClick={handleClick}>
              {click ? <FaTimes /> : <FaBars />}
            </div>
            <ul className={click ? 'nav-menu active' : 'nav-menu'}>
              <li className='nav-item'>
                <Link to='/' className='nav-links' onClick={closeMobileMenu}>
                  Home
                </Link>
              </li>
              <li className='nav-item'>
                <Link
                  to='/Graphing'
                  className='nav-links'
                  onClick={closeMobileMenu}
                >
                  Graphing
                </Link>
              </li>
              <li className='nav-item'>
                <Link
                  to='/TimetableStudent'
                  className='nav-links'
                  onClick={closeMobileMenu}
                >
                  Timetable Students
                </Link>
              </li>
              <li className='nav-item'>
                <Link
                  to='/TimetableLecturer'
                  className='nav-links'
                  onClick={closeMobileMenu}
                >
                  Timetable Lecturer
                </Link>
              </li>
            </ul>
          </div>
        </nav>
      </IconContext.Provider>
    </>
  );
}

export default Navbar;