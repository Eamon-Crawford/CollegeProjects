import React from 'react'
import HomeSection from '../../Components/HomeSection'
import {homeObjOne, homeObjTwo, homeObjThree} from './Data'

function Home() {
    return (
        <>
            <HomeSection {...homeObjOne} />
            {/*<HomeSection {...homeObjTwo} />
            <HomeSection {...homeObjThree} />*/}
        </>
    )
}

export default Home
